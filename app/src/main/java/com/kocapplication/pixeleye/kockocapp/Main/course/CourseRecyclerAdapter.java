package com.kocapplication.pixeleye.kockocapp.main.course;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;

import java.util.List;

/**
 * Created by Han_ on 2016-06-22.
 */
public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerViewHolder> {
    private List<Courses> items;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;
    private String flag = "";
    private View itemView;

    public CourseRecyclerAdapter(List<Courses> data, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
        super();
        if (data == null) throw new IllegalArgumentException("Data Must Not Be Null");
        this.items = data;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }
    public CourseRecyclerAdapter(List<Courses> data, View.OnClickListener listener,String flag) {
        super();
        if (data == null) throw new IllegalArgumentException("Data Must Not Be Null");
        this.items = data;
        this.listener = listener;
        this.flag = flag;
    }

    @Override
    public CourseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(flag.equals("main")) itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main_course, parent, false);
        else itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course, parent, false);
        itemView.setOnClickListener(listener);
        itemView.setOnLongClickListener(longClickListener);
        return new CourseRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseRecyclerViewHolder holder, int position) {
        Courses data = items.get(position);

        holder.getTitle().setText(data.getTitle());

        List<Course> courses = data.getCourses();
        String temp = "";
        for (Course course : courses) {
            temp += ("#" + course.getTitle() + " - ");
        }

        if (temp.length() > 3) temp = temp.substring(0, temp.length() - 3);

        holder.getBoardTitles().setText(temp);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Courses> getItems() {
        return items;
    }

    public void setItems(List<Courses> items) {
        this.items = items;
    }
}
