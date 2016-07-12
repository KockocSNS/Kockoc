package com.kocapplication.pixeleye.kockocapp.write.course;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Han on 2016-07-11.
 */
public class MemoDialog {
    private AlertDialog dialog;

    private CourseWriteActivity activity;
    private EditText messageEditText;
    private int courseNo;
    private boolean isUpdate = false;

    public MemoDialog(CourseWriteActivity activity, int courseNo, String memo) {
        this.activity = activity;

        isUpdate = memo.equals("") ? false : true;

        this.messageEditText = new EditText(activity);
        this.messageEditText.setText(memo);
        messageEditText.setSelection(memo.length());
        this.courseNo = courseNo;

        dialog = new AlertDialog.Builder(activity)
                .setTitle("메모")
                .setMessage("메모를 입력하세요.")
                .setView(messageEditText)
                .setPositiveButton("예", new ButtonListener())
                .setNegativeButton("아니오", new ButtonListener())
                .create();

        dialog.show();
    }

    private class ButtonListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                String message = messageEditText.getText().toString();

                if (message.equals("")) {
                    Toast.makeText(activity, "메모를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("MEMODIALOG", courseNo + "");
                new MemoWriteThread(message, courseNo).start();
                activity.setMemo(message);
            }
        }
    }
}
