package com.kocapplication.pixeleye.kockocapp.main.myKockoc.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseThread;
import com.kocapplication.pixeleye.kockocapp.model.Courses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pixeleye02 on 2016-06-28.
 */
public class CourseActivity extends BaseActivity {
    private RecyclerView recyclerView ;
    private CourseRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        View container = findViewById(R.id.recycler_layout);
        recyclerView = (RecyclerView) container.findViewById(R.id.recycler_view);

        adapter = new CourseRecyclerAdapter(new ArrayList<Courses>(), new ItemClickListener());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(CourseActivity.this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);

        Handler handler = new CourseHandler();
        Thread thread = new CourseThread(handler);
        thread.start();
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

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
