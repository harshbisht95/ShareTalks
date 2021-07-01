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

import com.example.sharetalks.Adapters.AdapterPost;
import com.example.sharetalks.Bean.Post;
import com.example.sharetalks.FcmNotificationsSender;
import com.example.sharetalks.R;
import com.example.sharetalks.SharedPref;
import com.example.sharetalks.Constants;
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

public class PostFragment extends Fragment {
    private static final String TAG = "re";
    private Context context;
    private RecyclerView recyclerView;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAQgCHUCg:APA91bEVbqgeTP_2o0tICcHQWvOLY3tFiV0sEQIfFuu3tNGlgoHWo5npuCht_4MnN4mlLxkK59EsZpE2-oRpeRsKI6q0ocgex_JmFymIBqsiIHfr3GMKQKJOQvMHV6OEgTiJJaE1dRYj";
    final private String contentType = "application/json";
    private AdapterPost adapterPost;
    private List<Post> chatList = new ArrayList<Post>();
    Button like;
    RadioGroup radioGroup;
    FloatingActionButton add;
    RadioButton rb;
    Spinner spinner;
    private String text;
    DatabaseReference ref_Global_Post;
    private static final String AUTH_KEY = "key=YOUR-SERVER-KEY";
    private String token;
    SharedPref sharedPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = new SharedPref(getActivity());
        View view = inflater.inflate(R.layout.rel, container, false);
        Log.d("Degggg", sharedPref.getStr(Constants.SHARED_PREF_USER_RANK));

    if(sharedPref.getStr(Constants.SHARED_PREF_USER_RANK).equals("Co-Leader")){
        Log.d("Degggg","YOLO");
        add = (FloatingActionButton) view.findViewById(R.id.addone);
        add.setOnClickListener(v -> showAlertDialogButtonClicked(v));
        add.setVisibility(View.VISIBLE);

    }

        recyclerView = view.findViewById(R.id.list_of_posts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        populateData();
    }

    private void populateData() {
        FirebaseDatabase.getInstance().getReference().child("Global Post").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Post modelChat = dataSnapshot1.getValue(Post.class);
                        chatList.add(modelChat); // add the chat in chatlist
                        adapterPost = new AdapterPost(context, chatList);
                        recyclerView.getRecycledViewPool().clear();
                        adapterPost.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterPost);
                        recyclerView.scrollToPosition(chatList.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /*
    private void likes(){
        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {if(TextUtils.isEmpty(input.getText().toString())){}
            else{
                Map<String, Object> values = new HashMap<>();
                values.put("messageText", input.getText().toString());
                values.put("messageUser", USERNAME);
                values.put("messageTime", ServerValue.TIMESTAMP);

                ref_Global_Chat.push().setValue(values);
                input.setText("");
            }
            }
        });
    }
    */
    public void showAlertDialogButtonClicked(View view) {
        Post post = new Post();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.post_dialog, null);
        builder.setView(dialogView);

        // Data
        EditText stock = dialogView.findViewById(R.id.stock);
        EditText buy = dialogView.findViewById(R.id.buy);
        EditText target1 = dialogView.findViewById(R.id.Target1);
        EditText target2 = dialogView.findViewById(R.id.Target2);
        EditText time = dialogView.findViewById(R.id.time);

        Button send = (Button) dialogView.findViewById(R.id.send);
        Button closeButton = (Button) dialogView.findViewById(R.id.cancel);

        spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(context, R.array.type_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ///////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                text = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        final AlertDialog dialog = builder.create();
        send.setOnClickListener(v -> {
            if (text.equals("--- Call Type ---")) {
                TextView errorText = (TextView) spinner.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
            } else {
                if (isEmpty(stock)) {
                    stock.setError("Please enter Stock");
                    stock.requestFocus();
                    return;
                }
                if (isEmpty(buy)) {
                    buy.setError("Please enter Price");
                    buy.requestFocus();
                    return;
                }
                if (isEmpty(target1)) {
                    target1.setError("Please enter 1st Target");
                    target1.requestFocus();
                    return;
                }
                if (isEmpty(target2)) {
                    target2.setError("Please enter 2nd Target");
                    target2.requestFocus();
                    return;
                }
                if (isEmpty(time)) {
                    time.setError("Please enter Days");
                    time.requestFocus();
                    return;
                }
                post.setStock(stock.getText().toString());
                post.setBuyPrice(buy.getText().toString());
                post.setTarget1(target1.getText().toString());
                post.setTarget2(target2.getText().toString());
                post.setTargetDays(time.getText().toString());
                post.setTradeType(text);
                sendPost(post);
                dialog.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sendDialogDataToActivity(String data) {
        Toast.makeText(context,
                data,
                Toast.LENGTH_SHORT)
                .show();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void sendPost(Post post) {
        ref_Global_Post = FirebaseDatabase.getInstance().getReference().child("Global Post");
        Log.d("DebuGG", post.getStock());
        Log.d("DebuGG", post.getBuyPrice());
        Log.d("DebuGG", post.getTarget1());
        Log.d("DebuGG", post.getTarget2());
        Log.d("DebuGG", post.getTradeType());
        Log.d("DebuGG", post.getTargetDays());
        Map<String, Object> values = new HashMap<>();
        values.put("BuyPrice", post.getBuyPrice());
        values.put("Stock", post.getStock());
        values.put("Target1", post.getTarget1());
        values.put("Target2", post.getTarget2());
        values.put("TargetDays", post.getTargetDays());
        values.put("TargetType", post.getTradeType());
        values.put("Likes", "0");
        values.put("messageTime", ServerValue.TIMESTAMP);

        ref_Global_Post.push().setValue(values);
        SendNotification(post.getStock());

    }

    private void SendNotification(String Stock) {
        String Title = "New Call";
        String Message = Stock;
        FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender("/topics/Clan",Title,Message,context, getActivity());
        fcmNotificationsSender.SendNotifications();
        Log.d("Debbgg", Message);
    }
        /////////////////////////////////////////////
}
