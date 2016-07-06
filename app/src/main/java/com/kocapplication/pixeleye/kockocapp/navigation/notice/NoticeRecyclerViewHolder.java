package com.kocapplication.pixeleye.kockocapp.navigation.notice;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by pixeleye02 on 2016-07-05.
 */
public class NoticeRecyclerViewHolder extends RecyclerView.ViewHolder{
    private ImageView notice_img;
    private TextView notice_name;

    public NoticeRecyclerViewHolder(View itemView) {
        super(itemView);
        notice_img = (ImageView) itemView.findViewById(R.id.notice_img);
        notice_name = (TextView) itemView.findViewById(R.id.notice_name);
    }

    public void setNotice_name(TextView notice_name) {
        this.notice_name = notice_name;
    }
    public TextView getNotice_name() {
        return notice_name;
    }

    public ImageView getNotice_img() {
        return notice_img;
    }
    public void setNotice_img(ImageView notice_img) {
        this.notice_img = notice_img;
    }
}
