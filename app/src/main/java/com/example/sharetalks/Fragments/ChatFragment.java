package com.example.sharetalks.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharetalks.Adapters.AdapterChat;
import com.example.sharetalks.AddDetails;
import com.example.sharetalks.Bean.message;
import com.example.sharetalks.Constants;
import com.example.sharetalks.R;
import com.example.sharetalks.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment{
    private Context context;
    private RecyclerView recyclerView;
    private AdapterChat adapterChat;
    private List<message> chatList = new ArrayList<message>();
    String USERNAME;
    EditText input;
    SharedPref sharedPref;
    FloatingActionButton send;
DatabaseReference ref_Global_Chat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPref = new SharedPref(getActivity());
        View view = inflater.inflate(R.layout.chat_activity, container, false);
        ref_Global_Chat = FirebaseDatabase.getInstance().getReference().child("Global Chat");
        send = (FloatingActionButton) view.findViewById(R.id.send);
        input = (EditText) view.findViewById(R.id.input);
        USERNAME = sharedPref.getStr(Constants.SHARED_PREF_USER_NAME);
        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {if(TextUtils.isEmpty(input.getText().toString())){}
            else{
                Map<String, Object> values = new HashMap<>();
                values.put("messageText", input.getText().toString().trim());
                values.put("messageUser", USERNAME);
                values.put("messageTime", ServerValue.TIMESTAMP);

                ref_Global_Chat.push().setValue(values);
                input.setText("");
            }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        recyclerView = view.findViewById(R.id.list_of_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        populateData();
    }
    private void populateData(){
        FirebaseDatabase.getInstance().getReference().child("Global Chat").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        message modelChat = dataSnapshot1.getValue(message.class);
                        chatList.add(modelChat); // add the chat in chatlist
                        Log.d(">>>>>>>>>>>>>>>>>>>> 8", modelChat.getMessageUser());
                        adapterChat = new AdapterChat(context, chatList, String.valueOf(modelChat.getMessageUser()));
                        recyclerView.getRecycledViewPool().clear();
                        adapterChat.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterChat);
                        recyclerView.scrollToPosition(chatList.size() - 1);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    }
