package com.kocapplication.pixeleye.kockocapp.main.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-06-21.
 */
public class CourseFragment extends Fragment {
    private RecyclerView recyclerView;
    private CourseRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        init(view);

        Handler handler = new CourseHandler();
        Thread thread = new CourseThread(handler);
        thread.start();

        return view;
    }

    private void init(View view) {
        View container = view.findViewById(R.id.recycler_layout);
        recyclerView = (RecyclerView) container.findViewById(R.id.recycler_view);

        adapter = new CourseRecyclerAdapter(new ArrayList<Courses>(), new ItemClickListener());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
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
