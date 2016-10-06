package com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Setting;

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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pixeleye02 on 2016-09-30.
 */

public class JspConn_ChangeNickname extends JspConn {

    //닉네임 변경
    static public String changeNickname(String nickname) throws IOException {
        String result = "";

        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Member/updateNickname.jsp";
        HttpPost post = new HttpPost(postURL);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
        params.add(new BasicNameValuePair("nickname", nickname));

        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        post.setEntity(ent);
        HttpResponse response = client.execute(post);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            return result;

    }
}