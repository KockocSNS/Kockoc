package com.kocapplication.pixeleye.kockocapp.main.myKockoc.course;

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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseThread;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.write.continuousWrite.CourseSelectActivity;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseTitleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pixeleye02 on 2016-06-28.
 */
public class CourseActivity extends BaseActivityWithoutNav {
    private final static String TAG = "CourseActivity";
    private SwipeRefreshLayout refreshLayout;
    private TextView courseAdd;
    private RecyclerView recyclerView ;
    private CourseRecyclerAdapter adapter;
    private View containView;
    private String flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getflag();

        init();

        if(flag.equals("이어쓰기"))
            actionBarTitleSet("코스 선택", Color.WHITE);
        else
            actionBarTitleSet("코스", Color.WHITE);

        container.setLayoutResource(R.layout.activity_course);
        containView = container.inflate();

        getComponent();

        Handler handler = new CourseHandler();
        Thread thread = new CourseThread(handler);
        thread.start();
    }
    private void getflag(){
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
    }

    protected void getComponent(){
        courseAdd = (TextView) containView.findViewById(R.id.course_add);
        courseAdd.setOnClickListener(new ButtonListener());

        View recyclerContainer = containView.findViewById(R.id.recycler_layout);
        refreshLayout = (SwipeRefreshLayout) recyclerContainer.findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) recyclerContainer.findViewById(R.id.recycler_view);

        adapter = new CourseRecyclerAdapter(new ArrayList<Courses>(), new ItemClickListener());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(CourseActivity.this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CourseActivity.this, CourseTitleActivity.class);
            startActivity(intent);
        }
    }

    protected class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(flag.equals("이어쓰기")){
                int position = recyclerView.getChildLayoutPosition(v);
                List<Courses> coursesArr = (List<Courses>) adapter.getItems();
                Courses courses = (Courses) coursesArr.get(position);

                Intent intent = new Intent(CourseActivity.this,CourseSelectActivity.class);
                intent.putExtra("courses",courses);
                startActivity(intent);
                finish();
            }
        }
    }

    private class CourseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            List<Courses> courses = (ArrayList<Courses>) msg.getData().getSerializable("THREAD");

            adapter.setItems(courses);
            adapter.notifyDataSetChanged();
        }
    }

}
