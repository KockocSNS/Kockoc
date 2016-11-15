package com.kocapplication.pixeleye.kockocapp.model;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Han_ on 2016-06-22.
 */
public class Course implements Serializable {
    final static String TAG = "Course";
    private int courseNo;
    private int userNo;
    private int coursePosition;
    private String title;
    private Date dateTime = new Date();
    private String memo="not_change";


    public Course (int courseNo, int coursePosition, String title, Date date, String memo){
        this.courseNo = courseNo;
        this.coursePosition = coursePosition;
        this.title = title;
        this.dateTime = date;
        this.memo = memo;
    }

    public  Course(String title) {
        this.title = title;
    }

    public Course(String title, Date dateTime) {
        this.title = title;
        this.dateTime = dateTime;
    }

    public Course(String title, Date dateTime, int coursePosition) {
        this.title = title;
        this.dateTime = dateTime;
        this.coursePosition = coursePosition;
    }

    public Course(String title, Date dateTime, int coursePosition, String memo) {
        this.title = title;
        this.dateTime = dateTime;
        this.coursePosition = coursePosition;
        this.memo = memo;
    }

    public Course(int courseNo, String title, Date dateTime) {
        this(title, dateTime);
        this.courseNo = courseNo;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return title.equals(((Course)o).getTitle());
    }

    public int getCourseNo() {
        return courseNo;
    }

    public int getCoursePosition() {
        return coursePosition;
    }

    public String getTitle() {
        return title;
    }

    public String getDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
        return format.format(dateTime);
    }

    public String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
        return format.format(dateTime);
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(dateTime);
    }

    public long getDataByMilSec() {
        return dateTime.getTime();
    }

    public String getMemo() {
        return memo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setMemo(String memo) { this.memo = memo; }

    public void setCourseNo(int courseNo){ this.courseNo = courseNo; }

}