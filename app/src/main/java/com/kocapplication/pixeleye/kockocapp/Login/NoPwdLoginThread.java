package com.kocapplication.pixeleye.kockocapp.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

/**
 * Created by Han_ on 2016-05-10.
 */
public class NoPwdLoginThread extends Thread {
    private Context context;
    private Handler handler;
    private String id;
    private String pw;
    private String name;

    private boolean autoLoginState;

    public NoPwdLoginThread(Context context, Handler handler, String id) {
        super();
        this.context = context;
        this.handler = handler;
        this.id = id;
    }



    @Override
    public void run() {
        super.run();
        Message msg = Message.obtain();
        msg.what = 0;

        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int number = JspConn.getUserNo(id);

        Log.e("LOGINACTIVITY", number + "");

        editor.putInt("login", number);
        editor.putInt("login", -1);
        editor.commit();

        if (number != 0) {
            msg.what = 1;
            BasicValue.getInstance().setUserNo(number);
        }

        handler.sendMessage(msg);


    }
}