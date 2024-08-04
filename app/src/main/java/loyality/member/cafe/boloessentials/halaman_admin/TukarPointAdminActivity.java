package loyality.member.cafe.boloessentials.halaman_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class TukarPointAdminActivity extends AppCompatActivity {
    private TextView tvUser, tvAbsen, tvTukarHadiah, tvAdministrator, tvDashboard, tvTukarPoint;
    private RelativeLayout logout;
    private Button btnBerhasil, btnBaru, btnPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_point_admin);

        int textColor = getIntent().getIntExtra("textColorTukarPoint", R.color.brownAdmin);
        tvTukarPoint = findViewById(R.id.tvTukarPoint);
        tvTukarPoint.setTextColor(getResources().getColor(textColor));

        btnBaru = findViewById(R.id.btnBaru);
        btnPending = findViewById(R.id.btnPending);
        btnBerhasil = findViewById(R.id.btnBerhasil);

        btnBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new BaruTukarPointFragment());
            }
        });

        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new PendingTukarPointFragment());
            }
        });

        btnBerhasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new BerhasilTukarPointFragment());
            }
        });

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
                tvUser.setTextColor(getResources().getColor(R.color.brownAdmin));
                Intent intent = new Intent(TukarPointAdminActivity.this, UserAdminActivity.class);
                intent.putExtra("textColorUser",R.color.brownAdmin);
                startActivity(intent);
            }
        });

        tvDashboard = findViewById(R.id.tvDashboard);

        tvDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDashboard.setTextColor(getResources().getColor(R.color.brownAdmin));
                Intent intent = new Intent(TukarPointAdminActivity.this, DashboardAdminActivity.class);
                intent.putExtra("textColorDashboard",R.color.brownAdmin);
                startActivity(intent);
            }
        });

        tvAbsen = findViewById(R.id.tvAbsen);

        tvAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAbsen.setTextColor(getResources().getColor(R.color.brownAdmin));
                Intent intent = new Intent(TukarPointAdminActivity.this, KaryawanAdminActivity.class);
                intent.putExtra("textColorKaryawan",R.color.brownAdmin);
                startActivity(intent);
            }
        });

        tvTukarHadiah = findViewById(R.id.tvTukarHadiah);

        tvTukarHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTukarHadiah.setTextColor(getResources().getColor(R.color.brownAdmin));
                Intent intent = new Intent(TukarPointAdminActivity.this, TukarHadiahAdminActivity.class);
                intent.putExtra("textColorTukarHadiah",R.color.brownAdmin);
                startActivity(intent);
            }
        });

        tvAdministrator = findViewById(R.id.tvAdministrator);

        tvAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAdministrator.setTextColor(getResources().getColor(R.color.brownAdmin));
                Intent intent = new Intent(TukarPointAdminActivity.this, AdministratorAdminActivity.class);
                intent.putExtra("textColorAdministrator",R.color.brownAdmin);
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
                Intent intent = new Intent(TukarPointAdminActivity.this, LoadingScreenActivity.class);
                startActivity(intent);
                Toast.makeText(TukarPointAdminActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popupMenu.show();
    }
    private void displayFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}