package com.app.superdistributor.payments;

import static com.app.superdistributor.RequestService.ReplaceByDealerActivity.REQUEST_ID_MULTIPLE_PERMISSIONS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.DealerHomeActivity;
import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BankTransferPaymentActivity extends AppCompatActivity {

    TextInputEditText BankTransferAmountEt;
    Button BankPaymentSubmitBtn, BankPaymentScreenshotBtn, BankTransferDateBtn;

    String DealerName;
    TextView SelectedDate;
    String selectedDate;
    private DatabaseReference mref;
    private ProgressDialog LoadingBar;

    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_payment);

        checkAndRequestPermissions(BankTransferPaymentActivity.this);

        DealerName = getIntent().getStringExtra("DealerName");

        SelectedDate = findViewById(R.id.bank_transfer_selected_date);

        mref= FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        BankTransferAmountEt = findViewById(R.id.bankTransferAmountET);
        BankPaymentSubmitBtn = findViewById(R.id.submitBankPaymentBtn);
        BankPaymentScreenshotBtn = findViewById(R.id.attachBankTransferScreenshotBtn);
        BankTransferDateBtn = findViewById(R.id.banktransferdateBtn);

        LoadingBar=new ProgressDialog(this);

        BankPaymentScreenshotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(BankTransferPaymentActivity.this);
            }
        });

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        BankTransferDateBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        SelectedDate.setText("Selected Date is : " + materialDatePicker.getHeaderText());
                        selectedDate = materialDatePicker.getHeaderText();
                    }
                });

        BankPaymentSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BankTransferAmountEt.getText().toString().equals(""))
                {
                    Toast.makeText(BankTransferPaymentActivity.this, "Please enter amount..", Toast.LENGTH_SHORT).show();
                }
                else if(SelectedDate.getText().toString().equals("Selected Date is : "))
                {
                    Toast.makeText(BankTransferPaymentActivity.this, "Please select date..", Toast.LENGTH_SHORT).show();
                }
                else if(filePath == null)
                {
                    Toast.makeText(BankTransferPaymentActivity.this, "Please select screenshot of bank transfer..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LoadingBar.setTitle("Please Wait..");
                    LoadingBar.setMessage("Please Wait we are saving your details...");
                    LoadingBar.show();

                    uploadBankTransferDetails(BankTransferAmountEt.getText().toString(),selectedDate);
                }
            }
        });
    }

    private void uploadBankTransferDetails(String amount, String selectedDate) {
        if (filePath != null) {


            LoadingBar.setTitle("Please Wait..");
            LoadingBar.setMessage("Please Wait we submitting your details...");
            LoadingBar.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();

                                            Map<String, Object> bankTransferPaymentDetails = new HashMap<String, Object>();
                                            bankTransferPaymentDetails.put("Amount", amount);
                                            bankTransferPaymentDetails.put("Date", selectedDate);
                                            bankTransferPaymentDetails.put("TransactionScreenshotUrl", url);
                                            bankTransferPaymentDetails.put("Status","Pending");

                                            mref.child("Dealers").child(DealerName).child("Payments").child("BankTransfer").
                                                    push().updateChildren(bankTransferPaymentDetails)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            LoadingBar.dismiss();
                                                            Toast.makeText(BankTransferPaymentActivity.this, "Details saved in our database successfully..", Toast.LENGTH_SHORT).show();
                                                            BankTransferAmountEt.setText("");
                                                            SelectedDate.setText("Selected Date is : ");
                                                            Intent i = new Intent(BankTransferPaymentActivity.this, PaymentMethodActivity.class);
                                                            startActivity(i);
                                                        }
                                                    });
                                            //Toast.makeText(RegisterActivity.this, ""+url.toString(), Toast.LENGTH_SHORT).show();
                                            //Do what you need to do with url
                                        }
                                    });
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            LoadingBar.dismiss();
                            Toast
                                    .makeText(BankTransferPaymentActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } else {
            Toast.makeText(BankTransferPaymentActivity.this, "Please select screenshot image..", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(BankTransferPaymentActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(BankTransferPaymentActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //chooseImage(ReplaceByDealerActivity.this);
                }
                break;
        }
    }
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                //ProductImageChooseBtn.setImageBitmap(bitmap);
                Toast.makeText(this, "Image added..", Toast.LENGTH_SHORT).show();
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
}