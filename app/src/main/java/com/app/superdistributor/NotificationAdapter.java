package com.app.superdistributor;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.compose.material3.CardColors;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.admin.notification.AdminNotificationActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    Context context;
    ArrayList<NotificationItemModel> list;
    DatabaseReference databaseReference;

    public NotificationAdapter(Context context, ArrayList<NotificationItemModel> list){
        this.context = context;
        this.list = list;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationAdapter.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.complaint_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {

        NotificationItemModel notificationItemModel = list.get(position);
        holder.type.setText(notificationItemModel.getNotificationType());
        holder.tag.setText(notificationItemModel.getNotificationTag());
        holder.description.setText(notificationItemModel.getNotificationDesc());
        if (notificationItemModel.getReportUrl() != null) {

            holder.reportUrlTv.setVisibility(View.VISIBLE);
            holder.reportUrlTv.setMovementMethod(LinkMovementMethod.getInstance());
            holder.reportUrlTv.setPaintFlags(holder.reportUrlTv.getPaintFlags());
            holder.reportUrlTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(notificationItemModel.getReportUrl()));
                    context.startActivity(intent);
                }
            });
        }

        if (notificationItemModel.getNotificationPriority().equals("Yes")) {
            holder.type.setTextColor(Color.RED);
            holder.tag.setTextColor(Color.RED);
            holder.description.setTextColor(Color.rgb(255,150,150));
            holder.reminderIcon.setVisibility(View.VISIBLE);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Context",context.toString());
                if (context.toString().substring(0,49).equals("com.app.superdistributor.PendingApprovalsActivity")) {
                    holder.item.setText("Send a Reminder");
                    sendReminderDialogBox(holder.getAdapterPosition(),
                            notificationItemModel.getNotificationType(),
                            notificationItemModel.getNotificationTag(),
                            notificationItemModel.getNotificationDesc());

                } else {
                    showExpandedDialog(holder.getAdapterPosition(),
                            notificationItemModel.getNotificationType(),
                            notificationItemModel.getNotificationTag(),
                            notificationItemModel.getNotificationDesc());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Button item;
        TextView type, tag, description,reportUrlTv;
        ImageView reminderIcon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.complaint_item_cv);
            type = itemView.findViewById(R.id.dealer_name);
            tag = itemView.findViewById(R.id.tag);
            reminderIcon = itemView.findViewById(R.id.reminder_icon);
            description = itemView.findViewById(R.id.complaint_desc);
            reportUrlTv = itemView.findViewById(R.id.reportUrlTV);
        }
    }

  private void showExpandedDialog(int position, String type, String tag, String description) {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_expanded_view, null);
      TextView tagTv = dialogView.findViewById(R.id.notif_tag);
      TextView descTv = dialogView.findViewById(R.id.notif_description);
      builder.setView(dialogView);
      tagTv.setText(tag);
      descTv.setText(description);
      HashMap<String,Object> updateStatus = new HashMap<>();
      builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              updateStatus.put("Status","Rejected");
              updateStatus.put("Reminder","No");
              if(type.equals("SR Product Confirmation")){
                  databaseReference.child("Admin").child("Notifications")
                          .child("ProductConfirmation").child("SRs")
                          .child(tag).updateChildren(updateStatus);

                  String productID = description.split("ProductID")[1].substring(3,description.split("ProductID")[1].length()).split("Quantity")[0].trim();

                  String userID = description.split("Placed by : ")[1].trim();

                  Map<String, Object> status = new HashMap<>();
                  status.put("Status", "Rejected");

                  databaseReference.child("Dealers").child(userID).child("Orders").child(productID).updateChildren(status);


              }
              else if (type.equals("Dealer Complaint")) {
                  databaseReference.child("Dealers").child("RequestServices")
                          .child("RegisterComplaints").child(tag)
                          .updateChildren(updateStatus);
              }
              else if (type.equals("Replacement by Dealer")) {
                      databaseReference.child("Dealers").child("RequestServices")
                              .child("ReplacementByDealer").child(tag)
                              .updateChildren(updateStatus);
              }
              else if (type.equals("Grievance")) {
                  databaseReference.child("Grievances").child(tag).removeValue();
              }else if(type.equals("Expense")){
                 DatabaseReference exReference = databaseReference.child("SRs");
                 exReference.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for (DataSnapshot srSnapshot : snapshot.getChildren()) {
                             for (DataSnapshot expenseSnapshot : srSnapshot.child("Expenses").getChildren()) {
                                 if (expenseSnapshot.getKey().equals(tag)) {
                                     // Found the expense, now update its status
                                     DatabaseReference expenseRef = expenseSnapshot.getRef();
                                     expenseRef.updateChildren(updateStatus);
                                     return; // Stop iterating once the expense is found and updated
                                 }
                             }
                         }
                         // If the expense is not found
                         Toast.makeText(context, "Expense not found", Toast.LENGTH_SHORT).show();
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
              }
              if(position<list.size()) list.remove(position);
              ((Activity)context).finish();
              context.startActivity(new Intent(context,AdminNotificationActivity.class));
              Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
          }
      }).setNegativeButton("Accept", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              updateStatus.put("Status","Accepted");
              updateStatus.put("Reminder","No");
              if(type.equals("SR Product Confirmation")){
                  databaseReference.child("Admin").child("Notifications")
                          .child("ProductConfirmation").child("SRs")
                          .child(tag).updateChildren(updateStatus);

                  String productID = description.split("ProductID")[1].substring(3,description.split("ProductID")[1].length()).split("Quantity")[0].trim();

                  String userID = description.split("Placed by : ")[1].trim();

                  Map<String, Object> status = new HashMap<>();
                  status.put("Status", "Accepted");

                  databaseReference.child("Dealers").child(userID).child("Orders").child(productID).updateChildren(status);
              }
              else if (type.equals("Dealer Complaint")) {
                  databaseReference.child("Dealers").child("RequestServices")
                          .child("RegisterComplaints").child(tag)
                          .updateChildren(updateStatus);
              }
              else if (type.equals("Replacement by Dealer")) {
                  databaseReference.child("Dealers").child("RequestServices")
                          .child("ReplacementByDealer").child(tag)
                          .updateChildren(updateStatus);
              }
              else if (type.equals("Grievance")) {
                  databaseReference.child("Grievances").child(tag).removeValue();
              }else if(type.equals("Expense")){
                  DatabaseReference exReference = databaseReference.child("SRs");
                  exReference.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          for (DataSnapshot srSnapshot : snapshot.getChildren()) {
                              for (DataSnapshot expenseSnapshot : srSnapshot.child("Expenses").getChildren()) {
                                  if (expenseSnapshot.getKey().equals(tag)) {
                                      // Found the expense, now update its status
                                      DatabaseReference expenseRef = expenseSnapshot.getRef();
                                      expenseRef.updateChildren(updateStatus);
                                      return; // Stop iterating once the expense is found and updated
                                  }
                              }
                          }
                          // If the expense is not found
                          Toast.makeText(context, "Expense not found", Toast.LENGTH_SHORT).show();
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });
              }
              if(position<list.size()) list.remove(position);
              ((Activity)context).finish();
              context.startActivity(new Intent(context,AdminNotificationActivity.class));
              Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
          }
      });
      AlertDialog dialog = builder.create();
      dialog.setTitle(type);
      dialog.show();
  }
  private void sendReminderDialogBox(int position, String type, String tag, String description) {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setTitle("Send a Reminder?");
      HashMap<String,Object> updateStatus = new HashMap<>();
      builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              updateStatus.put("Reminder","No");
              if(type.equals("SR Product Confirmation")){
                  databaseReference.child("Admin").child("Notifications")
                          .child("ProductConfirmation").child("SRs")
                          .child(tag).updateChildren(updateStatus);
              }
              else if (type.equals("Dealer Complaint")) {
                  databaseReference.child("Dealers").child("RequestServices")
                          .child("RegisterComplaints").child(tag)
                          .updateChildren(updateStatus);
              }
              else if (type.equals("Replacement by Dealer")) {
                      databaseReference.child("Dealers").child("RequestServices")
                              .child("ReplacementByDealer").child(tag)
                              .updateChildren(updateStatus);
              }
              else if (type.equals("Grievance")) {
                  databaseReference.child("Grievances").child(tag).removeValue();
              }
              if(position<list.size()) list.remove(position);
              ((Activity)context).finish();
              context.startActivity(new Intent(context,PendingApprovalsActivity.class));
              Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
          }
      }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              updateStatus.put("Reminder","Yes");
              if(type.equals("SR Product Confirmation")){
                  databaseReference.child("Admin").child("Notifications")
                          .child("ProductConfirmation").child("SRs")
                          .child(tag).updateChildren(updateStatus);
              }
              else if (type.equals("Dealer Complaint")) {
                  databaseReference.child("Dealers").child("RequestServices")
                          .child("RegisterComplaints").child(tag)
                          .updateChildren(updateStatus);
              }
              else if (type.equals("Replacement by Dealer")) {
                  databaseReference.child("Dealers").child("RequestServices")
                          .child("ReplacementByDealer").child(tag)
                          .updateChildren(updateStatus);
              }
              else if (type.equals("Grievance")) {
                  databaseReference.child("Grievances").child(tag).removeValue();
              }
              if(position<list.size()) list.remove(position);
              ((Activity)context).finish();
              context.startActivity(new Intent(context, PendingApprovalsActivity.class));
              Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
          }
      });
      AlertDialog dialog = builder.create();
      dialog.show();
  }
}
