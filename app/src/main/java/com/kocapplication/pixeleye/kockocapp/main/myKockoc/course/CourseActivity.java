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
import android.view.View;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseThread;
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
    public final static int DEFAULT_FLAG = 12422;
    public final static int CONTINUOUS_FLAG = 125322;

    private SwipeRefreshLayout refreshLayout;
    private TextView courseAdd;
    private RecyclerView recyclerView;
    private CourseRecyclerAdapter adapter;
    private View containView;
    private int flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFlagFromBeforeActivity();

        init();

        if (flag == CONTINUOUS_FLAG)
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

    private void getFlagFromBeforeActivity() {
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", DEFAULT_FLAG);
    }

    protected void getComponent() {
        courseAdd = (TextView) containView.findViewById(R.id.course_add);
        courseAdd.setOnClickListener(new ButtonListener());

        View recyclerContainer = containView.findViewById(R.id.recycler_layout);
        refreshLayout = (SwipeRefreshLayout) recyclerContainer.findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) recyclerContainer.findViewById(R.id.recycler_view);

        ItemClickListener listener = new ItemClickListener();
        adapter = new CourseRecyclerAdapter(new ArrayList<Courses>(), listener, listener);
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

    protected class ItemClickListener implements View.OnClickListener, View.OnLongClickListener{
        @Override
        public void onClick(View v) {
            if (flag == CONTINUOUS_FLAG) {
                int position = recyclerView.getChildLayoutPosition(v);
                Courses courses = adapter.getItems().get(position);

                Intent intent = new Intent(CourseActivity.this, CourseSelectActivity.class);
                intent.putExtra("FLAG", CourseSelectActivity.CONTINUOUS_FLAG);
                intent.putExtra("courses", courses);
                startActivity(intent);
                finish();
            }

            else if (flag == DEFAULT_FLAG) {

            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
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
