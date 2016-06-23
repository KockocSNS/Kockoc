package com.kocapplication.pixeleye.kockocapp.main.myKockoc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kocapplication.pixeleye.kockocapp.model.ProfileData;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-06-23.
 */
public class MyKocKocProfileThread extends Thread {
    private String postURL = BasicValue.getInstance().getUrlHead() + "Member/getUserInfo.jsp";
    private Handler handler;

    public MyKocKocProfileThread(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userNo", "" + 90));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(result).getAsJsonObject();

        String nickName = object.get("nickname").getAsString();

        postURL = BasicValue.getInstance().getUrlHead() + "Member/getUserScrapCourse.jsp";
        result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // TODO: 2016-06-23
//            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("userNo", "" + 90));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        object = parser.parse(result).getAsJsonObject();
        int course = object.get("courseCount").getAsInt();
        int neighbor = object.get("followCount").getAsInt();
        int scrap = object.get("scrapCount").getAsInt();

        ProfileData data = new ProfileData(nickName, scrap, neighbor, course);

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("THREAD", data);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }


}
