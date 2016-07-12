package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pixeleye02 on 2016-07-05.
 */
public class NoticeItem implements Serializable {
    private int userNo;
    private int boardNo;
    private int board_userNo;
    private int courseNo;
    private String date;
    private String nickName;

    public NoticeItem(int userNo, int boardNo, int board_userNo, int courseNo, String nickName, String date){
        this.userNo = userNo;
        this.boardNo = boardNo;
        this.date = date;
        this.board_userNo = board_userNo;
        this.courseNo = courseNo;
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public int getUserNo() {
        return userNo;
    }

    public String getDate() {
        return date;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public int getBoard_userNo() {
        return board_userNo;
    }

    public int getCourseNo() {
        return courseNo;
    }
}

