package com.kocapplication.pixeleye.kockocapp.main.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kocapplication.pixeleye.kockocapp.detail.DetailPageData;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course.JspConn_CourseComment;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course.JspConn_CourseExpression;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016-11-10.
 */
public class CourseDetailDataThread extends Thread {
    final static String TAG = CourseDetailDataThread.class.getSimpleName();
    private Handler handler;
    private int courseNo;
    private ArrayList<DetailPageData.Comment> comments;

    public CourseDetailDataThread(Handler handler, int courseNo) {
        super();
        this.handler = handler;
        this.courseNo = courseNo;
    }

    @Override
    public void run() {
        super.run();

        Message msg = Message.obtain();
        msg.what = 1;
        //좋아요
        String expression = JspConn_CourseExpression.courseCheckExpression(courseNo);
        //댓글
        try {
            JSONObject getObject = new JSONObject(JspConn_CourseComment.courseComment(courseNo));
            JSONArray commentArr = getObject.getJSONArray("commentArr");
            comments = new ArrayList<>();
            for (int l = 0; l < commentArr.length(); l++) {
                JSONObject commentObject = commentArr.getJSONObject(l);
                DetailPageData.Comment temp_Comment = new DetailPageData().getCommentClass();
                temp_Comment.setComment_date(commentObject.getString("Date"));
                temp_Comment.setComment_text(commentObject.getString("Text"));
                temp_Comment.setComment_userName(commentObject.getString("commentUserNickname"));
                temp_Comment.setComment_userNo(commentObject.getInt("User_No"));
                temp_Comment.setComment_No(commentObject.getInt("Comment_No"));
                //profile
                comments.add(temp_Comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString("expression", expression);
        bundle.putSerializable("comment",comments);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
