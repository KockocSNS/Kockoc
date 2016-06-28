package com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Neighbor;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class NeighborRecyclerAdapter extends RecyclerView.Adapter<NeighborRecyclerViewHolder> {
    private ArrayList<Neighbor> neighbors;
    private Context mContext;
    private BitmapPool bitmapPool;

    public NeighborRecyclerAdapter(ArrayList<Neighbor> data, Context context){
        this.mContext = context;
        this.neighbors = data;
    }

    @Override
    public NeighborRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.neighbor_item, parent, false);
        return new NeighborRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NeighborRecyclerViewHolder holder, int position) {
        Neighbor data = neighbors.get(position);

        holder.getNeighbor_nickname().setText(data.getNickname());
//        Glide.with(mContext).load(BasicValue.getInstance().getUrlHead()+"board_image/"+ (data.getUserNo() + "/profile.jpg"))
//                .error(R.drawable.detail_comment_empty).into(holder.getNeighbor_img());

        Glide.with(mContext).load(BasicValue.getInstance().getUrlHead()+"board_image/"+ (data.getUserNo() + "/profile.jpg"))
                .error(R.drawable.detail_comment_empty).override(200,200).bitmapTransform(new CropCircleTransformation(Glide.get(mContext).getBitmapPool())).into(holder.getNeighbor_img());
    }

    @Override
    public int getItemCount() {
        return neighbors.size();
    }
}
