package com.kocapplication.pixeleye.kockocapp.util.GCM;

/**
 * Created by Hyeongpil on 2016-04-22.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyFcmListenerService";

    // TODO: 2016-10-27 fcm 보내는 부분에서 map 형태로 보내고 키,밸류로 값을 받아오도록 해야한다
    @Override
    public void onMessageReceived(RemoteMessage fcm_message) {
        super.onMessageReceived(fcm_message);
        String from = fcm_message.getFrom();
        Map data = fcm_message.getData();
        Log.e(TAG,from);
        try {
            String msg = URLDecoder.decode(data.get("msg").toString(), "UTF-8");
            int boardNo = Integer.parseInt(msg.substring(msg.indexOf("|")+1,msg.indexOf("&"))); // 메세지에서 보드넘버 추출
            int courseNo = Integer.parseInt(msg.substring(msg.indexOf("&")+1));
            String message = msg.substring(0,msg.indexOf("|")); // 메세지에서 보드넘버 제거

            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Message: " + message);
            Log.d(TAG, "boardNo: " + boardNo);
            Log.d(TAG, "courseNo: " + courseNo);
            // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
            sendNotification("Kockoc", message,boardNo,courseNo);
        }catch(UnsupportedEncodingException e){
            Log.e(TAG,"인코딩 오류");
            e.printStackTrace();
        }
    }

    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message, int boardNo, int courseNo) {
        int id = (int) (Math.random()*10)+1; // id값
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("intentValue",2);
        intent.putExtra("gcmBoardNo",boardNo);
        intent.putExtra("gcmCourseNo",courseNo);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

        Vibrator vibrator =(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000); // 1초간 진동
    }

}