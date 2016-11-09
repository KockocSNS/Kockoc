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
import android.widget.TimePicker;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course.JspConn_UploadCourse;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

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
    public static int COURSE_WRITE_ACTIVITY = 2222;
    public static int ADJUST_FLAG = 779128;
    public static int DEFAULT_FLAG = 12221;
    private int flag;

    private int courseNo;
    private int coursePosition=1;
    private String courseTitle;
    private String memo = "";
    private boolean publicity = true;

    private RecyclerView recyclerView;
    private CourseWriteRecyclerAdapter adapter;

    private EditText courseInput;
    private Button memoButton;
    private Button dateButton;
    private Button timeButton;
    private Button addButton;
    private Button confirm;
    private CheckBox publicityCheckBox;

    public static int mNumCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        container.setLayoutResource(R.layout.activity_course_write);
        View containView = container.inflate();

        declare(containView);
        getDataFromBeforeActivity();
    }

    private void declare(View containView) {
        flag = getIntent().getIntExtra("FLAG", DEFAULT_FLAG);
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

        View recyclerLayout = containView.findViewById(R.id.course_recycler_layout);
        recyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
        adapter = new CourseWriteRecyclerAdapter(new ArrayList<Course>(), this, flag);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    private void getDataFromBeforeActivity() {
        if (flag == ADJUST_FLAG) {
            Courses courses = (Courses) getIntent().getSerializableExtra("COURSES"); // CourseFragment에서 받아옴

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
                    Log.e(TAG,"parse :"+e.getMessage());
                    Snackbar.make(addButton, "잘못된 날짜 형식입니다.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

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
                memo = ""; // 메모 초기화

            } else if (v.equals(confirm)) {
                if (adapter.getItems().isEmpty()) {
                    Snackbar.make(confirm, "코스를 입력해주세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                AlarmHelper manager = new AlarmHelper(getApplicationContext());
                Courses courses = new Courses(courseNo, courseTitle, new Date(), adapter.getItems());
                manager.setCourseAlarm(courses);

                //공개 여부
                publicity = publicityCheckBox.isChecked() ? true : false;

                Log.i(TAG, courses.getCourseNo() + "");
                //코스 수정
                if(flag == ADJUST_FLAG){
                    Log.i(TAG,"수정 진입");
                    JspConn.editCourse(courses.getCourses(),publicity);
                }else{// 새 코스 쓰기

                    // TODO: 2016-09-21  publicity 추가 필요
                    JspConn_UploadCourse.uploadCourse(courseTitle,courses.getCourses(), publicity);
                }

                memo = ""; // 메모 초기화
                finish();
            } else if (v.equals(memoButton)) {
                //하단의 메모 버튼을 누르면 memo 변수 안에 넣는다.
                Intent memoIntent = new Intent(CourseWriteActivity.this,CourseMemoActivity.class);
                memoIntent.putExtra("FLAG",COURSE_WRITE_ACTIVITY);
                memoIntent.putExtra("memo",memo);
                startActivityForResult(memoIntent, COURSE_WRITE_ACTIVITY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COURSE_WRITE_ACTIVITY){
            try {
                memo = data.getStringExtra("memo");
            }catch (NullPointerException e){}
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