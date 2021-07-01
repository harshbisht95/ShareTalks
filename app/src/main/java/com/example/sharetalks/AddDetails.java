package com.example.sharetalks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharetalks.Bean.UserDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class AddDetails extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference save_document_reference;

    SharedPref sharedPref;
    private EditText username;
    private EditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        sharedPref = new SharedPref(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        save_document_reference = firebaseFirestore.collection("Users").document(uid);


        username = (EditText) findViewById(R.id.username);
        location = (EditText) findViewById(R.id.location);
        Button go = (Button) findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (username.toString().isEmpty() || username.getText().toString().length() < 3) {
                    username.setError("Please enter valid Name");
                    username.requestFocus();
                    return;
                } else if (location.getText().toString().isEmpty() || location.getText().toString().length() < 3) {
                    location.setError("Please enter valid City Name");
                    location.requestFocus();
                    return;
                } else {
                    sharedPref.setStr(Constants.SHARED_PREF_USER_NAME, username.getText().toString());
                    sharedPref.setStr(Constants.SHARED_PREF_USER_LOCATION, location.getText().toString());
                    Log.d("SharedPref 9 Name",sharedPref.getStr(Constants.SHARED_PREF_USER_NAME));
                    Log.d("SharedPref 10 Location",sharedPref.getStr(Constants.SHARED_PREF_USER_LOCATION));
                    checkUser();
                }
            }
        });
    }

    //146D31
    private void addDataToFirestore() {
        UserDetails userData = new UserDetails(username.getText().toString(),
                sharedPref.getStr(Constants.SHARED_PREF_USER_PHONE_NUMBER),
                location.getText().toString(), "Member"
        );
        sharedPref.setStr(Constants.SHARED_PREF_USER_RANK, "Member");
        save_document_reference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.POST_TOPIC).addOnSuccessListener(aVoid1 -> sharedPref.setBool(Constants.SHARED_PREF_USER_SUBSCRIPTION, true));


                Toast.makeText(AddDetails.this, "Details added successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(AddDetails.this, "Fail to add Details" + e, Toast.LENGTH_SHORT).show());
    }

    private void checkUser() {
        Query check_user_query = firebaseFirestore.collection("Users").whereEqualTo("username", username.getText().toString());
        check_user_query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                            addDataToFirestore();
                            Intent intent = new Intent(AddDetails.this, MainActivity2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            sharedPref.setBool(Constants.SHARED_PREF_USER_DETAILS_ADDED, true);
                        } else {
                            username.setError("Already Taken");
                            username.requestFocus();
                        }
                    } else {
                        Log.d("Debug 4", "User Check Failed");
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPref.setBool(Constants.SHARED_PREF_USER_DETAILS_ADDED, false);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }
}