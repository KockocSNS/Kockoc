package com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage;

import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

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
 * Created by pixeleye02 on 2016-10-05.
 */

public class JspConn_WriteComment extends JspConn {
    //소식 댓글 쓰기
    static public String writeComment(String comment, int boardNo, int userNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Board/Comment/WriteComment.jsp";
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

    //코스 댓글 쓰기
    static public String writeCourseComment(String comment, int courseNo, int userNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Course_V2/writeCourseComment.jsp";
        HttpPost post = new HttpPost(postURL);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("comment", "" + comment));
        params.add(new BasicNameValuePair("courseNo", "" + String.valueOf(courseNo)));
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
        Log.d("JspConn_WriteComment","result :"+result);
        return result;
    }
}
