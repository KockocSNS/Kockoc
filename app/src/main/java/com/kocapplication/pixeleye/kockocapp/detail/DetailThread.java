package com.kocapplication.pixeleye.kockocapp.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

import java.util.ArrayList;

/**
 * Created by hp on 2016-06-23.
 */
public class DetailThread extends Thread {
    final static String TAG = "DetailThread";

    DetailPageData detailPageData;
    private Handler handler;
    private int boardNo;
    private int courseNo;

    public DetailThread(Handler handler, int boardNo, int courseNo) {
        super();
        this.handler = handler;
        this.boardNo = boardNo;
        this.courseNo = courseNo;
    }

    @Override
    public void run() {
        super.run();
        detailPageData = new DetailPageData();
        detailPageData = JsonParser.detailPageLoad(JspConn.loadDetailPage(String.valueOf(boardNo)));

        if (courseNo != 0) {
            String courseResult = JspConn.readCourseByCourseNo(courseNo);
            detailPageData.setCourse(JsonParser.readCourse(courseResult));
        } else detailPageData.setCourse(new ArrayList<String>());

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("THREAD", detailPageData);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
