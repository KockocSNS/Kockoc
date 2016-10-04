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
    private ArrayList<TourData> tourDataList;
    private TourDataList items;
    private TourRecyclerAdapter adapter;
    private String content = "12";
    private String area = "1";
    private String category = "A02";
    private String pageNo = "1";

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
        Thread thread = new AreaThread(getActivity(), content, area, category, pageNo, handler); // 기본으로 관광지, 서울, 인문 카테고리, 페이지1 선택
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

    /**
     * tour 데이터를 받아오는 핸들러
     */
    private class TourDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Snackbar.make(refreshLayout, "데이터를 불러오는데 실패하였습니다. 새로고침을 해주세요", Snackbar.LENGTH_SHORT).show();
                return;
            }
            items = (TourDataList) msg.getData().getSerializable("THREAD");
            tourDataList = items.getTourDataList();
            adapter.setItems(tourDataList);
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * 하단 새로고침 데이터를 받아오는 핸들러, pageNo를 1씩 늘린다.
     */
    private class BottomRefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Snackbar.make(refreshLayout, "데이터를 불러오는데 실패하였습니다. 새로고침을 해주세요", Snackbar.LENGTH_SHORT).show();
                return;
            }
            refreshLayout.setRefreshing(false);
            TourDataList temp = (TourDataList) msg.getData().getSerializable("THREAD");
            tourDataList.addAll(temp.getTourDataList());
            adapter.setItems(tourDataList);
            adapter.notifyDataSetChanged();
        }
    }

    private class TourClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.e(TAG,"클릭");
        }
    }

    /**
     * RecyclerView의 맨 밑에 닿으면 pageNo를 1씩 늘려 데이터를 받는다.
     */
    private class BottomRefreshListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int LastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

            if ((LastVisibleItem) == adapter.getItems().size() - 1 && !refreshLayout.isRefreshing() && adapter.getItems().size() > 6) {
                refreshLayout.setRefreshing(true);
                Handler bottomHandler = new BottomRefreshHandler();
                pageNo = String.valueOf(Integer.parseInt(pageNo)+1);
                Thread thread = new AreaThread(getActivity(), content, area, category, pageNo, bottomHandler);
                thread.start();
            }
        }
    }
}
