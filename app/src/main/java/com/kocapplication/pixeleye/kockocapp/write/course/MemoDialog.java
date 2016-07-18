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
    private String tempMemo="";


    public MemoDialog(CourseWriteActivity activity, int courseNo, String memo) {
        this.activity = activity;
        this.messageEditText = new EditText(activity);
        this.courseNo = courseNo;

        // memo 내용 값이 없다 => 코스 생성 후 처음 메모 클릭, memo 내용 값이 있다 => 코스 생성 후 처음 메모를 클릭한 것이 아님
        if(memo=="") {
            dialog = new AlertDialog.Builder(activity)
                    .setTitle("메모")
                    .setMessage("메모를 입력하세요.")
                    .setView(messageEditText)
                    .setPositiveButton("예", new ButtonListener())
                    .setNegativeButton("아니오", new ButtonListener())
                    .create();
        }
        else if(memo!=""){
            dialog = new AlertDialog.Builder(activity)
                    .setTitle("메모")
                    .setMessage("메모를 입력하세요.")
                    .setView(messageEditText)
                    .setMessage(memo)
                    .setPositiveButton("예", new ButtonListener())
                    .setNegativeButton("아니오", new ButtonListener())
                    .create();
        }

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

                Log.i("MEMODIALOG", ""+MemoWriteThread.resultcourseNo); // courseNO 확인
                new MemoWriteThread(message, MemoWriteThread.resultcourseNo).start();
                activity.setMemo(message);

            }
        }
    }
}