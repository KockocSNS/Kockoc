package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.search.SearchActivity;
import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-06-30.
 */
public class CourseWriteRecyclerAdapter extends RecyclerView.Adapter<CourseWriteRecyclerViewHolder> {
    private List<Course> items;
    private Activity activity;
    private View.OnClickListener itemClickListener;
    private String flag = "CourseWrite";

    public CourseWriteRecyclerAdapter(List<Course> data, Activity activity) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
        this.activity = activity;
    }

    public CourseWriteRecyclerAdapter(List<Course> data, Activity activity, View.OnClickListener itemClickListener, String flag) {
        this(data, activity);
        this.itemClickListener = itemClickListener;
        this.flag = flag;
    }

    @Override
    public CourseWriteRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course_add, parent, false);
        if (itemClickListener != null) itemView.setOnClickListener(itemClickListener);
        return new CourseWriteRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseWriteRecyclerViewHolder holder, int position) {
        Course item = items.get(position);

        // TODO: 2016-07-11 일단 코스 하나마다 메모는 달지 않았다.
        holder.getMemo().setVisibility(View.GONE);

        if(flag.equals("CourseSelect")){
            holder.getDelete().setVisibility(View.INVISIBLE);
            holder.getSearch().setVisibility(View.INVISIBLE);
        }else {
            View.OnClickListener listener = new ItemButtonListener(holder, position);
            holder.getDateButton().setOnClickListener(listener);
            holder.getTimeButton().setOnClickListener(listener);
            holder.getDelete().setOnClickListener(listener);
            holder.getSearch().setOnClickListener(listener);
        }

        if (position == 0) holder.getLineTop().setVisibility(View.GONE);
        else holder.getLineTop().setVisibility(View.VISIBLE);

        if (position == (items.size() - 1)) {
            holder.getLineBottom().setVisibility(View.GONE);
        }
        else holder.getLineBottom().setVisibility(View.VISIBLE);

        holder.getCourseName().setText("# " + item.getTitle());
        holder.getDateButton().setText(item.getDate());
        holder.getTimeButton().setText(item.getTime());
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
                new DatePickerDialog(activity, new TimeSetListener(holder, position),
                        currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }

            else if (v.equals(holder.getTimeButton())) {
                String time = holder.getTimeButton().getText().toString();
                String[] _time = time.split(":");
                new TimePickerDialog(activity, new TimeSetListener(holder, position), Integer.parseInt(_time[0]), Integer.parseInt(_time[1]), false).show();
            }

            else if (v.equals(holder.getDelete())) {
                items.remove(position);
                notifyDataSetChanged();
            }

            else if (v.equals(holder.getSearch())){
                Intent searchIntent = new Intent(activity, SearchActivity.class);
                searchIntent.putExtra("keyword",items.get(position).getTitle());
                activity.startActivity(searchIntent);
            }

        }
    }

    private class TimeSetListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
        private CourseWriteRecyclerViewHolder holder;
        private int position;

        public TimeSetListener(CourseWriteRecyclerViewHolder holder, int position) {
            super();
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String _year = String.valueOf(year).substring(2);
            String _month = String.valueOf(monthOfYear + 1);
            String _day = String.valueOf(dayOfMonth);

            _year = _year.length() < 2 ? "0" + _year : _year;
            _month = _month.length() < 2 ? "0" + _month : _month;
            _day = _day.length() < 2 ? "0" + _day : _day;

            String date = _year + "/" + _month + "/" + _day;
            String time = holder.getTimeButton().getText().toString();

            try {
                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
                Date selectedDate = format.parse(date + " " + time);
                items.get(position).setDateTime(selectedDate);
            } catch (ParseException e) {
                Snackbar.make(view, "날짜를 다시 설정해주세요.", Snackbar.LENGTH_SHORT).show();
            }

            holder.getDateButton().setText(date);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String _minute = String.valueOf(minute);
            String _hour = String.valueOf(hourOfDay);

            _hour = _hour.length() < 2 ? "0" + _hour : _hour;
            _minute = _minute.length() < 2 ? "0" + _minute : _minute;

            String date = holder.getDateButton().getText().toString();
            String time = _hour + ":" + _minute;

            try {
                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
                Date selectedDate = format.parse(date + " " + time);
                items.get(position).setDateTime(selectedDate);
            } catch (ParseException e) {
                Snackbar.make(view, "시간을 다시 설정해주세요.", Snackbar.LENGTH_SHORT).show();
            }

            holder.getTimeButton().setText(time);
        }
    }
}

