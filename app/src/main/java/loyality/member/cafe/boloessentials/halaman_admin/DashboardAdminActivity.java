package loyality.member.cafe.boloessentials.halaman_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;
import loyality.member.cafe.boloessentials.model.User;

public class DashboardAdminActivity extends AppCompatActivity {
    private TextView tvUser, tvAbsen, tvTukarHadiah, tvTukarPoint, tvAdministrator, tvDashboard, tvHadiah;
    private Button btnExport;
    private RelativeLayout logout;
    private TableLayout tableLayout;
    private ProgressBar progressBar;
    private TextView tvPointJumlahUser;
    private TextView tvPointTotalPointUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        int textColor = getIntent().getIntExtra("textColorDashboard", R.color.brownAdmin);
        tvDashboard = findViewById(R.id.tvDashboard);
        tvDashboard.setTextColor(getResources().getColor(textColor));

        // Menghubungkan TextView dengan layout
        tvPointJumlahUser = findViewById(R.id.tvPointJumlahUser);
        tvPointTotalPointUser = findViewById(R.id.tvPointTotalPointUser);

        btnExport = findViewById(R.id.btnExport);
        tableLayout = findViewById(R.id.tableLayout);

        // Menyembunyikan tabel saat memuat data
        tableLayout.setVisibility(View.GONE);

        // Mengambil data dari Firebase dan mengurutkan berdasarkan tanggal bergabung
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("tanggalBergabung").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Menghapus baris yang ada di tabel
                tableLayout.removeAllViews();

                // Menambahkan header tabel
                addTableHeader();

                // Menyimpan data pengguna dalam list untuk dibalik urutannya
                List<User> userList = new ArrayList<>();

                // Mengumpulkan data user ke dalam list
                int userCount = 0;
                int totalPoints = 0;

                // Menambahkan data user ke tabel
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        addUserRow(user);
                        userCount++;
                        totalPoints += user.getPointUser();
                    }
                }

                // Update TextView dengan jumlah pengguna dan total point
                tvPointJumlahUser.setText(String.valueOf(userCount));
                tvPointTotalPointUser.setText(String.valueOf(totalPoints));
                // Membalik urutan list untuk menampilkan secara descending
                Collections.reverse(userList);

                // Menambahkan data user yang sudah diurutkan ke tabel
                for (User user : userList) {
                    addUserRow(user);
                }

                // Menyembunyikan ProgressBar dan menampilkan tabel setelah data diambil
                progressBar.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardAdminActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
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

    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);
        String[] headers = {"Nama User", "Tanggal Bergabung", "Email", "No Telepon", "Tanggal Lahir", "Jumlah Point"};
        float[] weights = {1.3f, 0.7f, 2f, 1f, 1f, 0.6f}; // Anda dapat mengatur bobot sesuai kebutuhan

        for (int i = 0; i < headers.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(headers[i]);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setPadding(5, 5, 5, 5);

            // Mengatur layout_weight untuk TextView
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // width 0 agar weight bekerja
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i] // layout_weight
            );
            textView.setLayoutParams(params);

            headerRow.addView(textView);
        }
        headerRow.setBackgroundColor(getResources().getColor(R.color.brownAdmin));
        tableLayout.addView(headerRow);
    }

    private void addUserRow(User user) {
        TableRow row = new TableRow(this);
        String[] userData = {
                user.getNama(),
                formatTanggalBergabung(user.getTanggalBergabung()), // Memformat tanggal bergabung
                user.getEmail(),
                user.getTelpon(),
                formatTanggalLahir(user.getTanggalLahir()), // Memformat tanggal lahir
                String.valueOf(user.getPointUser())
        };

        float[] weights = {1.3f, 0.7f, 2f, 1f, 1f, 0.6f};

        for (int i = 0; i < userData.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(userData[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setPadding(5, 3, 5, 3);

            // Mengatur layout_weight untuk TextView
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // width 0 agar weight bekerja
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i] // layout_weight
            );
            textView.setLayoutParams(params);

            row.addView(textView);
        }
        tableLayout.addView(row);
    }

    private String formatTanggalBergabung(String tanggalBergabung) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(tanggalBergabung);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return tanggalBergabung;
        }
    }

    private String formatTanggalLahir(String tanggalLahir) {
        if (tanggalLahir != null && tanggalLahir.length() == 8) {
            String day = tanggalLahir.substring(0, 2);
            String month = tanggalLahir.substring(2, 4);
            String year = tanggalLahir.substring(4, 8);
            return day + "-" + month + "-" + year;
        } else {
            return tanggalLahir;
        }
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

    private void showLoaderAndStartActivity(final Class<?> targetActivity) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(DashboardAdminActivity.this, targetActivity);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);
    }
}
