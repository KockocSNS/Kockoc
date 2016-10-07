package com.kocapplication.pixeleye.kockocapp.main.course;

import android.os.Handler;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

/**
 * Created by Han_ on 2016-07-06.
 */
public class CourseDeleteThread extends Thread {
    private Handler handler;

    private int courseNo;
    private int userNo;
    private String title;

    public CourseDeleteThread(Handler handler, int userNo, int courseNo, String title) {
        super();
        this.handler = handler;
        this.userNo = userNo;
        this.courseNo = courseNo;
        this.title = title;
    }

    @Override
    public void run() {
        super.run();

        String result = JspConn.deleteCourse(userNo, courseNo, title);
        Log.i("COURSE_DELETE", result);

        handler.sendEmptyMessage(1);
    }
}
