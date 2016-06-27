package com.kocapplication.pixeleye.kockocapp.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class IntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    private void initialize() {
        Handler handler = new Handler() {   //introActivity배경사진의 시간을 늘려줌
            @Override
            public void handleMessage(Message msg) {
                finish();
                Intent login_intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(login_intent);
            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }
}
