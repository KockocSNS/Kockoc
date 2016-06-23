package com.kocapplication.pixeleye.kockocapp.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-06-21.
 */
public class Board implements Serializable {
    private BoardBasicAttr basicAttributes;
    private ExpressionCount expressionCount;
    private Coordinate coordinate;

    private String text;
    private String date;
    private String courseTitle;
    private String mainImageURL;
    private Drawable boardImage;

    private List<String> hashTags;
    private List<String> imagePaths;
    private List<String> imageNames;
    private List<String> courses;

    public Board(BoardBasicAttr attributes, ExpressionCount expressionCount, String text, String date, String mainImageURL, List<String> hashTags) {
        basicAttributes = attributes;
        this.expressionCount = expressionCount;

        this.text = text;
        this.date = date;

        this.coordinate = new Coordinate(0, 0);

        this.hashTags = hashTags;
        imagePaths = new ArrayList<>();
        imageNames = new ArrayList<>();
        courses = new ArrayList<>();

        try {
            InputStream inputStream = (InputStream) new URL("http://221.160.54.160:8080/board_image/" + attributes.getUserNo() + "/" + mainImageURL).getContent();
            this.boardImage = Drawable.createFromStream(inputStream, "test");
        } catch (Exception e) {
            Log.e("BOARD", "MAIN IMAGE RECEIVE ERROR");
        }
    }

    public Board(BoardBasicAttr attributes, ExpressionCount expressionCount, Coordinate coordinate, String text, String date, String mainImageURL, List<String> hashTags) {
        this(attributes, expressionCount, text, date, mainImageURL, hashTags);
        this.coordinate = coordinate;
    }

    public void setBoardImage(Drawable boardImage) {
        this.boardImage = boardImage;
    }

    public Drawable getBoardImage() {
        return boardImage;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getHashTagByString() {
        String returnValue = "";
        for (String temp:hashTags)
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
