package com.kocapplication.pixeleye.kockocapp.navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;

import java.util.zip.Inflater;

/**
 * Created by pixeleye02 on 2016-06-30.
 */
public class NoticeActivity extends BaseActivityWithoutNav {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("알림", Color.WHITE);

        container.setLayoutResource(R.layout.activity_setting);
        View containView = container.inflate();
    }
}
