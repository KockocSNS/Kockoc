package com.kocapplication.pixeleye.kockocapp.neighbor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;
import com.kocapplication.pixeleye.kockocapp.model.Neighbor;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

import java.util.ArrayList;
import java.util.List;

public class NeighborActivity extends BaseActivity {
    Neighbor neighbor,follower;
    RecyclerView recyclerView;

    NeighborRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbor);

        ArrayList<Neighbor> neighbors = JsonParser.getNeighborInfo(JspConn.getNeighborInfo(BasicValue.getInstance().getUserNo()));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new NeighborRecyclerAdapter(neighbors, NeighborActivity.this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(NeighborActivity.this ,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}