package loyality.member.cafe.boloessentials.halaman_admin;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class KaryawanAdminActivity extends AppCompatActivity {
    private Button btnTambahKaryawan,btnPrevPage, btnNextPage, btn1, btn2, btn3;
    private Dialog mDialog;
    private Dialog Dialog;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private TextView tvPointKaryawan, tvDashboard, tvTukarPoint, tvTukarHadiah, tvAdministrator, tvUser, tvAbsen, tvHadiah;
    private RelativeLayout logout;
    private DatabaseReference mDatabase;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private int currentPage = 1;
    private ProgressDialog progressDialog;
    private int totalPageCount;
    private static final int ITEMS_PER_PAGE = 9;
    private List<Karyawan> karyawanList = new ArrayList<>();
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

        btnPrevPage = findViewById(R.id.btnPrevious);
        btnNextPage = findViewById(R.id.btnNext);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);

        // Tambahkan OnClickListener untuk tombol pagination di dalam onCreate
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage = Integer.parseInt(btn1.getText().toString());
                displayPageData();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage = Integer.parseInt(btn2.getText().toString());
                displayPageData();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage = Integer.parseInt(btn3.getText().toString());
                displayPageData();
            }
        });


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

        btnPrevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    displayPageData();
                    updatePaginationButtons();
                }
            }
        });

        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < totalPageCount) {
                    currentPage++;
                    displayPageData();
                    updatePaginationButtons();
                }
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

                karyawanList.clear();
                int karyawanCount = 0;

                // Menambahkan data user ke tabel
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Karyawan karyawan = dataSnapshot.getValue(Karyawan.class);
                    if (karyawan != null) {
                        karyawanList.add(karyawan);
                        karyawanCount++;
                    }
                }

                tvPointKaryawan.setText(String.valueOf(karyawanCount));

                // Membalik urutan list untuk menampilkan secara descending
                Collections.reverse(karyawanList);

                totalPageCount = (int) Math.ceil((double) karyawanList.size() / ITEMS_PER_PAGE);

                currentPage = 1;
                displayPageData();
                updatePaginationButtons();

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
        setupExportButton();
    }

    @Override
    public void onBackPressed() {
        // Memicu showPopupMenu saat tombol back ditekan
        showPopupMenu(logout);
    }

    private void displayPageData() {
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, karyawanList.size());

        for (int i = startIndex; i < endIndex; i++) {
            addKaryawanRow(karyawanList.get(i));
        }
        updatePaginationButtons();
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
        actionButton.setText("Absen");
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
        Dialog previewDialog = new Dialog(this);
        previewDialog.setContentView(R.layout.modal_karyawan_admin);
        if (previewDialog.getWindow() != null) {
            previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        previewDialog.show();
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
            return tanggalLahir; // Mengembalikan string asli jika format tidak sesuai
        }
    }

    private void updatePaginationButtons() {
        // Reset semua tombol ke warna default
        btn1.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
        btn2.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
        btn3.setBackgroundTintList(getResources().getColorStateList(R.color.gray));

        btn1.setTextColor(getResources().getColor(R.color.black));
        btn2.setTextColor(getResources().getColor(R.color.black));
        btn3.setTextColor(getResources().getColor(R.color.black));

        // Menandai tombol berdasarkan halaman
        if (totalPageCount == 1) {
            btn1.setText("1");
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
        } else if (totalPageCount == 2) {
            btn1.setText("1");
            btn2.setText("2");
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.INVISIBLE);
        } else {
            btn1.setText(String.valueOf(Math.max(currentPage - 1, 1)));
            btn2.setText(String.valueOf(currentPage));
            btn3.setText(String.valueOf(Math.min(currentPage + 1, totalPageCount)));
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
        }

        // Menandai tombol aktif dengan warna brownAdmin
        if (currentPage == Integer.parseInt(btn1.getText().toString())) {
            btn1.setBackgroundTintList(getResources().getColorStateList(R.color.brownAdmin));
            btn1.setTextColor(getResources().getColor(R.color.white));
        } else if (currentPage == Integer.parseInt(btn2.getText().toString())) {
            btn2.setBackgroundTintList(getResources().getColorStateList(R.color.brownAdmin));
            btn2.setTextColor(getResources().getColor(R.color.white));
        } else if (currentPage == Integer.parseInt(btn3.getText().toString())) {
            btn3.setBackgroundTintList(getResources().getColorStateList(R.color.brownAdmin));
            btn3.setTextColor(getResources().getColor(R.color.white));
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
    private void setupExportButton() {
        Button btnExport = findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExportConfirmationDialog();
            }
        });
    }

    private void showExportConfirmationDialog() {
        // Dialog untuk konfirmasi export
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Export");
        builder.setMessage("Apakah Anda ingin melakukan export data?");

        // Jika user klik "Ya"
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showFileNameInputDialog();
            }
        });

        // Jika user klik "Tidak"
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Tampilkan dialog konfirmasi
        builder.create().show();
    }

    private void showFileNameInputDialog() {
        // Membuat dialog input untuk meminta nama file dari user
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nama File");

        // EditText untuk user input
        final EditText input = new EditText(this);
        input.setHint("Masukkan nama file");
        builder.setView(input);

        // Jika user klik "Simpan"
        builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fileName = input.getText().toString().trim();
                if (!fileName.isEmpty()) {
                    exportDataToExcel(fileName);
                } else {
                    Toast.makeText(KaryawanAdminActivity.this, "Nama file tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Jika user klik "Batal"
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Tampilkan dialog input nama file
        builder.create().show();
    }

    private void exportDataToExcel(String fileName) {
        progressDialog = new ProgressDialog(KaryawanAdminActivity.this);
        progressDialog.setTitle("Exporting Data");
        progressDialog.setMessage("Please wait while the data is being exported to an Excel file...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("karyawan");
        databaseReference.orderByChild("tanggalBergabung").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Karyawan> karyawanList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Karyawan karyawan = dataSnapshot.getValue(Karyawan.class);
                    if (karyawan != null) {
                        karyawanList.add(karyawan);
                    }
                }

                new Thread(() -> {
                    try {
                        File file = createExcelFile(karyawanList);
                        if (file != null) {
                            saveFileToDownloads(file, fileName);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            Toast.makeText(KaryawanAdminActivity.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                        });
                    } finally {
                        runOnUiThread(() -> {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                runOnUiThread(() -> {
                    Toast.makeText(KaryawanAdminActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void saveFileToDownloads(File file, String fileName) {
        new Thread(() -> {
            try {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File destFile = new File(downloadsDir, fileName + ".xlsx"); // Menggunakan nama file dari user
                try (FileInputStream inStream = new FileInputStream(file);
                     FileOutputStream outStream = new FileOutputStream(destFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, length);
                    }
                }

                runOnUiThread(() -> {
                    Toast.makeText(KaryawanAdminActivity.this, "File berhasil disimpan di Downloads", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(KaryawanAdminActivity.this, "Gagal menyimpan file", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }


    // Helper method to create an Excel file
    private File createExcelFile(List<Karyawan> karyawanList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Karyawan");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Nama Karyawan", "Tanggal Bergabung", "Email", "No Telepon", "Tanggal Lahir"};
        int cellIndex = 0;
        for (String header : headers) {
            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(header);
        }

        // Create data rows
        int rowIndex = 1;
        for (Karyawan karyawan : karyawanList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(karyawan.getNama());
            row.createCell(1).setCellValue(formatTanggalBergabung(karyawan.getTanggalBergabung()));
            row.createCell(2).setCellValue(karyawan.getEmail());
            row.createCell(3).setCellValue(karyawan.getTelpon());
            row.createCell(4).setCellValue(formatTanggalLahir(karyawan.getTanggalLahir()));
        }

        // Write the output to a file
        File file = new File(getExternalFilesDir(null), "KaryawanData.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
        workbook.close();
        return file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. Unable to save file.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
