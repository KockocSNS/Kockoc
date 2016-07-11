package com.kocapplication.pixeleye.kockocapp.detail;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-07-05.
 */
public class DetailCourseAdapter extends RecyclerView.Adapter<DetailCourseViewHolder> {
    private List<Course> items;

    public DetailCourseAdapter(List<String> data) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");

        setItems(data);
    }

    @Override
    public DetailCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_detail_course, parent, false);

        return new DetailCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailCourseViewHolder holder, int position) {
        Course item = items.get(position);

        holder.getCourseName().setText(item.getTitle());
        holder.getCourseTime().setText(item.getTime());

        Log.i("COURSEADAPTER", position + "");
        Log.i("COURSEADAPTER", (items.size() - 1) + "");

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
        // TODO: 2016-07-11 시간이 없어서 대충 만들었다 나중에 item을 코스 Class 로 바꿔서 깔끔하게 만들어야 한다.

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
}
