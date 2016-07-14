package com.kocapplication.pixeleye.kockocapp.main.myKockoc.scrap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;

import java.util.ArrayList;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class ScrapActivity extends BaseActivityWithoutNav {
    private static final int SCRAP_REQUEST_CODE = 12433;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;
    private ArrayList<BoardWithImage> initialData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        actionBarTitleSet("관심글", Color.WHITE);

        container.setLayoutResource(R.layout.activity_board);
        View containView = container.inflate();

        recyclerView = (RecyclerView) containView.findViewById(R.id.recycler_view);
        refreshLayout = (SwipeRefreshLayout) containView.findViewById(R.id.refresh_layout);

        adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new ItemClickListener(),ScrapActivity.this);

        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(ScrapActivity.this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        if (initialData == null) {
            Handler handler = new ScrapDataReceiveHandler();
            Thread thread = new ScrapThread(handler);
            refreshLayout.setRefreshing(true);
            thread.start();
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            BoardWithImage boardWithImage = adapter.getItems().get(position);

            Intent intent = new Intent(ScrapActivity.this, DetailActivity.class);
            intent.putExtra("boardNo", boardWithImage.getBasicAttributes().getBoardNo());
            intent.putExtra("courseNo", boardWithImage.getBasicAttributes().getCourseNo());
            intent.putExtra("board_userNo",boardWithImage.getBasicAttributes().getUserNo());
            startActivityForResult(intent, SCRAP_REQUEST_CODE);

            Handler handler = new ScrapDataReceiveHandler();
            Thread thread = new ScrapThread(handler);
            refreshLayout.setRefreshing(true);
            thread.start();

        }
    }

    private class ScrapDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<BoardWithImage> boardWithImages = (ArrayList<BoardWithImage>) msg.getData().getSerializable("THREAD");

            initialData = boardWithImages;
            adapter.setItems(boardWithImages);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SCRAP_REQUEST_CODE:
                Handler handler = new ScrapDataReceiveHandler();
                Thread thread = new ScrapThread(handler);
                refreshLayout.setRefreshing(true);
                thread.start();
                break;
        }
    }
}
