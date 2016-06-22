package com.kocapplication.pixeleye.kockocapp.util;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Han_ on 2016-06-21.
 */
public class DBConnect {
    private final String TAG = "DB_CONNECT";
    private HttpURLConnection connection;

    private void connect(String postUrl) {
        try {
            URL url = new URL(postUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDefaultUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

        } catch (IOException e) {
            Log.e(TAG, "Connection Error");
        }
    }

    public String post(String postUrl, String data) {
        String receive = "";

        connect(postUrl);

        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            pw.write(data);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            Log.e(TAG, "Data Send Error");
        }

        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder string = new StringBuilder();
            String line;

            while ((line = bf.readLine()) != null) {
                string.append(line);
            }
            receive = string.toString();

        } catch (Exception e) {
            Log.d(TAG, "Receive POST Error");
            e.printStackTrace();
        }

        return receive;
    }

    public String get(String postUrl) {
        String receive = "";

        connect(postUrl);

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
            BufferedReader bf = new BufferedReader(inputStreamReader);
            StringBuilder string = new StringBuilder();
            String line;

            while ((line = bf.readLine()) != null) {
                string.append(line);
            }
            receive = string.toString();

        } catch (Exception e) {
            Log.d(TAG, "Receive GET Error");
            e.printStackTrace();
        }

        return receive;
    }
}
