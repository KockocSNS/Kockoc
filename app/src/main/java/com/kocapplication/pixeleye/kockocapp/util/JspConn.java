package com.kocapplication.pixeleye.kockocapp.util;

import android.os.StrictMode;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.R;

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
 * Created by hp on 2016-06-23.
 */
public class JspConn {
    final static String TAG = "JspConn";

    /**
     * DetailPage
     */
    static public String loadDetailPage(String boardNo) {
        Log.e(TAG,"boardNo :"+boardNo);
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead()+"Board/LoadDetailPage.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("boardNo", "" + boardNo));
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
        Log.d(TAG,"loadDetailPage result :"+result);
        return result;
    }

    static public String WriteComment(String comment, int boardNo, int userNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead()+"Board/Comment/WriteComment.jsp";
        HttpPost post = new HttpPost(postURL);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("comment", "" + comment));
        params.add(new BasicNameValuePair("boardNo", "" + String.valueOf(boardNo)));
        params.add(new BasicNameValuePair("userNo", "" + String.valueOf(userNo)));
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
        return result;
    }

    /**
     * Course
     */
    //코스 넘버를 받아 course 반환
    static public String readCourseByCourseNo(int courseNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Course/readCourseByCourseNo.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("courseNo", "" + courseNo));
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
        return result;
    }

    static public String getNeighborInfo(int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"/Member/HPgetNeighborInfo.jsp";
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
        Log.e(TAG,""+result);
        return result;
    }

    static public String getFollowerInfo(int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"/Member/getFollowerInfo.jsp";
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
        return result;
    }


    static public void passiveMethod() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
