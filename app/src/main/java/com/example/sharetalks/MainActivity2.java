package com.example.sharetalks;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.sharetalks.Fragments.DraftFragment;
import com.example.sharetalks.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    String USERNAME;
    SharedPref sharedPref;

    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sharedPref = new SharedPref(this);
        Log.d("Debbgg",sharedPref.getStr(Constants.SHARED_PREF_USER_RANK));
        USERNAME = sharedPref.getStr(Constants.SHARED_PREF_USER_NAME);
        Log.d("SharedPref 11 Name",sharedPref.getStr(Constants.SHARED_PREF_USER_NAME));
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.send);
        checkRank();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Share Talks");

        drawerLayout = findViewById(R.id.nn);
        navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.Name);
        navUsername.setText(sharedPref.getStr(Constants.SHARED_PREF_USER_NAME));
        TextView navUserRank = (TextView) headerView.findViewById(R.id.Rank);
        navUserRank.setText(sharedPref.getStr(Constants.SHARED_PREF_USER_RANK));
        Log.d("SharedPref 12 Name",sharedPref.getStr(Constants.SHARED_PREF_USER_NAME));
        Log.d("SharedPref 13 Rank",sharedPref.getStr(Constants.SHARED_PREF_USER_RANK));

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        Log.d("Debbgg 4",sharedPref.getStr(Constants.SHARED_PREF_USER_RANK));
    }

    private void checkRank() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("User Rank").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Debbgg 5",uid);
                if (dataSnapshot.exists()) {
                    String rk = dataSnapshot.child("Rank").getValue().toString();
                    sharedPref.setStr(Constants.SHARED_PREF_USER_RANK, rk);
                    Log.d("Debbgg 7",sharedPref.getStr(Constants.SHARED_PREF_USER_RANK));
                } else {
                    sharedPref.setStr(Constants.SHARED_PREF_USER_RANK, "Member");
                    Log.d("Debbgg 8",sharedPref.getStr(Constants.SHARED_PREF_USER_RANK));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment frag = null;
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.My_Account33) {
            frag = new DraftFragment();
            if (frag != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.view_pager, frag); // replace a Fragment with Frame Layout
                transaction.commit(); // commit the changes
                drawerLayout.closeDrawers(); // close the all open Drawer Views
            }
            Toast.makeText(getApplicationContext(), "TODO /// Profile", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.clan) {
            Toast.makeText(getApplicationContext(), "TODO /// Clan", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.logout) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            FirebaseAuth.getInstance().signOut();
                            sharedPref.delete();
                            Toast.makeText(getApplicationContext(), "Successfully Logged out", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            return true;
        }
        return false;
    }
}