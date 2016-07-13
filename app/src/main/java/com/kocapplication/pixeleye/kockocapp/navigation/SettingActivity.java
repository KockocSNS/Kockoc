package com.kocapplication.pixeleye.kockocapp.navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;


import com.facebook.login.LoginManager;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.CustomAlertDialog;

/**
 * Created by pixeleye02 on 2016-06-30.
 */
public class SettingActivity extends BaseActivityWithoutNav {
    private Button passwordChange;
    private Button nicknameChange;
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
        nicknameChange = (Button) containView.findViewById(R.id.nick_change);
        serviceDropOutButton = (Button) containView.findViewById(R.id.service_drop_out_button);
        autoLoginSet = (Switch) containView.findViewById(R.id.auto_login_set);

        listenerSet();
    }

    private void listenerSet() {
        ButtonClickListener buttonClickListener = new ButtonClickListener();
        passwordChange.setOnClickListener(buttonClickListener);
        serviceDropOutButton.setOnClickListener(buttonClickListener);
        nicknameChange.setOnClickListener(new ChangeNicknameClickListener());
    }


    private class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.equals(passwordChange)){
                Intent passchange_intent = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                startActivity(passchange_intent);
            } else if (v.equals(serviceDropOutButton)){
                new CustomAlertDialog(SettingActivity.this, "계정을 삭제하시겠습니까?", new DialogButtonListener());
            }
        }
    }
    private class ChangeNicknameClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent nickchange_intent = new Intent(SettingActivity.this, NicknameChangeActivity.class);
            startActivity(nickchange_intent);
        }
    }
    private class DialogButtonListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                new UserDeleteThread(new DialogHandler(), BasicValue.getInstance().getUserNo()).start();
            }
        }
    }

    private class DialogHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                Snackbar.make(serviceDropOutButton, "탈퇴 되었습니다.", Snackbar.LENGTH_SHORT).show();
                BasicValue.getInstance().setUserNo(-1);
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.putExtra("logout", 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                try{
                    LoginManager.getInstance().logOut();
                } catch(Exception e){
                    e.printStackTrace();
                }
                finish();
//                setResult(RESULT_OK);
            }
        }
    }
}

