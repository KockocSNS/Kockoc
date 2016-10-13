package com.kocapplication.pixeleye.kockocapp.main.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.main.tour.TourDetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.tour.TourRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.model.TourData;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeongpil on 2016-09-29.
 */
public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener{
    final static String TAG = "MainFragment";
    private ViewPager viewPager;
    private LinearLayout indicator;
    private RecyclerView rv_tour;
    private RecyclerView rv_course;
    private RecyclerView rv_story;
    private TourRecyclerAdapter adapter_tour;
    private BoardRecyclerAdapter adapter_story;
    private CourseRecyclerAdapter adapter_course;
    private ViewPagerAdapter adapter_viewpager;

    private int dotsCount;
    private ImageView[] dots;
    private ArrayList<TourData> tourDataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        init(view);
        setViewPager();

        return view;
    }

    private void init(View view){
        viewPager = (ViewPager) view.findViewById(R.id.main_frag_viewpager);
        indicator = (LinearLayout) view.findViewById(R.id.iv_main_frag_indicator);
        rv_tour = (RecyclerView) view.findViewById(R.id.rv_main_frag_tour);
        rv_story = (RecyclerView) view.findViewById(R.id.rv_main_frag_story);
        rv_course = (RecyclerView) view.findViewById(R.id.rv_main_frag_course);
    }

    private void setViewPager(){
        adapter_viewpager = new ViewPagerAdapter(getActivity(), 3); // 아이템 갯수
        viewPager.setAdapter(adapter_viewpager);
        viewPager.addOnPageChangeListener(this);
        setUiPageViewController();
    }

    public void setTourData(ArrayList<TourData> tourDataList){
        this.tourDataList = tourDataList;
        adapter_tour = new TourRecyclerAdapter(tourDataList, new TourClickListener(), getActivity());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        rv_tour.setAdapter(adapter_tour);
        rv_tour.setLayoutManager(manager);
        rv_tour.setHasFixedSize(true);
    }

    public void setCourseData(List<Courses> courses){
        adapter_course = new CourseRecyclerAdapter(courses,new CourseClickListener());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        rv_course.setAdapter(adapter_course);
        rv_course.setLayoutManager(manager);
        rv_course.setHasFixedSize(true);
    }

    public void setStoryData(ArrayList<BoardWithImage> storyDataList){
        adapter_story = new BoardRecyclerAdapter(storyDataList, new StoryClickListener(), getActivity());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
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

    // TODO: 2016-10-13 모든 코스 불러오게 하고 코스 상세보기 만들어야함
    private class CourseClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = rv_course.getChildAdapterPosition(v);

            Intent intent = new Intent(getActivity(), CourseWriteActivity.class);
            intent.putExtra("FLAG", CourseWriteActivity.ADJUST_FLAG);
            intent.putExtra("COURSES", adapter_course.getItems().get(position));
            getActivity().startActivityForResult(intent, MainActivity.COURSE_WRITE_ACTIVITY_REQUEST_CODE);
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
    private void setUiPageViewController() {
        dotsCount = adapter_viewpager.getRealCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageResource(R.drawable.pager_nonselected_item_dot);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 10, 8, 10);

            indicator.addView(dots[i], params);
        }

        dots[0].setImageResource(R.drawable.pager_selected_item_dot);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageResource(R.drawable.pager_nonselected_item_dot);
        }
        dots[position % dotsCount].setImageResource(R.drawable.pager_selected_item_dot);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
