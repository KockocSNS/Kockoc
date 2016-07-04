package com.kocapplication.pixeleye.kockocapp.navigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;

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

        listenerset();
    }

    private void listenerset(){
        passwordChange.setOnClickListener(new ChangePwdClickListener());
        nicknameChange.setOnClickListener(new ChangeNicknameClickListener());
    }


    private class ChangePwdClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent passchange_intent = new Intent(SettingActivity.this, PasswordChangeActivity.class);
            startActivity(passchange_intent);
        }
    }
    private class ChangeNicknameClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent nickchange_intent = new Intent(SettingActivity.this, NicknameChangeActivity.class);
            startActivity(nickchange_intent);
        }
    }
}

