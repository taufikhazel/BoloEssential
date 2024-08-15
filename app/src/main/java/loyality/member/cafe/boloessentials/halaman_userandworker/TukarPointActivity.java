package loyality.member.cafe.boloessentials.halaman_userandworker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.adapter.MenuPagerAdapter;
import loyality.member.cafe.boloessentials.model.Menu;

public class TukarPointActivity extends AppCompatActivity {

    private ViewPager2 viewPagerMenu;
    private MenuPagerAdapter menuPagerAdapter;
    private List<Menu> menuList;
    private String UID;
    private Button btnSubmit;
    private DatabaseReference databaseReference;
    private int itemsPerPage = 10;
    private int pointUser; // Variable to store the user's points

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_point);

        viewPagerMenu = findViewById(R.id.viewPagerMenu);
        menuList = new ArrayList<>();
        btnSubmit = findViewById(R.id.btnSubmit);
        UID = getIntent().getStringExtra("UID");

        // Fetch user points from Firebase
        fetchUserPoints();
    }

    private void fetchUserPoints() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("nomorID").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Iterate through the matching nodes to find the correct user
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Integer pointUser = userSnapshot.child("pointUser").getValue(Integer.class);

                        if (pointUser != null) {
                            // Initialize the adapter with the retrieved points
                            initializeMenuAdapter(pointUser);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to retrieve user points", Toast.LENGTH_SHORT).show();
                        }
                        break; // Assuming there's only one match
                    }
                } else {
                    // Handle the case where the user doesn't exist
                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors
                Toast.makeText(getApplicationContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initializeMenuAdapter(int pointUser) {
        menuPagerAdapter = new MenuPagerAdapter(this, menuList, itemsPerPage, pointUser);
        viewPagerMenu.setAdapter(menuPagerAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Menu");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu menu = snapshot.getValue(Menu.class);
                    if (menu != null && menu.getShow()) {
                        menuList.add(menu);
                    }
                }
                menuPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database errors
            }
        });

        btnSubmit.setOnClickListener(v -> {
            List<Menu> selectedMenus = new ArrayList<>();
            for (int i = 0; i < menuPagerAdapter.getItemCount(); i++) {
                MenuFragment fragment = menuPagerAdapter.getFragment(i);
                if (fragment != null) {
                    selectedMenus.addAll(fragment.getSelectedMenus());
                }
            }

            if (!selectedMenus.isEmpty()) {
                String selectedNames = "";
                for (Menu menu : selectedMenus) {
                    selectedNames += menu.getNamaMenu() + ", ";
                }
                selectedNames = selectedNames.substring(0, selectedNames.length() - 2); // Remove last comma

                new AlertDialog.Builder(TukarPointActivity.this)
                        .setTitle("Konfirmasi Penukaran")
                        .setMessage("Apakah anda yakin ingin menukarkan point anda dengan " + selectedNames + " ?")
                        .setPositiveButton("Ya", (dialog, which) -> saveSelectedMenus(selectedMenus))
                        .setNegativeButton("Tidak", null)
                        .show();
            } else {
                Toast.makeText(TukarPointActivity.this, "Pilih menu terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSelectedMenus(List<Menu> selectedMenus) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Point anda sedang ditukar, Harap tunggu");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
        progressDialog.show();

        // Subtract the selected points from the user's points
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("nomorID").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Calculate the total points of selected menus
                int totalSelectedPoints = 0;
                for (Menu menu : selectedMenus) {
                    totalSelectedPoints += menu.getPoint();
                }

                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Integer currentPoints = userSnapshot.child("pointUser").getValue(Integer.class);

                        // Ensure currentPoints is not null
                        if (currentPoints != null) {
                            // Check if the user has enough points
                            if (currentPoints >= totalSelectedPoints) {
                                int updatedPoints = currentPoints - totalSelectedPoints;
                                userSnapshot.getRef().child("pointUser").setValue(updatedPoints);

                                // Retrieve the last ID from the tukarPoint table
                                DatabaseReference tukarPointRef = FirebaseDatabase.getInstance().getReference("tukarPoint");
                                tukarPointRef.orderByChild("IDTransaksi").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int lastIDTransaksi = 0; // Default value if no ID exists
                                        if (snapshot.exists()) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                Integer id = ds.child("IDTransaksi").getValue(Integer.class);
                                                if (id != null) {
                                                    lastIDTransaksi = id;
                                                }
                                            }
                                        }
                                        int newIDTransaksi = lastIDTransaksi + 1;

                                        // Save selected menus to the 'tukarPoint' table
                                        for (Menu menu : selectedMenus) {
                                            DatabaseReference newRef = tukarPointRef.push();
                                            newRef.child("NamaMenu").setValue(menu.getNamaMenu());
                                            newRef.child("Point").setValue(menu.getPoint());
                                            newRef.child("Status").setValue(false);
                                            newRef.child("Hasil").setValue(false);
                                            newRef.child("nomorID").setValue(UID);
                                            newRef.child("IDTransaksi").setValue(newIDTransaksi);
                                        }

                                        Toast.makeText(getApplicationContext(), "Items successfully Exchanged", Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(progressDialog::dismiss, 1000);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), "Failed to retrieve last ID", Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(progressDialog::dismiss, 1000);
                                    }
                                });

                            } else {
                                // Notify the user that they don't have enough points
                                Toast.makeText(getApplicationContext(), "Anda tidak memiliki cukup point", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(progressDialog::dismiss, 1000);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to retrieve user points", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(progressDialog::dismiss, 1000);
                        }
                        break;
                    }
                } else {
                    // Handle the case where the user doesn't exist
                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(progressDialog::dismiss, 1000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(progressDialog::dismiss, 1000);
            }
        });
    }


}
