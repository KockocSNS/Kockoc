package com.kocapplication.pixeleye.kockocapp.util;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kocapplication.pixeleye.kockocapp.detail.DetailPageData;
import com.kocapplication.pixeleye.kockocapp.model.Neighbor;
import com.kocapplication.pixeleye.kockocapp.model.NoticeItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by hp on 2016-06-23.
 */
public class JsonParser {
    final static String TAG = "JsonParser";

    /**
     * Course
     */
    static public ArrayList<String> readCourse(String MSG) {
        ArrayList<String> result = new ArrayList<>();
        try {
            // 임시로 title에 // 를 붙여 시간을 표시했다  DetailPageData에 왜 코스가 ArrayList<String>인지 모르겠다 ArrayList<Course>로 DetailCourseAdapter에서 다시 변환한다 굉장히 좋지않다...
            com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
            JsonArray stopoversArray = parser.parse(MSG).getAsJsonArray();
            ArrayList<String> tempArr = new ArrayList<>();
            Log.i("stopoversArray",stopoversArray.toString());
            for(int i = 0; i < stopoversArray.size(); i++) {
                String temp[];
                temp = stopoversArray.get(i).getAsJsonObject().get("stopover_"+(i+1)).toString().split("\"");
                tempArr.add(temp[1]+"//"+stopoversArray.get(i).getAsJsonObject().get("stopover_"+(i+1)+"_time").getAsString());
            }
            for (String temp : tempArr) {
                if (temp != null)
                    result.add(temp);
                else break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("stopoverResult", result.toString());
        return result;
    }

    //DetailPage Data
    static public DetailPageData detailPageLoad(String MSG) {
        DetailPageData detailPageData = new DetailPageData();
        try {
            JSONObject getObject = new JSONObject(MSG);
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
            for (int n = 0; n < hashTagArr.length(); n++) {
                hashTagList.add(hashTagArr.get(n).toString());
            }
            detailPageData.setHashTagArr(hashTagList);

            JSONArray expressionArr = temp.getJSONArray("expression");
            detailPageData.setRecommend_No(expressionArr.length());


            ArrayList<String> imagePathArr = new ArrayList<String>();
            JSONArray imageArr = temp.getJSONArray("imageArr");
            for (int k = 0; k < imageArr.length(); k++) {
                imagePathArr.add((imageArr.get(k)).toString());
            }
            detailPageData.setBoardImgArr(imagePathArr);

            JSONArray commentArr = temp.getJSONArray("commentArr");
            ArrayList<DetailPageData.Comment> comments = new ArrayList<DetailPageData.Comment>();
            for (int l = 0; l < commentArr.length(); l++) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailPageData;
    }

    static public ArrayList<Neighbor> getFollowInfo(String msg) {
        ArrayList<Neighbor> neighborList = new ArrayList<>();
        Log.e(TAG, "" + msg);
        try {
            JSONArray neighborArr = new JSONArray(msg);
            int length = neighborArr.length();
            for (int i = 0; i < length; i++) {
                JSONObject temp = neighborArr.getJSONObject(i);
                int userNo = temp.getInt("userNo");
                String nickName = temp.getString("nickname");

                neighborList.add(new Neighbor(userNo, nickName));
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return neighborList;
    }

    static public ArrayList<NoticeItem> getNoticeItem(String msg) {
        ArrayList<NoticeItem> NoticeItemList = new ArrayList<>();
        try {
            JSONArray noticeItemArr = new JSONArray(msg);
            int length = noticeItemArr.length();
            for (int i = 0; i < length; i++) {
                JSONObject temp = noticeItemArr.getJSONObject(i);
                String nickName = temp.getString("Nickname");
                int userNo = temp.getInt("User_No");
                int boardNo = temp.getInt("Board_No");
                int board_userNo = temp.getInt("BoardUser_No");
                int courseNo = 0;
                if(temp.isNull("Course_No")){
                    courseNo = 0;
                } else
                    courseNo = temp.getInt("Course_No");

                String date = temp.getString("Date");

                NoticeItemList.add(new NoticeItem(userNo, boardNo, board_userNo, courseNo, nickName, date));
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return NoticeItemList;
    }

}
