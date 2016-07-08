package com.kocapplication.pixeleye.kockocapp.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-06-29.
 */
public class Board implements Serializable{
    protected BoardBasicAttr basicAttributes;
    protected ExpressionCount expressionCount;
    protected Coordinate coordinate;

    protected String text;
    protected String date;
    protected String time;

    protected List<String> hashTags;
    protected List<String> imagePaths; // 이미지 경로들
    protected List<String> imagePathArr; // 이미지 폴더
    protected List<String> imageNames; // 이미지 이름
    private int imageNum = 0;
    private int imageNo = 0;
    private String mainImg;

    // 지도 없는 데이터를 받아올 때 사용되는 생성자.
    public Board(BoardBasicAttr attributes, ExpressionCount expressionCount, String text, String date, String time, List<String> hashTags) {
        basicAttributes = attributes;
        this.expressionCount = expressionCount;

        this.text = text;
        this.date = date;
        this.time = time;

        this.coordinate = new Coordinate(0, 0);

        this.hashTags = hashTags;
        imagePaths = new ArrayList<>();
        imageNames = new ArrayList<>();
        imagePathArr = new ArrayList<>();
    }

    // 지도가 함께 있는 데이터를 받아올 때 사용되는 생성자.
    public Board(BoardBasicAttr attributes, ExpressionCount expressionCount, Coordinate coordinate, String text, String date, String time, List<String> hashTags) {
        this(attributes, expressionCount, text, date, time, hashTags);
        this.coordinate = coordinate;
    }

    // 새글 작성시 사용되는 생성자.
    public Board(BoardBasicAttr attributes, Coordinate coordinate, String text, String date, String time, List<String> imagePaths, List<String> hashTags) {
        this(attributes, null, text, date, time, hashTags);
        this.coordinate = coordinate;
        this.imagePaths = imagePaths;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }


    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getHashTagByString() {
        String returnValue = "";
        for (String temp : hashTags)
            returnValue += (temp + ", ");

        returnValue = returnValue.substring(0, returnValue.length() - 2);

        return returnValue;
    }

    public BoardBasicAttr getBasicAttributes() {
        return basicAttributes;
    }

    public ExpressionCount getExpressionCount() {
        return expressionCount;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public List<String> getHashTags() {return hashTags;}

    public List<String> getImageNames() {return imageNames;}

    public List<String> getImagePaths() {return imagePaths;}

    public void setMainImg(String mainImg) {this.mainImg = mainImg;}
    public String getMainImg() {return mainImg;}

    public void setImageNo(int imageNo) {this.imageNo = imageNo;}

    public List<String> getImagePathArr() {return imagePathArr;}

    public boolean imageAdd(String ImagePath, String ImageName){
        if(checkImage(this.imageNames, this.imagePaths, ImagePath, ImageName)) {
            Log.d("insert Image","enter record");
            if (imageNum < 10) {
                Log.d("insert",ImagePath +"  "+ImageName);
                imageNames.add(ImageName);
                imagePathArr.add(ImagePath);
                imageNum++;
                return true;
            } else {
                Log.e("Image", "The imageNum overflow");
                return false;
            }
        }else{
            Log.e("Image","Existed Image");
            return false;
        }
    }
    public boolean checkImage(List<String> ImagePathArr, List<String> ImageNameArr, String path, String name){
        boolean result=true;
        for(int i = 0; i< imageNum; i++){
            if((ImagePathArr.get(i)+ImageNameArr.get(i)).equals(path+name)){
                result = false;
            }
        }
        return result;
    }
}
