package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;

import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-06-30.
 */
public class AlarmHelper {
    private final String TAG = "ALARM_MANAGER";

    private Context context;
    private AlarmManager manager;

    public AlarmHelper(Context context) {
        this.context = context;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setCourseAlarm(Courses courses) {
        Log.i(TAG, "SET ALARM");

        List<Course> _courses = courses.getCourses();

        for (Course course : _courses) {
            if (course.getDataByMilSec() > new Date().getTime()) {
                Log.i(TAG, course.getDate() + "/" + course.getTime());
            }
        }

        int requestCode = 124125;

//        Intent intent = new Intent(context.getApplicationContext(), AlarmDialogActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        //FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 는 취소하고 새로 만든다.
//        manager.set(AlarmManager.RTC_WAKEUP, , pendingIntent);
    }

}
