package com.kocapplication.pixeleye.kockocapp.model;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

import java.io.Serializable;

/**
 * Created by Han_ on 2016-06-23.
 */
public class ProfileData implements Serializable {
    private String nickName;
    private int scrapCount;
    private int neighborCount;
    private int courseCount;

    private String imagePath;
    private RoundedBitmapDrawable profileImage;

    public ProfileData(String nickName, int scrapCount, int neighborCount, int courseCount) {
        this.nickName = nickName;
        this.scrapCount = scrapCount;
        this.neighborCount = neighborCount;
        this.courseCount = courseCount;
    }

    public void setProfileImage(RoundedBitmapDrawable profileImage) {
        this.profileImage = profileImage;
    }

    public String getNickName() {
        return nickName;
    }

    public int getScrapCount() {
        return scrapCount;
    }

    public int getNeighborCount() {
        return neighborCount;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public RoundedBitmapDrawable getProfileImage() {
        return profileImage;
    }
}
