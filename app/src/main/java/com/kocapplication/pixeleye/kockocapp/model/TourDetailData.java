package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Hyeongpil on 2016-10-06.
 */
public class TourDetailData implements Serializable{
    private String title;
    private String text;
    private ArrayList<String> imgList;
    private ArrayList<String> thumbList;

    public TourDetailData() {
        imgList = new ArrayList<>();
        thumbList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<String> imgList) {
        this.imgList = imgList;
    }

    public ArrayList<String> getThumbList() {
        return thumbList;
    }

    public void setThumbList(ArrayList<String> thumbList) {
        this.thumbList = thumbList;
    }
}
