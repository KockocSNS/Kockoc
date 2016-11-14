package com.kocapplication.pixeleye.kockocapp.util.GCM;

/**
 * Created by Hyeongpil on 2016-04-22.
 */

import android.os.StrictMode;
import android.util.Log;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    // TODO: 2016-10-27 이 함수를 활용하여 새 토큰값을 받아야함
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        setGcmKey(refreshedToken);
    }

    static public String setGcmKey(String gcmKey) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"GCM/setGcmKey.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("gcmKey", "" + gcmKey));
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
        Log.d("GCM_SET", "UserNo : "+ BasicValue.getInstance().getUserNo());
        Log.d("GCM_SET", "setGcmKey result :" + result);
        return result;
    }
    static public void passiveMethod() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}