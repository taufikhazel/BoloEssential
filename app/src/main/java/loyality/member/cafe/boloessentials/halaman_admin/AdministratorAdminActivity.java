
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

public class AdministratorAdminActivity extends AppCompatActivity {
    private Button btnTambahAdministrator;
    private Dialog mDialog;
    private DatabaseReference mDatabase;
    private TableLayout tableLayout;
    private TextView tvAbsen, tvTukarHadiah, tvTukarPoint, tvDashboard, tvUser, tvAdministrator;
    private RelativeLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_admin);

        int textColor = getIntent().getIntExtra("textColorAdministrator", R.color.brownAdmin);
        tvAdministrator = findViewById(R.id.tvAdministrator);
        tvAdministrator.setTextColor(getResources().getColor(textColor));

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        btnTambahAdministrator = findViewById(R.id.btnTambahAdministrator);
        tableLayout = findViewById(R.id.tableLayout);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        tvUser = findViewById(R.id.tvUser);

        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministratorAdminActivity.this, UserAdminActivity.class);
                startActivity(intent);
            }
        });

        tvAbsen = findViewById(R.id.tvAbsen);

        tvAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministratorAdminActivity.this, KaryawanAdminActivity.class);
                startActivity(intent);
            }
        });

        tvTukarPoint = findViewById(R.id.tvTukarPoint);

        tvTukarPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministratorAdminActivity.this, TukarPointAdminActivity.class);
                startActivity(intent);
            }
        });

        tvTukarHadiah = findViewById(R.id.tvTukarHadiah);

        tvTukarHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministratorAdminActivity.this, TukarHadiahAdminActivity.class);
                startActivity(intent);
            }
        });

        tvDashboard = findViewById(R.id.tvDashboard);

        tvDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministratorAdminActivity.this,DashboardAdminActivity.class);
                startActivity(intent);
            }
        });

        mDialog = new Dialog(this);

        btnTambahAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.modal_tambah_administrator_admin);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();

                Button btnSubmit = mDialog.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText etNomorID = mDialog.findViewById(R.id.etNomorID);
                        EditText etNama = mDialog.findViewById(R.id.etNama);
                        EditText etEmail = mDialog.findViewById(R.id.etEmail);
                        EditText etTelpon = mDialog.findViewById(R.id.etTelpon);
                        EditText etTanggalLahir = mDialog.findViewById(R.id.etTanggalLahir);
                        EditText etPointUser = mDialog.findViewById(R.id.etPointUser);

                        String nomorID = etNomorID.getText().toString().trim();
                        String nama = etNama.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String telpon = etTelpon.getText().toString().trim();
                        String tanggalLahir = etTanggalLahir.getText().toString().trim();
                        String pointUser = etPointUser.getText().toString().trim();

                        DatabaseReference userRef = mDatabase.push();
                        userRef.child("nomorID").setValue(nomorID);
                        userRef.child("nama").setValue(nama);
                        userRef.child("email").setValue(email);
                        userRef.child("telpon").setValue(telpon);
                        userRef.child("tanggalLahir").setValue(tanggalLahir);
                        userRef.child("pointUser").setValue(pointUser);

                        mDialog.dismiss();
                    }
                });
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // Retrieve user data
                String nomorID = dataSnapshot.child("nomorID").getValue(String.class);
                String nama = dataSnapshot.child("nama").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String telpon = dataSnapshot.child("telpon").getValue(String.class);
                String tanggalLahir = dataSnapshot.child("tanggalLahir").getValue(String.class);
                String pointUser = dataSnapshot.child("pointUser").getValue(String.class);

                // Create a new table row
                TableRow tableRow = new TableRow(AdministratorAdminActivity.this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                ));

                // Add data to the table row
                TextView textViewNomorID = new TextView(AdministratorAdminActivity.this);
                textViewNomorID.setText(nomorID);
                textViewNomorID.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewNomorID.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewNomorID);

                TextView textViewNama = new TextView(AdministratorAdminActivity.this);
                textViewNama.setText(nama);
                textViewNama.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewNama.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewNama);

                TextView textViewEmail = new TextView(AdministratorAdminActivity.this);
                textViewEmail.setText(email);
                textViewEmail.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewEmail.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewEmail);

                TextView textViewTelpon = new TextView(AdministratorAdminActivity.this);
                textViewTelpon.setText(telpon);
                textViewTelpon.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewTelpon.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewTelpon);

                TextView textViewTanggalLahir = new TextView(AdministratorAdminActivity.this);
                textViewTanggalLahir.setText(tanggalLahir);
                textViewTanggalLahir.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewTanggalLahir.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewTanggalLahir);

                TextView textViewPointUser = new TextView(AdministratorAdminActivity.this);
                textViewPointUser.setText(pointUser);
                textViewPointUser.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));
                textViewPointUser.setPadding(8, 0, 0, 0); // Set padding here
                tableRow.addView(textViewPointUser);

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
                Intent intent = new Intent(AdministratorAdminActivity.this, LoadingScreenActivity.class);
                startActivity(intent);
                Toast.makeText(AdministratorAdminActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popupMenu.show();
    }

}

