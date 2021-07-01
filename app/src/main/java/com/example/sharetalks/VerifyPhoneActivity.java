package com.example.sharetalks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth mAuth;

    ProgressBar progressBar;
    EditText editText;
    Button buttonSignIn;
    String phoneNumber;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        sharedPref = new SharedPref(this);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        phoneNumber = sharedPref.getStr(Constants.SHARED_PREF_USER_PHONE_NUMBER);
        Log.d("SharedPref 3 Phone",sharedPref.getStr(Constants.SHARED_PREF_USER_PHONE_NUMBER));
        sendVerificationCode(phoneNumber);

        buttonSignIn.setOnClickListener(v -> {
            String code = editText.getText().toString().trim();

            if (code.isEmpty() || code.length() < 6) {
                editText.setError("Enter code...");
                editText.requestFocus();
                return;
            }
            verifyCode(code);
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userAlreadyExists(phoneNumber);
                    } else {
                        Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setSharedPref(String uid) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docRef = rootRef.collection("Users").document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    sharedPref.setStr(Constants.SHARED_PREF_USER_NAME, Objects.requireNonNull(document.getData().get("username")).toString());
                    sharedPref.setStr(Constants.SHARED_PREF_USER_RANK, Objects.requireNonNull(document.getData().get("rank")).toString());
                    sharedPref.setStr(Constants.SHARED_PREF_USER_LOCATION, Objects.requireNonNull(document.getData().get("location")).toString());
                    Log.d("SharedPref 4 Phone",sharedPref.getStr(Constants.SHARED_PREF_USER_PHONE_NUMBER));
                    Log.d("SharedPref 5 Name",sharedPref.getStr(Constants.SHARED_PREF_USER_NAME));
                    Log.d("SharedPref 6 Rank",sharedPref.getStr(Constants.SHARED_PREF_USER_RANK));
                    Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity2.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Log.d("Deebug 13", "No such document");
                }
            } else {
                Log.d("Deebug 14", "get failed with ", task.getException());
            }
        });
    }

    private void userAlreadyExists(String phoneNumber) {
        sharedPref.setBool(Constants.SHARED_PREF_USER_DETAILS_ADDED,true);
        Log.d("SharedPref 7 Details",String.valueOf(sharedPref.getBool(Constants.SHARED_PREF_USER_DETAILS_ADDED)));
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference docIdRef = rootRef.collection("Users").document(uid);
        docIdRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("Deeeeeeeeee00","doc exists");
                    setSharedPref(uid);

                } else {
                    Intent intent = new Intent(VerifyPhoneActivity.this, AddDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            } else {
                Log.d("TAG", "Failed with: ", task.getException());
            }
        });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallBack)
                        .build());
        progressBar.setVisibility(View.GONE);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };
}