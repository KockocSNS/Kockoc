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
import com.kocapplication.pixeleye.kockocapp.user.UserActivity;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

import java.util.ArrayList;

public class NeighborActivity extends BaseActivityWithoutNav {
    public static final int USER_FOLLOW_REQUEST_CODE = 12345;

    RecyclerView followingRecyclerView;
    RecyclerView followerRecyclerView;

    private int userNo;
    View containView;

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
        containView = container.inflate();

        getComponent();

        getFollow();
        getFollower();
    }
    private void getComponent(){
        View followingView = containView.findViewById(R.id.recycler_layout_following);
        followingRecyclerView = (RecyclerView) followingView.findViewById(R.id.recycler_view);
        View followerView = containView.findViewById(R.id.recycler_layout_follower);
        followerRecyclerView = (RecyclerView) followerView.findViewById(R.id.recycler_view) ;
    }

    private void getFollow(){
        ArrayList<Neighbor> following = JsonParser.getFollowInfo(JspConn.getFollowInfo(userNo));

        follow_adapter = new NeighborRecyclerAdapter(following, NeighborActivity.this, new FollowClickListener());
        followingRecyclerView.setAdapter(follow_adapter);

        LinearLayoutManager manager_follow = new LinearLayoutManager(NeighborActivity.this , LinearLayoutManager.VERTICAL,false);
        followingRecyclerView.setLayoutManager(manager_follow);
        followingRecyclerView.setHasFixedSize(true);
    }

    private void getFollower(){
        ArrayList<Neighbor> follower = JsonParser.getFollowInfo(JspConn.getFollowerInfo(userNo));

        follower_adapter = new NeighborRecyclerAdapter(follower, NeighborActivity.this, new FollowerClickListener());
        followerRecyclerView.setAdapter(follower_adapter);

        LinearLayoutManager manager_follower = new LinearLayoutManager(NeighborActivity.this , LinearLayoutManager.VERTICAL,false);
        followerRecyclerView.setLayoutManager(manager_follower);
        followerRecyclerView.setHasFixedSize(true);
    }
    private class FollowClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = followingRecyclerView.getChildLayoutPosition(v);
            int follow = follow_adapter.getNeighbors().get(position).getUserNo();

            Intent intent = new Intent(NeighborActivity.this, UserActivity.class);
            intent.putExtra("userNo",follow);
            startActivityForResult(intent, USER_FOLLOW_REQUEST_CODE);
        }
    }
    private class FollowerClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = followerRecyclerView.getChildLayoutPosition(v);
            int follow = follower_adapter.getNeighbors().get(position).getUserNo();

            Intent intent = new Intent(NeighborActivity.this, UserActivity.class);
            intent.putExtra("userNo",follow);
            startActivityForResult(intent, USER_FOLLOW_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == USER_FOLLOW_REQUEST_CODE){   //유저정보를 본 뒤 리프레쉬
            getComponent();
            getFollow();
            getFollower();
        }
    }
}