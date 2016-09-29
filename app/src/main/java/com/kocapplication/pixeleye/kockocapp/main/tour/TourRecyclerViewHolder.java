package com.kocapplication.pixeleye.kockocapp.main.tour;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Hyeongpil on 2016-06-21.
 */
public class TourRecyclerViewHolder extends RecyclerView.ViewHolder {
    private ImageView tourImage;
    private TextView tourTitle;

    public TourRecyclerViewHolder(View itemView) {
        super(itemView);
        tourImage = (ImageView) itemView.findViewById(R.id.tour_iv_image);
        tourTitle = (TextView) itemView.findViewById(R.id.tour_tv_title);
    }

    public ImageView getTourImage() {
        return tourImage;
    }
    public TextView getTourTitle() {return tourTitle;}
}
