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
    private String time;
    private Drawable boardImage;

    private List<String> hashTags;
    private List<String> imagePaths;
    private List<String> imageNames;

    // 지도 없는 데이터를 받아올 때 사용되는 생성자.
    public Board(BoardBasicAttr attributes, ExpressionCount expressionCount, String text, String date, String time, String mainImageURL, List<String> hashTags) {
        basicAttributes = attributes;
        this.expressionCount = expressionCount;

        this.text = text;
        this.date = date;
        this.time = time;

        this.coordinate = new Coordinate(0, 0);

        this.hashTags = hashTags;
        imagePaths = new ArrayList<>();
        imageNames = new ArrayList<>();

        try {
            InputStream inputStream = (InputStream) new URL("http://221.160.54.160:8080/board_image/" + attributes.getUserNo() + "/" + mainImageURL).getContent();
            this.boardImage = Drawable.createFromStream(inputStream, "board_image");
        } catch (Exception e) {
            Log.e("BOARD", "MAIN IMAGE RECEIVE ERROR");
        }
    }

    // 지도가 함께 있는 데이터를 받아올 때 사용되는 생성자.
    public Board(BoardBasicAttr attributes, ExpressionCount expressionCount, Coordinate coordinate, String text, String date, String time, String mainImageURL, List<String> hashTags) {
        this(attributes, expressionCount, text, date, time, mainImageURL, hashTags);
        this.coordinate = coordinate;
    }

    // 새글 작성시 사용되는 생성자.
    public Board(BoardBasicAttr attributes, Coordinate coordinate, String text, List<String> imagePaths, List<String> hashTags) {
        this(attributes, null, text, null, null, null, hashTags);
        this.coordinate = coordinate;
        this.imagePaths = imagePaths;
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

    public String getTime() {
        return time;
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
