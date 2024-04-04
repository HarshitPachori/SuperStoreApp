package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.app.superdistributor.admin.AdminPanelActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    LottieAnimationView LottieHomeView;

    TextView MainTextView;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();

        LottieHomeView = findViewById(R.id.lottiehomeview);
        MainTextView = findViewById(R.id.main_text);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_out);
        MainTextView.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                SharedPreferences sh = getSharedPreferences("LoginSharedPref", Context.MODE_PRIVATE);

                String UserType = sh.getString("UserType", "");
                String UserName = sh.getString("UserName", "");
                String UserPassword = sh.getString("UserPassword", "");

                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if(UserType.equals("Admin"))
                        {
                            Intent i = new Intent(MainActivity.this, AdminPanelActivity.class);
                            i.putExtra("Username","admin");
                            startActivity(i);
                        }
                        else if (UserType.equals("Managers")) {
                            Intent i = new Intent(MainActivity.this, AdminPanelActivity.class);
                            i.putExtra("Username",snapshot.child(UserType).child(UserName).child("Name").getValue().toString());
                            startActivity(i);
                        }
                        else if (UserType.equals("Accountants")) {
                            Intent i = new Intent(MainActivity.this, AdminPanelActivity.class);
                            i.putExtra("Username",snapshot.child(UserType).child(UserName).child("Name").getValue().toString());
                            startActivity(i);
                        }
                        else if (UserType.equals("Dealers")) {
                            Intent i = new Intent(MainActivity.this, DealerHomeActivity.class);
                            i.putExtra("DealerName",UserName);
                            startActivity(i);
                        }
                        else if (UserType.equals("SRs")) {
                            Intent i = new Intent(MainActivity.this, SRHomeActivity.class);
                            i.putExtra("SRUsername", UserName);
                            startActivity(i);
                        }
                        else if (UserType.equals("Technicians")) {
                            Intent i = new Intent(MainActivity.this, TechnicianHomeActivity.class);
                            i.putExtra("Username",snapshot.child(UserType).child(UserName).child("UserName").getValue().toString());
                            startActivity(i);
                        }
                        else
                        {
                            Intent i=new Intent(MainActivity.this, WelcomeActivity.class);
                            MainActivity.this.startActivity(i);
                            MainActivity.this.finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }
        },4500);

    }
}