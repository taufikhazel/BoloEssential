package loyality.member.cafe.boloessentials.halaman_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;

public class TukarHadiahAdminActivity extends AppCompatActivity {

    private TextView tvUser, tvAbsen, tvTukarPoint, tvAdministrator, tvDashboard, tvTukarHadiah, tvHadiah;
    private RelativeLayout logout;
    private ProgressBar progressBar;
    private Button btnPending, btnBaru, btnBerhasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_hadiah_admin);

        // Initialize views
        progressBar = findViewById(R.id.progressBar);
        btnBaru = findViewById(R.id.btnBaru);
        btnPending = findViewById(R.id.btnPending);
        btnBerhasil = findViewById(R.id.btnBerhasil);

        // Set progress bar visibility
        progressBar.setVisibility(View.GONE);

        // Update TextView color based on Intent
        int textColor = getIntent().getIntExtra("textColorTukarHadiah", R.color.brownAdmin);
        tvTukarHadiah = findViewById(R.id.tvTukarHadiah);
        tvTukarHadiah.setTextColor(ContextCompat.getColor(this, textColor));

        tvUser = findViewById(R.id.tvUser);
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(UserAdminActivity.class);
            }
        });

        tvDashboard = findViewById(R.id.tvDashboard);
        tvDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(DashboardAdminActivity.class);
            }
        });

        tvAbsen = findViewById(R.id.tvAbsen);
        tvAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(KaryawanAdminActivity.class);
            }
        });

        tvTukarPoint = findViewById(R.id.tvTukarPoint);
        tvTukarPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(TukarPointAdminActivity.class);
            }
        });

        tvAdministrator = findViewById(R.id.tvAdministrator);
        tvAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(AdministratorAdminActivity.class);
            }
        });

        tvHadiah = findViewById(R.id.tvHadiah);
        tvHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(HadiahAdminActivity.class);
            }
        });

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        // Set up button click listeners
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
                showLoaderAndStartActivity(UserAdminActivity.class);
            }
        });

        tvUser = findViewById(R.id.tvUser);
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

    @Override
    public void onBackPressed() {
        // Memicu showPopupMenu saat tombol back ditekan
        showPopupMenu(logout);
    }

    private void displayFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

    }

    public void updateButtonStylesForBaruTukarPointFragment() {
        int brownAdminColor = getResources().getColor(R.color.brownAdmin);
        int whiteColor = getResources().getColor(R.color.white);
        int putihColor = getResources().getColor(R.color.putih);
        int grayColor = getResources().getColor(R.color.brown); // Assuming gray color for the non-active state

        btnBaru.setBackgroundTintList(ColorStateList.valueOf(brownAdminColor));
        btnBaru.setTextColor(whiteColor);

        btnBerhasil.setBackgroundTintList(ColorStateList.valueOf(putihColor));
        btnBerhasil.setTextColor(grayColor);

        btnPending.setBackgroundTintList(ColorStateList.valueOf(putihColor));
        btnPending.setTextColor(grayColor);
    }

    public void updateButtonStylesForPendingTukarPointFragment() {
        int brownAdminColor = getResources().getColor(R.color.brownAdmin);
        int whiteColor = getResources().getColor(R.color.white);
        int putihColor = getResources().getColor(R.color.putih);
        int grayColor = getResources().getColor(R.color.brown); // Assuming gray color for the non-active state

        btnPending.setBackgroundTintList(ColorStateList.valueOf(brownAdminColor));
        btnPending.setTextColor(whiteColor);

        btnBerhasil.setBackgroundTintList(ColorStateList.valueOf(putihColor));
        btnBerhasil.setTextColor(grayColor);

        btnBaru.setBackgroundTintList(ColorStateList.valueOf(putihColor));
        btnBaru.setTextColor(grayColor);
    }

    public void updateButtonStylesForBerhasilTukarPointFragment() {
        int brownAdminColor = getResources().getColor(R.color.brownAdmin);
        int whiteColor = getResources().getColor(R.color.white);
        int putihColor = getResources().getColor(R.color.putih);
        int grayColor = getResources().getColor(R.color.brown); // Assuming gray color for the non-active state

        btnBerhasil.setBackgroundTintList(ColorStateList.valueOf(brownAdminColor));
        btnBerhasil.setTextColor(whiteColor);

        btnPending.setBackgroundTintList(ColorStateList.valueOf(putihColor));
        btnPending.setTextColor(grayColor);

        btnBaru.setBackgroundTintList(ColorStateList.valueOf(putihColor));
        btnBaru.setTextColor(grayColor);
    }
    private void showLoaderAndStartActivity(final Class<?> targetActivity) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(TukarHadiahAdminActivity.this, targetActivity);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);
    }
}
