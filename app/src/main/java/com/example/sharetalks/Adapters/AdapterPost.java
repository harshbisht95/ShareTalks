package com.example.sharetalks.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharetalks.Bean.Post;
import com.example.sharetalks.Constants;
import com.example.sharetalks.R;
import com.example.sharetalks.SharedPref;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.Myholder> {
    Context context;
    String username;
    List<Post> list;
SharedPref sharedPref;
    public AdapterPost(Context context, List<Post> list) {
        this.context = context;
        this.list = list;
        sharedPref = new SharedPref(context);
        username = sharedPref.getStr(Constants.SHARED_PREF_USER_NAME);
    }
    @NonNull
    @Override
    public AdapterPost.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_layout, parent, false);
        return new AdapterPost.Myholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterPost.Myholder holder, final int position) {
        holder.Stock.setText(list.get(position).getStock());
        holder.BuyPrice.setText(list.get(position).getBuyPrice());
        holder.Target1.setText(list.get(position).getTarget1());
        holder.Target2.setText(list.get(position).getTarget2());
        holder.Likes.setText(list.get(position).getLikes());
        holder.TradeType.setText(list.get(position).getTradeType());
        holder.TargetDays.setText(list.get(position).getTargetDays());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    class Myholder extends RecyclerView.ViewHolder {
        TextView Stock, BuyPrice, Target1, Target2, Likes, TradeType, TargetDays;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            Stock = itemView.findViewById(R.id.StockName);
            BuyPrice = itemView.findViewById(R.id.Buy);
            Target1 = itemView.findViewById(R.id.Target1);
            Target2 = itemView.findViewById(R.id.Target2);
            Likes = itemView.findViewById(R.id.Likes);
            TradeType = itemView.findViewById(R.id.TradeType);
            TargetDays = itemView.findViewById(R.id.TradeTime);
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
