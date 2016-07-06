package com.kocapplication.pixeleye.kockocapp.write.continuousWrite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Courses;

/**
 * Created by hp on 2016-07-05.
 */
public class CourseSelectActivity extends BaseActivityWithoutNav {
    private final static String TAG ="CourseSelectActivity";

    View containView;
    private RecyclerView recyclerView;
    private CourseSelectRecyclerAdapter adapter;
    private Courses courses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        actionBarTitleSet("코스 선택", Color.WHITE);

        container.setLayoutResource(R.layout.activity_course_select);
        containView = container.inflate();

        getComponent();

    }

    private void getComponent(){
        //코스 데이터 받아옴
        courses = (Courses)getIntent().getSerializableExtra("courses");

        recyclerView = (RecyclerView)containView.findViewById(R.id.recycler_layout_course_select);
        adapter = new CourseSelectRecyclerAdapter(courses.getCourses());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }



}
