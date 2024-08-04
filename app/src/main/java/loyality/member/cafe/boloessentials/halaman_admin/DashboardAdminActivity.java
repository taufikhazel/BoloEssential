package loyality.member.cafe.boloessentials.halaman_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;

public class DashboardAdminActivity extends AppCompatActivity {
    private TextView tvUser, tvAbsen, tvTukarHadiah, tvTukarPoint, tvAdministrator, tvDashboard;
    private Button btnExport;
    private RelativeLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        int textColor = getIntent().getIntExtra("textColorDashboard", R.color.brownAdmin);
        tvDashboard = findViewById(R.id.tvDashboard);
        tvDashboard.setTextColor(getResources().getColor(textColor));

        btnExport = findViewById(R.id.btnExport);

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });


        tvUser = findViewById(R.id.tvUser);

        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardAdminActivity.this, UserAdminActivity.class);
                startActivity(intent);
            }
        });

        tvAbsen = findViewById(R.id.tvAbsen);

        tvAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardAdminActivity.this, KaryawanAdminActivity.class);
                startActivity(intent);
            }
        });

        tvTukarPoint = findViewById(R.id.tvTukarPoint);

        tvTukarPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardAdminActivity.this, TukarPointAdminActivity.class);
                startActivity(intent);
            }
        });

        tvTukarHadiah = findViewById(R.id.tvTukarHadiah);

        tvTukarHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardAdminActivity.this, TukarHadiahAdminActivity.class);
                startActivity(intent);
            }
        });

        tvAdministrator = findViewById(R.id.tvAdministrator);

        tvAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardAdminActivity.this, AdministratorAdminActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_dropdown);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(DashboardAdminActivity.this, LoadingScreenActivity.class);
                startActivity(intent);
                Toast.makeText(DashboardAdminActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popupMenu.show();
    }

}

