package com.kocapplication.pixeleye.kockocapp.navigation.notice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.NoticeItem;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by pixeleye02 on 2016-07-05.
 */
public class NoticeRecyclerAdapter extends RecyclerView.Adapter<NoticeRecyclerViewHolder> {
    private ArrayList<NoticeItem> noticeItems;
    private Context mContext;
    View.OnClickListener listener;

    public NoticeRecyclerAdapter(ArrayList<NoticeItem> data, Context context, View.OnClickListener listener){
        this.noticeItems = data;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public NoticeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_notice, parent, false);
        itemView.setOnClickListener(listener);
        return new NoticeRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoticeRecyclerViewHolder holder, int position) {
        NoticeItem data = noticeItems.get(position);

        holder.getNotice_name().setText(data.getNickName());

        Glide.with(mContext).load(BasicValue.getInstance().getUrlHead()+"board_image/"+ (data.getUserNo() + "/profile.jpg"))
                .error(R.drawable.default_profile).override(100,100).bitmapTransform(new CropCircleTransformation(Glide.get(mContext).getBitmapPool())).into(holder.getNotice_img());
    }

    @Override
    public int getItemCount() {
        return noticeItems.size();
    }

    public ArrayList<NoticeItem> getNotices() {return noticeItems;}
}
