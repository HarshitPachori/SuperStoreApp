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

public class AddOfferActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 47 ;
    TextInputEditText OfferName;
    ShapeableImageView OfferImage;
    Spinner chooseDealer;
    boolean isImgAdded = false;
    Uri uploadedImageUri;
    Button SubmitOfferDetailsBtn;
    DatabaseReference database;
    StorageReference storageRef;
    private ProgressDialog LoadingBar;
    List<String> dealerUserNamesList;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private static final int PICK_IMAGE_REQUEST = 143;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        LoadingBar = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        dealerUserNamesList = new ArrayList<>();
        dealerUserNamesList.add("--Select a dealer--");

        OfferName = findViewById(R.id.offerNameET);
        OfferImage = findViewById(R.id.offerImage);
        SubmitOfferDetailsBtn = findViewById(R.id.submitOfferDetailsBtn);
        chooseDealer = findViewById(R.id.selectDealer);
        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if(snap.child("UserName").getValue()!=null)
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
                        OfferImage.setImageURI(uploadedImageUri);
                    }
                });
        OfferImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(galleryIntent);
            }
        });
        SubmitOfferDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OfferName.getText().toString().equals("") ){
                    Toast.makeText(AddOfferActivity.this, "Please enter offer name", Toast.LENGTH_SHORT).show();
                } else if (!isImgAdded){
                    Toast.makeText(AddOfferActivity.this, "Please enter offer image", Toast.LENGTH_SHORT).show();
                } else if (chooseDealer.getSelectedItem().toString().equals("--Select a dealer--")){
                    Toast.makeText(AddOfferActivity.this, "Please select & assign a dealer", Toast.LENGTH_SHORT).show();
                } else{
                    createNewOffer(OfferName.getText().toString());
                }
            }
        });
    }
    private void createNewOffer(String offerName) {
        if (uploadedImageUri != null) {
            LoadingBar.setTitle("Please Wait..");
            LoadingBar.setMessage("Please Wait while we are checking our database...");
            LoadingBar.show();
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());
             ref.putFile(uploadedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            saveToDb(offerName, imageUrl,chooseDealer.getSelectedItem().toString());
                            LoadingBar.dismiss();
                        });
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        }
        else{
            Toast.makeText(AddOfferActivity.this, "Error while locating Image Uri", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveToDb(String offerName, String imageUrl, String dealer){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Offers").child(offerName).exists()){
                    LoadingBar.dismiss();
                    Toast.makeText(AddOfferActivity.this, "Offer with this name already exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String,Object> offers = new HashMap<>();
                    offers.put("Name", offerName);
                    offers.put("ImageUrl",imageUrl);
                    offers.put("Dealer",dealer);

                    database.child("Offers").child(offerName).updateChildren(offers)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    LoadingBar.dismiss();
                                    Toast.makeText(AddOfferActivity.this,"Offer Added",Toast.LENGTH_SHORT).show();
                                    OfferName.setText("");
                                    OfferImage.setImageResource(R.drawable.baseline_add_image);
                                    chooseDealer.setSelection(0);
                                }
                            });
                    database.child("Dealers").child(chooseDealer.getSelectedItem().toString()).child("myOffer").setValue(offerName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(AddOfferActivity.this,"Couldn't save", Toast.LENGTH_SHORT).show();}
        });
    }
}