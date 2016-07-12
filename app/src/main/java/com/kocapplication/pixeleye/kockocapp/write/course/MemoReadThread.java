package com.kocapplication.pixeleye.kockocapp.write.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han on 2016-07-12.
 */
public class MemoReadThread extends Thread {
    private Handler handler;
    private int courseNo;

    public MemoReadThread(Handler handler, int courseNo) {
        super();
        this.handler = handler;
        this.courseNo = courseNo;
    }

    @Override
    public void run() {
        super.run();

        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Course/GetMemoByCourseNo.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courseNo", "" + courseNo));
        String result = "";

        try {
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

        Log.i("MEMOREAD", result);

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("THREAD", result);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
