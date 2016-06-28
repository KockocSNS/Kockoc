package com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class NeighborRecyclerViewHolder extends RecyclerView.ViewHolder {
    private ImageView neighbor_img;
    private TextView neighbor_nickname;

    public NeighborRecyclerViewHolder(View itemView) {
        super(itemView);
        neighbor_img = (ImageView) itemView.findViewById(R.id.neighbor_img);
        neighbor_nickname = (TextView) itemView.findViewById(R.id.neighbor_nickname);
    }

    public ImageView getNeighbor_img() {
        return neighbor_img;
    }
    public void setNeighbor_img(ImageView neighbor_img) {
        this.neighbor_img = neighbor_img;
    }

    public TextView getNeighbor_nickname() {
        return neighbor_nickname;
    }
    public void setNeighbor_nickname(TextView neighbor_nickname) {
        this.neighbor_nickname = neighbor_nickname;
    }
}
