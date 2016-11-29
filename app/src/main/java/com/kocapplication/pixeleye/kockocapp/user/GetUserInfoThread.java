package com.kocapplication.pixeleye.kockocapp.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kocapplication.pixeleye.kockocapp.model.ProfileData;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;

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
public class GetUserInfoThread extends Thread {
    final static String TAG = "GetUserInfoThread";
    private String postURL = BasicValue.getInstance().getUrlHead() + "Member/getUserInfo.jsp";
    private Handler handler;
    private int userNo = BasicValue.getInstance().getUserNo();

    public GetUserInfoThread(Handler handler) {
        super();
        this.handler = handler;
    }

    public GetUserInfoThread(Handler handler, int userNo){
        this(handler);
        this.userNo = userNo;
    }

    @Override
    public void run() {
        super.run();

        String result = "";

        //get User Info 프로필 기본정보
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(postURL);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userNo", "" + userNo));
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
        Log.d(TAG,"get user Info result :"+result);

        JsonParser parser = new JsonParser();
        JsonObject object = new JsonObject();
        String nickName = "";
        if(!result.equals("not exist No")){
            object = parser.parse(result).getAsJsonObject();
            nickName = object.get("nickname").getAsString();
        }

        postURL = BasicValue.getInstance().getUrlHead() + "Member/getUserScrapCourse.jsp";
        result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + userNo));

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
        Log.d(TAG,"getUserScrapCourse result :"+result);

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
