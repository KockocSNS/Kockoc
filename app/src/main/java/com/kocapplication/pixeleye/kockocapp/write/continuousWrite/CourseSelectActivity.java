package com.kocapplication.pixeleye.kockocapp.write.continuousWrite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.write.newWrite.NewWriteActivity;

/**
 * Created by hp on 2016-07-05.
 */
public class CourseSelectActivity extends BaseActivityWithoutNav {
    private final static String TAG = "CourseSelectActivity";

    View containView;
    private RecyclerView recyclerView;
    private CourseWriteRecyclerAdapter adapter;
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

    private void getComponent() {
        //코스 데이터 받아옴
        courses = (Courses) getIntent().getSerializableExtra("courses");

        recyclerView = (RecyclerView) containView.findViewById(R.id.recycler_layout_course_select);
        adapter = new CourseWriteRecyclerAdapter(courses.getCourses(), this, new ItemClickListener());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            Intent intent = new Intent(CourseSelectActivity.this, NewWriteActivity.class);
            intent.putExtra("FLAG", NewWriteActivity.CONTINUOUS_FLAG);
            intent.putExtra("COURSE_NO", courses.getCourseNo());
            intent.putExtra("COURSE_PO", (position + 1));
            startActivity(intent);
            finish();
        }
    }

}
