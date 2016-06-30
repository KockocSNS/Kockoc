package com.kocapplication.pixeleye.kockocapp.write.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Han_ on 2016-06-30.
 */
public class AlarmDialogActivity extends AppCompatActivity {
    private TextView courseTitle;
    private TextView alarmTime;
    private TextView alarmDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //title을 없앴다.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_alarm_dialog);
        init();

        Intent intent = getIntent();
//        courseTitle.setText(intent.getStringExtra());
//        alarmTime.setText(intent.getStringExtra());
//        alarmDetail.setText(intent.getStringExtra());



    }

    private void init() {
        courseTitle = (TextView) findViewById(R.id.course_title);
        alarmTime = (TextView) findViewById(R.id.course_alarm_time);
        alarmDetail = (TextView) findViewById(R.id.course_alarm_detail);

        ImageButton close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
