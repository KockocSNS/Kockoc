package com.kocapplication.pixeleye.kockocapp.model;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
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
    protected List<String> imagePaths;
    protected List<String> imageNames;

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
}
