package com.kocapplication.pixeleye.kockocapp.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.util.List;

/**
 * Created by Han_ on 2016-07-05.
 */
public class DetailCourseAdapter extends RecyclerView.Adapter<DetailCourseViewHolder> {
    private List<Course> items;

    public DetailCourseAdapter(List<Course> data) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        items = data;
    }

    @Override
    public DetailCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_detail_course, parent, false);

        return new DetailCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailCourseViewHolder holder, int position) {
        Course item = items.get(position);

        if (position == 0) holder.getLineTop().setVisibility(View.GONE);
        else holder.getLineTop().setVisibility(View.VISIBLE);

        if (position == items.size() - 1) holder.getLineBottom().setVisibility(View.GONE);
        else holder.getLineBottom().setVisibility(View.VISIBLE);

        holder.getCourseName().setText(item.getTitle());
        holder.getCourseTime().setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
