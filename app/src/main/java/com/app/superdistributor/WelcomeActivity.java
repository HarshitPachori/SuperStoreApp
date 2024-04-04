package com.app.superdistributor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Login=(Button)findViewById(R.id.loginBtn);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}