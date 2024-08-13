package loyality.member.cafe.boloessentials.halaman_userandworker;

import android.os.Bundle;
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Menu");

        // Mendengarkan perubahan data di Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu menu = snapshot.getValue(Menu.class);
                    if (menu != null && menu.getShow()) { // Check if Show == true
                        menuList.add(menu);
                    }
                }
                menuPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}