package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.halaman_userandworker.LoadingScreenActivity;

public class KaryawanAdminActivity extends AppCompatActivity {
    private Button btnTambahKaryawan;
    private Dialog mDialog;
    private DatabaseReference mDatabase;
    private TableLayout tableLayout;
    private TextView tvDashboard, tvTukarPoint, tvTukarHadiah, tvAdministrator, tvUser, tvAbsen;
    private RelativeLayout logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan_admin);

        int textColor = getIntent().getIntExtra("textColorKaryawan", R.color.brownAdmin);
        tvAbsen = findViewById(R.id.tvAbsen);
        tvAbsen.setTextColor(getResources().getColor(textColor));

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
                Intent intent = new Intent(KaryawanAdminActivity.this, DashboardAdminActivity.class);
                startActivity(intent);
            }
        });
        tvUser = findViewById(R.id.tvUser);

        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KaryawanAdminActivity.this, UserAdminActivity.class);
                startActivity(intent);
            }
        });
        tvTukarPoint = findViewById(R.id.tvTukarPoint);
        tvTukarPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KaryawanAdminActivity.this, TukarPointAdminActivity.class);
                startActivity(intent);
            }
        });
        tvTukarHadiah = findViewById(R.id.tvTukarHadiah);
        tvTukarHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KaryawanAdminActivity.this, TukarHadiahAdminActivity.class);
                startActivity(intent);
            }
        });

        tvAdministrator = findViewById(R.id.tvAdministrator);
        tvAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KaryawanAdminActivity.this, AdministratorAdminActivity.class);
                startActivity(intent);
            }
        });

        btnTambahKaryawan = findViewById(R.id.btnTambahKaryawan);
        tableLayout = findViewById(R.id.tableLayoutKaryawan);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("karyawan");

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
                        EditText etNomorKtpKaryawan = mDialog.findViewById(R.id.etNomorKtpKaryawan);
                        EditText etNomorIDKaryawan = mDialog.findViewById(R.id.etNomorIDKaryawan);
                        EditText etNamaKaryawan = mDialog.findViewById(R.id.etNamaKaryawan);
                        EditText etEmailKaryawan = mDialog.findViewById(R.id.etEmailKaryawan);
                        EditText etTelponKaryawan = mDialog.findViewById(R.id.etTelponKaryawan);
                        EditText etTanggalLahirKaryawan = mDialog.findViewById(R.id.etTanggalLahirKaryawan);

                        String nomorKTPKaryawan = etNomorKtpKaryawan.getText().toString().trim();
                        String nomorIDKaryawan = etNomorIDKaryawan.getText().toString().trim();
                        String namaKaryawan = etNamaKaryawan.getText().toString().trim();
                        String emailKaryawan = etEmailKaryawan.getText().toString().trim();
                        String telponKaryawan = etTelponKaryawan.getText().toString().trim();
                        String tanggalLahirKaryawan = etTanggalLahirKaryawan.getText().toString().trim();


                        DatabaseReference userRef = mDatabase.push();
                        userRef.child("nomorKTPKaryawan").setValue(nomorKTPKaryawan);
                        userRef.child("nomorIDKaryawan").setValue(nomorIDKaryawan);
                        userRef.child("namaKaryawan").setValue(namaKaryawan);
                        userRef.child("emailKaryawan").setValue(emailKaryawan);
                        userRef.child("telponKaryawan").setValue(telponKaryawan);
                        userRef.child("tanggalLahirKaryawan").setValue(tanggalLahirKaryawan);

                        mDialog.dismiss();
                    }
                });
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // Retrieve user data
                String nomorKTPKaryawan = dataSnapshot.child("nomorKTPKaryawan").getValue(String.class);
                String nomorIDKaryawan = dataSnapshot.child("nomorIDKaryawan").getValue(String.class);
                String namaKaryawan = dataSnapshot.child("namaKaryawan").getValue(String.class);
                String emailKaryawan = dataSnapshot.child("emailKaryawan").getValue(String.class);
                String telponKaryawan = dataSnapshot.child("telponKaryawan").getValue(String.class);
                String tanggalLahirKaryawan = dataSnapshot.child("tanggalLahirKaryawan").getValue(String.class);


                // Create a new table row
                TableRow tableRow = new TableRow(KaryawanAdminActivity.this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                ));

                // Add data to the table row

                TextView textViewNamaKaryawan = new TextView(KaryawanAdminActivity.this);
                textViewNamaKaryawan.setText(namaKaryawan);
                textViewNamaKaryawan.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewNamaKaryawan.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewNamaKaryawan);

                TextView textViewEmailKaryawan = new TextView(KaryawanAdminActivity.this);
                textViewEmailKaryawan.setText(emailKaryawan);
                textViewEmailKaryawan.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f
                ));
                textViewEmailKaryawan.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewEmailKaryawan);

                TextView textViewTelponKaryawan = new TextView(KaryawanAdminActivity.this);
                textViewTelponKaryawan.setText(telponKaryawan);
                textViewTelponKaryawan.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewTelponKaryawan.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewTelponKaryawan);

                TextView textViewTanggalLahirKaryawan = new TextView(KaryawanAdminActivity.this);
                textViewTanggalLahirKaryawan.setText(tanggalLahirKaryawan);
                textViewTanggalLahirKaryawan.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewTanggalLahirKaryawan.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewTanggalLahirKaryawan);

                // Add the table row to the table layout
                tableLayout.addView(tableRow);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Handle changes in user data if needed
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Handle removal of user data if needed
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // Handle movement of user data if needed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors in retrieving the data
            }
        });
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

}
