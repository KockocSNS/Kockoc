package com.kocapplication.pixeleye.kockocapp.detail.share;

import android.content.Intent;

/**
 * Created by Han on 2016-07-08.
 */
public interface ShareReceiveInterface {
    void shareText(Intent intent);

    void shareImage(Intent intent);

    void shareMultipleImage(Intent intent);
}
