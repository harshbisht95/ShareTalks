package com.example.sharetalks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText editTextCountryCode, editTextPhone;
    Button buttonContinue;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        sharedPref = new SharedPref(this);

        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);

        buttonContinue.setOnClickListener(v -> {
            String code = editTextCountryCode.getText().toString().trim();
            String number = editTextPhone.getText().toString().trim();
            if (code.isEmpty() || number.length() < 1) {
                editTextCountryCode.setError("Enter valid code");
                editTextCountryCode.requestFocus();
                return;
            }
            if (number.length() < 10) {
                editTextPhone.setError("Valid number is required");
                editTextPhone.requestFocus();
                return;
            }

            String phoneNumber = code + number;
            sharedPref.setStr(Constants.SHARED_PREF_USER_PHONE_NUMBER,phoneNumber);
            Log.d("SharedPref 1 Phone",sharedPref.getStr(Constants.SHARED_PREF_USER_PHONE_NUMBER));
            Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d("SharedPref 2 Details",String.valueOf(sharedPref.getBool(Constants.SHARED_PREF_USER_DETAILS_ADDED)));
            if(sharedPref.getBool(Constants.SHARED_PREF_USER_DETAILS_ADDED)){

                Intent intent = new Intent(this, MainActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(this, MainActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
    }
}