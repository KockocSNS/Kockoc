package com.kocapplication.pixeleye.kockocapp.navigation;

import android.os.Handler;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-07-04.
 */
public class UserDeleteThread extends Thread {
    private final String postURL = BasicValue.getInstance().getUrlHead() + "Member/userDelete.jsp";
    private Handler handler;
    private int userNo;

    public UserDeleteThread(Handler handler, int userNo) {
        super();
        this.handler = handler;
        this.userNo = userNo;
    }

    @Override
    public void run() {
        super.run();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(postURL);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userNo", String.valueOf(userNo)));

        String result = new String();

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null)
                result += line;

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("DELETE", result);

        if (result.equals("OK")) {
            handler.sendEmptyMessage(1);
        }
    }
}
