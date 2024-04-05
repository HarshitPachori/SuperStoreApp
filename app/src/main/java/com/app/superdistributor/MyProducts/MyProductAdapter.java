package com.app.superdistributor.MyProducts;

import static com.app.superdistributor.MyProducts.PlaceOrderActivity.orderMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<Products> list;
    String DealerName;


    public MyProductAdapter(Context context, ArrayList<Products> list, String dealerName) {
        this.context = context;
        this.list = list;
        this.DealerName = dealerName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.prodcut_item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Products products = list.get(position);
        holder.productName.setText(products.getName());
        holder.AddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.Qty.getText().toString().equals("0"))
                {
                    Toast.makeText(context, "Please increase quantity to add product..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String, Object> productMap = new HashMap<>();
                    Toast.makeText(context, "Product added..", Toast.LENGTH_SHORT).show();
                    productMap.put("Name",products.getName());
                    productMap.put("ProductID",products.getProductID());
                    productMap.put("ProductQty",holder.Qty.getText().toString());
                    productMap.put("DealerName",DealerName);
                    productMap.put("Status","Open");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedTimestamp = sdf.format(new Date());
                    productMap.put("Timestamp",formattedTimestamp);
                    orderMap.put(products.getProductID(),productMap);
                    holder.Qty.setText("0");
                    //Toast.makeText(context, ""+products.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView productName;
        Button AddProductBtn;

        TextInputEditText Qty;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.tvProductName);
            AddProductBtn = itemView.findViewById(R.id.addProduct);
            Qty = itemView.findViewById(R.id.qty);

        }
    }

}
