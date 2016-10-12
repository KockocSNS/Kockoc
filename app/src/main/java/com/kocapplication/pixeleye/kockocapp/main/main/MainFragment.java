package com.kocapplication.pixeleye.kockocapp.main.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.main.tour.TourDetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.tour.TourRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;
import com.kocapplication.pixeleye.kockocapp.model.TourData;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016-09-29.
 */
public class MainFragment extends Fragment {
    final static String TAG = "MainFragment";
    private ViewPager viewPager;
    private ImageView indicator;
    private RecyclerView rv_tour;
    private RecyclerView rv_course;
    private RecyclerView rv_story;
    private TourRecyclerAdapter adapter_tour;
    private BoardRecyclerAdapter adapter_story;

    private ArrayList<TourData> tourDataList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        init(view);

        return view;
    }

    private void init(View view){
        viewPager = (ViewPager) view.findViewById(R.id.main_frag_viewpager);
        indicator = (ImageView) view.findViewById(R.id.iv_main_frag_indicator);
        rv_tour = (RecyclerView) view.findViewById(R.id.rv_main_frag_tour);
        rv_story = (RecyclerView) view.findViewById(R.id.rv_main_frag_story);
        rv_course = (RecyclerView) view.findViewById(R.id.rv_main_frag_course);
    }

    public void setTourData(ArrayList<TourData> tourDataList){
        this.tourDataList = tourDataList;
        adapter_tour = new TourRecyclerAdapter(tourDataList, new TourClickListener(), getActivity());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rv_tour.setAdapter(adapter_tour);
        rv_tour.setLayoutManager(manager);
        rv_tour.setHasFixedSize(true);
    }

    public void setStoryData(ArrayList<BoardWithImage> storyDataList){
        adapter_story = new BoardRecyclerAdapter(storyDataList, new StoryClickListener(), getActivity());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rv_story.setAdapter(adapter_story);
        rv_story.setLayoutManager(manager);
        rv_story.setHasFixedSize(true);
    }


    private class TourClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = rv_tour.getChildLayoutPosition(v);
            TourData data = tourDataList.get(position);
            Intent intent = new Intent(getActivity(),TourDetailActivity.class);
            intent.putExtra("data",data);
            startActivity(intent);
        }
    }

    private class StoryClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = rv_story.getChildLayoutPosition(v);

            BoardWithImage boardWithImage = adapter_story.getItems().get(position);

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("boardNo", boardWithImage.getBasicAttributes().getBoardNo());
            intent.putExtra("courseNo", boardWithImage.getBasicAttributes().getCourseNo());
            intent.putExtra("board_userNo", boardWithImage.getBasicAttributes().getUserNo());
            getActivity().startActivityForResult(intent, MainActivity.DETAIL_ACTIVITY_REQUEST_CODE);
        }
    }

}
