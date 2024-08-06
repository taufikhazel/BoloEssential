
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;
import loyality.member.cafe.boloessentials.model.Admin;
import loyality.member.cafe.boloessentials.model.User;

public class AdministratorAdminActivity extends AppCompatActivity {
    private Button btnTambahAdministrator;
    private Dialog mDialog;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private TextView tvPointAdmin, tvAbsen, tvTukarHadiah, tvTukarPoint, tvDashboard, tvUser, tvAdministrator, tvHadiah;
    private RelativeLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_admin);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        int textColor = getIntent().getIntExtra("textColorAdministrator", R.color.brownAdmin);
        tvAdministrator = findViewById(R.id.tvAdministrator);
        tvAdministrator.setTextColor(getResources().getColor(textColor));
        tvPointAdmin = findViewById(R.id.tvPointAdmin);

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        btnTambahAdministrator = findViewById(R.id.btnTambahAdministrator);
        tableLayout = findViewById(R.id.tableLayout);

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

        tvHadiah = findViewById(R.id.tvHadiah);
        tvHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoaderAndStartActivity(HadiahAdminActivity.class);
            }
        });

        mDialog = new Dialog(this);
        tableLayout = findViewById(R.id.tableLayout);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin");
        btnTambahAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.modal_tambah_administrator_admin);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();

                EditText etNama = mDialog.findViewById(R.id.etNamaAdmin);
                EditText etEmail = mDialog.findViewById(R.id.etEmailAdmin);
                EditText etnomorID = mDialog.findViewById(R.id.etNomorIDAdmin);
                EditText etnotelp = mDialog.findViewById(R.id.etTelponAdmin);
                Button btnSubmit = mDialog.findViewById(R.id.btnSubmit);

                ProgressBar loader = new ProgressBar(AdministratorAdminActivity.this);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nama = etNama.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String nomorID = etnomorID.getText().toString().trim();
                        String notelp = etnotelp.getText().toString().trim();

                        if (nama.isEmpty() || email.isEmpty() || nomorID.isEmpty() || notelp.isEmpty()) {
                            Toast.makeText(AdministratorAdminActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        loader.setVisibility(View.VISIBLE);

                        Admin admin = new Admin(nama,email, nomorID, notelp);
                        databaseReference.push().setValue(admin).addOnCompleteListener(task -> {
                            loader.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(AdministratorAdminActivity.this, "User berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            } else {
                                Toast.makeText(AdministratorAdminActivity.this, "Gagal menambahkan user", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        tableLayout.setVisibility(View.GONE);

        databaseReference.orderByChild("nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableLayout.removeAllViews();
                addTableHeader();

                List<Admin> adminList = new ArrayList<>();
                int adminCount = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Admin admin = dataSnapshot.getValue(Admin.class);
                    if (admin != null) {
                        adminList.add(admin);
                        adminCount++;
                    }
                }

                tvPointAdmin.setText(String.valueOf(adminCount));

                for (Admin admin : adminList) {
                    addAdminRow(admin);
                }

                progressBar.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdministratorAdminActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);
        String[] headers = {"Nama Admin", "Email Admin", "Nomor ID", "No Telepon"};
        float[] weights = {1f, 2f, 0.8f, 1f};

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

    private void addAdminRow(Admin admin) {
        TableRow row = new TableRow(this);
        String[] adminData = {
                admin.getNama(),
                admin.getEmail(),
                admin.getnomorID(),
                admin.getTelpon()
        };

        float[] weights = {1f, 2f, 0.8f, 1f};

        for (int i = 0; i < adminData.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(adminData[i]);
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

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_dropdown);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(AdministratorAdminActivity.this, LoadingScreenActivity.class);
                startActivity(intent);
                Toast.makeText(AdministratorAdminActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(AdministratorAdminActivity.this, targetActivity);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);
    }
}

