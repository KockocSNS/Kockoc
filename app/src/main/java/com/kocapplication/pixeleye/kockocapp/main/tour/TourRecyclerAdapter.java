package com.kocapplication.pixeleye.kockocapp.main.tour;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;

import com.kocapplication.pixeleye.kockocapp.model.TourData;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016-06-21.
 */
public class TourRecyclerAdapter extends RecyclerView.Adapter<TourRecyclerViewHolder> {
    private ArrayList<TourData> items;
    private View.OnClickListener listener;
    private Context mContext;
    private String flag = "";
    private View itemView;

    public TourRecyclerAdapter(ArrayList<TourData> data, View.OnClickListener listener, Context mContext) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
        this.listener = listener;
        this.mContext = mContext;
    }
    public TourRecyclerAdapter(ArrayList<TourData> data, View.OnClickListener listener, Context mContext,String flag) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
        this.listener = listener;
        this.mContext = mContext;
        this.flag = flag;
    }

    @Override
    public TourRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(flag.equals("main")) itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main_tour, parent, false);
        else itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_tour, parent, false);
        itemView.setOnClickListener(listener);
        return new TourRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TourRecyclerViewHolder holder, int position) {
        TourData data = items.get(position);

        holder.getTourTitle().setText(data.getTitle());
        Glide.with(mContext).load(data.getImg()).into(holder.getTourImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<TourData> getItems() {
        return items;
    }

    public void setItems(ArrayList<TourData> items) {
        this.items = items;
    }
}
