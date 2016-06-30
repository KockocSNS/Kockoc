package com.kocapplication.pixeleye.kockocapp.navigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;

/**
 * Created by pixeleye02 on 2016-06-30.
 */
public class SettingActivity extends BaseActivityWithoutNav {
    private Button passwordChange;
    private Button logoutButton;
    private Button serviceDropOutButton;
    private Switch autoLoginSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("설정", Color.WHITE);

        container.setLayoutResource(R.layout.activity_setting);
        View containView = container.inflate();

        passwordChange = (Button) containView.findViewById(R.id.pass_change);
        logoutButton = (Button) containView.findViewById(R.id.logout_button);
        serviceDropOutButton = (Button) containView.findViewById(R.id.service_drop_out_button);
        autoLoginSet = (Switch) containView.findViewById(R.id.auto_login_set);

        listenerset();
    }

    private void listenerset(){
        ButtonClickListener buttonClickListener = new ButtonClickListener();
        passwordChange.setOnClickListener(buttonClickListener);
        logoutButton.setOnClickListener(buttonClickListener);
        serviceDropOutButton.setOnClickListener(buttonClickListener);
    }

    private class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.equals(passwordChange)){
                Log.e("SET","AA");
                Intent passchange_intent = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                startActivity(passchange_intent);
            } else if (v.equals(logoutButton)){
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                    }
                });
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.putExtra("logout", 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                try{
                    LoginManager.getInstance().logOut();
                } catch(Exception e){
                    e.printStackTrace();
                }

            } else if (v.equals(serviceDropOutButton)){

            }
        }
    }
}

