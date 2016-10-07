package com.kocapplication.pixeleye.kockocapp.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.login.Kakao.KakaoSignupActivity;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.model.User;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

/**
 * Created by Han_ on 2016-04-01.
 * Edited by hp on 2016-06-29.
 */
// TODO: 2016-06-29 로그인 시 BasicValue에 이름값 넣어야함
public class LoginActivity extends AppCompatActivity {
    final static String TAG = "LoginActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private EditText idText;
    private EditText pwText;

    private CheckBox autoLogin;
    private boolean autoLoginState = false;

    private LoginButton loginButton;
    private LoginButton facebookButton;
    private LoginButton naverButton;
    private LoginButton kakaoButton;

    private Button signUpButton;

    private com.facebook.login.widget.LoginButton facebookLogin;
    private com.kakao.usermgmt.LoginButton kakaoLogin;
    private com.nhn.android.naverlogin.ui.view.OAuthLoginButton naverLogin;

    //facebook
    private CallbackManager callbackManager;
    private AccessToken facebookToken;
    //kakao
    private SessionCallback callback;
    //naver
    private OAuthLogin oAuthLogin;



    int KakaoLinkBoardNo;
    int KakaoLinkCourseNo;

    int intentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        init();


        kakaoCallBack();
//        facebookCallBack();
        autoLoginNaverAndFacebook();
    }

    public void autoLoginNaverAndFacebook () {
        SharedPreferences naverLoginState = getSharedPreferences("naverLoginState",MODE_PRIVATE);
        Boolean loginStateByNaver = naverLoginState.getBoolean("isNaverLogin",false);
        SharedPreferences facebookLoginState = getSharedPreferences("facebookLoginState",MODE_PRIVATE);
        Boolean loginStateByFacebook = facebookLoginState.getBoolean("isFacebookLogin",false);

        if (loginStateByNaver == true){

            Log.i("login","in");
            View v=naverButton;
            LoginListener loginListener = new LoginListener();
            loginListener.onClick(v);
            finish();
        } else if (loginStateByFacebook == true) {
            facebookCallBack();
            facebookButton.performClick();
        }
        else { return;}
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Intent intent = getIntent();
            intentValue = intent.getIntExtra("intentValue", 0);
            if (intentValue == 1) { // 카카오 intent
                KakaoLinkBoardNo = intent.getIntExtra("kakaoLinkBoardNo", 0);
                KakaoLinkCourseNo = intent.getIntExtra("kakaoLinkCourseNo", 0);
            } else if (intentValue == 2) { // gcm intent
                KakaoLinkBoardNo = intent.getIntExtra("gcmBoardNo", 0);
                KakaoLinkCourseNo = intent.getIntExtra("gcmCourseNo", 0);
            }
        } catch (Exception e) {
            Log.d(TAG, "getIntent 값 없음");
        }

        try {
            if (getIntent().getIntExtra("logout", 0) == 0) {
                Handler handler = new LoginHandler();
                Log.i("logout","out");
                Thread thread = new LoginThread(getApplicationContext(), handler, "-1", "", false);
                thread.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        facebookToken = AccessToken.getCurrentAccessToken();
        if (facebookToken != null) {
            Log.i("facebook token user id", facebookToken.getUserId());
        }
    }

    private void init() {
        idText = (EditText) findViewById(R.id.id_input);
        pwText = (EditText) findViewById(R.id.pw_input);

        autoLogin = (CheckBox) findViewById(R.id.auto_login_check_box);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        facebookButton = (LoginButton) findViewById(R.id.facebook_login_button_temp);
        naverButton = (LoginButton) findViewById(R.id.naver_login_button_temp);
        kakaoButton = (LoginButton) findViewById(R.id.kakao_login_button_temp);

        signUpButton = (Button) findViewById(R.id.signup_button);

        loginButton.setOnClickListener(new LoginListener());
        facebookButton.setOnClickListener(new LoginListener());
        naverButton.setOnClickListener(new LoginListener());
        kakaoButton.setOnClickListener(new LoginListener());
        signUpButton.setOnClickListener(new LoginListener());

        pwText.setOnEditorActionListener(new EditTextListener());

        autoLogin.setOnCheckedChangeListener(new CheckBoxListener());

        facebookLogin = (com.facebook.login.widget.LoginButton) findViewById(R.id.facebook_login_button);
        kakaoLogin = (com.kakao.usermgmt.LoginButton) findViewById(R.id.kakao_login_button);
        naverLogin = (OAuthLoginButton) findViewById(R.id.naver_login_button);

        oAuthLogin = OAuthLogin.getInstance();
        oAuthLogin.init(LoginActivity.this, "DJy3asjXdzqH_xK5WNt4", "QEpiUBFAQb", "KocKoc");



    }

    private void kakaoCallBack() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void facebookCallBack() {
        facebookLogin.registerCallback(callbackManager, new com.facebook.FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook Login", "Success");

                SharedPreferences facebookLoginState = getSharedPreferences("facebookLoginState", MODE_PRIVATE);
                SharedPreferences.Editor editor = facebookLoginState.edit();
                editor.putBoolean("isFacebookLogin", true);
                editor.putString("AccessToken",facebookToken.getToken());
                editor.commit();

                if (AccessToken.getCurrentAccessToken()!= null) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new FacebookCallbackGraph());
                        Bundle parameter = new Bundle();
                        parameter.putString("fields", "id,name,birthday,email,gender");
                        request.setParameters(parameter);
                        request.executeAsync();
                        finish();
                }
            }

            @Override
            public void onCancel() {
                Log.d("Facebook Login", "Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook Login", "Error");
                error.printStackTrace();
            }
        });
    }

    private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(loginButton)) {
                String id = idText.getText().toString();
                String pw = pwText.getText().toString();

                if (!id.equals("") && !pw.equals("")) {
                    Handler handler = new LoginHandler();
                    Thread thread = new LoginThread(getApplicationContext(), handler, id, pw, autoLoginState);
                    thread.start();
                } else
                    Toast.makeText(getApplicationContext(), "빈칸을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (v.equals(facebookButton)) {
//                Toast.makeText(getApplicationContext(), "준비중입니다", Toast.LENGTH_SHORT).show();
//                facebookLogin = (com.facebook.login.widget.LoginButton)findViewById(R.id.facebook_login_button);
                facebookLogin.performClick();
            } else if (v.equals(naverButton))
                oAuthLogin.startOauthLoginActivity(LoginActivity.this, new NaverLoginHandler());
            else if (v.equals(kakaoButton)) kakaoLogin.performClick();
            else if (v.equals(signUpButton)) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                finish();
            }


            //soft keyboard hide
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private class EditTextListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (v.equals(pwText)) {
                loginButton.performClick();
            }
            return false;
        }
    }

    private class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isChecked()) autoLoginState = true;
            else autoLoginState = false;
        }
    }

    private class FacebookCallbackGraph implements GraphRequest.GraphJSONObjectCallback {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            User userData;
            try {
                userData = new User(object.getString("name"), object.getString("name"), "", object.getString("id"), "", "", object.getString("gender"));
                Handler handler = new LoginHandler();

                Log.i("userData",userData.e_mail);
                Log.i("userData",userData.name);
                Log.i("userData",userData.gender);


                if (!JspConn.checkDuplID(userData.e_mail)) {
//                    // Email이 이미 존재하는 경우
//                    getInstanceIdToken();
                    Log.d("facebook_login join","emaillogin");
                    Thread thread = new NoPwdLoginThread(getApplicationContext(), handler, userData.e_mail);
                    thread.start();
                    finish();
                } else {
                   // Email이 없는 경우이므로 회원가입
                    Log.d("facebook login join","join");
                    Intent intent = new Intent(LoginActivity.this, GetExtraInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", userData);
                    intent.putExtra("user", bundle);
                    intent.putExtra("flag", "facebook");
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            // Session Open Success
            Intent intent = new Intent(LoginActivity.this, KakaoSignupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            getInstanceIdToken();
            if (KakaoLinkBoardNo != 0) {
                intent.putExtra("boardNo", KakaoLinkBoardNo);
                intent.putExtra("courseNo", KakaoLinkCourseNo);
            }
            startActivity(intent);
            finish();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // Session Open Fail
            Log.e("Kakao Session Open", "Fail");
            if (exception != null) {
                exception.printStackTrace();
            }

        }
    }

    private class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
//                getInstanceIdToken(); // gcm 토큰값 가져오기
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                if (KakaoLinkBoardNo != 0) {
                    intent.putExtra("boardNo", KakaoLinkBoardNo);
                    intent.putExtra("courseNo", KakaoLinkCourseNo);
                    startActivity(intent);
                } else {
                    startActivity(intent);
                }
            }
        }
    }

    private class NaverHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Handler handler = new LoginHandler();
            User userData = (User) msg.getData().get("Message");
            if (!JspConn.checkDuplID(userData.e_mail)) {
//                getInstanceIdToken();
                Thread thread = new NoPwdLoginThread(getApplicationContext(), handler, userData.e_mail);
                thread.start();
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, GetExtraInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userData);
                intent.putExtra("user", bundle);
                intent.putExtra("flag", "naver");
                startActivity(intent);
                finish();
            }
        }
    }


    private class NaverLoginHandler extends OAuthLoginHandler {
        @Override
        public void run(boolean b) {
            if (b) {
                String accessToken = oAuthLogin.getAccessToken(getApplicationContext());
                String refreshToken = oAuthLogin.getRefreshToken(getApplicationContext());
                long expiresAt = oAuthLogin.getExpiresAt(getApplicationContext());
                String tokenType = oAuthLogin.getTokenType(getApplicationContext());

                SharedPreferences naverLoginState = getSharedPreferences("naverLoginState",MODE_PRIVATE);
                SharedPreferences.Editor editor = naverLoginState.edit();
                editor.putBoolean("isNaverLogin", true);
                editor.commit();

                Handler handler = new NaverHandler();
                Log.i("changePwd token", accessToken + " / " + refreshToken + " / " + expiresAt + " / " + tokenType);

                Thread thread = new NaverUserInfoGetThread(getApplicationContext(), handler, oAuthLogin, accessToken);
                thread.start();
                Log.i("naverauto","able");



            } else {
                String errorCode = oAuthLogin.getLastErrorCode(getApplicationContext()).getCode();
                String errorDesc = oAuthLogin.getLastErrorDesc(getApplicationContext());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) return;
        super.onActivityResult(requestCode, resultCode, data);
    }

//    /**
//     * getInstanceIdToken
//     * Gcm Token값 DB에 저장
//     */
//    public void getInstanceIdToken() {
//        if (checkPlayServices(this)) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//        }
//    }
//
//    private boolean checkPlayServices(Context context) { // gcm 사용을 위해서는 구글 플레이 서비스가 있어야 한다.
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.i("LoginActivityTest", "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
}