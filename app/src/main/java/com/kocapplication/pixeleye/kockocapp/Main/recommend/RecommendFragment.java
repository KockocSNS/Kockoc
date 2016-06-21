package com.kocapplication.pixeleye.kockocapp.main.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.Board;

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
        View view = inflater.inflate(R.layout.recyclerview_with_swipe_refresh, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new RefreshListener());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new BoardRecyclerAdapter(new ArrayList<Board>());

        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            Toast.makeText(getActivity(), "Recommend", Toast.LENGTH_SHORT).show();
        }
    }
}
