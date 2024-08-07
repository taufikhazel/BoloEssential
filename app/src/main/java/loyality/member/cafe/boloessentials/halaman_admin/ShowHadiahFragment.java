package loyality.member.cafe.boloessentials.halaman_admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.model.Menu;

import static android.app.Activity.RESULT_OK;

public class ShowHadiahFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button btnTambahHadiah;
    private Dialog mDialog;
    private EditText etNamaMenu, etPointMenu;
    private ImageView FotoMenu;
    private Uri imageUri;
    private ImageView FotoMenuUpdate;
    private Uri newImageUri;
    private Dialog updateDialog;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private int lastIDMenu = 0;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_hadiah, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize table layout
        tableLayout = view.findViewById(R.id.tableLayout);

        // Add table header
        addTableHeader();

        // Initialize Firebase reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Menu");

        // Fetch data from Firebase
        fetchData(databaseReference);

        // Initialize dialog
        mDialog = new Dialog(requireContext());
    }

    private void fetchData(DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) {
                    return;
                }

                tableLayout.removeAllViews();
                addTableHeader();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Menu menu = dataSnapshot.getValue(Menu.class);
                    if (menu != null && menu.getShow()) {
                        addMenuRow(menu);
                        if (menu.getIDMenu() > lastIDMenu) {
                            lastIDMenu = menu.getIDMenu();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addTableHeader() {
        if (getContext() == null) {
            Toast.makeText(getContext(), "Context is null", Toast.LENGTH_SHORT).show();
            return;
        }

        tableLayout.removeAllViews();
        TableRow headerRow = new TableRow(getContext());
        String[] headers = {"Nama Menu", "Point", "Gambar", "Aksi"};
        float[] weights = {1.5f, 1f, 1f, 3f};

        for (int i = 0; i < headers.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(headers[i]);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTextSize(12);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(5, 5, 5, 5);

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


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1000);
    }

    private void addMenuRow(Menu menu) {
        if (getContext() == null) {
            Toast.makeText(getContext(), "Context is null", Toast.LENGTH_SHORT).show();
            return;
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

    private void showBalloonTooltip(View anchor, Menu menu) {
        View balloonView = getLayoutInflater().inflate(R.layout.balloon_drop, null);

        Balloon balloon = new Balloon.Builder(requireContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setLayout(balloonView)
                .setArrowPosition(0.5f)
                .setCornerRadius(4f)
                .setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        Button btnDrop = balloonView.findViewById(R.id.btnDrop);
        Button btnUpdate = balloonView.findViewById(R.id.btnUpdate);

        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Apakah Anda yakin ingin tidak menampilkan menu ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dropMenu(menu);
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMenu(menu);
                balloon.dismiss();
            }
        });

        balloon.show(anchor);
    }

    private void showImagePreview(String imageUrl) {
        Dialog previewDialog = new Dialog(requireContext());
        previewDialog.setContentView(R.layout.modal_preview);
        if (previewDialog.getWindow() != null) {
            previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView previewImageView = previewDialog.findViewById(R.id.ivPreview);
        Picasso.get().load(imageUrl).into(previewImageView);

        previewDialog.show();
    }

    private void updateMenu(Menu menu) {
        updateDialog = new Dialog(requireContext());
        updateDialog.setContentView(R.layout.modal_update_hadiah);
        if (updateDialog.getWindow() != null) {
            updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText etUpdateNamaMenu = updateDialog.findViewById(R.id.etNamaMenuUpdate);
        EditText etUpdatePointMenu = updateDialog.findViewById(R.id.etPointMenuUpdate);
        FotoMenuUpdate = updateDialog.findViewById(R.id.FotoMenuUpdate);
        Button btnUpdateSubmit = updateDialog.findViewById(R.id.btnSelesaiUpdateHadiah);
        Button btnUpdateCancel = updateDialog.findViewById(R.id.btnBatalUpdateHadiah);
        ProgressBar progressBarUpdate = updateDialog.findViewById(R.id.progressBar);

        etUpdateNamaMenu.setText(menu.getNamaMenu());
        etUpdatePointMenu.setText(String.valueOf(menu.getPoint()));
        FotoMenuUpdate.setBackground(null);
        Picasso.get().load(menu.getGambar()).into(FotoMenuUpdate);

        FotoMenuUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryForUpdate();
            }
        });

        btnUpdateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedNamaMenu = etUpdateNamaMenu.getText().toString();
                String updatedPointMenu = etUpdatePointMenu.getText().toString();

                if (updatedNamaMenu.isEmpty() || updatedPointMenu.isEmpty()) {
                    Toast.makeText(getContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBarUpdate.setVisibility(View.VISIBLE);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Menu").child(String.valueOf(menu.getIDMenu()));

                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("NamaMenu", updatedNamaMenu);
                updatedData.put("Point", Integer.parseInt(updatedPointMenu));

                if (newImageUri != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
                    storageReference.putFile(newImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String downloadUrl = task.getResult().toString();
                                            updatedData.put("Gambar", downloadUrl);

                                            updateMenuInFirebase(databaseReference, updatedData, progressBarUpdate, updateDialog);
                                        } else {
                                            progressBarUpdate.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "Gagal mendapatkan URL gambar", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                progressBarUpdate.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Gagal mengupload gambar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    updateMenuInFirebase(databaseReference, updatedData, progressBarUpdate, updateDialog);
                }
            }
        });

        btnUpdateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });

        updateDialog.show();
    }

    private void openGalleryForUpdate() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1001);
    }

    private void updateMenuInFirebase(DatabaseReference databaseReference, Map<String, Object> updatedData, ProgressBar progressBar, Dialog updateDialog) {
        databaseReference.updateChildren(updatedData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Menu berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    updateDialog.dismiss();
                    fetchData(FirebaseDatabase.getInstance().getReference("Menu"));
                } else {
                    Toast.makeText(getContext(), "Gagal memperbarui menu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dropMenu(Menu menu) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Menu").child(String.valueOf(menu.getIDMenu()));
        databaseReference.child("Show").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Menu berhasil di Drop", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Menu gagal di show", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (requestCode == 1000) {
                imageUri = selectedImageUri;
                FotoMenu.setImageURI(imageUri);
                FotoMenu.setBackground(null); // Remove background
            } else if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
                newImageUri = data.getData();
                if (FotoMenuUpdate != null) {
                    FotoMenuUpdate.setImageURI(newImageUri);
                }
            }
        }
    }
}
