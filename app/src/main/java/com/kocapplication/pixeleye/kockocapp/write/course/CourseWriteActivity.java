package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseViewRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Han_ on 2016-06-29.
 */
public class CourseWriteActivity extends BaseActivityWithoutNav {
    private final String TAG = "COURSE_WRITE_ACTIVITY";
    private String courseTitle;

    private RecyclerView recyclerView;
    private CourseWriteRecyclerAdapter adapter;

    private EditText courseInput;
    private Button dateButton;
    private Button timeButton;
    private Button addButton;
    private Button confirm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseTitle = getIntent().getStringExtra("COURSE_TITLE");
        init();
        actionBarTitleSet(courseTitle, Color.WHITE);

        container.setLayoutResource(R.layout.activity_course_write);
        View containView = container.inflate();

        declare(containView);
    }

    private void declare(View containView) {
        courseInput = (EditText) containView.findViewById(R.id.course_name_input);
        dateButton = (Button) containView.findViewById(R.id.course_date_set);
        timeButton = (Button) containView.findViewById(R.id.course_time_set);
        addButton = (Button) containView.findViewById(R.id.add_button);
        confirm = (Button) containView.findViewById(R.id.confirm);

        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        boolean isAm = calendar.get(Calendar.AM_PM) != 1 ? true : false;
        String hour = isAm ? String.valueOf(calendar.get(Calendar.HOUR)) : String.valueOf(calendar.get(Calendar.HOUR) + 12);
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));

        String date = year.substring(2) + "/";
        date += month.length() < 2 ? "0" + month + "/" : month+ "/" ;
        date += day.length() < 2 ? "0" + day : day;

        String time = hour.length() < 2 ? "0" + hour + ":" : hour + ":";
        time += minute.length() < 2 ? "0" + minute : minute;

        dateButton.setText(date);
        timeButton.setText(time);

        View.OnClickListener listener = new ButtonListener();
        dateButton.setOnClickListener(listener);
        timeButton.setOnClickListener(listener);
        addButton.setOnClickListener(listener);
        confirm.setOnClickListener(listener);

        View recyclerLayout = containView.findViewById(R.id.recycler_layout);
        recyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
        adapter = new CourseWriteRecyclerAdapter(new ArrayList<Course>(), this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Calendar currentDate = Calendar.getInstance();

            if (v.equals(dateButton)) {
                new DatePickerDialog(CourseWriteActivity.this, new TimeSetListener(),
                        currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();

            } else if (v.equals(timeButton)) {
                boolean isAm = currentDate.get(Calendar.AM_PM) != 1 ? true : false;
                int hour = isAm ? currentDate.get(Calendar.HOUR) : currentDate.get(Calendar.HOUR) + 12;
                int minute = currentDate.get(Calendar.MINUTE);

                new TimePickerDialog(CourseWriteActivity.this, new TimeSetListener(), hour, minute, false).show();

            } else if (v.equals(addButton)) {
                String title = courseInput.getText().toString();
                if (title.isEmpty()) {
                    Snackbar.make(addButton, "코스 제목을 입력해주세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                String date = dateButton.getText().toString();
                String time = timeButton.getText().toString();
                String dateTime = date + " " + time;

                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
                Date courseDate = new Date();
                try {
                    courseDate = format.parse(dateTime);
                } catch (ParseException e) {
                    Snackbar.make(addButton, "잘못된 날짜 형식입니다.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Course addCourse = new Course(title, courseDate);
                if (adapter.contain(addCourse)) {
                    Snackbar.make(addButton, "이미 포함된 코스입니다.", Snackbar.LENGTH_SHORT).show();
                    courseInput.setText("");
                    return;
                }

                adapter.getItems().add(addCourse);
                adapter.notifyDataSetChanged();

                recyclerView.scrollToPosition(adapter.getItems().size());
                courseInput.setText("");

            } else if (v.equals(confirm)) {
                if (adapter.getItems().isEmpty()) {
                    Snackbar.make(confirm, "코스를 입력해주세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                AlarmHelper manager = new AlarmHelper(getApplicationContext());
                Courses courses = new Courses(courseTitle, new Date(), adapter.getItems());
                manager.setCourseAlarm(courses);
                JspConn.uploadCourse(courseTitle,courses.getCourses()); // 코스 디비 업로드
                finish();
            }
        }
    }

    private class TimeSetListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String _year = String.valueOf(year);
            String _month = String.valueOf(monthOfYear + 1);
            String _day = String.valueOf(dayOfMonth);

            String date = _year + "/" + _month + "/" + _day;

            dateButton.setText(date);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String _minute = String.valueOf(minute);
            String _hour = String.valueOf(hourOfDay);

            _hour = _hour.length() < 2 ? "0" + _hour : _hour;
            _minute = _minute.length() < 2 ? "0" + _minute : _minute;

            String time = _hour + ":" + _minute;

            timeButton.setText(time);
        }
    }

}
