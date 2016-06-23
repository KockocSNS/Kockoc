package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Han_ on 2016-06-23.
 */
public class Courses implements Serializable {
    private int courseNo;
    private String title;
    private String date;
    private String time;

    private List<Course> courses;

    public Courses(int courseNo, String title, String date, String time, List<Course> courses) {
        this.courseNo = courseNo;
        this.title = title;
        this.date = date;
        this.time = time;
        this.courses = courses;
    }

    public int getCourseNo() {
        return courseNo;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Course getFirstCourse() {
        return courses.get(0);
    }

    public Course getSecondCourse() {
        return courses.get(1);
    }

    public Course getThirdCourse() {
        return courses.get(2);
    }

    public Course getForthCourse() {
        return courses.get(3);
    }

    public Course getFifthCourse() {
        return courses.get(4);
    }

    public Course getSixthCourse() {
        return courses.get(5);
    }

    public Course getSeventhCourse() {
        return courses.get(6);
    }

    public Course getEighThCourse() {
        return courses.get(7);
    }

    public Course getNineThCourse() {
        return courses.get(8);
    }
}
