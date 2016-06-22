package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;

/**
 * Created by Han_ on 2016-06-22.
 */
public class BoardBasicAttr implements Serializable {
    private int userNo;
    private int boardNo;
    private int courseNo;
    private int coursePo;

    public BoardBasicAttr(int userNo, int boardNo, int courseNo, int coursePo) {
        this.userNo = userNo;
        this.boardNo = boardNo;
        this.courseNo = courseNo;
        this.coursePo = coursePo;
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

    public int getCoursePo() {
        return coursePo;
    }
}

