package loyality.member.cafe.boloessentials.halaman_userandworker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import loyality.member.cafe.boloessentials.halaman_admin.DashboardAdminActivity;
import loyality.member.cafe.boloessentials.MainActivity;
import loyality.member.cafe.boloessentials.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etID;
    private Button btnAkses;
    private TextView tvClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etID = findViewById(R.id.etID);
        btnAkses = findViewById(R.id.btnAkses);
        tvClick = findViewById(R.id.tvClick);

        btnAkses.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        tvClick.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, DashboardAdminActivity.class);
            startActivity(intent);
        });
    }
}