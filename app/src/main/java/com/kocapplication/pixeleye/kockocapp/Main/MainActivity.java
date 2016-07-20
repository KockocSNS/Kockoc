package com.kocapplication.pixeleye.kockocapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseFragment;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.MyKocKocFragment;
import com.kocapplication.pixeleye.kockocapp.main.recommend.RecommendFragment;
import com.kocapplication.pixeleye.kockocapp.main.story.StoryFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private final String TAG = "MainActivity";
    public static final int DETAIL_ACTIVITY_REQUEST_CODE = 1222;
    public static final int COURSE_WRITE_ACTIVITY_REQUEST_CODE = 575;
    public static final int NEW_WRITE_REQUEST_CODE = 12433;
    public static final int CONTINUOUS_WRITE_REQUEST_CODE = 1234;
    private final int PROFILE_SET = 1;

    ViewPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        actionBarTitleSet();

        ImageView logo = (ImageView) findViewById(R.id.actionbar_image_title);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "AAA");
            }
        });
    }

    @Override
    protected void init() {
        super.init();

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new StoryFragment());
        fragments.add(new RecommendFragment());
        fragments.add(new CourseFragment());
        fragments.add(new MyKocKocFragment());

        List<String> titles = new ArrayList<String>();
        titles.add("소식");
        titles.add("추천");
        titles.add("코스");
        titles.add("내콕콕");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new ViewPageAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private class ViewPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> items;
        private List<String> titles;

        public ViewPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            if (fragments == null) throw new IllegalArgumentException("Data Must Not be Null");
            this.items = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * set_navProfile
     * myKocKocFragment의 핸들러에서 데이터를 받아 BaseActivity에 set
     */
    public void set_navProfileImg() {
        super.set_navProfileImg();
    } // 프사 변경 할때

    public void set_navProfileName(String name) {
        super.set_navProfileName(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DETAIL_ACTIVITY_REQUEST_CODE:
                ((StoryFragment) adapter.getItem(0)).refresh();
                break;
            case NEW_WRITE_REQUEST_CODE:
                ((StoryFragment) adapter.getItem(0)).refresh();
                detail_intent(data);
                break;
            case COURSE_WRITE_ACTIVITY_REQUEST_CODE:
                ((CourseFragment) adapter.getItem(2)).refresh();
                break;
            case CONTINUOUS_WRITE_REQUEST_CODE:
                ((StoryFragment) adapter.getItem(0)).refresh();
                detail_intent(data);
                break;
            default:
                break;
        }
    }

    void detail_intent(Intent data) {
        if (data == null) return;

        int boardNo = data.getIntExtra("boardNo", 0);
        int courseNo = data.getIntExtra("courseNo", 0);
        int userNo = data.getIntExtra("board_userNo", 0);

        if (boardNo == 0 || courseNo == 0 || userNo == 0) return;

        Intent detail_intent = new Intent(MainActivity.this, DetailActivity.class);
        detail_intent.putExtra("boardNo", boardNo);
        detail_intent.putExtra("courseNo", courseNo);
        detail_intent.putExtra("board_userNo", userNo);
        startActivity(detail_intent);
    }
}