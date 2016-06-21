package com.kocapplication.pixeleye.kockocapp.main.story;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.model.Board;

import java.util.List;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerViewHolder> {
    private List<Board> items;

    public BoardRecyclerAdapter(List<Board> data) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
    }

    @Override
    public BoardRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BoardRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
