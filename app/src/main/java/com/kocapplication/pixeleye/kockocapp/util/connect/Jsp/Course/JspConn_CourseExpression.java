package com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course;

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

public class JspConn_CourseExpression extends JspConn {
    //코스넘버를 받아 좋아요 카운트
    static public String courseCheckExpression(int courseNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Course_V2/courseCheckExpression.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
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
    //좋아요
    static public String writeExpression(int courseNo, int Status) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Course_V2/writeExpression.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("userNo", "" + String.valueOf(BasicValue.getInstance().getUserNo())));
        params.add(new BasicNameValuePair("courseNo", "" + String.valueOf(courseNo)));
        params.add(new BasicNameValuePair("status", "" + String.valueOf(Status)));
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
}
