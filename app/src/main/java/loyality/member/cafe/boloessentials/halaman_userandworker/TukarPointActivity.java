package loyality.member.cafe.boloessentials.halaman_userandworker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_point);

        viewPagerMenu = findViewById(R.id.viewPagerMenu);
        menuList = new ArrayList<>();
        menuPagerAdapter = new MenuPagerAdapter(this, menuList, itemsPerPage);
        viewPagerMenu.setAdapter(menuPagerAdapter);
        btnSubmit = findViewById(R.id.btnSubmit);
        UID = getIntent().getStringExtra("UID");

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
                        .setPositiveButton("Ya", (dialog, which) ->  saveSelectedMenus(selectedMenus))
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
        for (Menu menu : selectedMenus) {
            DatabaseReference tukarPointRef = FirebaseDatabase.getInstance().getReference("tukarPoint");
            DatabaseReference newRef = tukarPointRef.push();
            newRef.child("NamaMenu").setValue(menu.getNamaMenu());
            newRef.child("Point").setValue(menu.getPoint());
            newRef.child("Status").setValue(false);
            newRef.child("Hasil").setValue(false);
            newRef.child("nomorID").setValue(UID);
        }
        Toast.makeText(this, "Items successfully saved", Toast.LENGTH_SHORT).show();
        new android.os.Handler().postDelayed(() -> progressDialog.dismiss(), 1000);
    }
}