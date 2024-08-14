package loyality.member.cafe.boloessentials;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import loyality.member.cafe.boloessentials.halaman_userandworker.LoginActivity;
import loyality.member.cafe.boloessentials.halaman_userandworker.TambahPointActivity;
import loyality.member.cafe.boloessentials.halaman_userandworker.TukarPointActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnAbsen, btnTukarPoint, btnCekPoint, btnTambahPoint, btnLogout;
    private Dialog mDialog;
    private String userType;
    private String UID;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAbsen = findViewById(R.id.btnAbsen);
        btnCekPoint = findViewById(R.id.btnCekPoint);
        btnTambahPoint = findViewById(R.id.btnTambahPoint);
        btnTukarPoint = findViewById(R.id.btnTukarPoint);
        btnLogout = findViewById(R.id.btnLogout);

        mDialog = new Dialog(this);

        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Ambil tipe pengguna dan UID dari Intent
        Intent intent = getIntent();
        userType = intent.getStringExtra("USER_TYPE");
        UID = intent.getStringExtra("UID");

        // Set visibilitas tombol berdasarkan tipe pengguna
        setupUIBasedOnUserType();

        btnAbsen.setOnClickListener(v -> checkAndRecordAbsence());

        btnTambahPoint.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, TambahPointActivity.class);
            intent1.putExtra("UID", UID);
            startActivity(intent1);
        });

        btnCekPoint.setOnClickListener(v -> {
            // Set up the modal
            mDialog.setContentView(R.layout.modal_cek_point);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView tvPoint = mDialog.findViewById(R.id.tvPoint);
            TextView tvNama = mDialog.findViewById(R.id.tvNama);

            databaseRef.child("users").orderByChild("nomorID").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Get the first matched user
                        DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                        Integer pointUser = userSnapshot.child("pointUser").getValue(Integer.class);
                        String nama = userSnapshot.child("nama").getValue(String.class);

                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                        String formattedPointUser = numberFormat.format(pointUser);
                        // Set the TextViews with the retrieved data
                        if (pointUser != null) {
                            tvPoint.setText(String.valueOf(formattedPointUser + " Point")); // Convert integer to string
                        }
                        tvNama.setText(nama);

                        // Show the modal
                        mDialog.show();
                    } else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Data Tidak Ditemukan")
                                .setMessage("UID tidak ditemukan di database pengguna.")
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                                .show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("Terjadi kesalahan: " + databaseError.getMessage())
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                            .show();
                }
            });
        });




        btnTukarPoint.setOnClickListener(v -> {
            Intent intent2 = new Intent(MainActivity.this, TukarPointActivity.class);
            intent2.putExtra("UID", UID);
            startActivity(intent2);
        });

        btnLogout.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setupUIBasedOnUserType() {
        if ("karyawan".equals(userType)) {
            btnTukarPoint.setVisibility(View.GONE);
            btnCekPoint.setVisibility(View.GONE);
            btnTambahPoint.setVisibility(View.GONE);
        } else if ("users".equals(userType)) {
            btnAbsen.setVisibility(View.GONE);
        }
    }

    private void checkAndRecordAbsence() {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        // Format hari ke bahasa Indonesia
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("id", "ID"));
        String currentDay = dayFormat.format(calendar.getTime());

        // Find karyawan data by UID
        databaseRef.child("karyawan").orderByChild("nomorIDKaryawan").equalTo(UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot karyawanSnapshot = dataSnapshot.getChildren().iterator().next();
                            String namaKaryawan = karyawanSnapshot.child("nama").getValue(String.class);

                            // Check if user has already checked in twice today
                            databaseRef.child("absenKaryawan").child(UID).child(currentDate)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            long count = dataSnapshot.getChildrenCount();

                                            if (count >= 2) {
                                                String lastCheckInTime = dataSnapshot.child("2").child("jam").getValue(String.class);
                                                String lastCheckInDay = dataSnapshot.child("2").child("hari").getValue(String.class);
                                                String lastCheckInDate = dataSnapshot.child("2").child("tanggal").getValue(String.class);

                                                new AlertDialog.Builder(MainActivity.this)
                                                        .setTitle("Absensi")
                                                        .setMessage(namaKaryawan + " telah absen dua kali pada " + lastCheckInDay + ", " + lastCheckInDate + ", " + lastCheckInTime)
                                                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                                                        .show();
                                            } else {
                                                // Proceed to record absence
                                                recordAbsence(currentDate, currentTime, currentDay, namaKaryawan, count + 1);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Handle possible errors.
                                        }
                                    });
                        } else {
                            // UID not found in karyawan database
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Error")
                                    .setMessage("UID tidak ditemukan di database.")
                                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    private void recordAbsence(String date, String time, String day, String namaKaryawan, long absensiKe) {
        // Save absence record
        DatabaseReference absenRef = databaseRef.child("absenKaryawan").child(UID).child(date).child(String.valueOf(absensiKe));
        absenRef.child("tanggal").setValue(date);
        absenRef.child("jam").setValue(time);
        absenRef.child("hari").setValue(day);
        absenRef.child("absen").setValue(true);
        absenRef.child("namaKaryawan").setValue(namaKaryawan);

        // Display the absen modal
        mDialog.setContentView(R.layout.modal_absen);
        TextView jamAbsen = mDialog.findViewById(R.id.jamAbsen);
        TextView namaKaryawanView = mDialog.findViewById(R.id.namaKaryawan);

        jamAbsen.setText(date + ", Jam " + time);
        namaKaryawanView.setText(namaKaryawan);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }


    @Override
    public void onBackPressed() {
        // Buat AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set pesan untuk alert
        builder.setMessage("Apakah Anda yakin ingin keluar?");

        // Set aksi jika pengguna memilih "Iya"
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Intent untuk kembali ke LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
                startActivity(intent);
                finish(); // Tutup aktivitas saat ini
            }
        });

        // Set aksi jika pengguna memilih "Tidak"
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Tutup dialog dan tetap di aktivitas
            }
        });

        // Tampilkan alert dialog
        builder.show();
    }
}
