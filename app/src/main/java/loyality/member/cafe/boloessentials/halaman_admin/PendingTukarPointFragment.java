package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.model.Menu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingTukarPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingTukarPointFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Dialog updateDialog;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private String mParam1;
    private String mParam2;
    private int currentPage = 1;
    private int totalPageCount;
    private static final int ITEMS_PER_PAGE = 5;
    private Button btnPrevPage, btnNextPage, btn1, btn2, btn3;


    public PendingTukarPointFragment() {
        // Required empty public constructor
    }

    public static PendingTukarPointFragment newInstance(String param1, String param2) {
        PendingTukarPointFragment fragment = new PendingTukarPointFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_tukar_point, container, false);
        btnPrevPage = view.findViewById(R.id.btnPrevious);
        btnNextPage = view.findViewById(R.id.btnNext);
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);

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

        tableLayout = view.findViewById(R.id.tableLayout);

        // Initialize Firebase reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Menu");

        // Fetch data from Firebase
        fetchData(databaseReference);
        return view;
    }

    private void fetchData(DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) {
                    return;
                }

                menuList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Menu menu = dataSnapshot.getValue(Menu.class);
                    if (menu != null && !menu.getShow()) {
                        menuList.add(menu);
                        if (menu.getIDMenu() > lastIDMenu) {
                            lastIDMenu = menu.getIDMenu();
                        }
                    }
                }
                totalPageCount = (int) Math.ceil((double) menuList.size() / ITEMS_PER_PAGE); // Hitung total halaman
                displayPageData(); // Tampilkan data untuk halaman saat ini
                updatePaginationButtons(); // Update tombol pagination
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void displayPageData() {
        // Hapus semua baris kecuali header
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, menuList.size());

        for (int i = startIndex; i < endIndex; i++) {
            addMenuRow(menuList.get(i)); // Tambahkan baris menu untuk setiap item
        }

        updatePaginationButtons();
    }
    private void addMenuRow(Menu menu) {
        if (getContext() == null) {
            return; // Exit the method if context is not available
        }

        TableRow row = new TableRow(getContext());
        String[] menuData = {
                menu.getNamaMenu(),
                String.valueOf(menu.getPoint()),
                menu.getGambar(),
                String.valueOf(menu.getShow())
        };

        float[] weights = {1.5f, 1f, 1f, 3f};

        for (int i = 0; i < menuData.length; i++) {
            if (i == 2) { // For image preview
                TextView previewTextView = new TextView(getContext());
                previewTextView.setText("Preview");
                previewTextView.setTextColor(getResources().getColor(R.color.brownAdmin));
                previewTextView.setGravity(Gravity.CENTER);
                previewTextView.setPadding(5, 5, 5, 5);
                previewTextView.setTextSize(12);
                previewTextView.setBackgroundResource(R.drawable.preview_border);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        weights[i]
                );
                int marginInPixels = (int) (7 * getResources().getDisplayMetrics().density);
                params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
                previewTextView.setLayoutParams(params);
                row.addView(previewTextView);

                final String imageUrl = menuData[i];
                previewTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showImagePreview(imageUrl);
                    }
                });
            } else if (i == 3) { // For actions
                TextView actionButton = new TextView(getContext());
                actionButton.setText("Actions");
                actionButton.setAllCaps(false);
                actionButton.setTextColor(getResources().getColor(R.color.brownAdmin));
                actionButton.setGravity(Gravity.CENTER);
                actionButton.setPadding(5, 5, 5, 5);
                actionButton.setTextSize(12);
                actionButton.setBackgroundResource(R.drawable.preview_border);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        weights[i]
                );
                int marginInPixels = (int) (7 * getResources().getDisplayMetrics().density);
                params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

                actionButton.setLayoutParams(params);
                row.addView(actionButton);

                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBalloonTooltip(view, menu);
                    }
                });
            } else {
                TextView textView = new TextView(getContext());
                textView.setText(menuData[i]);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setPadding(5, 5, 5, 5);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        weights[i]
                );
                int marginInPixels = (int) (7 * getResources().getDisplayMetrics().density);
                params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
                textView.setLayoutParams(params);
                row.addView(textView);
            }
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

}