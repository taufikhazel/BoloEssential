package loyality.member.cafe.boloessentials.halaman_userandworker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import loyality.member.cafe.boloessentials.MainActivity;
import loyality.member.cafe.boloessentials.R;

public class TambahPointActivity extends AppCompatActivity {
    private Button btnSubmit;
    private TextView tvNama, tvPoint;
    private EditText etID, etBiaya;
    private Dialog mDialog;
    private DatabaseReference databaseReference;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_point);
        btnSubmit = findViewById(R.id.btnSubmit);
        etID = findViewById(R.id.etID);
        etBiaya = findViewById(R.id.etBiaya);

        mDialog = new Dialog(this);

        // Ambil UID dari MainActivity
        UID = getIntent().getStringExtra("UID");

        // Inisialisasi referensi ke Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil nilai dari EditText
                String idTransaksi = etID.getText().toString();
                String biayaStr = etBiaya.getText().toString();

                // Validasi apakah semua field telah diisi
                if (TextUtils.isEmpty(idTransaksi) || TextUtils.isEmpty(biayaStr)) {
                    Toast.makeText(TambahPointActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi biaya harus berupa angka
                int biaya;
                try {
                    biaya = Integer.parseInt(biayaStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(TambahPointActivity.this, "Biaya harus berupa angka", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hitung poin
                int point = biaya / 3000;

                // Cek apakah ID Transaksi sudah ada di tabel "tambahPoint"
                databaseReference.child("tambahPoint").orderByChild("IDTransaksi").equalTo(idTransaksi)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Tampilkan dialog error jika ID Transaksi sudah terpakai
                                    new AlertDialog.Builder(TambahPointActivity.this)
                                            .setTitle("Error")
                                            .setMessage("ID Transaksi sudah terpakai, coba lagi dengan Transaksi baru")
                                            .setPositiveButton("OK", (dialog, which) -> {
                                                // Reset semua field
                                                etID.setText("");
                                                etBiaya.setText("");
                                            })
                                            .show();
                                } else {
                                    // Jika ID Transaksi belum ada, lanjutkan dengan penyimpanan data
                                    // Buat dialog untuk menampilkan hasil
                                    mDialog.setContentView(R.layout.modal_tambah_point);
                                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    // Ambil nama dari tabel "users" berdasarkan UID
                                    databaseReference.child("users").orderByChild("nomorID").equalTo(UID)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            String nama = snapshot.child("nama").getValue(String.class);

                                                            // Buat objek untuk menyimpan data ke Firebase
                                                            Map<String, Object> tambahPoint = new HashMap<>();
                                                            tambahPoint.put("IDTransaksi", idTransaksi);
                                                            tambahPoint.put("nama", nama);
                                                            tambahPoint.put("nomorID", UID);
                                                            tambahPoint.put("point", point);
                                                            tambahPoint.put("status", "false");

                                                            // Simpan data ke Firebase di tabel "tambahPoint"
                                                            databaseReference.child("tambahPoint").push().setValue(tambahPoint)
                                                                    .addOnCompleteListener(task -> {
                                                                        if (task.isSuccessful()) {
                                                                            // Ambil data yang baru ditambahkan
                                                                            databaseReference.child("tambahPoint").orderByKey().limitToLast(1)
                                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.exists()) {
                                                                                                // Ambil data dari snapshot
                                                                                                DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                                                                                                String nama = snapshot.child("nama").getValue(String.class);
                                                                                                String nomorID = snapshot.child("nomorID").getValue(String.class);
                                                                                                Integer point = snapshot.child("point").getValue(Integer.class);

                                                                                                // Set TextView di dialog
                                                                                                TextView tvNama = mDialog.findViewById(R.id.tvNama);
                                                                                                TextView tvPoint = mDialog.findViewById(R.id.tvPoint);

                                                                                                tvNama.setText(nama);
                                                                                                tvPoint.setText(String.valueOf(point) + " Point");
                                                                                                mDialog.setOnDismissListener(dialog -> {
                                                                                                    Intent intent = new Intent(TambahPointActivity.this, MainActivity.class);
                                                                                                    intent.putExtra("USER_TYPE", "users");
                                                                                                    intent.putExtra("UID", nomorID); // kirim nomorID ke MainActivity
                                                                                                    startActivity(intent);
                                                                                                    finish(); // Tutup TambahPointActivity
                                                                                                });
                                                                                                // Tampilkan dialog
                                                                                                mDialog.show();
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError databaseError) {
                                                                                            // Tangani kesalahan jika ada
                                                                                            Toast.makeText(TambahPointActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                        } else {
                                                                            Toast.makeText(TambahPointActivity.this, "Gagal menambahkan poin", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                        }
                                                    } else {
                                                        Toast.makeText(TambahPointActivity.this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Toast.makeText(TambahPointActivity.this, "Terjadi kesalahan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(TambahPointActivity.this, "Terjadi kesalahan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
