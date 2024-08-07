package loyality.member.cafe.boloessentials.halaman_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;

public class HadiahAdminActivity extends AppCompatActivity {
    private Button btnTambahHadiah, btnShow, btnDrop;
    private Dialog mDialog;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private TextView  tvAbsen, tvDashboard, tvTukarPoint, tvTukarHadiah, tvAdministrator, tvUser, tvHadiah;
    private RelativeLayout logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadiah_admin);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        int textColor = getIntent().getIntExtra("textColorUser", R.color.brownAdmin);
        tvHadiah = findViewById(R.id.tvHadiah);
        tvHadiah.setTextColor(getResources().getColor(textColor));

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
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

        tvTukarHadiah = findViewById(R.id.tvTukarHadiah);
        tvTukarHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(TukarHadiahAdminActivity.class);
            }
        });

        tvUser = findViewById(R.id.tvUser);
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(UserAdminActivity.class);
            }
        });

        tvAdministrator = findViewById(R.id.tvAdministrator);
        tvAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(AdministratorAdminActivity.class);
            }
        });

        btnShow = findViewById(R.id.btnShow);
        btnDrop = findViewById(R.id.btnDrop);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new ShowHadiahFragment());
            }
        });

        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(new DropHadiahFragment());
            }
        });

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_dropdown);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(HadiahAdminActivity.this, LoadingScreenActivity.class);
                startActivity(intent);
                Toast.makeText(HadiahAdminActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popupMenu.show();
    }

    private void showLoaderAndStartActivity(final Class<?> targetActivity) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HadiahAdminActivity.this, targetActivity);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);
    }

    private void displayFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}