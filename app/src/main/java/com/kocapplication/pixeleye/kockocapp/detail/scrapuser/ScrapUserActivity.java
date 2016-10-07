package com.kocapplication.pixeleye.kockocapp.detail.scrapuser;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor.NeighborRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.Neighbor;
import com.kocapplication.pixeleye.kockocapp.user.UserActivity;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

import java.util.ArrayList;

/**
 * Created by pixeleye02 on 2016-07-06.
 */
public class ScrapUserActivity extends BaseActivityWithoutNav {
    private RecyclerView scrapRecyclerView;
    private View containView;
    private NeighborRecyclerAdapter scrapUserRecyclerAdapter;

    private int boardNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("관심글 등록 현황", Color.WHITE);

        Intent intent = getIntent();
        boardNo = intent.getIntExtra("boardNo",0);

        container.setLayoutResource(R.layout.activity_scrap_user);
        containView = container.inflate();

        View scrapUserView = containView.findViewById(R.id.recycler_layout_scrap_user);
        scrapRecyclerView = (RecyclerView) scrapUserView.findViewById(R.id.recycler_view);

        ArrayList<Neighbor> scrapUser = JsonParser.getFollowInfo(JspConn.getScrapUser(boardNo));
        scrapUserRecyclerAdapter = new NeighborRecyclerAdapter(scrapUser, ScrapUserActivity.this, new ItemClickListener());
        scrapRecyclerView.setAdapter(scrapUserRecyclerAdapter);
        LinearLayoutManager manager_follower = new LinearLayoutManager(ScrapUserActivity.this , LinearLayoutManager.VERTICAL,false);
        scrapRecyclerView.setLayoutManager(manager_follower);
        scrapRecyclerView.setHasFixedSize(true);
    }

    private class ItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = scrapRecyclerView.getChildLayoutPosition(v);
            int userNo = scrapUserRecyclerAdapter.getNeighbors().get(position).getUserNo();

            Intent intent = new Intent(ScrapUserActivity.this, UserActivity.class);
            intent.putExtra("userNo",userNo);
            startActivity(intent);
        }
    }
}
