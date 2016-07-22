package com.kocapplication.pixeleye.kockocapp.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kocapplication.pixeleye.kockocapp.model.User;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pixeleye02 on 2016-04-15.
 */
public class NaverUserInfoGetThread extends Thread {
    private Handler handler;

    private Context context;
    private OAuthLogin oAuthLogin;
    private String accessToken;

    public NaverUserInfoGetThread(Context context, Handler handler, OAuthLogin oAuthLogin, String accessToken) {
        super();
        this.context = context;
        this.oAuthLogin = oAuthLogin;
        this.accessToken = accessToken;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        String temp = oAuthLogin.requestApi(context, accessToken, "https://openapi.naver.com/v1/nid/me");
        Message msg = Message.obtain();

        User user = null;

        try {
            JSONObject upperObject = new JSONObject(temp);
            JSONObject object = upperObject.getJSONObject("response");
            String name = object.getString("name");
            String email = object.getString("email");
            String age = object.getString("age");
            String gender = object.getString("gender");
            String nickName = object.getString("nickname");
            String birthday = object.getString("birthday");

            user = new User(name, nickName, "", email, "", birthday, gender);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Message", user);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }
}