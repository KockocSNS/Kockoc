package com.kocapplication.pixeleye.kockocapp.main.story;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;

import java.util.List;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerViewHolder> {
    public static String TAG = BoardRecyclerAdapter.class.getSimpleName();
    private List<BoardWithImage> items;
    private View.OnClickListener listener;
    private Context mContext;
    private String flag = "";
    private View itemView;

    public BoardRecyclerAdapter(List<BoardWithImage> data, View.OnClickListener listener, Context mContext) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
        this.listener = listener;
        this.mContext = mContext;
    }
    public BoardRecyclerAdapter(List<BoardWithImage> data, View.OnClickListener listener, Context mContext,String flag) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
        this.listener = listener;
        this.mContext = mContext;
        this.flag = flag;
    }

    @Override
    public BoardRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(flag.equals("main"))itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main_board, parent, false);
        else itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_board, parent, false);
        itemView.setOnClickListener(listener);
        return new BoardRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BoardRecyclerViewHolder holder, int position) {
        BoardWithImage data = items.get(position);

        int courseCount = data.getBasicAttributes().getCourseCount();

        holder.getLike().setText(String.valueOf(data.getExpressionCount().getExpressionCount()));
        holder.getScrap().setText(String.valueOf(data.getExpressionCount().getScrapCount()));
        holder.getComment().setText(String.valueOf(data.getExpressionCount().getCommentCount()));

        if (data.getBoardImage() != null){
            Glide.with(mContext).load("").placeholder(data.getBoardImage()).into(holder.getBoardImage());
//            holder.getBoardImage().setImageDrawable(data.getBoardImage());
        } else
            Glide.with(mContext).load("").placeholder(mContext.getResources().getDrawable(R.drawable.board_background)).into(holder.getBoardImage());
//            holder.getBoardImage().setImageDrawable(mContext.getResources().getDrawable(R.drawable.board_background));

//        try {
//        } catch (Exception e) {
//            holder.getBoardImage().setImageDrawable(mContext.getResources().getDrawable(R.drawable.advice_1));
//        }
        holder.getDate().setText(data.getDate());
        holder.getTitle().setText(data.getText());
        holder.getTag().setText(data.getHashTagByString());

        if (courseCount == 0) {
            holder.getCourseCount().setVisibility(View.GONE);
            holder.getCourseImage().setVisibility(View.GONE);
        }
        else {
            holder.getCourseCount().setVisibility(View.VISIBLE);
            holder.getCourseImage().setVisibility(View.VISIBLE);
            holder.getCourseCount().setText(String.valueOf(courseCount));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<BoardWithImage> getItems() {
        return items;
    }

    public void setItems(List<BoardWithImage> items) {
        this.items = items;
    }

    public void deleteItems(int position){
        if(position > -1) {
            this.items.remove(position);
            notifyDataSetChanged();
        }
    }
}
