package com.kocapplication.pixeleye.kockocapp.main.recommend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.BoardBasicAttr;
import com.kocapplication.pixeleye.kockocapp.model.Coordinate;
import com.kocapplication.pixeleye.kockocapp.model.ExpressionCount;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-06-23.
 */
public class RecommendThread extends Thread {
    private String postURL;
    private Handler handler;

    public RecommendThread(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        Message msg = Message.obtain();
        msg.what = 1;
        String result = "";

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("boardNo", "" + (-1)));

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
            msg.what = 0;
        }

        Log.i("RECOMMENDTHREAD", result);

        JsonParser parser = new JsonParser();
        JsonObject upperObject = parser.parse(result).getAsJsonObject();
        JsonArray array = upperObject.getAsJsonArray("boardArr");

        ArrayList<Board> receiveData = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            JsonObject object = element.getAsJsonObject();
            JsonObject expression = object.get("etcObject").getAsJsonObject();

            BoardBasicAttr attributes =
                    new BoardBasicAttr(
                            object.get("User_No").getAsInt(),
                            object.get("Board_No").getAsInt(),
                            object.get("Course_No").getAsInt(),
                            // TODO: 2016-06-23 coursePo 가 JSP 에서 던져주지 않는다.
                            0,
                            0
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

            List<String> hashTags = new ArrayList<>();

            for (int ti = 0; ti < hashTagArr.size(); ti++)
                hashTags.add(hashTagArr.get(ti).getAsString());

            Board board = new Board(attributes, expressionCount, coordinate,
                    object.get("Text").getAsString(),
                    object.get("Time").getAsString(),
                    object.get("mainImg").getAsString(),
                    hashTags);

            receiveData.add(board);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("THREAD", receiveData);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
