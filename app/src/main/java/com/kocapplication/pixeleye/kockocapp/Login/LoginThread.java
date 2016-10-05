package com.kocapplication.pixeleye.kockocapp.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

/**
 * Created by Han_ on 2016-04-01.
 */
public class LoginThread extends Thread {
    final static String TAG = "LoginThread";
    private Context context;
    private Handler handler;
    private String id;
    private String pw;
    private boolean autoLoginState;

    public LoginThread(Context context, Handler handler, String id, String pw, boolean autoLoginState) {
        super();
        this.context = context;
        this.handler = handler;
        this.id = id;
        this.pw = pw;
        this.autoLoginState = autoLoginState;
    }

    @Override
    public void run() {
        super.run();
        Message msg = Message.obtain();
        msg.what = 0;

        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int number = JspConn.checkPwd(id, pw);
        Log.e(TAG, number + "");

        if (autoLoginState) {
            editor.putInt("login", number);
        } else {
            editor.putInt("login", -1);
        }
        editor.commit();

        if (number != 0) {
            msg.what = 1;
            BasicValue.getInstance().setUserNo(number);
        }

        handler.sendMessage(msg);
    }
}
