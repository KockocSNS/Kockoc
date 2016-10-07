package com.kocapplication.pixeleye.kockocapp.main.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-06-23.
 */
public class CourseThread extends Thread {
    private final static String TAG = "CourseThread";
    private final String postURL = BasicValue.getInstance().getUrlHead() + "Course_V2/readMyCourse.jsp";
    private Handler handler;

    public CourseThread(Handler handler) {
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

            params.add(new BasicNameValuePair("userNo", String.valueOf(BasicValue.getInstance().getUserNo())));
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

        Log.i("COURSE_THREAD_real", result);
        JsonParser parser = new JsonParser();
        JsonObject upperObject = parser.parse(result).getAsJsonObject();
        JsonArray courseArr = upperObject.getAsJsonArray("searchResult");

        ArrayList<Courses> courses = new ArrayList<Courses>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Log.i("courseArr",courseArr.toString());
        for (int i = 0; i < courseArr.size(); i++) {
            JsonObject resultObject = courseArr.get(i).getAsJsonObject();

            ArrayList<Course> course = new ArrayList<Course>();

            int courseNo = resultObject.get("index").getAsInt();
            String title = resultObject.get("title").getAsString();
            String date = resultObject.get("courseDate").getAsString();
            Log.i("resultObject",resultObject.toString());
            JsonArray stopoverArr = resultObject.getAsJsonArray("courseArr");
            Log.i("stopoverArr",stopoverArr.toString());
            for (int j = 0; j < stopoverArr.size(); j++) {
                JsonObject stopoverObject = stopoverArr.get(j).getAsJsonObject();

                int stopoverPosition = stopoverObject.get("stopoverPosition").getAsInt();
                String name = stopoverObject.get("name").getAsString();
                String stopoverDate = stopoverObject.get("stopoverDate").getAsString();
                String memo = stopoverObject.get("memo").getAsString();

                Date temp = new Date();
                try {
                    temp = format.parse(stopoverDate);
                } catch (ParseException e) {
                    Log.e(TAG,"temp :"+e.getMessage());
                    e.printStackTrace();
                }
                course.add(new Course(courseNo,stopoverPosition, name, temp, memo));
            }

            Date temp = new Date();
            try {
                temp = format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            courses.add(new Courses(courseNo, title, temp, course));

        }
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("THREAD", courses);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

}
