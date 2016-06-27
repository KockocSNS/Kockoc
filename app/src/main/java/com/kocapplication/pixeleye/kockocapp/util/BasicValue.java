package com.kocapplication.pixeleye.kockocapp.util;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BasicValue {
    private static BasicValue ourInstance = new BasicValue();

    public static BasicValue getInstance() {
        return ourInstance;
    }

    private BasicValue() {
    }

    private String urlHead = "http://221.160.54.160:8080/";

    public String getUrlHead() {
        return urlHead;
    }

    public int getUserNo() {
        return userNo;
    }


    private int userNo = 90;

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }


    private final String DAUM_MAP_API_KEY = "fd87e70c9d3984b8efea777c78112f1e";

    public String getDAUM_MAP_API_KEY() {
        return DAUM_MAP_API_KEY;
    }
}
