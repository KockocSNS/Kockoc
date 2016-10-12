package com.kocapplication.pixeleye.kockocapp.main.myKockoc.scrap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kocapplication.pixeleye.kockocapp.model.BoardBasicAttr;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;
import com.kocapplication.pixeleye.kockocapp.model.Coordinate;
import com.kocapplication.pixeleye.kockocapp.model.ExpressionCount;
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
 * Created by Han_ on 2016-06-21.
 */
public class ScrapThread extends Thread {
    private String postURL = BasicValue.getInstance().getUrlHead() + "Scrap/readScrap.jsp";
    private Handler handler;
    private int boardNo = -1;

    public ScrapThread(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        String result = "";

        // read News
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));

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
        JsonArray array = upperObject.getAsJsonArray("boardArr");

        ArrayList<BoardWithImage> receiveData = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            JsonObject object = element.getAsJsonObject();
            JsonObject expression = object.get("etcObject").getAsJsonObject();

            // TODO: 2016-06-23 TO Much Connection이 생긴닷
            // TODO: 요거 JSP 를 조금 수정해서 DB에서 한번에 들고 올수 있도록 하는게 좋을것 같다.
//            String courseJsonString = courseJsonObject(object.get("Course_No").getAsInt());
//
//            JsonObject courseObject = parser.parse(courseJsonString).getAsJsonObject();
//            int courseCount = 0;
//
//            for (int innerI = 1; innerI < 10; innerI++) {
//                String temp = "Course" + innerI;
//                JsonElement courseElement = courseObject.get(temp);
//                if (courseElement.isJsonNull()) {
//                    Log.i("COURSE_THREAD", "COURSE COUNT IS NOT 9 / CURRENT COUNT IS " + (innerI));
//                    break;
//                }
//                courseCount++;
//            }

            BoardBasicAttr attributes =
                    new BoardBasicAttr(
                            object.get("User_No").getAsInt(),
                            object.get("Board_No").getAsInt(),
                            object.get("Course_No").getAsInt(),
                            // TODO: 2016-06-23 coursePo 가 JSP 에서 던져주지 않는다.
                            0,
                            0,
                            ""
                    );

            ExpressionCount expressionCount =
                    new ExpressionCount(
                            expression.get("expressionCount").getAsInt(),
                            expression.get("scrapCount").getAsInt(),
                            expression.get("commentCount").getAsInt()
                    );

            String[] mapString = object.get("Map").getAsString().split(" ");
            double latitude = Double.valueOf(mapString[0]);
            double longitude = Double.valueOf(mapString[1]);

            Coordinate coordinate = new Coordinate(latitude, longitude);

            JsonArray hashTagArr = object.get("HashTagArr").getAsJsonArray();

            ArrayList<String> hashTags = new ArrayList<>();

            for (int ti = 0; ti < hashTagArr.size(); ti++)
                hashTags.add(hashTagArr.get(ti).getAsString());

            BoardWithImage boardWithImage = new BoardWithImage(attributes, expressionCount, coordinate,
                    object.get("Text").getAsString(),
                    object.get("Date").getAsString(),
                    object.get("Time").getAsString(),
                    object.get("mainImg").getAsString(),
                    hashTags);

            receiveData.add(boardWithImage);
        }

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("THREAD", receiveData);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    private String courseJsonObject(int courseNo) {
        String result = "";

        postURL = BasicValue.getInstance().getUrlHead() + "Course/readCourseByCourseNo.jsp";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();
            // params.add(new BasicNameValuePair("UserNo",""));
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

}
