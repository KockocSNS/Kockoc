package com.kocapplication.pixeleye.kockocapp.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;

import java.util.ArrayList;

/**
 * Created by hp on 2016-06-23.
 */
public class CommentRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerViewHolder>{
    final static String TAG = CommentRecyclerAdapter.class.getSimpleName();
    private ArrayList<DetailPageData.Comment> commentArr;
    private Context mContext;
    private View.OnClickListener listener;

    public CommentRecyclerAdapter(ArrayList<DetailPageData.Comment> data, Context mContext, View.OnClickListener listener) {
        this.mContext = mContext;
        this.commentArr = data;
        this.listener = listener;
    }

    @Override
    public DetailRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_comment_item, parent, false);
        itemView.setOnClickListener(listener);
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



    public void removeItem(int position){
        commentArr.remove(position);
        notifyItemRemoved(position);
    }

}
