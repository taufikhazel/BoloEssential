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

        if (savedInstanceState == null) {
            displayFragment(new BaruTukarHadiahFragment(), "baru");
        }

        // Set up button click listeners
        btnBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new BaruTukarHadiahFragment(), "baru");
            }
        });

        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new PendingTukarHadiahFragment(), "pending");
            }
        });

        btnBerhasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new BerhasilTukarHadiahFragment(), "berhasil");
            }
        });

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

    private void displayFragment(Fragment fragment, String fragmentName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

        // Update button styles
        updateButtonStylesForFragment(fragmentName);
    }

    private void updateButtonStylesForFragment(String activeFragment) {
        int brownAdminColor = ContextCompat.getColor(this, R.color.brownAdmin);
        int whiteColor = ContextCompat.getColor(this, R.color.white);
        int grayColor = ContextCompat.getColor(this, R.color.brownAdmin); // Pastikan grayColor sesuai dengan warna yang diinginkan

        switch (activeFragment) {
            case "baru":
                btnBaru.setBackgroundTintList(ColorStateList.valueOf(brownAdminColor));
                btnBaru.setTextColor(whiteColor);

                btnPending.setBackgroundTintList(ColorStateList.valueOf(whiteColor)); // Warna default atau non-aktif
                btnPending.setTextColor(grayColor);

                btnBerhasil.setBackgroundTintList(ColorStateList.valueOf(whiteColor)); // Warna default atau non-aktif
                btnBerhasil.setTextColor(grayColor);
                break;

            case "pending":
                btnBaru.setBackgroundTintList(ColorStateList.valueOf(whiteColor)); // Warna default atau non-aktif
                btnBaru.setTextColor(grayColor);

                btnPending.setBackgroundTintList(ColorStateList.valueOf(brownAdminColor));
                btnPending.setTextColor(whiteColor);

                btnBerhasil.setBackgroundTintList(ColorStateList.valueOf(whiteColor)); // Warna default atau non-aktif
                btnBerhasil.setTextColor(grayColor);
                break;

            case "berhasil":
                btnBaru.setBackgroundTintList(ColorStateList.valueOf(whiteColor)); // Warna default atau non-aktif
                btnBaru.setTextColor(grayColor);

                btnPending.setBackgroundTintList(ColorStateList.valueOf(whiteColor)); // Warna default atau non-aktif
                btnPending.setTextColor(grayColor);

                btnBerhasil.setBackgroundTintList(ColorStateList.valueOf(brownAdminColor));
                btnBerhasil.setTextColor(whiteColor);
                break;
        }
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
