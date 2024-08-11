package loyality.member.cafe.boloessentials.halaman_userandworker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.acs.smartcard.Reader;
import com.acs.smartcard.ReaderException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import loyality.member.cafe.boloessentials.MainActivity;
import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_admin.DashboardAdminActivity;

public class LoginActivity extends AppCompatActivity {
    private TextView etID;
    private Button btnAkses;
    private TextView tvClick;

    private UsbManager mManager;
    private Reader mReader;
    private ImageButton refresh;
    private PendingIntent mPermissionIntent;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final String TAG = LoginActivity.class.getSimpleName();
    private DatabaseReference databaseRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etID = findViewById(R.id.etID);
        btnAkses = findViewById(R.id.btnAkses);
        tvClick = findViewById(R.id.tvClick);

        refresh = findViewById(R.id.refresh);
        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mReader = new Reader(mManager);

        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu Sebentar...");
        progressDialog.setCancelable(false);

        refresh.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, LoadingScreenActivity.class);
            startActivity(intent);
        });

        btnAkses.setOnClickListener(view -> {
            String id = etID.getText().toString();
            if (!id.isEmpty()) {
                progressDialog.show(); // Show ProgressDialog
                checkUser(id);
            } else {
                Toast.makeText(LoginActivity.this, "ID cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        tvClick.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, DashboardAdminActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeReader();  // Ensure NFC Reader is initialized when LoginActivity is active
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReader != null) {
            mReader.close();  // Ensure NFC Reader is closed when LoginActivity is paused
        }
    }

    private void initializeReader() {
        if (mReader != null) {
            // Find connected NFC devices and request permission
            for (UsbDevice device : mManager.getDeviceList().values()) {
                if (mReader.isSupported(device)) {
                    if (mManager.hasPermission(device)) {
                        new OpenTask().execute(device);
                    } else {
                        mManager.requestPermission(device, mPermissionIntent);
                    }
                }
            }

            // Set NFC Reader listener
            mReader.setOnStateChangeListener((slotNum, prevState, currState) -> {
                if (currState == Reader.CARD_PRESENT) {
                    Log.d(TAG, "NFC tag detected. Ready to read...");

                    final byte[] command = {(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                    final byte[] response = new byte[256];

                    try {
                        int byteCount = mReader.control(slotNum, Reader.IOCTL_CCID_ESCAPE,
                                command, command.length, response, response.length);

                        // Get UID
                        StringBuilder uid = new StringBuilder();
                        for (int i = 0; i < (byteCount - 2); i++) {
                            uid.append(String.format("%02X", response[i]));
                        }

                        // Log the detected UID
                        Log.d(TAG, "Detected NFC UID: " + uid.toString());

                        runOnUiThread(() -> {
                            Long result = Long.parseLong(uid.toString(), 16);
                            etID.setText(String.valueOf(result));
                        });

                    } catch (ReaderException | NumberFormatException e) {
                        e.printStackTrace();
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "NFC tag read failed. Please try again.", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            });
        }
    }

    private void checkUser(String id) {
        databaseRef.child("admin").orderByChild("nomorID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss(); // Dismiss ProgressDialog
                    DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                    String nama = userSnapshot.child("nama").getValue(String.class);
                    showSuccessDialog("Selamat Datang ke Bolo Essential, " + nama, DashboardAdminActivity.class, "admin");
                } else {
                    checkKaryawanOrUsers(id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss(); // Dismiss ProgressDialog
                Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkKaryawanOrUsers(String id) {
        databaseRef.child("karyawan").orderByChild("nomorIDKaryawan").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss(); // Dismiss ProgressDialog
                    DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                    String namaKaryawan = userSnapshot.child("namaKaryawan").getValue(String.class);
                    showSuccessDialog("Selamat Datang ke Bolo Essential, " + namaKaryawan, MainActivity.class, "karyawan");
                } else {
                    databaseRef.child("users").orderByChild("nomorID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                progressDialog.dismiss(); // Dismiss ProgressDialog
                                DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                                String nama = userSnapshot.child("nama").getValue(String.class);
                                showSuccessDialog("Selamat Datang ke Bolo Essential, " + nama, MainActivity.class, "users");
                            } else {
                                progressDialog.dismiss(); // Dismiss ProgressDialog
                                Toast.makeText(LoginActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss(); // Dismiss ProgressDialog
                            Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss(); // Dismiss ProgressDialog
                Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog(String message, Class<?> nextActivity, String userType) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Intent intent = new Intent(LoginActivity.this, nextActivity);
                    intent.putExtra("USER_TYPE", userType); // Kirim tipe pengguna ke MainActivity
                    intent.putExtra("UID", etID.getText().toString()); // Kirim UID ke MainActivity
                    startActivity(intent);
                    finish();
                })
                .show();
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
}
