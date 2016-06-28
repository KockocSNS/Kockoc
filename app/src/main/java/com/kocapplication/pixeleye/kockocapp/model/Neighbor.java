package com.kocapplication.pixeleye.kockocapp.model;

import android.graphics.Bitmap;

import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pixeleye02 on 2015-09-14.
 */
public class Neighbor implements Serializable {
    private int userNo;
    private String nickname;

    public Neighbor(int userNo, String nickname){
        this.userNo = userNo;
        this.nickname = nickname;
    }

    public int getUserNo() {
        return userNo;
    }

    public String getNickname() {
        return nickname;
    }
}