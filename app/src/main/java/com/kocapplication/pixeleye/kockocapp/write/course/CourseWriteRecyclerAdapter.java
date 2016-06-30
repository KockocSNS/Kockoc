package com.kocapplication.pixeleye.kockocapp.write.course;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseRecyclerViewHolder;
import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.util.List;

/**
 * Created by Han_ on 2016-06-30.
 */
public class CourseWriteRecyclerAdapter extends RecyclerView.Adapter<CourseWriteRecyclerViewHolder> {
    private List<Course> items;

    public CourseWriteRecyclerAdapter(List<Course> data) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
    }

    @Override
    public CourseWriteRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course_add, parent, false);
        return new CourseWriteRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseWriteRecyclerViewHolder holder, int position) {
        Course item = items.get(position);

        holder.getCourseName().setText(item.getTitle());
        holder.getDateButton().setText(item.getDate());
        holder.getTimeButton().setText(item.getTime());

//        holder.getDelete().setOnClickListener();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Course> getItems() {
        return items;
    }

    public void setItems(List<Course> items) {
        this.items = items;
    }

    public boolean contain(Course course) {
        for (Course item:items)
            if (item.equals(course)) return true;

        return false;
    }
}

