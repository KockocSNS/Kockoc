package com.kocapplication.pixeleye.kockocapp.util.GCM;

/**
 * Created by pixeleye03 on 2016-04-22.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.kocapplication.pixeleye.kockocapp.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = "KocKoc";
        String message = data.getString("msg");
        int boardNo = 0;
        int courseNo = 0;
        try {
            message = URLDecoder.decode(message, "UTF-8");
            boardNo = Integer.parseInt(message.substring(message.indexOf("|")+1,message.indexOf("&"))); // 메세지에서 보드넘버 추출
            courseNo = Integer.parseInt(message.substring(message.indexOf("&")+1));
            message = message.substring(0,message.indexOf("|")); // 메세지에서 보드넘버 제거

            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Title: " + title);
            Log.d(TAG, "Message: " + message);
            Log.d(TAG, "boardNo: " + boardNo);
            Log.d(TAG, "courseNo: " + courseNo);

        }catch(UnsupportedEncodingException e){
            Log.e(TAG,"인코딩 오류");
            e.printStackTrace();
        }

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(title, message,boardNo,courseNo);
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message, int boardNo, int courseNo) {
        int id = (int) (Math.random()*10)+1; // id값
// TODO: 2016-06-21 gcm 링크 구현
//        Intent intent = new Intent(this, IntroActivity.class);
//        intent.putExtra("gcmBoardNo",boardNo);
//        intent.putExtra("gcmCourseNo",courseNo);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.app_icon)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
//        Vibrator vibrator =(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(1000); // 1초간 진동
    }

}