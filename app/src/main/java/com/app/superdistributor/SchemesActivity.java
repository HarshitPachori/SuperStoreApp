package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SchemesActivity extends AppCompatActivity {
    Button schemeBtn, offerBtn, missedClaimPointsBtn;
    String dealerName;
    String dialogImgPathScheme, dialogTitleScheme, dialogImgPathOffer, dialogTitleOffer;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemes);

        dealerName = getIntent().getStringExtra("DealerName");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        schemeBtn = findViewById(R.id.yourSchemesBtn);
        offerBtn = findViewById(R.id.runningOffersBtn);
        missedClaimPointsBtn = findViewById(R.id.claimPointsBtn);

        databaseReference.child("Schemes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("Dealer").getValue().toString().equals(dealerName)) {
                        dialogTitleScheme = dataSnapshot.child("Name").getValue().toString();
                        dialogImgPathScheme = dataSnapshot.child("ImageUrl").getValue().toString();
                        Log.d("Check", dialogTitleScheme);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("Offers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("Dealer").getValue().toString().equals(dealerName)) {
                        dialogTitleOffer = dataSnapshot.child("Name").getValue().toString();
                        dialogImgPathOffer = dataSnapshot.child("ImageUrl").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        schemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogTitleScheme != null) {
                    showCustomDialog(dialogTitleScheme, dialogImgPathScheme);
                } else {
                    Toast.makeText(SchemesActivity.this, "No Schemes available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        offerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogTitleOffer != null) {
                    showCustomDialog(dialogTitleOffer, dialogImgPathOffer);
                } else {
                    Toast.makeText(SchemesActivity.this, "No Offers available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showCustomDialog(String title, String imgPath) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        ImageView dialogImage = dialogView.findViewById(R.id.dialogImage);
        Log.d("ImagePath", imgPath);
        Glide.with(this)
//                .load(FirebaseStorage.getInstance().getReference().child(imgPath))
                .load(imgPath)
                .placeholder(R.drawable.loading_img)
                .into(dialogImage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle(title)
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadImage(imgPath);
                    }
                })
                .setNegativeButton("Done", null)
                .show();
    }

    private void downloadImage(String imageUrl) {
        // Create a DownloadManager request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "myDeal.jpg");
        // Get the DownloadManager service and enqueue the request
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }
}
