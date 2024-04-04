package com.app.superdistributor.MyProducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.app.superdistributor.admin.AddSRActivity;
import com.app.superdistributor.admin.AdminPanelActivity;
import com.app.superdistributor.admin.ViewSRActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewProductList extends AppCompatActivity {

    private ListView ProductDataLV;
    private ProgressDialog LoadingBar;
    ArrayList<String> productList, productNameList;
    DatabaseReference database;

    ArrayList<String> productArrayList = new ArrayList<>();
    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_list);

        Username = getIntent().getStringExtra("Username");
        ProductDataLV = findViewById(R.id.viewProductlv);
        productList = new ArrayList<String>();
        productNameList = new ArrayList<>();
        LoadingBar=new ProgressDialog(this);
        initializeListView();
    }
    private void initializeListView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productList);

        database = FirebaseDatabase.getInstance().getReference();

        productList.clear();
        productArrayList.clear();
        productNameList.clear();

        database.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    productArrayList.add(snap.getKey());
                    productNameList.add(snap.child("Name").getValue().toString());
                }

                for (int i = 0; i < productArrayList.size(); i++) {
                    productList.add(productNameList.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ProductDataLV.setAdapter(adapter);

        ProductDataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ViewProductList.this);

                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.product_dialog, null);
                builder.setView(customLayout);

                EditText productName = customLayout.findViewById(R.id.productNameET);
                EditText productID = customLayout.findViewById(R.id.productIDET);
                productID.setEnabled(false);

                database = FirebaseDatabase.getInstance().getReference();

                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productID.setText(snapshot.child("Products").child(productArrayList.get(i)).child("ProductID").getValue().toString());
                        productName.setText(snapshot.child("Products").child(productArrayList.get(i)).child("Name").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // add a button
                builder.setPositiveButton("Update Product", (dialog, which) -> {
                    // send data from the AlertDialog to the Activity

                    LoadingBar.setTitle("Update Product");
                    LoadingBar.setMessage("Please wait we are updating product in our database..");
                    LoadingBar.setCanceledOnTouchOutside(false);
                    LoadingBar.show();

                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("Name",productName.getText().toString());

                            database.child("Products").child(productID.getText().toString()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    LoadingBar.dismiss();
                                    Toast.makeText(ViewProductList.this, "Product Updated!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ViewProductList.this, AdminPanelActivity.class);
                                    i.putExtra("Username", Username);
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                });
                // create and show the alert dialog
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ProductDataLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewProductList.this);
                builder.setTitle("Action!");
                builder.setMessage("Do You want to delete Product?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child("Products").child(productArrayList.remove(i)).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(ViewProductList.this, AdminPanelActivity.class);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(ViewProductList.this, "Product - " + productList.get(i) + " is deleted successfully from system.", Toast.LENGTH_SHORT).show();
                                        intent.putExtra("Username", Username);
                                        startActivity(intent);
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
                builder.show();

                return false;
            }
        });
    }
}