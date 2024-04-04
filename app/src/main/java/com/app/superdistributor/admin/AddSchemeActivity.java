package com.app.superdistributor.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddSchemeActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 47;
    TextInputEditText SchemeName;
    ShapeableImageView SchemeImage;
    Spinner chooseDealer;
    boolean isImgAdded = false;
    Uri uploadedImageUri;
    Button SubmitSchemeDetailsBtn;
    DatabaseReference database;
    StorageReference storageRef;
    private ProgressDialog LoadingBar;
    List<String> dealerUserNamesList;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private static final int PICK_IMAGE_REQUEST = 143;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scheme);

        LoadingBar = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        dealerUserNamesList = new ArrayList<>();
        dealerUserNamesList.add("--Select a dealer--");

        SchemeName = findViewById(R.id.schemeNameET);
        SchemeImage = findViewById(R.id.schemeImage);
        SubmitSchemeDetailsBtn = findViewById(R.id.submitSchemeDetailsBtn);
        chooseDealer = findViewById(R.id.selectDealer);
        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.child("UserName").getValue() != null)
                    {
                        dealerUserNamesList.add(snap.child("UserName").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        ArrayAdapter<String> dealerUserNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dealerUserNamesList);
        chooseDealer.setAdapter(dealerUserNameAdapter);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        isImgAdded = true;
                        Intent data = result.getData();
                        // Get the selected image URI
                        uploadedImageUri = data.getData();
                        Log.d("Uploaded Image Path : ",uploadedImageUri.toString());
                        SchemeImage.setImageURI(uploadedImageUri);
                    }
                });
        SchemeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(galleryIntent);
            }
        });
        SubmitSchemeDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SchemeName.getText().toString().equals("")) {
                    Toast.makeText(AddSchemeActivity.this, "Please enter scheme name", Toast.LENGTH_SHORT).show();
                } else if (!isImgAdded) {
                    Toast.makeText(AddSchemeActivity.this, "Please enter scheme image", Toast.LENGTH_SHORT).show();
                } else if (chooseDealer.getSelectedItem().toString().equals("--Select a dealer--")) {
                    Toast.makeText(AddSchemeActivity.this, "Please select & assign a dealer", Toast.LENGTH_SHORT).show();
                } else {
                    createNewScheme(SchemeName.getText().toString());
                }
            }
        });
    }
    private void createNewScheme(String schemeName) {
        if (uploadedImageUri != null) {
            LoadingBar.setTitle("Please Wait..");
            LoadingBar.setMessage("Please Wait while we are checking our database...");
            LoadingBar.show();
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());
             ref.putFile(uploadedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            saveToDb(schemeName, imageUrl,chooseDealer.getSelectedItem().toString());
                            LoadingBar.dismiss();
                        });
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        }
        else {
            Toast.makeText(AddSchemeActivity.this, "Error while locating Image Uri", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveToDb(String schemeName,  String imageUrl, String dealer) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Schemes").child(schemeName).exists()){
                    LoadingBar.dismiss();
                    Toast.makeText(AddSchemeActivity.this, "Offer with this name already exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String,Object> schemes = new HashMap<>();
                    schemes.put("Name",schemeName);
                    schemes.put("ImageUrl",imageUrl);
                    schemes.put("Dealer",dealer);

                    database.child("Schemes").child(schemeName).updateChildren(schemes)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    LoadingBar.dismiss();
                                    Toast.makeText(AddSchemeActivity.this,"Scheme Added",Toast.LENGTH_SHORT).show();
                                    SchemeName.setText("");
                                    SchemeImage.setImageResource(R.drawable.baseline_add_image);
                                    chooseDealer.setSelection(0);
                                }
                            });
                    database.child("Dealers").child(chooseDealer.getSelectedItem().toString()).child("myScheme").setValue(schemeName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {Toast.makeText(AddSchemeActivity.this,"Couldn't save", Toast.LENGTH_SHORT).show();}
        });
    }
}