package com.app.superdistributor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class MyMessagesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ExtendedFloatingActionButton addMessage, clearAllMessages;
    DatabaseReference databaseReference;
    List<String> dealers,srs,technicians ;
    ArrayList<MessageModel> list;
    MessagesAdapter messagesAdapter;
    String username;
    ImageView noMessagesIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);
        username = getIntent().getStringExtra("Username");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        addMessage = findViewById(R.id.add_msg);
        clearAllMessages = findViewById(R.id.clear_all_msg);
        noMessagesIcon = findViewById(R.id.nomsgssign);
        recyclerView = findViewById(R.id.messages_rcv);
        list = new ArrayList<>();
        dealers = new ArrayList<>();
        srs = new ArrayList<>();
        technicians = new ArrayList<>();
        databaseReference.child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("Recipient").getValue().toString().equals(username)) {

                        list.add(dataSnapshot.getValue(MessageModel.class));
                    }
                }
                messagesAdapter.notifyDataSetChanged();
                Log.d("Chekar",list.toString());
                if (list.isEmpty()) {
                    Toast.makeText(MyMessagesActivity.this, "No messages", Toast.LENGTH_LONG).show();
                    noMessagesIcon.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.scrollToPosition(list.size()-1);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        databaseReference.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!dataSnapshot.getKey().equals("RequestServices")) dealers.add(dataSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        databaseReference.child("SRs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!dataSnapshot.getKey().equals("RequestServices")) srs.add(dataSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        databaseReference.child("Technicians").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!dataSnapshot.getKey().equals("RequestServices")) technicians.add(dataSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(username,this, list);
//        if(messagesAdapter.getItemCount() == 0) Toast.makeText(this, "No messages", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(messagesAdapter);
        addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] users = {"Admin","Dealer", "SR", "Technician"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MyMessagesActivity.this);
                builder.setTitle("Select User Type");
                builder.setItems(users, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (users[i]){
                            case "Admin" : generateMessageBodyDialog("admin"); break;
                            case "Dealer" : generateDialogWithNames(dealers.toArray(new String[0])); break;
                            case "SR" : generateDialogWithNames(srs.toArray(new String[0])); break;
                            case "Technician" : generateDialogWithNames(technicians.toArray(new String[0])); break;
                        }
                    }
                });
                builder.show();
            }
        });
       if(!username.equals("admin")){
           clearAllMessages.setVisibility(View.GONE);
       }else{
           clearAllMessages.setVisibility(View.VISIBLE);
       }
        clearAllMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        messagesAdapter.notifyDataSetChanged();
    }
    protected void generateDialogWithNames(String[] users){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyMessagesActivity.this);
        builder.setTitle("Select recipient");
        builder.setItems(users, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                generateMessageBodyDialog(users[i]);
            }
        });
        builder.show();

    }
    protected void generateMessageBodyDialog(String user){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyMessagesActivity.this);
        builder.setTitle("Send a message to " + user);
        EditText messageBody = new EditText(this);
        builder.setView(messageBody);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = messageBody.getText().toString();
                Map<String,Object> message = new HashMap<>();
                message.put("Sender",username);
                message.put("Recipient",user);
                message.put("Message",text);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedTimestamp = sdf.format(new Date());
                message.put("Timestamp",formattedTimestamp);
                databaseReference.child("Messages").child(UUID.randomUUID().toString()).updateChildren(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MyMessagesActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyMessagesActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyMessagesActivity.this);
        builder.setTitle("Confirm deletion");
        builder.setMessage("Are you sure you want to clear all messages?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearAllMessages();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    protected void clearAllMessages(){
  databaseReference.child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
          for(DataSnapshot dataSnapshot : snapshot.getChildren()){
              dataSnapshot.getRef().removeValue()
                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void unused) {
Toast.makeText(MyMessagesActivity.this,"All messages deleted successfully",Toast.LENGTH_SHORT).show();
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Toast.makeText(MyMessagesActivity.this,"Failed to delete messages",Toast.LENGTH_SHORT).show();
                          }
                      });
          }
          list.clear();
          messagesAdapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
  });
    }
}