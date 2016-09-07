package com.kocapplication.pixeleye.kockocapp.navigation;

import android.content.Context;

import com.bumptech.glide.Glide;

/**
 * Created by pixeleye02 on 2016-07-26.
 */
public class ClearCacheThread extends Thread {

    private Context context;

    public ClearCacheThread(Context context) {
        super();
        this.context = context;

    }

    @Override
    public void run(){

        Glide.get(context).clearDiskCache();
    }
}
