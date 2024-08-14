package loyality.member.cafe.boloessentials.halaman_admin;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import com.acs.smartcard.Reader;
import com.acs.smartcard.ReaderException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;
import loyality.member.cafe.boloessentials.model.Absen;
import loyality.member.cafe.boloessentials.model.Karyawan;
import loyality.member.cafe.boloessentials.model.User;

public class KaryawanAdminActivity extends AppCompatActivity {
    private Button btnTambahKaryawan,btnPrevPage, btnNextPage, btn1, btn2, btn3;
    private Dialog mDialog;
    private Dialog nfcDialog;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private TextView tvNamaKaryawan, tvPointKaryawan, tvDashboard, tvTukarPoint, tvTukarHadiah, tvAdministrator, tvUser, tvAbsen, tvHadiah;
    private RelativeLayout logout;
    private DatabaseReference mDatabase;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private int currentPage = 1;
    private ProgressDialog progressDialog;
    private int totalPageCount;
    private static final int ITEMS_PER_PAGE = 9;
    private List<Karyawan> karyawanList = new ArrayList<>();
    private UsbManager mManager;
    private Reader mReader;
    private PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final String TAG = KaryawanAdminActivity.class.getSimpleName();
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

        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mReader = new Reader(mManager);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("karyawan");

        btnTambahKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.modal_tambah_karyawan_admin);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();

                TextView etNomorID = mDialog.findViewById(R.id.etNomorID);
                EditText etNama = mDialog.findViewById(R.id.etNama);
                EditText etEmail = mDialog.findViewById(R.id.etEmail);
                EditText etTelpon = mDialog.findViewById(R.id.etTelpon);
                TextView etTanggalLahir = mDialog.findViewById(R.id.etTanggalLahir);
                Button btnSubmit = mDialog.findViewById(R.id.btnSubmit);
                ProgressBar loader = new ProgressBar(KaryawanAdminActivity.this);
                Button btnDate = mDialog.findViewById(R.id.btnDate);
                Button btnTambahID = mDialog.findViewById(R.id.btnTambahID);

                btnTambahID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nfcDialog = new Dialog(KaryawanAdminActivity.this);
                        nfcDialog.setContentView(R.layout.modal_nfc);
                        nfcDialog.setCancelable(true);

                        TextView UID = nfcDialog.findViewById(R.id.UID);
                        Button btnAcceptUID = nfcDialog.findViewById(R.id.btnAcceptUID);

                        nfcDialog.show();

                        initializeReader(UID, btnAcceptUID);
                    }
                });


                btnDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Mendapatkan tanggal saat ini
                        final Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        // Membuka dialog tanggal
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                KaryawanAdminActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        // Menampilkan tanggal yang dipilih di EditText
                                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                        etTanggalLahir.setText(selectedDate);
                                    }
                                },
                                year, month, day);
                        datePickerDialog.show();
                    }
                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Mengambil nilai dari EditText
                        String nomorIDKaryawan = etNomorID.getText().toString().trim();
                        String nama = etNama.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String telpon = etTelpon.getText().toString().trim();
                        String tanggalLahir = etTanggalLahir.getText().toString().trim();

                        // Cek apakah semua field diisi
                        if (nomorIDKaryawan.isEmpty() || nama.isEmpty() || email.isEmpty() || telpon.isEmpty() || tanggalLahir.isEmpty()) {
                            Toast.makeText(KaryawanAdminActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Cek apakah nomorIDKaryawan sudah ada di database
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("karyawan");
                        userRef.orderByChild("nomorIDKaryawanKaryawan").equalTo(nomorIDKaryawan).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Jika nomorIDKaryawan sudah ada, tampilkan alert dan reset field
                                    Toast.makeText(KaryawanAdminActivity.this, "ID sudah digunakan, gunakan id lain", Toast.LENGTH_SHORT).show();
                                    etNomorID.setText(""); // Mengosongkan field nomorIDKaryawan
                                } else {
                                    // Jika nomorIDKaryawan belum ada, lanjutkan dengan penambahan karyawan
                                    loader.setVisibility(View.VISIBLE);

                                    // Format tanggal bergabung
                                    String tanggalBergabung = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                    // Membuat objek Karyawan
                                    Karyawan karyawan = new Karyawan(nomorIDKaryawan, nama, tanggalBergabung, email, telpon, tanggalLahir);
                                    databaseReference.push().setValue(karyawan).addOnCompleteListener(task -> {
                                        loader.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(KaryawanAdminActivity.this, "Karyawan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                            mDialog.dismiss();
                                        } else {
                                            Toast.makeText(KaryawanAdminActivity.this, "Gagal menambahkan karyawan", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle possible errors
                                Toast.makeText(KaryawanAdminActivity.this, "Gagal memeriksa ID", Toast.LENGTH_SHORT).show();
                            }
                        });
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

    @Override
    protected void onResume() {
        super.onResume(); // Pastikan NFC Reader diinisialisasi ketika UserAdminActivity aktif
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReader != null) {
            mReader.close();  // Pastikan NFC Reader ditutup saat UserAdminActivity tidak aktif
        }
    }

    private void initializeReader(TextView UID, Button btnAcceptUID) {
        if (mReader != null) {
            for (UsbDevice device : mManager.getDeviceList().values()) {
                if (mReader.isSupported(device)) {
                    if (mManager.hasPermission(device)) {
                        new OpenTask().execute(device);
                    } else {
                        mManager.requestPermission(device, mPermissionIntent);
                    }
                }
            }

            mReader.setOnStateChangeListener((slotNum, prevState, currState) -> {
                if (currState == Reader.CARD_PRESENT) {
                    Log.d(TAG, "NFC tag detected. Ready to read...");

                    final byte[] command = {(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                    final byte[] response = new byte[256];

                    try {
                        int byteCount = mReader.control(slotNum, Reader.IOCTL_CCID_ESCAPE,
                                command, command.length, response, response.length);

                        StringBuilder uid = new StringBuilder();
                        for (int i = 0; i < (byteCount - 2); i++) {
                            uid.append(String.format("%02X", response[i]));
                        }

                        Log.d(TAG, "Detected NFC UID: " + uid.toString());

                        runOnUiThread(() -> {
                            Long result = Long.parseLong(uid.toString(), 16);
                            UID.setText(String.valueOf(result));
                        });

                    } catch (ReaderException | NumberFormatException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "NFC tag read failed. Please try again.", Toast.LENGTH_LONG).show());
                    }
                }
            });

            btnAcceptUID.setOnClickListener(v -> {
                if (nfcDialog != null && nfcDialog.isShowing()) {
                    // Mengisi nilai dari TextView UID ke dalam EditText di dialog utama
                    TextView etNomorID = mDialog.findViewById(R.id.etNomorID);
                    if (etNomorID != null) {
                        etNomorID.setText(UID.getText().toString());
                    }
                    nfcDialog.dismiss(); // Menutup dialog NFC
                }
            });
        }
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            new OpenTask().execute(device);
                        }
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                synchronized (this) {
                    mReader.close();
                }
            }
        }
    };

    private class OpenTask extends AsyncTask<UsbDevice, Void, Exception> {
        @Override
        protected Exception doInBackground(UsbDevice... params) {
            Exception result = null;
            try {
                mReader.open(params[0]);
            } catch (Exception e) {
                result = e;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                Toast.makeText(getApplicationContext(), "Error opening NFC reader", Toast.LENGTH_LONG).show();
            }
        }
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
        Dialog previewDialog = new Dialog(this);
        previewDialog.setContentView(R.layout.modal_karyawan_admin);
        if (previewDialog.getWindow() != null) {
            previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Bind the views
        TextView tvNamaKaryawan = previewDialog.findViewById(R.id.tvNamaKaryawan);
        TableLayout tableLayout = previewDialog.findViewById(R.id.tableLayoutKaryawan);

        // Set the employee's name
        tvNamaKaryawan.setText(karyawan.getNama());

        // Dynamically create the table header
        createTableHeader(tableLayout);

        // Query Firebase database for attendance records of the selected employee
        DatabaseReference absenKaryawanRef = FirebaseDatabase.getInstance().getReference("absenKaryawan");
        absenKaryawanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Navigate through each level of the database
                for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) { // Day level
                    for (DataSnapshot monthSnapshot : daySnapshot.getChildren()) { // Month level
                        for (DataSnapshot yearSnapshot : monthSnapshot.getChildren()) { // Year level
                            for (DataSnapshot absensiKeSnapshot : yearSnapshot.getChildren()) { // AbsensiKe level
                                // Check if there are further levels under AbsensiKe
                                if (absensiKeSnapshot.hasChildren()) {
                                    // Process each child under AbsensiKe
                                    for (DataSnapshot childSnapshot : absensiKeSnapshot.getChildren()) {
                                        String childKey = childSnapshot.getKey(); // Get the key of the child
                                        Object value = childSnapshot.getValue();

                                        if (value instanceof Map) {
                                            Map<String, Object> absenMap = (Map<String, Object>) value;
                                            if (absenMap != null) {
                                                // Create Absen object
                                                Absen absen = new Absen(absenMap);
                                                // Use the key and value as needed
                                                Log.d("DataCheck", "childKey: " + childKey); // Log the key of the child
                                                addRowToTable(tableLayout, previewDialog, absen, childKey);
                                            } else {
                                                Log.e("DataError", "Received null map for child: " + childKey);
                                            }
                                        } else if (value instanceof List) {
                                            List<Map<String, Object>> absenList = (List<Map<String, Object>>) value;
                                            for (Map<String, Object> absenMap : absenList) {
                                                if (absenMap != null) {
                                                    Absen absen = new Absen(absenMap);
                                                    addRowToTable(tableLayout, previewDialog, absen, childKey);
                                                } else {
                                                    Log.e("DataError", "Received null map in list for child: " + childKey);
                                                }
                                            }
                                        } else {
                                            Log.e("DataError", "Unexpected data type for child: " + childKey);
                                        }
                                    }
                                } else {
                                    // Process AbsensiKe directly if no further levels
                                    Object value = absensiKeSnapshot.getValue();

                                    if (value instanceof Map) {
                                        Map<String, Object> absenMap = (Map<String, Object>) value;
                                        if (absenMap != null) {
                                            String absensiKe = absensiKeSnapshot.getKey(); // Get the key for AbsensiKe
                                            Log.d("DataCheck", "absensiKe: " + absensiKe); // Log the value of absensiKe
                                            Absen absen = new Absen(absenMap);
                                            addRowToTable(tableLayout, previewDialog, absen, absensiKe);
                                        } else {
                                            Log.e("DataError", "Received null map for absensiKe: " + absensiKeSnapshot.getKey());
                                        }
                                    } else if (value instanceof List) {
                                        List<Map<String, Object>> absenList = (List<Map<String, Object>>) value;
                                        String absensiKe = absensiKeSnapshot.getKey(); // Get the key for AbsensiKe
                                        Log.d("DataCheck", "absensiKe: " + absensiKe); // Log the value of absensiKe
                                        for (Map<String, Object> absenMap : absenList) {
                                            if (absenMap != null) {
                                                Absen absen = new Absen(absenMap);
                                                addRowToTable(tableLayout, previewDialog, absen, absensiKe);
                                            } else {
                                                Log.e("DataError", "Received null map in list for absensiKe: " + absensiKe);
                                            }
                                        }
                                    } else {
                                        Log.e("DataError", "Unexpected data type for absensiKe: " + absensiKeSnapshot.getKey());
                                    }
                                }
                            }
                        }
                    }
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        previewDialog.show();
    }

    private void addRowToTable(TableLayout tableLayout, Dialog previewDialog, Absen absen, String absensiKe) {
        TableRow row = new TableRow(previewDialog.getContext());
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        // Column 1: Hari
        TextView hariText = new TextView(previewDialog.getContext());
        hariText.setText(absen.getHari());
        hariText.setGravity(Gravity.CENTER);
        hariText.setTextSize(12);
        hariText.setLayoutParams(createColumnLayoutParams(1, 0)); // Use the createColumnLayoutParams method
        row.addView(hariText);

        // Column 2: Tanggal
        TextView tanggalText = new TextView(previewDialog.getContext());
        tanggalText.setText(absen.getTanggal());
        tanggalText.setGravity(Gravity.CENTER);
        tanggalText.setTextSize(12);
        tanggalText.setLayoutParams(createColumnLayoutParams(1, 0)); // Use the createColumnLayoutParams method
        row.addView(tanggalText);

        // Column 3: Jam
        TextView jamText = new TextView(previewDialog.getContext());
        jamText.setText(absen.getJam());
        jamText.setGravity(Gravity.CENTER);
        jamText.setTextSize(12);
        jamText.setLayoutParams(createColumnLayoutParams(0.5f, 0)); // Use the createColumnLayoutParams method
        row.addView(jamText);

        // Column 4: Absen Status based on absensiKe
        TextView statusText = new TextView(previewDialog.getContext());
        String status = "Absen ke " + absensiKe;
        statusText.setText(status);
        statusText.setGravity(Gravity.CENTER);
        statusText.setTextSize(12);
        statusText.setLayoutParams(createColumnLayoutParams(1, 20)); // Use the createColumnLayoutParams method
        row.addView(statusText);

        // Add the row to the table
        tableLayout.addView(row);
    }

    private TableRow.LayoutParams createColumnLayoutParams(float weight, int marginLeft) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0, // width 0 so that weight works
                TableRow.LayoutParams.WRAP_CONTENT,
                weight // layout_weight
        );
        if (marginLeft > 0) {
            params.setMargins(marginLeft, 0, 0, 0);
        }
        return params;
    }

    private void createTableHeader(TableLayout tableLayout) {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        headerRow.setBackgroundColor(getResources().getColor(R.color.brownAdmin));
        headerRow.setPadding(5, 5, 5, 5);

        String[] headers = {"Hari", "Tanggal", "Jam", "Keterangan"};
        float[] weights = {1, 1, 0.5f, 1}; // Same weights as in the XML

        for (int i = 0; i < headers.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(headers[i]);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setPadding(5, 0, 5, 0);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // width 0 so that weight works
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i] // layout_weight
            );
            if (i != 0) {
                params.setMargins(20, 0, 0, 0); // Margin as defined in the XML
            }
            textView.setLayoutParams(params);
            headerRow.addView(textView);
        }

        tableLayout.addView(headerRow);
    }

    // Metode untuk memformat tanggal bergabung dari "yyyy-mm-dd" menjadi "dd-mm-yyyy"
    private String formatTanggalBergabung(String tanggalBergabung) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
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
            return day  + month  + year;
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

        if (totalPageCount == 0) {
            btnNextPage.setVisibility((View.INVISIBLE));
            btnPrevPage.setVisibility((View.INVISIBLE));
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
        } else if (totalPageCount == 1) {
            btn1.setText("1");
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
        } else if (totalPageCount == 2) {
            btn1.setText("1");
            btn2.setText("2");
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.INVISIBLE);
        } else {
            // Mengatur teks tombol berdasarkan halaman saat ini
            if (currentPage == 1) {
                btn1.setText("1");
                btn2.setText("2");
                btn3.setText("3");
            } else if (currentPage == totalPageCount) {
                btn1.setText(String.valueOf(totalPageCount - 2));
                btn2.setText(String.valueOf(totalPageCount - 1));
                btn3.setText(String.valueOf(totalPageCount));
            } else {
                btn1.setText(String.valueOf(currentPage - 1));
                btn2.setText(String.valueOf(currentPage));
                btn3.setText(String.valueOf(currentPage + 1));
            }

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
