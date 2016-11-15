package com.kocapplication.pixeleye.kockocapp.main.course;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course.JspConn_ReadAllCourseThread;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseTitleActivity;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-06-21.
 */
public class CourseFragment extends Fragment {
    private static final int COURSE_ADD_REQUEST_CODE = 1009;

    private TextView courseAdd;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private CourseRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        init(view);

        Handler handler = new CourseHandler();
        Thread thread = new JspConn_ReadAllCourseThread(handler);
        thread.start();

        return view;
    }

    private void init(View view) {
        courseAdd = (TextView) view.findViewById(R.id.course_add);
        courseAdd.setOnClickListener(new ButtonListener());

        View container = view.findViewById(R.id.course_recycler_layout);
        refreshLayout = (SwipeRefreshLayout) container.findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) container.findViewById(R.id.recycler_view);

        refreshLayout.setOnRefreshListener(new RefreshListener());

        ItemClickListener listener = new ItemClickListener();
        adapter = new CourseRecyclerAdapter(new ArrayList<Courses>(), listener, listener);
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    public void refresh() {
        if (refreshLayout == null) return;
        refreshLayout.setRefreshing(true);
        Handler handler = new CourseHandler();
        Thread thread = new JspConn_ReadAllCourseThread(handler);
        thread.start();
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CourseTitleActivity.class);
            startActivityForResult(intent, COURSE_ADD_REQUEST_CODE);
        }
    }

    private class ItemClickListener implements View.OnClickListener, View.OnLongClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
            intent.putExtra("COURSES", adapter.getItems().get(position));
            getActivity().startActivityForResult(intent, MainActivity.COURSE_WRITE_ACTIVITY_REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            if(adapter.getItems().get(position).getUserNo() == BasicValue.getInstance().getUserNo()){
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("코스 삭제")
                        .setMessage("코스를 삭제하시겠습니까")
                        .setPositiveButton("예", new DialogClickListener(position))
                        .setNegativeButton("아니오", new DialogClickListener(position))
                        .create();
                dialog.show();
            }
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

            List<Courses> courses = (ArrayList<Courses>) msg.getData().getSerializable("THREAD");

            adapter.setItems(courses);
            adapter.notifyDataSetChanged();

            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case COURSE_ADD_REQUEST_CODE:
                refresh();
                break;
        }
    }
}
