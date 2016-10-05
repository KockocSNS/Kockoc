package com.kocapplication.pixeleye.kockocapp.intro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;

import java.security.MessageDigest;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }

        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 3G나 LTE등 데이터 네트워크에 연결된 상태
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // 와이파이에 연결된 상태

        try {
            autoLogin();
        } catch (Exception e) {
            Log.e("intro", "intro network not connect");
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("안내");
            dialog.setMessage("인터넷 연결상태를 확인해주세요");
            dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.show();
            e.printStackTrace();
        }

//        if (wifi.isConnected() || mobile.isConnected()) { // 와이파이에 연결된 경우
//            autoLogin();
//        } else { // 인터넷에 연결되지 않은 경우
//            Log.e("intro", "intro network not connect");
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setTitle("안내");
//            dialog.setMessage("인터넷 연결상태를 확인해주세요");
//            dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//            dialog.show();
//        }
    }

    private void autoLogin() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        if (pref.getInt("login", -1) != -1) {
            int login = pref.getInt("login", -1);
            if (login != -1) {
                BasicValue.getInstance().setUserNo(login);

                Intent intent = new Intent(this, MainActivity.class);
                Uri uri = getIntent().getData();
                try {
                    intent.putExtra("kakaoLinkBoardNo", Integer.parseInt(uri.getQueryParameter("boardNo")));
                    intent.putExtra("kakaoLinkCourseNo", Integer.parseInt(uri.getQueryParameter("courseNo")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        } else {
            initialize();
        }
    }

    private void initialize() {
        Handler handler = new Handler() {   //introActivity배경사진의 시간을 늘려줌
            @Override
            public void handleMessage(Message msg) {
                finish();
                Intent login_intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(login_intent);
            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
