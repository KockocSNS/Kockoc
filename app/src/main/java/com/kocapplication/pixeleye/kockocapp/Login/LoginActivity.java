package com.kocapplication.pixeleye.kockocapp.Login;

import android.content.Intent;
import android.os.Bundle;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.kocapplication.pixeleye.kockocapp.Kakao.KakaoSignupActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;

/**
 * Created by hp on 2016-06-21.
 */
public class LoginActivity extends BaseActivity {
    final static String TAG = "LoginActivity";

//    <com.kakao.usermgmt.LoginButton
//    android:id="@+id/com_kakao_login"
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:layout_gravity="bottom"
//    android:layout_marginBottom="30dp"
//    android:layout_marginLeft="20dp"
//    android:layout_marginRight="20dp"/>
    //카카오 버튼

    private SessionCallback callback;      //콜백 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// TODO: 2016-06-21 로그인 xml 만들고 카카오 버튼 추가하기
//        setContentView(R.layout.activity_login);

        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
