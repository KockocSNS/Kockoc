package com.kocapplication.pixeleye.kockocapp.util;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BasicValue {
    private static BasicValue ourInstance = new BasicValue();
    private String urlHead = "http://221.160.54.160:8080/";
    private int userNo = 90;

    public static BasicValue getInstance() {
        return ourInstance;
    }

    private BasicValue() {
    }

    public String getUrlHead() {
        return urlHead;
    }
    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
}
