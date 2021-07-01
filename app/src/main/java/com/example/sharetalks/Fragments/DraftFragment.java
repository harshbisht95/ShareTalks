package com.example.sharetalks.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharetalks.Adapters.AdapterClan;
import com.example.sharetalks.Adapters.AdapterPost;
import com.example.sharetalks.Bean.Post;
import com.example.sharetalks.Bean.UserDetails;
import com.example.sharetalks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DraftFragment extends Fragment {
    Context context;
    RecyclerView recyclerView;
    private AdapterPost adapterPost;
    private List<Post> postList = new ArrayList<Post>();
    private AdapterClan adapterClan;
    private List<UserDetails> clanlist = new ArrayList<UserDetails>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draft, container, false);

        recyclerView = view.findViewById(R.id.list_of_posts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        ll();
    }

    private void populateData() {
        FirebaseDatabase.getInstance().getReference().child("Global Post").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Post modelChat = dataSnapshot1.getValue(Post.class);
                        postList.add(modelChat);
                        adapterPost = new AdapterPost(context, postList);
                        recyclerView.getRecycledViewPool().clear();
                        adapterPost.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterPost);
                        recyclerView.scrollToPosition(postList.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void ll() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").orderBy("username")
                .get()
                .addOnCompleteListener(task -> {
                    clanlist.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserDetails userDetails = document.toObject(UserDetails.class);
                            clanlist.add(userDetails);
                            adapterClan = new AdapterClan(context, clanlist);
                            recyclerView.getRecycledViewPool().clear();
                            adapterClan.notifyDataSetChanged();
                            recyclerView.setAdapter(adapterClan);
                            recyclerView.scrollToPosition(clanlist.size() - 1);
                        }
                    } else {

                    }
                });
    }
}