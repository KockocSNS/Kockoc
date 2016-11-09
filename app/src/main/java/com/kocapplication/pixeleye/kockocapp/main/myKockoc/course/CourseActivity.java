package com.kocapplication.pixeleye.kockocapp.main.myKockoc.course;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseDeleteThread;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseThread;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.write.continuousWrite.CourseSelectActivity;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseTitleActivity;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pixeleye02 on 2016-06-28.
 */
public class CourseActivity extends BaseActivityWithoutNav {
    private final static String TAG = "CourseActivity";
    public final static int DEFAULT_FLAG = 12422;
    public final static int CONTINUOUS_FLAG = 125322;
    public static final int COURSE_WRITE_ACTIVITY_REQUEST_CODE = 575;

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

        View recyclerContainer = containView.findViewById(R.id.course_recycler_layout);
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
            startActivityForResult(intent, COURSE_WRITE_ACTIVITY_REQUEST_CODE);
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
                startActivityForResult(intent, MainActivity.CONTINUOUS_WRITE_REQUEST_CODE);
            }

            else if (flag == DEFAULT_FLAG) {
                int position = recyclerView.getChildAdapterPosition(v);

                Intent intent = new Intent(CourseActivity.this, CourseWriteActivity.class);
                intent.putExtra("FLAG", CourseWriteActivity.ADJUST_FLAG);
                intent.putExtra("COURSES", adapter.getItems().get(position));
                startActivityForResult(intent, COURSE_WRITE_ACTIVITY_REQUEST_CODE);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);

            AlertDialog dialog = new AlertDialog.Builder(CourseActivity.this)
                    .setTitle("코스 삭제")
                    .setMessage("코스를 삭제하시겠습니까")
                    .setPositiveButton("예", new DialogClickListener(position))
                    .setNegativeButton("아니오", new DialogClickListener(position))
                    .create();
            dialog.show();

            return true;
        }
    }

    private class DialogClickListener implements DialogInterface.OnClickListener {
        int position;
        public DialogClickListener(int position) {
            super();
            this.position = position;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == Dialog.BUTTON_POSITIVE) {
                Courses course = adapter.getItems().get(position);
                Handler handler = new CourseDeleteHandler(position);
                new CourseDeleteThread(handler, BasicValue.getInstance().getUserNo(), course.getCourseNo(), course.getTitle()).start();
            }
        }
    }

    private class CourseDeleteHandler extends Handler {
        int position;
        public CourseDeleteHandler(int position) {
            super();
            this.position = position;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                adapter.getItems().remove(position);
                adapter.notifyDataSetChanged();
                Snackbar.make(recyclerView, "코스가 삭제되었습니다", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private class CourseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            List<Courses> courses = (ArrayList<Courses>) msg.getData().getSerializable("THREAD"); // CourseThread에서 받아옴

            adapter.setItems(courses);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COURSE_WRITE_ACTIVITY_REQUEST_CODE){
            Handler handler = new CourseHandler();
            Thread thread = new CourseThread(handler);
            thread.start();
        } else if(requestCode == MainActivity.CONTINUOUS_WRITE_REQUEST_CODE){
            try {
                setResult(MainActivity.CONTINUOUS_WRITE_REQUEST_CODE, data);
                finish();
            } catch (NullPointerException e) {
                Log.d(TAG,"onActivityResult null");
            }
        }
    }
}
