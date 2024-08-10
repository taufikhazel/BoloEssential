package loyality.member.cafe.boloessentials.halaman_userandworker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.acs.smartcard.Reader;
import com.acs.smartcard.ReaderException;

import loyality.member.cafe.boloessentials.halaman_admin.DashboardAdminActivity;
import loyality.member.cafe.boloessentials.MainActivity;
import loyality.member.cafe.boloessentials.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etID;
    private Button btnAkses;
    private TextView tvClick;

    private UsbManager mManager;
    private Reader mReader;
    private PendingIntent mPermissionIntent;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etID = findViewById(R.id.etID);
        btnAkses = findViewById(R.id.btnAkses);
        tvClick = findViewById(R.id.tvClick);

        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mReader = new Reader(mManager);

        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

        btnAkses.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        tvClick.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, DashboardAdminActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeReader();  // Inisialisasi ulang NFC Reader setiap kali LoginActivity aktif
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReader.close();  // Pastikan NFC Reader ditutup saat LoginActivity dijeda
    }

    private void initializeReader() {
        mReader.setOnStateChangeListener((slotNum, prevState, currState) -> {
            if (currState == Reader.CARD_PRESENT) {
                Log.d(TAG, "NFC tag detected. Ready to read...");

                final byte[] command = {(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                final byte[] response = new byte[256];

                try {
                    int byteCount = mReader.control(slotNum, Reader.IOCTL_CCID_ESCAPE,
                            command, command.length, response, response.length);

                    // Get UID
                    StringBuffer uid = new StringBuffer();
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

        // Find connected NFC devices and request permission
        for (UsbDevice device : mManager.getDeviceList().values()) {
            if (mReader.isSupported(device)) {
                mManager.requestPermission(device, mPermissionIntent);
            }
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
                Toast.makeText(LoginActivity.this, "Failed to open NFC reader.", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "NFC reader opened successfully.");
            }
        }
    }

    @Override
    protected void onDestroy() {
        mReader.close();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}