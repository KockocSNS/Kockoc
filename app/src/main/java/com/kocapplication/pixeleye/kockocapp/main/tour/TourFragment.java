package com.kocapplication.pixeleye.kockocapp.main.tour;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.TourData;
import com.kocapplication.pixeleye.kockocapp.model.TourDataList;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016-09-27.
 *
 * 현재 TourAPI에서 서울의 유명한곳을 기본으로 띄워두게 해놨음
 * 향후 FlotingActionButton으로 필터를 구현해서 검색한 결과값을 띄워야함
 * 하단 RefreshListener 구현 필요
 * 새로고침 구현 필요
 * 상세보기 페이지 구현 필요
 */
public class TourFragment extends Fragment {
    final static String TAG = "TourFragment";
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TourDataList items;
    private TourRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour, container, false);

        init(view);
        getTourData();
        return view;
    }

    private void getTourData() {
        Handler handler = new TourDataReceiveHandler();
        Thread thread = new AreaThread(getActivity(),"12","1","A02",handler);
        thread.start();
    }

    private void init(View view){
        items = new TourDataList(new ArrayList<TourData>());
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tour_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.tour_recycler_view);
        adapter = new TourRecyclerAdapter(items.getTourDataList(),new TourClickListener(), getActivity());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnScrollListener(new BottomRefreshListener());
    }

    private class TourDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Snackbar.make(refreshLayout, "데이터를 불러오는데 실패하였습니다. 새로고침을 해주세요", Snackbar.LENGTH_SHORT).show();
                return;
            }
            items = (TourDataList) msg.getData().getSerializable("THREAD");

            adapter.setItems(items.getTourDataList());
            adapter.notifyDataSetChanged();
        }
    }

    private class TourClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.e(TAG,"클릭");
        }
    }
    private class BottomRefreshListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
// TODO: 2016-09-29 구현 필요함
//            int LastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//
//            if ((LastVisibleItem) == adapter.getItems().size() - 1 && !refreshLayout.isRefreshing() && adapter.getItems().size() > 6) {
//                refreshLayout.setRefreshing(true);
//                Handler handler = new BottomRefreshHandler();
//                Thread thread = new StoryThread(handler, initialData.get(initialData.size() - 1).getBasicAttributes().getBoardNo());
//                thread.start();
//            }
        }
    }
}
