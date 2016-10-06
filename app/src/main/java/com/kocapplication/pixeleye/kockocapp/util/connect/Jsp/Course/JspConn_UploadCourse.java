package com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course;

import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.model.Course;
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

public class JspConn_UploadCourse extends JspConn {
    //코스 업로드
    static public String uploadCourse(String title, List<Course> Arr, boolean publicity) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Course_V2/insertCourse.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("courseNum", "" + String.valueOf(Arr.size()))); //stopover 갯수
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("courseNo",String.valueOf(Arr.get(0).getCourseNo())));
            if (publicity) {
                params.add(new BasicNameValuePair("publicity", "0"));
            } else {
                params.add(new BasicNameValuePair("publicity", "1"));
            }


            int i = 0;
            for (Course temp : Arr) {
                params.add(new BasicNameValuePair("course" + i +"Name", "" + temp.getTitle()));
                params.add(new BasicNameValuePair("course" + i +"Position", "" + temp.getCoursePosition()));
                params.add(new BasicNameValuePair("course" + i +"Memo", "" + temp.getMemo()));
                params.add(new BasicNameValuePair("course" + i +"DateTime", "" + temp.getDateTime()));
                i++;
            }
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {

        }

        return result;
    }

}
