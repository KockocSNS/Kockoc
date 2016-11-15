package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-06-23.
 */
public class Courses implements Serializable {
    private int userNo;
    private int courseNo;
    private String title;
    private Date dateTime;
    private String memo;

    private List<Course> courses;

    public Courses(int courseNo, String title, Date dateTime, List<Course> courses) {
        this.courseNo = courseNo;
        this.title = title;
        this.dateTime = dateTime;
        this.courses = courses;
    }

    public Courses(int courseNo, String title, Date dateTime, List<Course> courses, String memo) { //메모 있는 코스
        this.courseNo = courseNo;
        this.title = title;
        this.dateTime = dateTime;
        this.courses = courses;
        this.memo = memo;
    }
    public Courses(int courseNo, String title, Date dateTime, List<Course> courses, int userNo) { //메모 있는 코스
        this.courseNo = courseNo;
        this.title = title;
        this.dateTime = dateTime;
        this.courses = courses;
        this.userNo = userNo;
    }

    public Courses(String title, Date dateTime, List<Course> courses) {
        this(0, title, dateTime, courses);
    }

    public Courses(String title, Date dateTime){
        this.title = title;
        this.dateTime = dateTime;
    }

    public Courses(List<Course> courses){
        this.courses = courses;
    }

    public int getCourseNo() {
        return courseNo;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public String getMemo() {return memo;}
    public void setCourseNo(int courseNo) {
        this.courseNo = courseNo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setMemo(String memo) {this.memo = memo;}

    public int getUserNo() {
        return userNo;
    }
}
