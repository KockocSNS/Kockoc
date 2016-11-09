package com.kocapplication.pixeleye.kockocapp.main.course;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteRecyclerAdapter;


/**
 * Created by Hyeongpil on 2016-11-09.
 */
public class CourseDetailActivity extends BaseActivityWithoutNav {
    final static String TAG = CourseDetailActivity.class.getSimpleName();
    public final static int COURSE_DETAIL_FLAG = 123141;
    private View containView;
    private RecyclerView rv_course_detail;
    private RecyclerView rv_course_comment;
    private CourseWriteRecyclerAdapter course_adapter;

    // TODO: 2016-11-09 하단 메뉴 및 댓글 리스트 뿌리기
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("코스 상세보기", Color.WHITE);
        container.setLayoutResource(R.layout.activity_course_detail);
        containView = container.inflate();

        getComponent();
        setCourseData();
    }

    private void getComponent(){
        rv_course_detail = (RecyclerView)containView.findViewById(R.id.rv_course_detail);
        rv_course_comment = (RecyclerView)containView.findViewById(R.id.rv_course_comment);
    }

    private void setCourseData() {
        Courses courses = (Courses) getIntent().getSerializableExtra("COURSES"); // CourseFragment에서 코스 데이터를 받아옴
        course_adapter = new CourseWriteRecyclerAdapter(courses.getCourses(), this, COURSE_DETAIL_FLAG);
        rv_course_detail.setAdapter(course_adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        rv_course_detail.setLayoutManager(manager);
        rv_course_detail.setHasFixedSize(true);
    }
}
