package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Han_ on 2016-06-22.
 */
public class Course implements Serializable {
//    private int courseNo;
    private int coursePosition;
    private String title;
    private Date dateTime;

//    public Course(String title, Date dateTime) {
//        this.title = title;
//        this.dateTime = dateTime;
//    }

    public Course(String title, Date dateTime, int coursePosition) {
        this.title = title;
        this.dateTime = dateTime;
        this.coursePosition = coursePosition;
    }

//    public Course(int courseNo, String title, Date dateTime) {
//        this(title, dateTime);
//        this.courseNo = courseNo;
//    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return title.equals(((Course)o).getTitle());
    }

//    public int getCourseNo() {
//        return courseNo;
//    }

    public int getCoursePosition() {
        return coursePosition;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateTime() {
        return dateTime;
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

    //    public String getDateByFormat() {
//
//    }


}
