package com.kocapplication.pixeleye.kockocapp.main.myKockoc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.ProfileSend;
import com.kocapplication.pixeleye.kockocapp.util.Thumbnail;

/**
 * Created by hp on 2016-06-28.
 */
public class MyProfileImgThread extends Thread {
    final static String TAG = "MyProfileImgThread";
    Context mContext;
    Handler handler;
    Intent data;

    public MyProfileImgThread(Handler handler, Intent data, Context mContext) {
        this.handler = handler;
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public void run() {
        super.run();
        try {
            //Uri에서 이미지 경로를 얻어온다.
            Uri selPhotoUri = data.getData();

            Cursor c = mContext.getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
            c.moveToNext();
            int column_index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            c.moveToFirst();

            String temp = c.getString(column_index);
            String imgName = temp.substring(temp.lastIndexOf("/") + 1);
            String imgPath = temp.replaceAll(imgName, ""); // 이미지 폴더 경로

            Log.i(TAG, imgPath + imgName);

            String thumbProfile = Thumbnail.profileImgThum(imgPath, imgName);
            //ProfileSend 에서 ftp로 서버에 이미지 전송
            ProfileSend profileSend = new ProfileSend(BasicValue.getInstance().getUserNo(), thumbProfile);
            profileSend.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
    }
}
