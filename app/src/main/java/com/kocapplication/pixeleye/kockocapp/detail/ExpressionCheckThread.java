package com.kocapplication.pixeleye.kockocapp.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_CheckExpression;
/**
 * Created by hp on 2016-05-04.
 */
public class ExpressionCheckThread extends Thread {
    final static String TAG = "ExpressionCheckThread";
    private Handler handler;
    private int boardNo;

    public ExpressionCheckThread(Handler handler, int boardNo) {
        super();
        this.handler = handler;
        this.boardNo = boardNo;
    }

    @Override
    public void run() {
        super.run();

        Message msg = Message.obtain();
        msg.what = 1;
        String result = JspConn_CheckExpression.checkExpression(boardNo);

        Bundle bundle = new Bundle();
        bundle.putString("MESSAGE", result);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
