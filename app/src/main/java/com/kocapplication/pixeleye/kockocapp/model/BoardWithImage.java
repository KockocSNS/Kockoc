package com.kocapplication.pixeleye.kockocapp.model;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BoardWithImage extends Board implements Serializable {
    private Drawable boardImage;

    public BoardWithImage(BoardBasicAttr attributes, ExpressionCount expressionCount, String text, String date, String time, String mainImageURL, ArrayList<String> hashTags) {
        super(attributes, expressionCount, text, date, time, hashTags);
        imageFromServer(mainImageURL);
    }

    public BoardWithImage(BoardBasicAttr attributes, ExpressionCount expressionCount, Coordinate coordinate, String text, String date, String time, String mainImageURL, ArrayList<String> hashTags) {
        super(attributes, expressionCount, coordinate, text, date, time, hashTags);
        imageFromServer(mainImageURL);
    }

    public BoardWithImage(BoardBasicAttr attributes, Coordinate coordinate, String text, String date, String time, List<String> imagePaths, ArrayList<String> hashTags) {
        super(attributes, coordinate, text, date, time, imagePaths, hashTags);
    }

    private void imageFromServer(String mainImageURL) {
        try {
            InputStream inputStream = (InputStream) new URL("http://115.68.14.27:8080/board_image/" + this.basicAttributes.getUserNo() + "/" + mainImageURL).getContent();
            this.boardImage = Drawable.createFromStream(inputStream, "board_image");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Drawable getBoardImage() {
        return boardImage;
    }

    public void setBoardImage(Drawable boardImage) {
        this.boardImage = boardImage;
    }
}
