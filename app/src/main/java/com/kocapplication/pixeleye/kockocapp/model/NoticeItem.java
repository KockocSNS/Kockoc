package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pixeleye02 on 2016-07-05.
 */
public class NoticeItem implements Serializable {
    private int userNo;
    private int boardNo;
    private String date;

    public NoticeItem(int userNo, int boardNo, String date){
        this.userNo = userNo;
        this.boardNo = boardNo;
        this.date = date;
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
}

