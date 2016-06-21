package com.kocapplication.pixeleye.kockocapp.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;

/**
 * Created by Han_ on 2016-06-20.
 */
public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("테스틋틋틋!");
    }
}