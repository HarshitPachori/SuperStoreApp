package com.app.superdistributor.RequestService;

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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.app.superdistributor.RequestServiceActivity;
import com.app.superdistributor.admin.AdminPanelActivity;
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

public class ReplaceByDealerActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private ProgressDialog LoadingBar;
    private Uri filePath;
    private DatabaseReference mref;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextInputEditText CustomerNameTI, PhoneNumberTI, ModelNumberTI, SerialNumberTI, NewProductSerialNumberTI;
    Button AttachReportBtn, SendForApprovalBtn, mPickDateButton;
    TextView DealerDateOfPurchaseTV;
    String selectedDate, userType, username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_by_dealer);

        userType = getIntent().getType();

        username = (userType.equals("Dealers"))?getIntent().getStringExtra("Username"):getIntent().getStringExtra("SRUserame");
        Log.d("user : ", userType + "   "+ username);
        CustomerNameTI = findViewById(R.id.customerNameTI);
        PhoneNumberTI = findViewById(R.id.phoneNoTI);
        mPickDateButton = findViewById(R.id.dealerdateOfPurchase);
        DealerDateOfPurchaseTV = findViewById(R.id.dealer_show_selected_date);

        ModelNumberTI = findViewById(R.id.modelNoTI);
        SerialNumberTI = findViewById(R.id.serialNoTI);
        NewProductSerialNumberTI = findViewById(R.id.newProductSerialNoTI);

        AttachReportBtn = findViewById(R.id.attachReportBtn);
        SendForApprovalBtn = findViewById(R.id.sendForApprovalBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        LoadingBar=new ProgressDialog(this);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(
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
                        selectedDate = materialDatePicker.getHeaderText();
                        DealerDateOfPurchaseTV.setText(selectedDate);
                    }
                });

        mref= FirebaseDatabase.getInstance().getReference();


        checkAndRequestPermissions(ReplaceByDealerActivity.this);



        AttachReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(ReplaceByDealerActivity.this);
            }
        });

        SendForApprovalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(CustomerNameTI.getText().toString()))
                {
                    Toast.makeText(ReplaceByDealerActivity.this, "Please enter customer name..", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(PhoneNumberTI.getText().toString()))
                {
                    Toast.makeText(ReplaceByDealerActivity.this, "Please enter phone number..", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(ModelNumberTI.getText().toString()))
                {
                    Toast.makeText(ReplaceByDealerActivity.this, "Please enter model number..", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(SerialNumberTI.getText().toString()))
                {
                    Toast.makeText(ReplaceByDealerActivity.this, "Please enter serial number..", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(NewProductSerialNumberTI.getText().toString()))
                {
                    Toast.makeText(ReplaceByDealerActivity.this, "Please enter new product serial number..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadProduct(CustomerNameTI.getText().toString(),
                            PhoneNumberTI.getText().toString(),
                            selectedDate,
                            ModelNumberTI.getText().toString(),
                            SerialNumberTI.getText().toString(),
                            NewProductSerialNumberTI.getText().toString());
                }
            }
        });

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
                if (ContextCompat.checkSelfPermission(ReplaceByDealerActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(ReplaceByDealerActivity.this,
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
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                        filePath = data.getData();
//                        CheckIV.setImageBitmap(selectedImage);
//                        Toast.makeText(this, "Image ."+selectedImage.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case 1:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        filePath = data.getData();
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                CheckIV.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                                Toast.makeText(this, "Image Added.."+picturePath, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                    break;
//            }
//        }
//    }
//        @Override
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
    private void uploadProduct(String customerName, String phoneNumber, String dateOfPurchase, String modelNumber, String serialNumber, String newProductSerialNumber) {

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
                                            .makeText(ReplaceByDealerActivity.this,
                                                    "Report Image Saved in our Storage!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();

                                            String replacementID = UUID.randomUUID().toString();

                                            Map<String, Object> replacementDetails = new HashMap<String, Object>();
                                            replacementDetails.put("CustomerName", customerName);
                                            replacementDetails.put("PhoneNumber", phoneNumber);
                                            replacementDetails.put("DateOfPurchase", dateOfPurchase);
                                            replacementDetails.put("ModelNumber", modelNumber);
                                            replacementDetails.put("SerialNumber", serialNumber);
                                            replacementDetails.put("NewProductSerialNumber", newProductSerialNumber);
                                            replacementDetails.put("ReportUrl", url);
                                            replacementDetails.put("Status", "Pending");

                                            mref.child(userType).child(username).child("RequestServices").child("ReplacementByDealer").child(customerName).push().setValue(replacementDetails)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            LoadingBar.dismiss();
                                                            Toast.makeText(ReplaceByDealerActivity.this, "Replacement Details Uploaded Successfully..", Toast.LENGTH_SHORT).show();

                                                            Intent i = new Intent(ReplaceByDealerActivity.this, RequestServiceActivity.class);
                                                            i.setType("viaSr");
                                                            i.putExtra("Username",username);
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
                                    .makeText(ReplaceByDealerActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } else {
            Toast.makeText(ReplaceByDealerActivity.this, "Please select product image..", Toast.LENGTH_SHORT).show();
        }
    }

}