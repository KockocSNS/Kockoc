package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Han_ on 2016-06-22.
 */
public class Course implements Serializable {
    private String title;
    private String timeStamp;

    public Course(String title, String timeStamp) {
        super();
        this.title = title;
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
