package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class UserAdminActivity extends AppCompatActivity {
    private Button btntambahUser;
    private Dialog mDialog;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private TextView tvPointUser, tvAbsen, tvDashboard, tvTukarPoint, tvTukarHadiah, tvAdministrator, tvUser, tvHadiah;
    private RelativeLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        int textColor = getIntent().getIntExtra("textColorUser", R.color.brownAdmin);
        tvUser = findViewById(R.id.tvUser);
        tvUser.setTextColor(getResources().getColor(textColor));

        tvPointUser = findViewById(R.id.tvPointUser);
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

        btntambahUser = findViewById(R.id.btnTambahUser);
        tableLayout = findViewById(R.id.tableLayout);
        mDialog = new Dialog(this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        btntambahUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.modal_tambah_user_admin);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();

                EditText etNama = mDialog.findViewById(R.id.etNama);
                EditText etEmail = mDialog.findViewById(R.id.etEmail);
                EditText etTelpon = mDialog.findViewById(R.id.etTelpon);
                EditText etTanggalLahir = mDialog.findViewById(R.id.etTanggalLahir);
                Button btnSubmit = mDialog.findViewById(R.id.btnSubmit);
                ProgressBar loader = new ProgressBar(UserAdminActivity.this);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nama = etNama.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String telpon = etTelpon.getText().toString().trim();
                        String tanggalLahir = etTanggalLahir.getText().toString().trim();

                        if (nama.isEmpty() || email.isEmpty() || telpon.isEmpty() || tanggalLahir.isEmpty()) {
                            Toast.makeText(UserAdminActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        loader.setVisibility(View.VISIBLE);

                        String tanggalBergabung = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        int pointUser = 0;

                        User user = new User(nama, tanggalBergabung, email, telpon, tanggalLahir, pointUser);
                        databaseReference.push().setValue(user).addOnCompleteListener(task -> {
                            loader.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAdminActivity.this, "User berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            } else {
                                Toast.makeText(UserAdminActivity.this, "Gagal menambahkan user", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        tableLayout.setVisibility(View.GONE);

        databaseReference.orderByChild("tanggalBergabung").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableLayout.removeAllViews();
                addTableHeader();

                List<User> userList = new ArrayList<>();
                int userCount = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                        userCount++;
                    }
                }

                Collections.reverse(userList);
                tvPointUser.setText(String.valueOf(userCount));

                for (User user : userList) {
                    addUserRow(user);
                }

                progressBar.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserAdminActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);
        String[] headers = {"Nama User", "Tanggal Bergabung", "Email", "No Telepon", "Tanggal Lahir", "Jumlah Point"};
        float[] weights = {1.3f, 0.7f, 2f, 1f, 1f, 0.6f};

        for (int i = 0; i < headers.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(headers[i]);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(5, 5, 5, 5);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i]
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
                formatTanggalBergabung(user.getTanggalBergabung()),
                user.getEmail(),
                user.getTelpon(),
                formatTanggalLahir(user.getTanggalLahir()),
                String.valueOf(user.getPointUser())
        };

        float[] weights = {1.3f, 0.7f, 2f, 1f, 1f, 0.6f};

        for (int i = 0; i < userData.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(userData[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setPadding(5, 3, 5, 3);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i]
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
                Intent intent = new Intent(UserAdminActivity.this, LoadingScreenActivity.class);
                startActivity(intent);
                Toast.makeText(UserAdminActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popupMenu.show();
    }

    // Metode untuk menampilkan loader dan memulai aktivitas
    private void showLoaderAndStartActivity(final Class<?> targetActivity) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(UserAdminActivity.this, targetActivity);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);
    }
}
