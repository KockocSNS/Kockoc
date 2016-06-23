package com.kocapplication.pixeleye.kockocapp.main.story;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Board;

import java.util.List;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerViewHolder> {
    private List<Board> items;
    private View.OnClickListener listener;

    public BoardRecyclerAdapter(List<Board> data, View.OnClickListener listener) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
        this.listener = listener;
    }

    @Override
    public BoardRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_board, parent, false);
        itemView.setOnClickListener(listener);
        return new BoardRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BoardRecyclerViewHolder holder, int position) {
        Board data = items.get(position);

        holder.getDate().setText(data.getDate());
        holder.getTitle().setText(data.getText());
        holder.getTag().setText(data.getHashTagByString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Board> getItems() {
        return items;
    }

    public void setItems(List<Board> items) {
        this.items = items;
    }
}
