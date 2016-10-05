package com.kocapplication.pixeleye.kockocapp.login;

import android.os.Handler;
import android.os.Message;

import com.kocapplication.pixeleye.kockocapp.model.User;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;


/**
 * Created by Han_ on 2016-04-06.
 */
public class FacebookJoinThread extends Thread {
    private Handler handler;
    private User user;

    public FacebookJoinThread(Handler handler, String email, String name, String gender) {
        super();
        this.handler = handler;
        this.user = new User(name, name, "", email, "", "now", gender);
    }

    public FacebookJoinThread(Handler handler, User user) {
        super();
        this.handler = handler;
        this.user = user;
    }

    public User getUser(){return user;}

    @Override
    public void run() {
        super.run();

        Message msg = Message.obtain();
        msg.what = 0;

        if (JspConn.recordMember(user) != 0) {
            msg.what = 1;
        }

        handler.sendMessage(msg);
    }
}
