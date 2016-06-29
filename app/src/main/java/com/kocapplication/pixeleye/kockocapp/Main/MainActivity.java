package com.kocapplication.pixeleye.kockocapp.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseFragment;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.MyKocKocFragment;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.MyProfileImgThread;
import com.kocapplication.pixeleye.kockocapp.main.recommend.RecommendFragment;
import com.kocapplication.pixeleye.kockocapp.main.story.StoryFragment;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private final String TAG = "MainActivity";
    private final int PROFILE_SET = 1;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        actionBarTitleSet();
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

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new ViewPageAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PROFILE_SET:
                //ProfileImgReceiveHandler 에서 서버로 프로필 이미지 전송
                Handler handler = new ProfileImgReceiveHandler();
                Thread thread = new MyProfileImgThread(handler,data,this);
                thread.start();
                break;
        }
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

    private class ProfileImgReceiveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG,"메인핸들러 수신");
        }
    }
}