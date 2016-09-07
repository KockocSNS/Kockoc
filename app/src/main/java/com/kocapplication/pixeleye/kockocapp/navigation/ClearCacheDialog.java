package com.kocapplication.pixeleye.kockocapp.navigation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by pixeleye02 on 2016-07-25.
 */
public class ClearCacheDialog {

    private Context context;

    public ClearCacheDialog(Context context, String message) {
       this.context = context;
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("알림!")
                .setMessage(message)
                .setPositiveButton("예", new ButtonListener())
                .setNegativeButton("아니오", new ButtonListener())
                .create();

        dialog.show();
    }

    private class ButtonListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which== DialogInterface.BUTTON_POSITIVE)
                new ClearCacheThread(context).start();
        }
    }
}
