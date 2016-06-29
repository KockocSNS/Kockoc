package com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Neighbor;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

import java.util.ArrayList;

public class NeighborActivity extends BaseActivityWithoutNav {
    RecyclerView followingRecyclerView;
    RecyclerView followerRecyclerView;

    NeighborRecyclerAdapter adapter;
    NeighborRecyclerAdapter adapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        actionBarTitleSet("이웃", Color.WHITE);

        container.setLayoutResource(R.layout.activity_neighbor);
        View containView = container.inflate();

        ArrayList<Neighbor> following = JsonParser.getFollowInfo(JspConn.getFollowInfo(BasicValue.getInstance().getUserNo()));
        ArrayList<Neighbor> follower = JsonParser.getFollowInfo(JspConn.getFollowerInfo(BasicValue.getInstance().getUserNo()));

        View followingView = containView.findViewById(R.id.recycler_layout_following);
        followingRecyclerView = (RecyclerView) followingView.findViewById(R.id.recycler_view);

        View followerView = containView.findViewById(R.id.recycler_layout_follower);
        followerRecyclerView = (RecyclerView) followerView.findViewById(R.id.recycler_view) ;

        adapter = new NeighborRecyclerAdapter(following, NeighborActivity.this);
        adapter2 = new NeighborRecyclerAdapter(follower, NeighborActivity.this);

        followingRecyclerView.setAdapter(adapter);
        followerRecyclerView.setAdapter(adapter2);
        LinearLayoutManager manager_follow = new LinearLayoutManager(NeighborActivity.this , LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager manager_follower = new LinearLayoutManager(NeighborActivity.this , LinearLayoutManager.VERTICAL,false);
        followingRecyclerView.setLayoutManager(manager_follow);
        followingRecyclerView.setHasFixedSize(true);
        followerRecyclerView.setLayoutManager(manager_follower);
        followerRecyclerView.setHasFixedSize(true);

    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}