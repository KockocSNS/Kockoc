package com.kocapplication.pixeleye.kockocapp.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;

import java.util.ArrayList;

/**
 * Created by pixeleye03 on 2016-06-23.
 */
public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerViewHolder>{
    private ArrayList<DetailPageData.Comment> data;

    // TODO: 2016-06-23 댓글 목록 데이터 받는지 확인하고 뿌려주기
    public DetailRecyclerAdapter(ArrayList<DetailPageData.Comment> data) {
        this.data = data;
    }

    @Override
    public DetailRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_content, parent, false);
        return new DetailRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailRecyclerViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
