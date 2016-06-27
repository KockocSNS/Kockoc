package com.kocapplication.pixeleye.kockocapp.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import java.util.ArrayList;

/**
 * Created by pixeleye03 on 2016-06-23.
 */
public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerViewHolder>{
    final static String TAG = "DetailRecyclerAdapter";
    private ArrayList<DetailPageData.Comment> commentArr;
    private Context mContext;

    // TODO: 2016-06-23 댓글 목록 데이터 받는지 확인하고 뿌려주기
    public DetailRecyclerAdapter(ArrayList<DetailPageData.Comment> data,Context mContext) {
        this.mContext = mContext;
        this.commentArr = data;
    }

    @Override
    public DetailRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_comment_item, parent, false);
        return new DetailRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailRecyclerViewHolder holder, int position) {
        DetailPageData.Comment data = commentArr.get(position);

        holder.getComment_text().setText(data.getComment_text());
        holder.getComment_date().setText(data.getComment_date());
        holder.getComment_nickname().setText(data.getComment_userName());
        Glide.with(mContext).load(BasicValue.getInstance().getUrlHead()+"board_image/" + (data.getComment_userNo() + "/profile.jpg"))
                .error(R.drawable.detail_comment_empty).into(holder.getComment_img());

    }

    @Override
    public int getItemCount() {
        return commentArr.size();
    }

//    public ArrayList<DetailPageData.Comment> getItems() {return commentArr;}
//
//    public void setItems(ArrayList<DetailPageData.Comment> commentArr) {this.commentArr = commentArr;}
}
