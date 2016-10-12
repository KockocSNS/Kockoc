package com.kocapplication.pixeleye.kockocapp.util.connect;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BasicValue {
    private static BasicValue ourInstance = new BasicValue();
    private String urlHead;
    private final String DAUM_MAP_API_KEY = "fd87e70c9d3984b8efea777c78112f1e";
    private boolean isRealServer = true; //서버 쉬프트 true : 레알서버
                                         //            false : 테썹
    private int userNo = -1;
    private String userNickname = "";
    private String userName = "";

    public static BasicValue getInstance() {
        return ourInstance;
    }

    private BasicValue() {
        if(isRealServer){
            urlHead = "http://115.68.14.27:8080/";
        } else {
            urlHead = "http://221.160.54.160:8080/";
        }
    }

    public String getUrlHead() {
        return urlHead;
    }
    public int getUserNo() {
        return userNo;
    }

    public String getDAUM_MAP_API_KEY() {return DAUM_MAP_API_KEY;}
    public String getUserNickname() {return userNickname;}
    public String getUserName() {return userName;}


    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    public void setUserNickname(String userNickname) {this.userNickname = userNickname;}
    public void setUserName(String userName){this.userName = userName;}

}
