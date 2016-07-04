package com.kocapplication.pixeleye.kockocapp.main.course;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;

import java.util.List;

/**
 * Created by Han_ on 2016-07-04.
 */
public class CourseViewRecyclerAdapter extends RecyclerView.Adapter<CourseViewRecyclerViewHolder> {
    private List<Course> item;

    public CourseViewRecyclerAdapter(List<Course> data) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        item = data;
    }

    @Override
    public CourseViewRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course_view, parent, false);
//        itemView.setOnClickListener();
        return new CourseViewRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewRecyclerViewHolder holder, int position) {
        Course course = item.get(position);

        holder.getTitle().setText(course.getTitle());
        holder.getDate().setText(course.getDate());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
}
