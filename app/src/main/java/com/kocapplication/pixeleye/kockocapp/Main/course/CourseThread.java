package com.kocapplication.pixeleye.kockocapp.main.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
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
public class CourseThread extends Thread {
    private final String postURL = BasicValue.getInstance().getUrlHead() + "Course/readMyCourse.jsp";
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

//            params.add(new BasicNameValuePair("userNo", String.valueOf(BasicValue.getInstance().getUserNo())));
            // TODO: 2016-06-23 위의 코드로 바꿔야한닷.
            params.add(new BasicNameValuePair("userNo", String.valueOf(90)));
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
        JsonObject upperObject = parser.parse(result).getAsJsonObject();
        JsonArray array = upperObject.getAsJsonArray("courseArr");

        ArrayList<Courses> courses = new ArrayList<Courses>();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            int courseNo = object.get("CourseNo").getAsInt();
            String title = object.get("title").getAsString();
            String date = object.get("Date").getAsString();
            String time = object.get("Time").getAsString();

            ArrayList<Course> course = new ArrayList<Course>();

            for (int innerI = 1; innerI < 10; innerI++) {
                String temp = "Course" + innerI;
                JsonElement element = object.get(temp);
                if (element.isJsonNull()) {
//                    Log.i("COURSE_THREAD", "COURSE COUNT IS NOT 9 / CURRENT COUNT IS " + (innerI - 1));
                    break;
                }

                String board = element.getAsString();
                String[] split = board.split("/");
                course.add(new Course(split[0], split[1]));
            }

            courses.add(new Courses(courseNo, title, date, time, course));
        }

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("THREAD", courses);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
