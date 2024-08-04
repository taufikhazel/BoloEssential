package loyality.member.cafe.boloessentials;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import loyality.member.cafe.boloessentials.halaman_userandworker.TambahPointActivity;
import loyality.member.cafe.boloessentials.halaman_userandworker.TukarPointActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnAbsen, btnTukarPoint, btnCekPoint, btnTambahPoint;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAbsen = findViewById(R.id.btnAbsen);
        btnCekPoint = findViewById(R.id.btnCekPoint);
        btnTambahPoint = findViewById(R.id.btnTambahPoint);
        btnTukarPoint = findViewById(R.id.btnTukarPoint);

        mDialog = new Dialog(this);

        btnAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.setContentView(R.layout.modal_absen);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });

        btnTambahPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TambahPointActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCekPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.setContentView(R.layout.modal_cek_point);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });

        btnTukarPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TukarPointActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}