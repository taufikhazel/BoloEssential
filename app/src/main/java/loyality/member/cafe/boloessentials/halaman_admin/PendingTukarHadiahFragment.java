package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.model.TambahPoint;
import loyality.member.cafe.boloessentials.model.TukarHadiah;

public class PendingTukarHadiahFragment extends Fragment {
    private static final int ITEMS_PER_PAGE = 5;
    private int currentPage = 1;
    private int totalPageCount;
    private TextView tvPointTukarHadiah;
    private List<TukarHadiah> tukarHadiahList = new ArrayList<>();
    private Button btnPrevPage, btnNextPage, btn1, btn2, btn3;
    private TableLayout tableLayout;
    public PendingTukarHadiahFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baru_tukar_hadiah, container, false);

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

        addTableHeader();

        return view;
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
        String[] headers = {"Nama", "ID Transaksi", "Jumlah Point", "Aksi"};
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

        TableRow row = new TableRow(requireContext());
        String[] tambahPointData = {
                tukarHadiah.getNama(),
                tukarHadiah.getNomorID(),
                String.valueOf(tukarHadiah.getPoint()),
        };

        float[] weights = {1.5f, 1.2f, 1f, 2.5f};

        for (int i = 0; i < tambahPointData.length; i++) {
            TextView textView = new TextView(requireContext());
            textView.setText(tambahPointData[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setPadding(5, 5, 5, 5);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    weights[i]
            );
            int marginInPixels = (int) (5 * getResources().getDisplayMetrics().density);
            params.setMargins(marginInPixels, -8, marginInPixels, 0);
            textView.setLayoutParams(params);
            row.addView(textView);
        }

        // Actions
        Button deniedButton = new Button(requireContext());
        Button acceptButton = new Button(requireContext());

        // Mengambil drawable
        Drawable deniedDrawable = getResources().getDrawable(R.drawable.baseline_close_24, null);
        Drawable acceptDrawable = getResources().getDrawable(R.drawable.baseline_check_24, null);

        // Mengatur ukuran drawable
        int drawableWidth = 30; // Lebar drawable dalam piksel
        int drawableHeight = 30; // Tinggi drawable dalam piksel
        deniedDrawable.setBounds(0, 0, drawableWidth, drawableHeight);
        acceptDrawable.setBounds(0, 0, drawableWidth, drawableHeight);

        // Mengatur drawable ke tombol
        deniedButton.setCompoundDrawables(null, null, deniedDrawable, null); // Drawable di kanan tombol
        acceptButton.setCompoundDrawables(null, null, acceptDrawable, null); // Drawable di kanan tombol

        // Setel latar belakang tombol ke null
        deniedButton.setBackground(null);
        acceptButton.setBackground(null);

        // Mengatur padding agar drawable berada di tengah
        int paddingHorizontal = (int) (30 * getResources().getDisplayMetrics().density); // Padding horizontal dalam piksel
        int paddingVertical = (int) (0 * getResources().getDisplayMetrics().density); // Padding vertikal dalam piksel
        deniedButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal + drawableWidth, paddingVertical);
        acceptButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal + drawableWidth, paddingVertical);

//        deniedButton.setOnClickListener(view -> showDenialDialog(tambahPoint));
//
//        acceptButton.setOnClickListener(view -> showAcceptanceDialog(tambahPoint));

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