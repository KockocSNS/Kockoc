package com.kocapplication.pixeleye.kockocapp.detail;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hp on 2016-06-21.
 */
public class DetailPageData implements Serializable {
    private int boardNo;
    private int courseNo;
    private String userName;
    private String userNo;
    private String profileImage;
    private String boardDate;
    private String boardText;
    private ArrayList<String> boardImgArr = new ArrayList<String>();
    private ArrayList<String> boardImgPathArr = new ArrayList<String>();
    private ArrayList<String> course = new ArrayList<String>();
    private ArrayList<String> hashTagArr = new ArrayList<String>();
    private ArrayList<Comment> commentArr = new ArrayList<Comment>();

    private double mLatitude;
    private double mLongitude;
    private String boardTime;

    private int recommend_No;
    private int scrapNumber;

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public void setBoardTime(String boardTime) {
        this.boardTime = boardTime;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setBoardDate(String date) {
        this.boardDate = date;
    }

    public void setBoardText(String text) {
        this.boardText = text;
    }

    public void setBoardImgArr(ArrayList<String> boardImgArr) {
        this.boardImgArr = boardImgArr;
    }

    public void setCourse(ArrayList<String> course) {
        this.course = course;
    }

    public void setHashTagArr(ArrayList<String> hashTagArr) {
        this.hashTagArr = hashTagArr;
    }

    public void setCommentArr(ArrayList<Comment> commentArr) {
        this.commentArr = commentArr;
    }

    public void setScrapNumber(int scrapNumber) {
        this.scrapNumber = scrapNumber;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public void setCourseNo(int courseNo) {
        this.courseNo = courseNo;
    }

    public void setRecommend_No(int recommend_No) {
        this.recommend_No = recommend_No;
    }


    public int getBoardNo() {
        return this.boardNo;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public String getBoardText() {
        return this.boardText;
    }

    public String getBoardDate() {
        return this.boardDate;
    }

    public ArrayList<String> getBoardImgArr() {
        return this.boardImgArr;
    }

    public ArrayList<String> getCourse() {
        return this.course;
    }

    public ArrayList<String> getHashTagArr() {
        return this.hashTagArr;
    }

    public ArrayList<Comment> getCommentArr() {
        return this.commentArr;
    }

    public String getUserNo() {
        return this.userNo;
    }

    public String getBoardTime() {
        return this.boardTime;
    }

    public int getScrapNumber() {
        return this.scrapNumber;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public int getCourseNo() {
        return courseNo;
    }

    public int getRecommend_No() {
        return recommend_No;
    }

    public class Comment implements Serializable{
        private int comment_userNo;
        private String comment_userName;
        private String comment_profileImage;
        private String comment_text;
        private String comment_date;
        private int comment_No;

        public void setComment_userNo(int userNo) {
            this.comment_userNo = userNo;
        }

        public void setComment_userName(String userName) {
            this.comment_userName = userName;
        }

        public void setComment_profileImage(String comment_profileImage) {
            this.comment_profileImage = comment_profileImage;
        }

        public void setComment_text(String comment_text) {
            this.comment_text = comment_text;
        }

        public void setComment_date(String comment_date) {
            this.comment_date = comment_date;
        }

        public void setComment_No(int comment_No) {
            this.comment_No = comment_No;
        }

        public int getComment_No() {
            return this.comment_No;
        }

        public int getComment_userNo() {
            return this.comment_userNo;
        }

        public String getComment_userName() {
            return this.comment_userName;
        }

        public String getComment_profileImage() {
            return this.comment_profileImage;
        }

        public String getComment_text() {
            return this.comment_text;
        }

        public String getComment_date() {
            return this.comment_date;
        }
    }

    public Comment getCommentClass() {
        return new Comment();
    }
}
