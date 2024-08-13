package loyality.member.cafe.boloessentials.halaman_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
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

public class TukarPointAdminActivity extends AppCompatActivity {
    private TextView tvUser, tvAbsen, tvTukarHadiah, tvAdministrator, tvDashboard, tvTukarPoint, tvHadiah;
    private RelativeLayout logout;
    private ProgressBar progressBar;
    private Button btnBerhasil, btnBaru, btnPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_point_admin);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        int textColor = getIntent().getIntExtra("textColorTukarPoint", R.color.brownAdmin);
        tvTukarPoint = findViewById(R.id.tvTukarPoint);
        tvTukarPoint.setTextColor(getResources().getColor(textColor));

        btnBaru = findViewById(R.id.btnBaru);
        btnPending = findViewById(R.id.btnPending);
        btnBerhasil = findViewById(R.id.btnBerhasil);

        if (savedInstanceState == null) {
            displayFragment(new BaruTukarPointFragment(), "baru");
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

        tvTukarHadiah = findViewById(R.id.tvTukarHadiah);
        tvTukarHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(TukarHadiahAdminActivity.class);
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
    private void displayFragment(Fragment fragment, String fragmentName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

        // Update button styles
        updateButtonStylesForFragment(fragmentName);
    }

    @Override
    public void onBackPressed() {
        // Memicu showPopupMenu saat tombol back ditekan
        showPopupMenu(logout);
    }

    private void showLoaderAndStartActivity(final Class<?> targetActivity) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(TukarPointAdminActivity.this, targetActivity);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);
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
}