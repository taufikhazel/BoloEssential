package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.model.TukarHadiah;

public class PendingTukarHadiahFragment extends Fragment {
    private static final int ITEMS_PER_PAGE = 5;
    private int currentPage = 1;
    private int totalPageCount;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView tvPointTukarHadiah;
    private List<TukarHadiah> tukarHadiahList = new ArrayList<>();
    private Button btnPrevPage, btnNextPage, btn1, btn2, btn3;
    private TableLayout tableLayout;
    private String mParam1;
    private String mParam2;
    public PendingTukarHadiahFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        // Notify the activity to update UI
        if (getActivity() instanceof TukarHadiahAdminActivity) {
            ((TukarHadiahAdminActivity) getActivity()).updateButtonStylesForPendingTukarHadiahFragment();
        }
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
        View view = inflater.inflate(R.layout.fragment_pending_tukar_hadiah, container, false);

        btnPrevPage = view.findViewById(R.id.btnPrevious);
        btnNextPage = view.findViewById(R.id.btnNext);
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);

        tvPointTukarHadiah = view.findViewById(R.id.tvPointTukarHadiah);

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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tukarPoint");

        addTableHeader();
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

                tukarHadiahList.clear();
                int tukarHadiahCount = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TukarHadiah tukarHadiah = dataSnapshot.getValue(TukarHadiah.class);
                    if (tukarHadiah != null && !tukarHadiah.getStatus()) {
                        tukarHadiahList.add(tukarHadiah);
                        tukarHadiahCount++;
                    }
                }

                tvPointTukarHadiah.setText(String.valueOf(tukarHadiahCount));
                totalPageCount = (int) Math.ceil((double) tukarHadiahList.size() / ITEMS_PER_PAGE); // Hitung total halaman
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
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, tukarHadiahList.size());

        for (int i = startIndex; i < endIndex; i++) {
            addMenuRow(tukarHadiahList.get(i));
        }

        updatePaginationButtons();
    }

    private void addTableHeader() {
        if (getContext() == null) {
            Toast.makeText(getContext(), "Context is null", Toast.LENGTH_SHORT).show();
            return;
        }

        tableLayout.removeAllViews();
        TableRow headerRow = new TableRow(getContext());
        String[] headers = {"Nama User", "Nama Menu", "Point", "Aksi"};
        float[] weights = {1.5f, 1.2f, 1f, 2.5f};

        for (int i = 0; i < headers.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(headers[i]);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTextSize(12);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(5, 0, 5, 5);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i]
            );

            // Set margins for header
            int marginInPixels = (int) (1 * getResources().getDisplayMetrics().density); // Convert dp to pixels
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i]
            );
            layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
            textView.setLayoutParams(layoutParams);

            headerRow.addView(textView);
        }
        headerRow.setBackgroundColor(getResources().getColor(R.color.brownAdmin));
        tableLayout.addView(headerRow);
    }
    private void addMenuRow(TukarHadiah tukarHadiah) {
        if (getContext() == null) {
            return;
        }

        // Firebase query to match nomorID
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        Query query = usersRef.orderByChild("nomorID").equalTo(tukarHadiah.getNomorID());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TableRow row = new TableRow(requireContext());

                // Retrieve 'nama' from Firebase
                String nama = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    nama = snapshot.child("nama").getValue(String.class);
                    break; // Assuming there is only one match
                }

                // Add 'nama' TextView to the row at index 0
                TextView namaTextView = new TextView(requireContext());
                namaTextView.setText(nama);
                namaTextView.setGravity(Gravity.CENTER);
                namaTextView.setTextSize(12);
                namaTextView.setPadding(5, 5, 5, 5);

                TableRow.LayoutParams namaParams = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.5f // Assuming the weight for 'nama' is the first one
                );
                int namaMarginInPixels = (int) (5 * getResources().getDisplayMetrics().density);
                namaParams.setMargins(namaMarginInPixels, -8, namaMarginInPixels, 0);
                namaTextView.setLayoutParams(namaParams);
                row.addView(namaTextView);

                // Add other TextViews
                String[] tukarHadiahData = {
                        tukarHadiah.getNamaMenu(),
                        String.valueOf(tukarHadiah.getPoint()),
                };

                float[] weights = {1.5f, 1.2f, 1f, 2.5f}; // Adjust weights as needed

                for (int i = 0; i < tukarHadiahData.length; i++) {
                    TextView textView = new TextView(requireContext());
                    textView.setText(tukarHadiahData[i]);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(12);
                    textView.setPadding(5, 5, 5, 5);

                    TableRow.LayoutParams params = new TableRow.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            weights[i + 1] // Shift weights if necessary
                    );
                    int marginInPixels = (int) (5 * getResources().getDisplayMetrics().density);
                    params.setMargins(marginInPixels, -8, marginInPixels, 0);
                    textView.setLayoutParams(params);
                    row.addView(textView);
                }

                // Actions
                Button deniedButton = new Button(requireContext());
                Button acceptButton = new Button(requireContext());

                // Get drawables
                Drawable deniedDrawable = getResources().getDrawable(R.drawable.baseline_close_24, null);
                Drawable acceptDrawable = getResources().getDrawable(R.drawable.baseline_check_24, null);

                // Set drawable size
                int drawableWidth = 30;
                int drawableHeight = 30;
                deniedDrawable.setBounds(0, 0, drawableWidth, drawableHeight);
                acceptDrawable.setBounds(0, 0, drawableWidth, drawableHeight);

                // Set drawable to buttons
                deniedButton.setCompoundDrawables(null, null, deniedDrawable, null);
                acceptButton.setCompoundDrawables(null, null, acceptDrawable, null);

                // Set button background to null
                deniedButton.setBackground(null);
                acceptButton.setBackground(null);

                // Mengatur padding agar drawable berada di tengah
                int paddingHorizontal = (int) (30 * getResources().getDisplayMetrics().density); // Padding horizontal dalam piksel
                int paddingVertical = (int) (0 * getResources().getDisplayMetrics().density); // Padding vertikal dalam piksel
                deniedButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal + drawableWidth, paddingVertical);
                acceptButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal + drawableWidth, paddingVertical);

                deniedButton.setOnClickListener(view -> showDenialDialog(tukarHadiah));

                acceptButton.setOnClickListener(view -> showAcceptanceDialog(tukarHadiah));

                // Mengatur layout params
                TableRow.LayoutParams actionParams = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        weights[1]
                );
                // Atur margin dengan nilai khusus
                int marginHorizontalInPixels = (int) (7 * getResources().getDisplayMetrics().density); // Margin horizontal dalam piksel
                actionParams.setMargins(marginHorizontalInPixels, -8, marginHorizontalInPixels, 0);

                row.addView(deniedButton, actionParams);
                row.addView(acceptButton, actionParams);

                tableLayout.addView(row);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void showAcceptanceDialog(TukarHadiah tukarHadiah) {
        if (getContext() == null) {
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Query to get the 'nama' from the 'users' table
        usersRef.orderByChild("nomorID").equalTo(tukarHadiah.getNomorID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nama = "";
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        nama = userSnapshot.child("nama").getValue(String.class);
                        break; // Assuming there's only one match
                    }
                }

                // Build and show the AlertDialog with the retrieved 'nama'
                new AlertDialog.Builder(requireContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin menukarkan " + tukarHadiah.getNamaMenu() + " ke " + nama + " ?")
                        .setPositiveButton("Iya", (dialog, which) -> {
                            DatabaseReference tukarHadiahRef = FirebaseDatabase.getInstance().getReference("tukarPoint");

                            // Update Hasil dan status
                            tukarHadiahRef.orderByChild("IDTransaksi").equalTo(tukarHadiah.getIDTransaksi()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot pointSnapshot : snapshot.getChildren()) {
                                            pointSnapshot.getRef().child("Status").setValue(true);
                                            pointSnapshot.getRef().child("Hasil").setValue(true);
                                        }
                                        Toast.makeText(getContext(), "Menu berHasil ditukarkan", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If IDTransaksi not found, add new data
                                        tukarHadiah.setStatus(true);
                                        tukarHadiah.setHasil(true);
                                        tukarHadiahRef.push().setValue(tukarHadiah).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Menu berHasil ditukarkan", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Gagal memperbarui data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    // Refresh data if needed
                                    fetchData(FirebaseDatabase.getInstance().getReference("tukarPoint"));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showDenialDialog(TukarHadiah tukarHadiah) {
        if (getContext() == null) {
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Query to get the 'nama' from the 'users' table
        usersRef.orderByChild("nomorID").equalTo(tukarHadiah.getNomorID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                String nama = "";
                if (userSnapshot.exists()) {
                    for (DataSnapshot snapshot : userSnapshot.getChildren()) {
                        nama = snapshot.child("nama").getValue(String.class);
                        break; // Assuming there's only one match
                    }
                }

                // Build and show the AlertDialog with the retrieved 'nama'
                new AlertDialog.Builder(requireContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin membatalkan penukaran hadiah di akun " + nama + " ?")
                        .setPositiveButton("Iya", (dialog, which) -> {
                            DatabaseReference tukarHadiahRef = FirebaseDatabase.getInstance().getReference("tukarPoint");

                            // Update Hasil, status, and pointUser
                            tukarHadiahRef.orderByChild("IDTransaksi").equalTo(tukarHadiah.getIDTransaksi()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot pointSnapshot : snapshot.getChildren()) {
                                            // Update status to true and Hasil to false
                                            pointSnapshot.getRef().child("Status").setValue(true);
                                            pointSnapshot.getRef().child("Hasil").setValue(false);
                                        }

                                        // Add points back to pointUser
                                        for (DataSnapshot user : userSnapshot.getChildren()) {
                                            Integer currentPoints = user.child("pointUser").getValue(Integer.class);
                                            if (currentPoints != null) {
                                                int updatedPoints = currentPoints + tukarHadiah.getPoint();
                                                user.getRef().child("pointUser").setValue(updatedPoints);
                                            } else {
                                                Toast.makeText(getContext(), "Gagal mendapatkan point saat ini", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        Toast.makeText(getContext(), "Penukaran Hadiah Dibatalkan", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If IDTransaksi not found, create new data with status true and Hasil false
                                        tukarHadiah.setStatus(true);
                                        tukarHadiah.setHasil(false);
                                        tukarHadiahRef.push().setValue(tukarHadiah).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Penambahan point dibatalkan", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Gagal memperbarui data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    // Refresh data if needed
                                    fetchData(FirebaseDatabase.getInstance().getReference("tukarPoint"));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updatePaginationButtons() {
        btn1.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
        btn2.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
        btn3.setBackgroundTintList(getResources().getColorStateList(R.color.gray));

        btn1.setTextColor(getResources().getColor(R.color.black));
        btn2.setTextColor(getResources().getColor(R.color.black));
        btn3.setTextColor(getResources().getColor(R.color.black));

        if (totalPageCount == 0) {
            btnNextPage.setVisibility(View.INVISIBLE);
            btnPrevPage.setVisibility(View.INVISIBLE);
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
        } else if (totalPageCount == 1) {
            btn1.setText("1");
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
            btnNextPage.setVisibility(View.INVISIBLE);
            btnPrevPage.setVisibility(View.INVISIBLE);
        } else if (totalPageCount == 2) {
            btn1.setText("1");
            btn2.setText("2");
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.INVISIBLE);
            btnNextPage.setVisibility(currentPage == 2 ? View.INVISIBLE : View.VISIBLE);
            btnPrevPage.setVisibility(currentPage == 1 ? View.INVISIBLE : View.VISIBLE);
        } else {
            // Logic for 3 or more pages
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

            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);

            btnNextPage.setVisibility(currentPage == totalPageCount ? View.INVISIBLE : View.VISIBLE);
            btnPrevPage.setVisibility(currentPage == 1 ? View.INVISIBLE : View.VISIBLE);
        }

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