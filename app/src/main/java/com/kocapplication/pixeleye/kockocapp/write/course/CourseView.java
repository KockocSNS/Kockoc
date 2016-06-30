package com.kocapplication.pixeleye.kockocapp.write.course;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kocapplication.pixeleye.kockocapp.main.myKockoc.course.CourseActivity;
import com.kocapplication.pixeleye.kockocapp.model.Courses;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Han_ on 2016-06-30.
 */
public class CourseView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread;
    private Courses courses;

    public CourseView(Context context, SurfaceView surfaceView, Courses courses, CourseActivity courseActivity) {
        super(context);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);


    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) { }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }


    private class Spot {
        private int x, y;
        private String hashTag;
        private Date dateTime;

        public String getTime() {
            SimpleDateFormat format = new SimpleDateFormat("a HH:mm");
            return format.format(this.dateTime);
        }
    }

    private class DrawThread extends Thread {

        public DrawThread() {
            super();
        }

        @Override
        public void run() {
            super.run();
        }
    }
}
