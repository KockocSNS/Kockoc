package com.kocapplication.pixeleye.kockocapp.util;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BasicValue {
    private static BasicValue ourInstance = new BasicValue();
//    private String urlHead = "http://221.160.54.160:8080/";
    private String urlHead = "http://115.68.14.27:8080/";
    private final String DAUM_MAP_API_KEY = "fd87e70c9d3984b8efea777c78112f1e";

    private int userNo = -1;
    private String userNickname = "";

    public static BasicValue getInstance() {
        return ourInstance;
    }

    private BasicValue() {}

    public String getUrlHead() {
        return urlHead;
    }
    public int getUserNo() {
        return userNo;
    }
    public String getDAUM_MAP_API_KEY() {return DAUM_MAP_API_KEY;}
    public String getUserNickname() {return userNickname;}

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    public void setUserNickname(String userNickname) {this.userNickname = userNickname;}

}
