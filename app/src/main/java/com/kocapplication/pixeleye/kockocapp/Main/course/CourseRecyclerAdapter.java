package com.kocapplication.pixeleye.kockocapp.main.course;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.util.List;

/**
 * Created by Han_ on 2016-06-22.
 */
public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerViewHolder> {
    private List<Course> items;

    public CourseRecyclerAdapter(List<Course> data) {
        super();
        if (data == null) throw new IllegalArgumentException("Data Must Not Be Null");
        this.items = data;
    }

    @Override
    public CourseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course, parent, false);
//        itemView.setOnClickListener();
        return new CourseRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseRecyclerViewHolder holder, int position) {
        Course data = items.get(position);

//        holder.getTitle().setText(data.getTitle());
//        holder.getTitle().setText(data.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
