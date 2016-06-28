package com.kocapplication.pixeleye.kockocapp.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by hp on 2016-06-23.
 */
public class DetailRecyclerViewHolder extends RecyclerView.ViewHolder {
    private ImageView comment_img;
    private TextView comment_nickname;
    private TextView comment_text;
    private TextView comment_date;

    public DetailRecyclerViewHolder(View itemView) {
        super(itemView);
        comment_img = (ImageView)itemView.findViewById(R.id.img_detail_comment_profile);
        comment_nickname = (TextView)itemView.findViewById(R.id.tv_detail_comment_nickname);
        comment_text = (TextView)itemView.findViewById(R.id.tv_detail_comment_text);
        comment_date = (TextView)itemView.findViewById(R.id.tv_detail_comment_date);
    }

    public ImageView getComment_img() {return comment_img;}
    public void setComment_img(ImageView comment_img) {this.comment_img = comment_img;}

    public TextView getComment_nickname() {return comment_nickname;}
    public void setComment_nickname(TextView comment_nickname) {this.comment_nickname = comment_nickname;}

    public TextView getComment_text() {return comment_text;}
    public void setComment_text(TextView comment_text) {this.comment_text = comment_text;}

    public TextView getComment_date() {return comment_date;}
    public void setComment_date(TextView comment_date) {this.comment_date = comment_date;}
}
