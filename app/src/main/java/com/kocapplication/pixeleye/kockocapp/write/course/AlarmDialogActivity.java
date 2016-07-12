package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;

import java.text.SimpleDateFormat;

/**
 * Created by Han_ on 2016-06-30.
 */
public class AlarmDialogActivity extends AppCompatActivity {
    public static int broadcastCode = 0;    //알람을 구분하기 위한 ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //title을 없앴다.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_alarm_dialog);
        init();

        // 알림시 진동 설정.
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        Thread thread = new TimerThread(new TimerHandler());
        thread.start();
    }

    private void init() {
        TextView courseTitle = (TextView) findViewById(R.id.course_title);
        TextView alarmTime = (TextView) findViewById(R.id.course_alarm_time);
        TextView alarmDetail = (TextView) findViewById(R.id.course_alarm_detail);

        Bundle bundle = getIntent().getExtras();
        Courses courses = (Courses) bundle.getSerializable("COURSES");
        Course currentCourse = (Course) bundle.getSerializable("COURSE");

        String _courseTitle = courses.getTitle();
        String _dateTime = currentCourse.getDate() + " " + currentCourse.getTime();
        String _detail = currentCourse.getTitle();

        courseTitle.setText(_courseTitle);
        alarmTime.setText(_dateTime);
        alarmDetail.setText(_detail);

        ImageButton close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notificationSet(_courseTitle, _dateTime, _detail);
        activityLocationSet();
        screenOnAndLocateTopOfScreen();
    }

    //Screen 켜고 화면의 최상위에 위치시킨다.
    private void screenOnAndLocateTopOfScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyLock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        keyLock.disableKeyguard();
    }

    //activity 의 Layout 위치를 설정한다.
    private void activityLocationSet() {
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();

        params.y = -metrics.heightPixels / 4;
        params.x = 1;

        this.getWindow().setAttributes(params);
    }

    //notification 을 설정한다.
    private void notificationSet(String title, String dateTime, String detail) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("코스 제목:" + title)
                .setContentText("알림 시간: " + dateTime + "\n알림 내용:" + detail)
                .setAutoCancel(true)
                .setTicker("Kockoc 알림")
                .build();

        manager.notify(broadcastCode, notification);
    }

    private class TimerThread extends Thread {
        private Handler handler;

        public TimerThread(Handler handler) {
            super();
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();

            try {
                sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.sendEmptyMessage(1);
        }
    }

    private class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    }
}
