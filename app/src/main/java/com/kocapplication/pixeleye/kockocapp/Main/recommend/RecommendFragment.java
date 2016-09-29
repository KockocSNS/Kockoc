package com.kocapplication.pixeleye.kockocapp.main.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;

import java.util.ArrayList;

/**
 * Created by Han_ on 2016-06-21.
 */
public class RecommendFragment extends Fragment {
    private final String TAG = "RECOMMEND_FRAGMENT";

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.story_recyclerview, container, false);

        init(view);

        Handler handler = new Handler(new RecommendDataReceiveHandleCallback());
        Thread thread = new RecommendThread(handler);
        thread.start();

        return view;
    }

    private void init(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new RefreshListener());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new ItemClickListener(),getActivity());

        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            BoardWithImage boardWithImage = adapter.getItems().get(position);

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("boardNo", boardWithImage.getBasicAttributes().getBoardNo());
            intent.putExtra("courseNo", boardWithImage.getBasicAttributes().getCourseNo());
            intent.putExtra("board_userNo",boardWithImage.getBasicAttributes().getUserNo());
            startActivity(intent);
        }
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            Handler handler = new Handler(new RecommendDataReceiveHandleCallback());
            Thread thread = new RecommendThread(handler);
            thread.start();
        }
    }

    private class RecommendDataReceiveHandleCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                Snackbar.make(refreshLayout, "데이터를 불러오는데 실패하였습니다. 새로고침을 해주세요", Snackbar.LENGTH_SHORT).show();
                return false;
            }

            ArrayList<BoardWithImage> boardWithImages = (ArrayList<BoardWithImage>) msg.getData().getSerializable("THREAD");

            adapter.setItems(boardWithImages);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);

            return true;
        }
    }
}
