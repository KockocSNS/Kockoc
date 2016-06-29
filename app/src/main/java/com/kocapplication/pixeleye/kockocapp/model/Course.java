package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Han_ on 2016-06-22.
 */
public class Course implements Serializable {
    private String title;
    private String date;
    private String time;

    private Date dateTime;

    public Course(String title, String date, String time) {
        super();
        this.title = title;
        this.date = date;
        this.time = time;
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

//    public String getDateByFormat() {
//
//    }


}
