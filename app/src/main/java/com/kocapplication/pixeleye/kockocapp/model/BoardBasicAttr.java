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
    private String courseName; // 추가해야함

    // 데이터를 받아올 때 사용되는 생성자
    public BoardBasicAttr(int userNo, int boardNo, int courseNo, int coursePosition, int courseCount, String courseName) {
        this.userNo = userNo;
        this.boardNo = boardNo;
        this.courseNo = courseNo;
        this.coursePosition = coursePosition;
        this.courseCount = courseCount;
        this.courseName = courseName;
    }

    // 새글 작성 시에 사용되는 생성자
    public BoardBasicAttr(int userNo) {
        this(userNo, 0, 0, 0, 0,"");
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

    public String getCourseName() {return courseName;}
}

