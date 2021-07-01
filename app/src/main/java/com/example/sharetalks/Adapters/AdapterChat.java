package com.example.sharetalks.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharetalks.Constants;
import com.example.sharetalks.R;
import com.example.sharetalks.Bean.message;
import com.example.sharetalks.SharedPref;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.Myholder> {
/*
    Context context;
    String username;
    List<message> list;
    SharedPref sharedPref;
    String uname;
    public AdapterChat(Context context, List<message> list, String st) {
        this.context = context;
        this.list = list;
        uname = st;
        sharedPref = new SharedPref(context);
        username = sharedPref.getStr("User_Name");
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.left_message, parent, false);
            return new Myholder(view);
            case 1: View v2 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_message, parent, false);
            return new Myholder(v2);
            default: View v3 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_message, parent, false);
            return new Myholder(v3);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, final int position) {
        if(username.equals(list.get(position).getMessageUser())) {
            holder.user.setGravity(Gravity.RIGHT);
            holder.time.setGravity(Gravity.RIGHT);
            holder.text.setGravity(Gravity.RIGHT);
        }
        holder.user.setText(list.get(position).getMessageUser());
        holder.text.setText(list.get(position).getMessageText());
        holder.time.setText(getTimeDate(list.get(position).getMessageTime()));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(username.equals(uname))
            return 1;
        else
            return 0;
    }
    class Myholder extends RecyclerView.ViewHolder {
        TextView time, user, text;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.username1);
            text = itemView.findViewById(R.id.message1);
            time = itemView.findViewById(R.id.time2);
        }
    }

    public static String getTimeDate(long timestamp) {
        try {
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch (Exception e) {
            return "date";
        }
    }

 */


    Context context;
    String username;
    List<message> list;
    SharedPref sharedPref;
    String uname;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPR_RIGHT = 1;
    public AdapterChat(Context context, List<message> list, String st) {
        this.context = context;
        this.list = list;
        uname = st;
        sharedPref = new SharedPref(context);
        username = sharedPref.getStr(Constants.SHARED_PREF_USER_NAME);
        Log.d("Debugg uname : ",uname);
        Log.d("Debugg Username : ",username);
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("Debugg viewType : ",String.valueOf(viewType));
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.left_message, parent, false);
            return new Myholder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_message, parent, false);
            return new Myholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, final int position) {
        if(username.equals(list.get(position).getMessageUser())) {
            holder.user.setGravity(Gravity.RIGHT);
            holder.time.setGravity(Gravity.RIGHT);
            holder.text.setGravity(Gravity.RIGHT);

        }
        holder.user.setText(list.get(position).getMessageUser());
        holder.text.setText(list.get(position).getMessageText());
        holder.time.setText(getTimeDate(list.get(position).getMessageTime()));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView time, user, text;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.username1);
            text = itemView.findViewById(R.id.message1);
            time = itemView.findViewById(R.id.time2);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if ((list.get(position).getMessageUser()).equals(username)) {
            return MSG_TYPR_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
    public static String getTimeDate(long timestamp) {
        try {
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch (Exception e) {
            return "date";
        }
    }

}