package com.kocapplication.pixeleye.kockocapp.login.Kakao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.login.GetExtraInfoActivity;
import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.model.User;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

/**
 * Created by hp on 2016-06-21.
 */
public class KakaoSignupActivity extends Activity {
    final static String TAG = "KakaoSignupActivity";
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    public static String kakaoimg;
    int getBoardNo =0;
    int getCourseNo =0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        getBoardNo = intent.getIntExtra("boardNo",0);
        getCourseNo = intent.getIntExtra("courseNo",0);
        requestMe();
    }
    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp(){} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {
                String kakaoID = String.valueOf(userProfile.getId());
                String kakaoNickname = userProfile.getNickname();
                kakaoimg = userProfile.getProfileImagePath();

                Logger.d("UserProfile : " + userProfile);
                int userNo = JspConn.kakaoCheck(kakaoID, kakaoNickname);
                Log.e(TAG,"userNo :"+userNo);
                if(userNo>0){
                    BasicValue.getInstance().setUserNo(userNo);
                    redirectMainActivity();
                } else if(userNo == -1){
//                    JspConn.kakaoRecordUser(kakaoID,kakaoNickname);
//                    userNo = JspConn.kakaoCheck(kakaoID, kakaoNickname);
//                    BasicValue.getInstance().setUserNo(userNo);
                    redirectGetUserActivity(kakaoID);
                }else {redirectGetUserActivity(kakaoID);}
            }
        });
    }

    private void redirectMainActivity() {
        // TODO: 2016-06-29 카카오링크 추가해야함
        startActivity(new Intent(this, MainActivity.class));
        if(getBoardNo != 0) {
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra("boardNo", getBoardNo);
            detailIntent.putExtra("courseNo", getCourseNo);
            startActivity(detailIntent);
        }
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    protected void redirectGetUserActivity(String name) {
        Intent intent = new Intent(this, GetExtraInfoActivity.class);

        User user = new User();
        user.setUserEmail(name);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);

        intent.putExtra("user",bundle);
        intent.putExtra("flag","kakao");
        startActivity(intent);
        finish();
    }
}
