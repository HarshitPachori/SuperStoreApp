package com.app.superdistributor.RequestService;

import static com.app.superdistributor.RequestService.ReplaceByDealerActivity.checkAndRequestPermissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.app.superdistributor.RequestServiceActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class RegisterComplaintAcitivty extends AppCompatActivity {

    private ProgressDialog LoadingBar;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private DatabaseReference mref;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextInputEditText RegisterCustomerNameTI, RegisterPhoneNumberTI, RegisterDateOfPurchaseTI,
            RegisterModelNumberTI, RegisterSerialNumberTI;
    Button RegisterAttachReportBtn, RegisterSendForApprovalBtn;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint_acitivty);
        userType = getIntent().getType();
        Toast.makeText(this, userType, Toast.LENGTH_SHORT).show();

        RegisterCustomerNameTI = findViewById(R.id.registercustomerNameTI);
        RegisterPhoneNumberTI = findViewById(R.id.registerphoneNoTI);
        RegisterDateOfPurchaseTI = findViewById(R.id.registerdateOfPurchaseTI);

        RegisterModelNumberTI = findViewById(R.id.registermodelNoTI);
        RegisterSerialNumberTI = findViewById(R.id.registerserialNoTI);

        RegisterAttachReportBtn = findViewById(R.id.registerattachReportBtn);
        RegisterSendForApprovalBtn = findViewById(R.id.registersendForApprovalBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        LoadingBar = new ProgressDialog(this);

        mref = FirebaseDatabase.getInstance().getReference();
        checkAndRequestPermissions(RegisterComplaintAcitivty.this);
        RegisterDateOfPurchaseTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogBox();
            }
        });
        RegisterAttachReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(RegisterComplaintAcitivty.this);
            }
        });

        RegisterSendForApprovalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(RegisterCustomerNameTI.getText().toString())) {
                    Toast.makeText(RegisterComplaintAcitivty.this, "Please enter customer name..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(RegisterPhoneNumberTI.getText().toString())) {
                    Toast.makeText(RegisterComplaintAcitivty.this, "Please enter phone number..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(RegisterDateOfPurchaseTI.getText().toString())) {
                    Toast.makeText(RegisterComplaintAcitivty.this, "Please enter date of purchase..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(RegisterModelNumberTI.getText().toString())) {
                    Toast.makeText(RegisterComplaintAcitivty.this, "Please enter model number..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(RegisterSerialNumberTI.getText().toString())) {
                    Toast.makeText(RegisterComplaintAcitivty.this, "Please enter serial number..", Toast.LENGTH_SHORT).show();
                } else {
                    uploadProduct(RegisterCustomerNameTI.getText().toString(),
                            RegisterPhoneNumberTI.getText().toString(),
                            RegisterDateOfPurchaseTI.getText().toString(),
                            RegisterModelNumberTI.getText().toString(),
                            RegisterSerialNumberTI.getText().toString());
                }
            }
        });

    }

    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void uploadProduct(String registerCustomerName, String registerPhoneNumber, String registerDateOfPurchase, String registerModelNumber, String registerSerialNumber) {
        if (filePath != null) {
            LoadingBar.setTitle("Please Wait..");
            LoadingBar.setMessage("Please Wait we are uploading report and data...");
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

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    Toast
                                            .makeText(RegisterComplaintAcitivty.this,
                                                    "Report Image Saved in our Storage!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();

                                            String replacementID = registerCustomerName;

                                            Map<String, Object> replacementDetails = new HashMap<String, Object>();
                                            replacementDetails.put("CustomerName", registerCustomerName);
                                            replacementDetails.put("PhoneNumber", registerPhoneNumber);
                                            replacementDetails.put("DateOfPurchase", registerDateOfPurchase);
                                            replacementDetails.put("ModelNumber", registerModelNumber);
                                            replacementDetails.put("SerialNumber", registerSerialNumber);
                                            replacementDetails.put("ReportUrl", url);
                                            replacementDetails.put("Status", "Pending");

                                            mref.child(userType).child("RequestServices").child("RegisterComplaints").child(replacementID).updateChildren(replacementDetails)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            LoadingBar.dismiss();
                                                            Toast.makeText(RegisterComplaintAcitivty.this, "Complaints Details Uploaded Successfully..", Toast.LENGTH_SHORT).show();

                                                            Intent i = new Intent(RegisterComplaintAcitivty.this, RequestServiceActivity.class);
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
                            Toast.makeText(RegisterComplaintAcitivty.this,
                                            e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();

                            //TODO : fix badFilePath
                            String url = "badFilePath";

                            String replacementID = registerCustomerName;

                            Map<String, Object> replacementDetails = new HashMap<String, Object>();
                            replacementDetails.put("CustomerName", registerCustomerName);
                            replacementDetails.put("PhoneNumber", registerPhoneNumber);
                            replacementDetails.put("DateOfPurchase", registerDateOfPurchase);
                            replacementDetails.put("ModelNumber", registerModelNumber);
                            replacementDetails.put("SerialNumber", registerSerialNumber);
                            replacementDetails.put("ReportUrl", url);
                            replacementDetails.put("Status", "Pending");

                            mref.child(userType).child("RequestServices").child("RegisterComplaints").child(replacementID).updateChildren(replacementDetails)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            LoadingBar.dismiss();
                                            Toast.makeText(RegisterComplaintAcitivty.this, "Complaints Details Uploaded Successfully..", Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(RegisterComplaintAcitivty.this, RequestServiceActivity.class);
                                            startActivity(i);
                                        }
                                    });

                        }
                    });
        } else {
            Toast.makeText(RegisterComplaintAcitivty.this, "Please select product image..", Toast.LENGTH_SHORT).show();
        }
    }

    // Override onActivityResult method
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

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
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void showDatePickerDialogBox() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                .datePicker().setTitleText("select a date")
                .build();
        datePicker.addOnPositiveButtonClickListener(selection->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = sdf.format(calendar.getTime());
            RegisterDateOfPurchaseTI.setText(formattedDate);
        });
        datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
    }
}