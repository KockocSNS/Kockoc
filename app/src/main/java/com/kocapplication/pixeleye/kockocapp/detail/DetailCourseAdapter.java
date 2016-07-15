package com.kocapplication.pixeleye.kockocapp.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-07-05.
 */
public class DetailCourseAdapter extends RecyclerView.Adapter<DetailCourseViewHolder> {
    private int coursePo;
    private List<Course> items;
    private View.OnClickListener listener;
    private Context context;

    public DetailCourseAdapter(List<String> data, View.OnClickListener listener, int coursePo, Context context) {
        super();
        this.coursePo = coursePo;
        this.listener = listener;
        this.context = context;
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");

        setItems(data);
    }

    @Override
    public DetailCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_detail_course, parent, false);
        itemView.setOnClickListener(listener);
        return new DetailCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailCourseViewHolder holder, int position) {
        Course item = items.get(position);

        holder.getCourseName().setText(item.getTitle());
        holder.getCourseTime().setText(item.getTime());

        if (position == coursePo) holder.getCourseImage().setImageDrawable(context.getResources().getDrawable(R.drawable.circle_main_color));
        else holder.getCourseImage().setImageDrawable(context.getResources().getDrawable(R.drawable.circle_gray));

        if (position == 0) holder.getLineTop().setVisibility(View.GONE);
        else holder.getLineTop().setVisibility(View.VISIBLE);

        if (position == (items.size() - 1)) holder.getLineBottom().setVisibility(View.GONE);
        else holder.getLineBottom().setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<String> data) {

        items = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String temp = data.get(i);

            if (!temp.equals("null")) {
                String[] split = temp.split("/");
                long dateMilSec = Long.parseLong(split[1]);
                items.add(new Course(split[0], new Date(dateMilSec), i));
            } else break;
        }
    }

    public List<Course> getItems() {
        return items;
    }
}
