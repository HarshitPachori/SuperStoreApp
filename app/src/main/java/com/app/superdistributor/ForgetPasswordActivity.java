package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgetPasswordActivity extends AppCompatActivity {

    RadioGroup ResetradioGroup;
    RadioButton ResetradioButton;

    TextInputEditText ForgetPasswordUsername, OTPET, NewPasswordET, ConfirmPasswordET;
    Button SendOTPBtn, SubmitOTP, SetNewPasswordBtn;

    private ProgressDialog LoadingBar;
    String CountryCode="+91";
    String VerificationId;

    boolean output;
    private DatabaseReference mref;

    String userType = "";
    private FirebaseAuth mAuth;
    TextInputLayout ForgetPasswordUserNameTIL, OTPTIL, NewPasswordTIL, ConfirmPasswordTIL;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        mAuth = FirebaseAuth.getInstance();

        ResetradioGroup=(RadioGroup)findViewById(R.id.resetradioGroup);

        LoadingBar=new ProgressDialog(this);

        mref= FirebaseDatabase.getInstance().getReference();

        ForgetPasswordUsername = findViewById(R.id.forgetUserNameET);
        ForgetPasswordUserNameTIL = findViewById(R.id.forgetPasswordUserNameTIL);
        SendOTPBtn = findViewById(R.id.sendOTPBtn);

        OTPET = findViewById(R.id.OTPET);
        OTPTIL = findViewById(R.id.OTPTIL);
        SubmitOTP = findViewById(R.id.submitOTPBtn);

        NewPasswordET = findViewById(R.id.NewPasswordET);
        ConfirmPasswordET = findViewById(R.id.ConfirmPasswordET);
        NewPasswordTIL = findViewById(R.id.NewPasswordTIL);
        ConfirmPasswordTIL = findViewById(R.id.ConfirmPasswordTIL);

        SetNewPasswordBtn = findViewById(R.id.setNewPasswordBtn);



        SendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int radioId=ResetradioGroup.getCheckedRadioButtonId();
                ResetradioButton=findViewById(radioId);

                if(ResetradioButton.getText().toString().equals("Admin"))
                {
                    userType = "Admin";
                }
                else if(ResetradioButton.getText().toString().equals("Dealer"))
                {
                    userType = "Dealers";
                }
                else if(ResetradioButton.getText().toString().equals("SR"))
                {
                    userType = "SRs";
                }
                else
                {
                    userType = "Technicians";
                }

                Log.d("userdata",userType);
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
Log.d("userdata",snapshot.child(userType).child(ForgetPasswordUsername.getText().toString()).toString());
                        if((snapshot.child(userType).child(ForgetPasswordUsername.getText().toString()).exists()))
                        {

                            String mobileNumber = snapshot.child(userType)
                                    .child(ForgetPasswordUsername.getText().toString()).child("Phone")
                                    .getValue().toString();
                            Log.d("userdata",mobileNumber);
                            String phoneNumber = CountryCode + mobileNumber;
                            Log.d("userdata",phoneNumber);
                            ForgetPasswordUserNameTIL.setVisibility(View.INVISIBLE);
                            SendOTPBtn.setVisibility(View.INVISIBLE);
                            ResetradioGroup.setVisibility(View.INVISIBLE);

                            OTPTIL.setVisibility(View.VISIBLE);
                            SubmitOTP.setVisibility(View.VISIBLE);

                            sendVerificationCode(phoneNumber);
                        }
                        else
                        {
                            Toast.makeText(ForgetPasswordActivity.this, "Account doesn't exist with this username", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        SubmitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=OTPET.getText().toString().trim();
                if(code.isEmpty() || code.length() <6)
                {
                    OTPET.setError("Enter Code..");
                    OTPET.requestFocus();
                    return;
                }
                LoadingBar.setTitle("Please Wait..");
                LoadingBar.setMessage("Please Wait while we are checking our credentials...");
                LoadingBar.show();


                verifyCode(code);
            }
        });

        SetNewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = NewPasswordET.getText().toString();
                String confirmpassword = ConfirmPasswordET.getText().toString();

                if(password.equals(confirmpassword))
                {
                    mref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mref.child(userType).child(ForgetPasswordUsername.getText().toString()).child("DealerPassword")
                                    .setValue(confirmpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ForgetPasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                            startActivity(i);
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(ForgetPasswordActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                VerificationId = s;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // OTP automatically verified
                        signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Verification failed
                        Log.e("verification_failed", e.getMessage());
                        LoadingBar.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
            }
        };
    }
    private void verifyCode(String code)
    {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(VerificationId,code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            LoadingBar.dismiss();

                            NewPasswordTIL.setVisibility(View.VISIBLE);
                            ConfirmPasswordTIL.setVisibility(View.VISIBLE);
                            SetNewPasswordBtn.setVisibility(View.VISIBLE);

                            ForgetPasswordUserNameTIL.setVisibility(View.INVISIBLE);
                            SendOTPBtn.setVisibility(View.INVISIBLE);

                            OTPTIL.setVisibility(View.INVISIBLE);
                            SubmitOTP.setVisibility(View.INVISIBLE);

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Log.d("incorrectcode",task.getException().getMessage());
                            Toast.makeText(ForgetPasswordActivity.this, "After : -"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String phone) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}