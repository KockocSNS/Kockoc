package com.kocapplication.pixeleye.kockocapp.write.course;

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
 * Created by Han on 2016-07-11.
 */
public class MemoWriteThread extends Thread {
    public static String message="";
    private int courseNo;
    public static int memoNum=0; // memo 넘버
    public static int memoClickCount=0; // memo 아이콘 클릭 횟수
    public static int resultmemoNo=0;
    public static int resultcourseNo=0;
    public static int resultuserNo=0;


    public MemoWriteThread(String message, int courseNo) {
        super();
        this.message = message;
        this.courseNo = courseNo;

    }

    @Override
    public void run() {
        super.run();

        Log.i("MEMOWRITETHREAD", "memo : " + message);


        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Course/CourseMemoWrite.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courseNo", "" + courseNo));
        params.add(new BasicNameValuePair("memo", message));
        params.add(new BasicNameValuePair("memoClickCount","" +memoClickCount));
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
            memoNum = Integer.parseInt(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Log.d("MEMOWRITETHREAD", "result :" + memoNum + "memoClickCount :" + memoClickCount);
        memoClickCount++;
    }
}