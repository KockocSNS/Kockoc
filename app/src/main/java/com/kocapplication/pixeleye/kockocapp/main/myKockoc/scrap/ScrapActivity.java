package com.kocapplication.pixeleye.kockocapp.main.myKockoc.scrap;

import android.content.Intent;
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
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.main.story.StoryThread;
import com.kocapplication.pixeleye.kockocapp.model.Board;

import java.util.ArrayList;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class ScrapActivity extends BaseActivity {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;
    private ArrayList<Board> initialData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        adapter = new BoardRecyclerAdapter(new ArrayList<Board>(), new ItemClickListener());

        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(ScrapActivity.this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        if (initialData == null) {
            Handler handler = new StoryDataReceiveHandler();
            Thread thread = new StoryThread(handler);
            refreshLayout.setRefreshing(true);
            thread.start();
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            Board board = adapter.getItems().get(position);

            Intent intent = new Intent(ScrapActivity.this, DetailActivity.class);
            intent.putExtra("boardNo", board.getBasicAttributes().getBoardNo());
            intent.putExtra("courseNo", board.getBasicAttributes().getCourseNo());
            startActivity(intent);

        }
    }

    private class StoryDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<Board> boards = (ArrayList<Board>) msg.getData().getSerializable("THREAD");

            initialData = boards;
            adapter.setItems(boards);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }
}
