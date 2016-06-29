package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;

/**
 * Created by pixeleye03 on 2015-08-22.
 */
public class User implements Serializable {
    public int no;
    public String name,nickName,tel,e_mail,pwd,ymd,gender, profile;

    public User(String name, String nickName, String tel, String e_mail, String pwd, String ymd, String gender){
        this.name= name;
        this.nickName = nickName;
        this.tel = tel;
        this.e_mail = e_mail;
        this.pwd = pwd;
        this.ymd = ymd;
        this.gender = gender;
    }
    public User(){

    }

    public void setUserName(String name){
        this.name = name;
    }
    public void setUserNickname(String nickName){
        this.nickName = nickName;
    }
    public void setUserTel(String tel){
        this.tel = tel;
    }
    public void setUserEmail(String e_mail){
        this.e_mail = e_mail;
    }
    public void setUserYMD(String ymd){
        this.ymd = ymd;
    }
    public void setUserGender(String gender){
        this.gender = gender;
    }
    public void setUserProfile(String profile){
        this.profile = profile;
    }

    public User getUser(){
        return this;
    }

}