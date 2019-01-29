package com.israt.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String mVerificationId="";
    private EditText etCode,etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
       mAuth = FirebaseAuth.getInstance();

        etCode =(EditText) findViewById(R.id.etCode);
        etPhone =(EditText) findViewById(R.id.etPhone);
    }

    public void btnCodeGenerator(View view){  generateVerificationCode();}
 public void btnValidate(View view){
     Log.e("code","."+mVerificationId);
     PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,  etCode.getText().toString());
     signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "Suucessfull", Toast.LENGTH_SHORT).show();
                            Log.e("success","...>>>>>>>>>>>>>>>>>>>>>>>. yey");
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.e("failed", "..<<<<<<<<<<< so bad");
                            }
                        }
                    }
                });
    }

    private void generateVerificationCode() {
        Log.e("click","...");
        try {
String phone ="+1"+            etPhone.getText().toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,        // Phone number to verify
                    10,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        } catch (Exception ex){

            Log.e("Err","."+ex.getMessage());
        }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
             // signInWithPhoneAuthCredential(credential);


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId=verificationId;

            Log.e("....","this "+token);

        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            Log.e("ggggggg","time out");
            super.onCodeAutoRetrievalTimeOut(s);
        }
    };





}
