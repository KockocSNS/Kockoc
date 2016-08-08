package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.course.CourseActivity;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.navigation.SettingActivity;

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
        long currentMil = new Date().getTime();

        for (Course course : courses.getCourses()) {
            if (course.getDataByMilSec() > currentMil) {

                Log.i(TAG, "SET ALARM -> " + course.getTime());

                Bundle bundle = new Bundle();
                bundle.putSerializable("COURSES", courses);
                bundle.putSerializable("COURSE", course);


                SharedPreferences sharedPreferences = context.getSharedPreferences("settingState",Context.MODE_MULTI_PROCESS);
                int settingTime = sharedPreferences.getInt("settingTime", 0);
                boolean alarmState = sharedPreferences.getBoolean("alarmState",false);

                Log.d("settingTime",String.valueOf(settingTime));

                Intent intent = new Intent(context, AlarmDialogActivity.class);
                intent.putExtras(bundle);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, AlarmDialogActivity.broadcastCode, intent, 0);
                //FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 는 취소하고 새로 만든다.

                if(alarmState == true) {
                    manager.set(AlarmManager.RTC_WAKEUP, course.getDataByMilSec() - settingTime, pendingIntent);


                    AlarmDialogActivity.broadcastCode++;        //course 의 ID 를 바꿔준다.
                }
                else{
                    return;
                }
                }
        }
    }

    public void removeCourseAlarm() {

    }

    public void removeAllAlarm() {

    }

}
