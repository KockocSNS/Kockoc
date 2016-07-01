package com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Neighbor;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

import java.util.ArrayList;

public class NeighborActivity extends BaseActivityWithoutNav {
    RecyclerView followingRecyclerView;
    RecyclerView followerRecyclerView;

    private int userNo;

    NeighborRecyclerAdapter follow_adapter;
    NeighborRecyclerAdapter follower_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        Intent intent = getIntent();
        userNo = intent.getIntExtra("userNo",0);

        actionBarTitleSet("이웃", Color.WHITE);

        container.setLayoutResource(R.layout.activity_neighbor);
        View containView = container.inflate();

        ArrayList<Neighbor> following = JsonParser.getFollowInfo(JspConn.getFollowInfo(userNo));
        ArrayList<Neighbor> follower = JsonParser.getFollowInfo(JspConn.getFollowerInfo(userNo));

        View followingView = containView.findViewById(R.id.recycler_layout_following);
        followingRecyclerView = (RecyclerView) followingView.findViewById(R.id.recycler_view);

        View followerView = containView.findViewById(R.id.recycler_layout_follower);
        followerRecyclerView = (RecyclerView) followerView.findViewById(R.id.recycler_view) ;

        follow_adapter = new NeighborRecyclerAdapter(following, NeighborActivity.this);
        followingRecyclerView.setAdapter(follow_adapter);
        LinearLayoutManager manager_follow = new LinearLayoutManager(NeighborActivity.this , LinearLayoutManager.VERTICAL,false);
        followingRecyclerView.setLayoutManager(manager_follow);
        followingRecyclerView.setHasFixedSize(true);


        follower_adapter = new NeighborRecyclerAdapter(follower, NeighborActivity.this);
        followerRecyclerView.setAdapter(follower_adapter);
        LinearLayoutManager manager_follower = new LinearLayoutManager(NeighborActivity.this , LinearLayoutManager.VERTICAL,false);
        followerRecyclerView.setLayoutManager(manager_follower);
        followerRecyclerView.setHasFixedSize(true);
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}