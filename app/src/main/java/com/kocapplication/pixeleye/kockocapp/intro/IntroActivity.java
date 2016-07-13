package com.kocapplication.pixeleye.kockocapp.intro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import java.security.MessageDigest;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class IntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    private void autologin(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        if (pref.getInt("login", -1) != -1) {
            int login = pref.getInt("login", -1);
            if (login != -1){
                BasicValue.getInstance().setUserNo(login);

                // TODO: 2016-06-30 이 부분에 카카오 링크랑 gcm링크 구현
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
        else{
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
        autologin();
    }
}
