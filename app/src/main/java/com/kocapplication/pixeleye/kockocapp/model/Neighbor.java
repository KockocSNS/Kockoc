package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;

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