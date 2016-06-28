package com.kocapplication.pixeleye.kockocapp.util;

import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.detail.DetailPageData;
import com.kocapplication.pixeleye.kockocapp.model.Neighbor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016-06-23.
 */
public class JsonParser {
    final static String TAG = "JsonParser";
    /**
     * Course
     */
    static public ArrayList<String> readCourse(String MSG){
        ArrayList<String> result = new ArrayList<>();
        try{
            JSONObject courses = new JSONObject(MSG);
            ArrayList<String> tempArr = new ArrayList<>();
            tempArr.add(courses.getString("Course1"));
            tempArr.add(courses.getString("Course2"));
            tempArr.add(courses.getString("Course3"));
            tempArr.add(courses.getString("Course4"));
            tempArr.add(courses.getString("Course5"));
            tempArr.add(courses.getString("Course6"));
            tempArr.add(courses.getString("Course7"));
            tempArr.add(courses.getString("Course8"));
            tempArr.add(courses.getString("Course9"));
            tempArr.add(courses.getString("Course10"));
            for(String temp:tempArr){
                if(temp!=null){
                    result.add(temp);
                }else{break;}
            }
        }catch (Exception e){}
        return result;
    }
    //DetailPage Data
    static public DetailPageData detailPageLoad(String MSG) {
        DetailPageData detailPageData = new DetailPageData();
        try{
            JSONObject getObject = new JSONObject(MSG);
            Log.e(TAG,"json :"+getObject);
            JSONArray Details = getObject.getJSONArray("boardArr");
            JSONObject temp = Details.getJSONObject(0);

            detailPageData.setBoardNo(temp.getInt("Board_No"));
            detailPageData.setCourseNo(temp.getInt("Course_No"));
            detailPageData.setBoardDate(temp.getString("Date"));
            detailPageData.setBoardText(temp.getString("Text"));
            detailPageData.setUserNo(temp.getString("User_No"));

            String Latitude = temp.getString("Map").split(" ")[0];
            String Longitude = temp.getString("Map").split(" ")[1];

            detailPageData.setLongitude(Double.parseDouble(Longitude));
            detailPageData.setLatitude(Double.parseDouble(Latitude));

            detailPageData.setBoardDate(temp.getString("Date"));
            detailPageData.setBoardTime(temp.getString("Time"));

            JSONObject temp_user = temp.getJSONObject("writeUser");
            detailPageData.setUserName(temp_user.getString("nickname"));
            detailPageData.setProfileImage(temp_user.getString("profile"));

            detailPageData.setScrapNumber(temp.getInt("scrapCount"));

            //hash tag list
            ArrayList<String> hashTagList = new ArrayList<String>();
            JSONArray hashTagArr = temp.getJSONArray("hashList");
            for(int n =0; n<hashTagArr.length(); n++) {
                hashTagList.add(hashTagArr.get(n).toString());
            }
            detailPageData.setHashTagArr(hashTagList);
            //hash tag list end

            JSONArray expressionArr = temp.getJSONArray("expression");
            for(int m=0; m<expressionArr.length(); m++) {
                detailPageData.plusRecommend_No();
            }

            ArrayList<String>imagePathArr = new ArrayList<String>();
            JSONArray imageArr = temp.getJSONArray("imageArr");
            for(int k=0; k<imageArr.length(); k++) {
                imagePathArr.add((imageArr.get(k)).toString());
            }
            detailPageData.setBoardImgArr(imagePathArr);

            JSONArray commentArr = temp.getJSONArray("commentArr");
            ArrayList<DetailPageData.Comment> comments = new ArrayList<DetailPageData.Comment>();
            for(int l=0; l<commentArr.length(); l++) {
                JSONObject commentObject = commentArr.getJSONObject(l);
                DetailPageData.Comment temp_Comment = detailPageData.getCommentClass();
                temp_Comment.setComment_date(commentObject.getString("Date"));
                temp_Comment.setComment_text(commentObject.getString("Text"));
                temp_Comment.setComment_userName(commentObject.getString("commentUserNickname"));
                temp_Comment.setComment_userNo(commentObject.getInt("User_No"));
                temp_Comment.setComment_No(commentObject.getInt("Comment_No"));
                //profile
                comments.add(temp_Comment);
            }
            detailPageData.setCommentArr(comments);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return detailPageData;
    }

    static public ArrayList<Neighbor> getNeighborInfo(String msg){
        ArrayList<Neighbor> neighborList = new ArrayList<>();
        Log.e(TAG,""+msg);
        try{
            JSONArray neighborArr = new JSONArray(msg);
            int length = neighborArr.length();
            for(int i=0; i<length; i++){
                JSONObject temp = neighborArr.getJSONObject(i);
                int userNo = temp.getInt("userNo");
                String nickName = temp.getString("nickname");

                neighborList.add(new Neighbor(userNo, nickName));
            }
        }catch (Exception e){
            e.getMessage();
        }

        return neighborList;
    }

}
