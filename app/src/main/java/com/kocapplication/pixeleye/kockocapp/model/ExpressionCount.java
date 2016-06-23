package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;

/**
 * Created by Han_ on 2016-06-22.
 */
public class ExpressionCount implements Serializable {
    private int expressionCount;
    private int scrapCount;
    private int commentCount;

    public ExpressionCount(int expressionCount, int scrapCount, int commentCount) {
        this.expressionCount = expressionCount;
        this.scrapCount = scrapCount;
        this.commentCount = commentCount;
    }

    public int getExpressionCount() {
        return expressionCount;
    }

    public int getScrapCount() {
        return scrapCount;
    }

    public int getCommentCount() {
        return commentCount;
    }
}
