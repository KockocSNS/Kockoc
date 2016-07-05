package com.kocapplication.pixeleye.kockocapp.write.continuousWrite;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;

/**
 * Created by pixeleye03 on 2016-07-05.
 */
public class CourseSelectRecyclerAdapter extends RecyclerView.Adapter<CourseSelectRecyclerViewHolder>{
    private final static String TAG = "CourseSelectAdapter";
    private List<Course> items;

    public CourseSelectRecyclerAdapter(List<Course> data) {
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
    }

    @Override
    public CourseSelectRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course_select,parent,false);

        return new CourseSelectRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseSelectRecyclerViewHolder holder, int position) {
        Course item = items.get(position);
        holder.getCourseName().setText("# " + item.getTitle());
        holder.getDateButton().setText(item.getDate());
        holder.getTimeButton().setText(item.getTime());

        holder.getCourse_ll().setOnClickListener(new ItemClickListener());
        holder.getMemoButton().setOnClickListener(new MemoClickListener());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // TODO: 2016-07-05 코스 새글쓰기로
            Log.e(TAG,"클릭됨");
        }
    }

    private class MemoClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.e(TAG,"메모 클릭");
        }
    }
}
