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

import loyality.member.cafe.boloessentials.halaman_userandworker.LoginActivity;
import loyality.member.cafe.boloessentials.halaman_userandworker.TambahPointActivity;
import loyality.member.cafe.boloessentials.halaman_userandworker.TukarPointActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnAbsen, btnTukarPoint, btnCekPoint, btnTambahPoint, btnLogout;
    private Dialog mDialog;
    private String userType;

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

        // Ambil tipe pengguna dari Intent
        Intent intent = getIntent();
        userType = intent.getStringExtra("USER_TYPE");

        // Set visibilitas tombol berdasarkan tipe pengguna
        setupUIBasedOnUserType();

        btnAbsen.setOnClickListener(v -> {
            mDialog.setContentView(R.layout.modal_absen);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.show();
        });

        btnTambahPoint.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, TambahPointActivity.class);
            startActivity(intent1);
        });

        btnCekPoint.setOnClickListener(v -> {
            mDialog.setContentView(R.layout.modal_cek_point);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.show();
        });

        btnTukarPoint.setOnClickListener(v -> {
            Intent intent2 = new Intent(MainActivity.this, TukarPointActivity.class);
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
