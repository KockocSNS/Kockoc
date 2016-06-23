package com.kocapplication.pixeleye.kockocapp.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

/**
 * Created by pixeleye03 on 2016-06-23.
 */
public class DetailThread extends Thread {
    final static String TAG = "DetailThread";

    DetailPageData detailPageData;
    private Handler handler;
    private int boardNo;
    private int courseNo;

    public DetailThread(Handler handler,int boardNo, int courseNo){
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
        detailPageData.setCourse(JsonParser.readCourse(JspConn.readCourseByCourseNo(courseNo)));

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("THREAD",detailPageData);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
