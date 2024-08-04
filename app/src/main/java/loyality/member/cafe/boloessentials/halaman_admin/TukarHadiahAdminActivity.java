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

public class TukarHadiahAdminActivity extends AppCompatActivity {
    private TextView tvUser, tvAbsen, tvTukarPoint, tvAdministrator, tvDashboard, tvTukarHadiah;
    private RelativeLayout logout;
    private Button btnPending, btnBaru, btnBerhasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_hadiah_admin);

        int textColor = getIntent().getIntExtra("textColorTukarHadiah", R.color.brownAdmin);
        tvTukarHadiah = findViewById(R.id.tvTukarHadiah);
        tvTukarHadiah.setTextColor(getResources().getColor(textColor));

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        btnBaru = findViewById(R.id.btnBaru);
        btnPending = findViewById(R.id.btnPending);
        btnBerhasil = findViewById(R.id.btnBerhasil);

        btnBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new BaruTukarHadiahFragment());
            }
        });

        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new PendingTukarHadiahFragment());
            }
        });

        btnBerhasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new BerhasilTukarHadiahFragment());
            }
        });

        tvUser = findViewById(R.id.tvUser);

        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TukarHadiahAdminActivity.this, UserAdminActivity.class);
                startActivity(intent);
            }
        });

        tvDashboard = findViewById(R.id.tvDashboard);

        tvDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TukarHadiahAdminActivity.this, DashboardAdminActivity.class);
                startActivity(intent);
            }
        });

        tvAbsen = findViewById(R.id.tvAbsen);

        tvAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TukarHadiahAdminActivity.this, KaryawanAdminActivity.class);
                startActivity(intent);
            }
        });

        tvTukarPoint = findViewById(R.id.tvTukarPoint);

        tvTukarPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TukarHadiahAdminActivity.this, TukarPointAdminActivity.class);
                startActivity(intent);
            }
        });

        tvAdministrator = findViewById(R.id.tvAdministrator);

        tvAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TukarHadiahAdminActivity.this, AdministratorAdminActivity.class);
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
                Intent intent = new Intent(TukarHadiahAdminActivity.this, LoadingScreenActivity.class);
                startActivity(intent);
                Toast.makeText(TukarHadiahAdminActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
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
