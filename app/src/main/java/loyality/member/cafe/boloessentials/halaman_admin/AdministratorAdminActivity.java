
package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import com.acs.smartcard.Reader;
import com.acs.smartcard.ReaderException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;
import loyality.member.cafe.boloessentials.model.Admin;
import loyality.member.cafe.boloessentials.model.User;

public class AdministratorAdminActivity extends AppCompatActivity {
    private Button btnTambahAdministrator, btnPrevPage, btnNextPage, btn1, btn2, btn3;
    private Dialog mDialog, nfcDialog;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private TextView tvPointAdmin, tvAbsen, tvTukarHadiah, tvTukarPoint, tvDashboard, tvUser, tvAdministrator, tvHadiah;
    private RelativeLayout logout;
    private int currentPage = 1;
    private int totalPageCount;
    private static final int ITEMS_PER_PAGE = 9;
    private List<Admin> adminList = new ArrayList<>();
    private UsbManager mManager;
    private Reader mReader;
    private PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final String TAG = AdministratorAdminActivity.class.getSimpleName();

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

                TextView etNomorID = mDialog.findViewById(R.id.etNomorID);
                EditText etNama = mDialog.findViewById(R.id.etNama);
                EditText etEmail = mDialog.findViewById(R.id.etEmail);
                EditText etTelpon = mDialog.findViewById(R.id.etTelpon);
                TextView etTanggalLahir = mDialog.findViewById(R.id.etTanggalLahir);
                Button btnSubmit = mDialog.findViewById(R.id.btnSubmit);
                ProgressBar loader = new ProgressBar(AdministratorAdminActivity.this);
                Button btnDate = mDialog.findViewById(R.id.btnDate);
                Button btnTambahID = mDialog.findViewById(R.id.btnTambahID);

                btnTambahID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nfcDialog = new Dialog(AdministratorAdminActivity.this);
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
                                AdministratorAdminActivity.this,
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
                        String nomorID = etNomorID.getText().toString().trim();
                        String nama = etNama.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String telpon = etTelpon.getText().toString().trim();
                        String tanggalLahir = etTanggalLahir.getText().toString().trim();

                        // Cek apakah semua field diisi
                        if (nomorID.isEmpty() || nama.isEmpty() || email.isEmpty() || telpon.isEmpty() || tanggalLahir.isEmpty()) {
                            Toast.makeText(AdministratorAdminActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        loader.setVisibility(View.VISIBLE);

                        // Format tanggal bergabung
                        String tanggalBergabung = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        // Membuat objek User
                        Admin admin = new Admin(nomorID, nama, tanggalBergabung, email, telpon, tanggalLahir);
                        databaseReference.push().setValue(admin).addOnCompleteListener(task -> {
                            loader.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(AdministratorAdminActivity.this, "Administrator berhasil ditambahkan", Toast.LENGTH_SHORT).show();
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

        databaseReference.orderByChild("nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableLayout.removeAllViews();
                addTableHeader();

                adminList.clear();
                int adminCount = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Admin admin = dataSnapshot.getValue(Admin.class);
                    if (admin != null) {
                        adminList.add(admin);
                        adminCount++;
                    }
                }

                tvPointAdmin.setText(String.valueOf(adminCount));

                totalPageCount = (int) Math.ceil((double) adminList.size() / ITEMS_PER_PAGE);

                currentPage = 1;
                displayPageData();
                updatePaginationButtons();

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

    @Override
    public void onBackPressed() {
        // Memicu showPopupMenu saat tombol back ditekan
        showPopupMenu(logout);
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
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, adminList.size());

        for (int i = startIndex; i < endIndex; i++) {
            addAdminRow(adminList.get(i));
        }

        updatePaginationButtons();
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
            textView.setTypeface(null, Typeface.BOLD);
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
                admin.getNomorID(),
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

