package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import loyality.member.cafe.boloessentials.R;

public class ShowHadiahFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button btnTambahHadiah;
    private Dialog mDialog;

    public ShowHadiahFragment() {
        // Required empty public constructor
    }

    public static ShowHadiahFragment newInstance(String param1, String param2) {
        ShowHadiahFragment fragment = new ShowHadiahFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_hadiah, container, false);

        // Initialize dialog
        mDialog = new Dialog(requireContext());

        // Initialize btnTambahHadiah
        btnTambahHadiah = view.findViewById(R.id.btnTambahHadiah);
        btnTambahHadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.modal_tambah_hadiah);
                if (mDialog.getWindow() != null) {
                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                mDialog.show();
            }
        });

        return view;
    }
}
