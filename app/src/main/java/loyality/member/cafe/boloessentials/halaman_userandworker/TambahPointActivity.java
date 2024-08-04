package loyality.member.cafe.boloessentials.halaman_userandworker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import loyality.member.cafe.boloessentials.R;

public class TambahPointActivity extends AppCompatActivity {
    private Button btnSubmit;
    private EditText etID, etBiaya;
    private Dialog mDialog;

    // tambahkan variabel database reference
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_point);

        // inisialisasi database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnSubmit = findViewById(R.id.btnSubmit);
        etID = findViewById(R.id.etID);
        etBiaya = findViewById(R.id.etBiaya);

        mDialog = new Dialog(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // simpan data ke database Firebase Realtime
                String id = etID.getText().toString();
                int biaya = Integer.parseInt(etBiaya.getText().toString());
                int point = biaya / 1000;
                mDatabase.child("Add_Point_Member").child(id).child("point").setValue(point);

                // tampilkan dialog untuk konfirmasi
                mDialog.setContentView(R.layout.modal_tambah_point);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // tambahkan kode untuk menampilkan point pada modal
                TextView tvPoint = mDialog.findViewById(R.id.tvPoint);
                tvPoint.setText(String.valueOf(point) + " Point");

                mDialog.show();
            }
        });

    }
}
