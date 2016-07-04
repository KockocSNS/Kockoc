package com.kocapplication.pixeleye.kockocapp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Han_ on 2016-07-04.
 */
public class CustomAlertDialog {

    public CustomAlertDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("알림!")
                .setMessage(message)
                .setPositiveButton("예", listener)
                .setNegativeButton("아니오", listener)
                .create();

        dialog.show();
    }
}
