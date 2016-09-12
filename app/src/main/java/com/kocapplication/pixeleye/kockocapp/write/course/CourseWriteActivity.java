package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.course.CourseActivity;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.navigation.SettingActivity;
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
    public static int ADJUST_FLAG = 779128;
    public static int DEFAULT_FLAG = 12221;
    private int flag;

    private int courseNo;
    private int coursePosition=1;
    private String courseTitle;

    private RecyclerView recyclerView;
    private CourseWriteRecyclerAdapter adapter;

    private EditText courseInput;
    private Button memoButton;
    private Button dateButton;
    private Button timeButton;
    private Button addButton;
    private Button confirm;
    private CheckBox publicityCheckBox;

    private String memo = "test";
    public static int mNumCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        container.setLayoutResource(R.layout.activity_course_write);
        View containView = container.inflate();



        declare(containView);
        getDataFromBeforeActivity();

        new MemoReadThread(new MemoReadHandler(), courseNo).start();
    }

    public void setMemo(String memo) {
        //Log.i(TAG, memo);
        this.memo = memo;
    }



    private void declare(View containView) {
        courseInput = (EditText) containView.findViewById(R.id.course_name_input);
        memoButton = (Button) containView.findViewById(R.id.course_note_set);
        dateButton = (Button) containView.findViewById(R.id.course_date_set);
        timeButton = (Button) containView.findViewById(R.id.course_time_set);
        addButton = (Button) containView.findViewById(R.id.add_button);
        confirm = (Button) containView.findViewById(R.id.confirm);
        publicityCheckBox = (CheckBox) containView.findViewById(R.id.publicity);

        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        boolean isAm = calendar.get(Calendar.AM_PM) != 1 ? true : false;
        String hour = isAm ? String.valueOf(calendar.get(Calendar.HOUR)) : String.valueOf(calendar.get(Calendar.HOUR) + 12);
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));

        String date = year.substring(2) + "/";
        date += month.length() < 2 ? "0" + month + "/" : month + "/";
        date += day.length() < 2 ? "0" + day : day;

        String time = hour.length() < 2 ? "0" + hour + ":" : hour + ":";
        time += minute.length() < 2 ? "0" + minute : minute;

        dateButton.setText(date);
        timeButton.setText(time);

        View.OnClickListener listener = new ButtonListener();
        memoButton.setOnClickListener(listener);
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

    private void getDataFromBeforeActivity() {
        flag = getIntent().getIntExtra("FLAG", DEFAULT_FLAG);

        if (flag == ADJUST_FLAG) {
            Courses courses = (Courses) getIntent().getSerializableExtra("COURSES");

            courseNo = courses.getCourseNo();
            courseTitle = courses.getTitle();
            memo=courses.getMemo();
            actionBarTitleSet(courseTitle, Color.WHITE);

            adapter.setItems(courses.getCourses());
            adapter.notifyDataSetChanged();
        } else if (flag == DEFAULT_FLAG) {
            courseNo = 0;
            courseTitle = getIntent().getStringExtra("COURSE_TITLE");
            actionBarTitleSet(courseTitle, Color.WHITE);
        }
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
                Log.i(TAG,"sdf : "+ memo);
                Course addCourse = new Course(title, courseDate, (adapter.getItemCount() + 1), memo);
                //경유지 중복 안되게하는 조건문이지만 필요없음 나중에 지우면 될듯
//                if (adapter.contain(addCourse)) {
//                    Snackbar.make(addButton, "이미 포함된 코스입니다.", Snackbar.LENGTH_SHORT).show();
//                    courseInput.setText("");
//                    return;
//                }

                adapter.getItems().add(addCourse);
                adapter.notifyDataSetChanged();

                recyclerView.smoothScrollToPosition(adapter.getItems().size() - 1);
                courseInput.setText("");
                coursePosition++;

            } else if (v.equals(confirm)) {
                if (adapter.getItems().isEmpty()) {
                    Snackbar.make(confirm, "코스를 입력해주세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                    AlarmHelper manager = new AlarmHelper(getApplicationContext());
                    Courses courses = new Courses(courseNo, courseTitle, new Date(), adapter.getItems());
                    manager.setCourseAlarm(courses);


                    Log.i(TAG, courses.getCourseNo() + "");

                    int memoNum = MemoWriteThread.memoNum;
                    mNumCheck = memoNum;
                    Log.e("mNumCheck = memoNum", ""+mNumCheck+" "+memoNum);
                if (publicityCheckBox.isChecked()) {
                    JspConn.uploadCourse(courseTitle, courses.getCourses(), true);
                } else {
                    JspConn.uploadCourse(courseTitle, courses.getCourses(), false);
                }
//                if (flag == DEFAULT_FLAG && memoNum!=0) { //메모를 건든 코스
////                    JspConn.uploadCourseAndMemo(courseTitle, courses.getCourses(), memoNum);
//                    JspConn.uploadCourse(courseTitle, courses.getCourses());
//                    Log.e("JspConnuploadCourse", "off");
////                    CourseFragment.memo = MemoWriteThread.message; // nam
//                    MemoWriteThread.memoClickCount=0;
//                    MemoWriteThread.message="";
//                    MemoWriteThread.resultmemoNo=0;
//                    MemoWriteThread.resultuserNo=0;
//                    MemoWriteThread.resultcourseNo=0;
//                    MemoWriteThread.memoNum=0;
//                }
//
//                else if (flag == DEFAULT_FLAG && memoNum == 0 ) { //메모를 건들지 않은 코스
//                    JspConn.uploadCourse(courseTitle, courses.getCourses()); // 코스 디비 업로드
//                    Log.e("JspConnuploadCourse","on");
//                }
//                else if (flag ==ADJUST_FLAG && memoNum !=0) { //메모를 수정했으면 또는 메모가 있는 코스
//                    Log.e("editCourseMemo", "in");
//                    JspConn.uploadCourse(courseTitle, courses.getCourses());
////                    JspConn.editCourseAndMemo(courseNo, courses.getTitle(), courses.getCourses(), memo);
//                }
//                else if (flag == ADJUST_FLAG && memoNum == 0) { //메모가 없던 코스
////                    JspConn.editCourse(courseNo, courses.getTitle(), courses.getCourses());
//                    JspConn.uploadCourse(courseTitle, courses.getCourses());
//                    Log.e("editCourse","in");
//                }

                    finish();

            } else if (v.equals(memoButton)) {
                Courses courses = new Courses(courseNo, courseTitle, new Date(), adapter.getItems());
                Log.e("MmemoNum: ", ""+courseNo);
                if (mNumCheck == 0 && memo =="") {
                    Log.e("MemoDialog","null");
                    new MemoDialog(CourseWriteActivity.this, courses.getCourses(), "",coursePosition);
                }
                else if(memo != "") {
                    Log.e("MemoDialog","not null");
                    new MemoDialog(CourseWriteActivity.this, courses.getCourses(), memo,coursePosition);
                }
                Log.d("memosdf",memo);
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

    private class MemoReadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            memo = msg.getData().getString("THREAD");
        }
    }
}