package com.example.sharetalks.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharetalks.Bean.UserDetails;
import com.example.sharetalks.R;
import com.example.sharetalks.SharedPref;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AdapterClan extends RecyclerView.Adapter<AdapterClan.Myholder> {
    Context context;
    List<UserDetails> list;

    public AdapterClan(Context context, List<UserDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterClan.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clan_layout, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClan.Myholder holder, final int position) {
        holder.user.setText(list.get(position).getUsername());
        holder.text.setText(list.get(position).getLocation());
        holder.time.setText(list.get(position).getRank());
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
