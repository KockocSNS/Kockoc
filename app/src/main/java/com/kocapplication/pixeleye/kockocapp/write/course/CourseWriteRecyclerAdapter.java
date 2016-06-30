package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Han_ on 2016-06-30.
 */
public class CourseWriteRecyclerAdapter extends RecyclerView.Adapter<CourseWriteRecyclerViewHolder> {
    private List<Course> items;
    private Activity activity;

    public CourseWriteRecyclerAdapter(List<Course> data, Activity activity) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
        this.activity = activity;
    }

    @Override
    public CourseWriteRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course_add, parent, false);
        return new CourseWriteRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseWriteRecyclerViewHolder holder, int position) {
        Course item = items.get(position);
        View.OnClickListener listener = new ItemButtonListener(holder, position);
        holder.getCourseName().setText(item.getTitle());
        holder.getDateButton().setText(item.getDate());
        holder.getTimeButton().setText(item.getTime());

        holder.getDateButton().setOnClickListener(listener);
        holder.getTimeButton().setOnClickListener(listener);
        holder.getDelete().setOnClickListener(listener);
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

    private class ItemButtonListener implements View.OnClickListener {
        private CourseWriteRecyclerViewHolder holder;
        private int position;

        public ItemButtonListener(CourseWriteRecyclerViewHolder holder, int position) {
            super();
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (v.equals(holder.getDateButton())) {
                Calendar currentDate = Calendar.getInstance();
                new DatePickerDialog(activity, new TimeSetListener(holder),
                        currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }

            else if (v.equals(holder.getTimeButton())) {
                new TimePickerDialog(activity, new TimeSetListener(holder), 9, 0, false).show();
            }

            else if (v.equals(holder.getDelete())) {
                items.remove(position);
                notifyDataSetChanged();
            }
        }
    }

    private class TimeSetListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
        private CourseWriteRecyclerViewHolder holder;

        public TimeSetListener(CourseWriteRecyclerViewHolder holder) {
            super();
            this.holder = holder;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String _year = String.valueOf(year);
            String _month = String.valueOf(monthOfYear + 1);
            String _day = String.valueOf(dayOfMonth);

            String date = _year + "/" + _month + "/" + _day;

            holder.getDateButton().setText(date);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String _minute = String.valueOf(minute);
            String _hour = String.valueOf(hourOfDay);

            _hour = _hour.length() < 2 ? "0" + _hour : _hour;
            _minute = _minute.length() < 2 ? "0" + _minute : _minute;

            String time = _hour + ":" + _minute;

            holder.getTimeButton().setText(time);
        }
    }
}

