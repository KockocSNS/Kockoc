package com.kocapplication.pixeleye.kockocapp.main.search;

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

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;

import java.util.ArrayList;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class SearchBoardActivity extends BaseActivityWithoutNav {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;
    private ArrayList<BoardWithImage> initialData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        actionBarTitleSet("검색 결과", Color.WHITE);

        container.setLayoutResource(R.layout.activity_board);
        View containView = container.inflate();

        recyclerView = (RecyclerView) containView.findViewById(R.id.recycler_view);
        refreshLayout = (SwipeRefreshLayout) containView.findViewById(R.id.refresh_layout);

        adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new ItemClickListener(),SearchBoardActivity.this);

        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(SearchBoardActivity.this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        if (initialData == null) {
            Handler handler = new SearchDataReceiveHandler();
            Thread thread = new SearchThread(handler);
            refreshLayout.setRefreshing(true);
            thread.start();
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            BoardWithImage boardWithImage = adapter.getItems().get(position);

            Intent intent = new Intent(SearchBoardActivity.this, DetailActivity.class);
            intent.putExtra("boardNo", boardWithImage.getBasicAttributes().getBoardNo());
            intent.putExtra("courseNo", boardWithImage.getBasicAttributes().getCourseNo());
            intent.putExtra("board_userNo",boardWithImage.getBasicAttributes().getUserNo());
            startActivity(intent);

        }
    }

    private class SearchDataReceiveHandler extends Handler {
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
}
