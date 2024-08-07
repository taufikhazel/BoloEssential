package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;
import loyality.member.cafe.boloessentials.model.Karyawan;
import loyality.member.cafe.boloessentials.model.User;

public class KaryawanAdminActivity extends AppCompatActivity {
    private Button btnTambahKaryawan;
    private Dialog mDialog;

    private Dialog Dialog;
    private ProgressBar progressBar;
    private TableLayout tableLayout;

    private TextView tvPointKaryawan, tvDashboard, tvTukarPoint, tvTukarHadiah, tvAdministrator, tvUser, tvAbsen, tvHadiah;

    private RelativeLayout logout;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan_admin);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        tableLayout = findViewById(R.id.tableLayoutKaryawan);
        tableLayout.setVisibility(View.GONE);

        int textColor = getIntent().getIntExtra("textColorKaryawan", R.color.brownAdmin);
        tvAbsen = findViewById(R.id.tvAbsen);
        tvAbsen.setTextColor(getResources().getColor(textColor));
        tvPointKaryawan = findViewById(R.id.tvPointKaryawan);

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

        btnTambahKaryawan = findViewById(R.id.btnTambahKaryawan);
        tableLayout = findViewById(R.id.tableLayoutKaryawan);
        mDialog = new Dialog(this);

        btnTambahKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.modal_tambah_karyawan_admin);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();

                Button btnSubmitTambahKaryawan = mDialog.findViewById(R.id.btnSubmitTambahKaryawan);
                btnSubmitTambahKaryawan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });



            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("karyawan");
        databaseReference.orderByChild("tanggalBergabung").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Menghapus baris yang ada di tabel
                tableLayout.removeAllViews();

                // Menambahkan header tabel
                addTableHeader();

                // Menyimpan data pengguna dalam list untuk dibalik urutannya
                List<Karyawan> karyawanList = new ArrayList<>();
                int karyawanCount = 0;

                // Menambahkan data user ke tabel
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Karyawan karyawan = dataSnapshot.getValue(Karyawan.class);
                    if (karyawan != null) {
                        addKaryawanRow(karyawan);
                        karyawanCount++;
                    }
                }

                tvPointKaryawan.setText(String.valueOf(karyawanCount));

                // Membalik urutan list untuk menampilkan secara descending
                Collections.reverse(karyawanList);

                // Menambahkan data user yang sudah diurutkan ke tabel
                for (Karyawan karyawan : karyawanList) {
                    addKaryawanRow(karyawan);
                }

                // Menyembunyikan ProgressBar dan menampilkan tabel setelah data diambil
                progressBar.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(KaryawanAdminActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);
        String[] headers = {"Nama Karyawan", "Tanggal Bergabung", "Email", "No Telepon", "Tanggal Lahir", "Aksi"};
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

    private void addKaryawanRow(Karyawan karyawan) {
        TableRow row = new TableRow(this);
        String[] karyawanData = {
                karyawan.getNama(),
                formatTanggalBergabung(karyawan.getTanggalBergabung()), // Memformat tanggal bergabung
                karyawan.getEmail(),
                karyawan.getTelpon(),
                formatTanggalLahir(karyawan.getTanggalLahir()) // Memformat tanggal lahir
        };

        Log.d("KaryawanData", "Nama: " + karyawanData[0] +
                ", Tanggal Bergabung: " + karyawanData[1] +
                ", Email: " + karyawanData[2] +
                ", Telpon: " + karyawanData[3] +
                ", Tanggal Lahir: " + karyawanData[4]);

        float[] weights = {1.3f, 0.7f, 2f, 1f, 1f, 0.6f}; // Bobot yang diinginkan untuk setiap kolom

        for (int i = 0; i < karyawanData.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(karyawanData[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setPadding(5, 3, 5, 3);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // width 0 agar weight bekerja
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i] // layout_weight
            );
            int marginInPixels = (int) (7 * getResources().getDisplayMetrics().density);
            params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
            textView.setLayoutParams(params);
            row.addView(textView);
        }

        // Menambahkan kolom untuk aksi
        TextView actionButton = new TextView(this);
        actionButton.setText("Preview");
        actionButton.setTextColor(getResources().getColor(R.color.brownAdmin));
        actionButton.setGravity(Gravity.CENTER);
        actionButton.setPadding(5, 5, 5, 5);
        actionButton.setTextSize(12);
        actionButton.setBackgroundResource(R.drawable.preview_border);

        TableRow.LayoutParams actionParams = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                weights[weights.length - 1] // Menggunakan bobot terakhir untuk kolom aksi
        );
        int marginInPixels = (int) (7 * getResources().getDisplayMetrics().density);
        actionParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        actionButton.setLayoutParams(actionParams);
        row.addView(actionButton);

        // Menetapkan aksi klik pada tombol Preview
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementasikan aksi preview di sini
                showKaryawanPreview(karyawan);
            }
        });

        tableLayout.addView(row);
    }

    private void showKaryawanPreview(Karyawan karyawan) {
        // Inflate custom layout for dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.modal_absen_karyawan, null);
        // Build and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    }



    // Metode untuk memformat tanggal bergabung dari "yyyy-mm-dd" menjadi "dd-mm-yyyy"
    private String formatTanggalBergabung(String tanggalBergabung) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(tanggalBergabung);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return tanggalBergabung; // Mengembalikan string asli jika terjadi kesalahan parsing
        }
    }
    // Metode untuk memformat tanggal lahir dari "ddmmyyyy" menjadi "dd-mm-yyyy"
    private String formatTanggalLahir(String tanggalLahir) {
        if (tanggalLahir != null && tanggalLahir.length() == 8) {
            String day = tanggalLahir.substring(0, 2);
            String month = tanggalLahir.substring(2, 4);
            String year = tanggalLahir.substring(4, 8);
            return day + "-" + month + "-" + year;
        } else {
            return tanggalLahir; // Mengembalikan string asli jika format tidak sesuai
        }
    }


    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_dropdown);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(KaryawanAdminActivity.this, LoadingScreenActivity.class);
                startActivity(intent);
                Toast.makeText(KaryawanAdminActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(KaryawanAdminActivity.this, targetActivity);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);
    }
}
