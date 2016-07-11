package com.kocapplication.pixeleye.kockocapp.write.course;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

/**
 * Created by Han on 2016-07-11.
 */
public class MemoDialog {
    private AlertDialog dialog;

    public MemoDialog(Context context) {
        dialog = new AlertDialog.Builder(context)
                .setTitle("메모")
                .setMessage("메모를 입력하세요.")
                .setView(new EditText(context))
                .setPositiveButton("예", new ButtonListener())
                .setNegativeButton("아니오", new ButtonListener())
                .create();

        dialog.show();
    }

    private class ButtonListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }
}
