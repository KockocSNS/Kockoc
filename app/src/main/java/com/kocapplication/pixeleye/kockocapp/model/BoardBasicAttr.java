package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;

/**
 * Created by Han_ on 2016-06-22.
 */
public class BoardBasicAttr implements Serializable {
    private int userNo;
    private int boardNo;
    private int courseNo;
    private int coursePosition;
    private int courseCount;

    public BoardBasicAttr(int userNo, int boardNo, int courseNo, int coursePosition, int courseCount) {
        this.userNo = userNo;
        this.boardNo = boardNo;
        this.courseNo = courseNo;
        this.coursePosition = coursePosition;
        this.courseCount = courseCount;
    }

    public int getUserNo() {
        return userNo;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public int getCourseNo() {
        return courseNo;
    }

    public int getCoursePosition() {
        return coursePosition;
    }

    public int getCourseCount() {
        return courseCount;
    }
}

