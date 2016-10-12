package com.kocapplication.pixeleye.kockocapp.navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;


import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.CustomAlertDialog;

/**
 * Created by pixeleye02 on 2016-06-30.
 */
public class SettingActivity extends BaseActivityWithoutNav {
    private Button passwordChange;
    private Button nicknameChange;
    private Button serviceDropOutButton;
    private Switch autoLoginSet;
    private Switch alarmSet;
    private Button clearCache;
    private RadioGroup radioGroup;
    private RadioButton timeSetting1, timeSetting2, timeSetting3;
    private int settingTime;
    private boolean alarmSettingState;


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
        clearCache = (Button) containView.findViewById(R.id.glide_cache_clear_btn);
        alarmSet = (Switch) containView.findViewById(R.id.alarm_set);
        radioGroup = (RadioGroup) containView.findViewById(R.id.radiogroup);
        timeSetting1 = (RadioButton) containView.findViewById(R.id.setting_time_1);
        timeSetting2 = (RadioButton) containView.findViewById(R.id.setting_time_2);
        timeSetting3 = (RadioButton) containView.findViewById(R.id.setting_time_3);

        settingTime = 0;//알람시간 0으로 초기화

        listenerSet();

        loadPreviousSetting();

        saveCurrentSetting();
    }
    private void saveCurrentSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences("settingState",MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("settingTime",settingTime);
        editor.commit();
    }

    private void loadPreviousSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences("settingState",MODE_MULTI_PROCESS);
        settingTime = sharedPreferences.getInt("settingTime",0);
        alarmSettingState = sharedPreferences.getBoolean("alarmState",false);

        Log.d("settingTimeTest",String.valueOf(alarmSettingState));
        switch (settingTime) {
            case 600000:
                radioGroup.check(R.id.setting_time_1);
                break;
            case 1800000:
                radioGroup.check(R.id.setting_time_2);
                break;
            case 3600000:
                radioGroup.check(R.id.setting_time_3);
                break;
            default:
                radioGroup.clearCheck();
                break;
        }
        if (alarmSettingState) {
            alarmSet.setChecked(true);
        } else {
            alarmSet.setChecked(false);
        }

    }


    private void listenerSet() {
        ButtonClickListener buttonClickListener = new ButtonClickListener();
        passwordChange.setOnClickListener(buttonClickListener);
        serviceDropOutButton.setOnClickListener(buttonClickListener);
        nicknameChange.setOnClickListener(new ChangeNicknameClickListener());
        clearCache.setOnClickListener(buttonClickListener);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.setting_time_1)
                    settingTime = 600000;
                else if (checkedId == R.id.setting_time_2)
                    settingTime = 1800000;
                else if (checkedId == R.id.setting_time_3)
                    settingTime = 3600000;
            }
        });
        alarmSet.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            SharedPreferences sharedPreferences = getSharedPreferences("settingState",MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (settingTime == 0) {
                        Toast.makeText(getApplicationContext(), "알람 시간을 정해주세요", Toast.LENGTH_SHORT).show();
                        alarmSet.setChecked(false);
                    } else {
                        saveCurrentSetting();
                        editor.putBoolean("alarmState",true);
                        Log.d("settingBoolean",String.valueOf(sharedPreferences.getBoolean("alarmState",false)));
                    }
                } else {
                    editor.putBoolean("alarmState",false);
                    editor.putInt("settingTime",0);
                }
                editor.commit();

            }
        });
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(passwordChange)) {
                Intent passchange_intent = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                startActivity(passchange_intent);
            } else if (v.equals(serviceDropOutButton)) {
                new CustomAlertDialog(SettingActivity.this, "계정을 삭제하시겠습니까?", new DialogButtonListener());
            } else if (v.equals(clearCache)) {
                new ClearCacheDialog(SettingActivity.this, "캐시를 삭제하시겠습니까?");
            }
        }
    }

    private class ChangeNicknameClickListener implements View.OnClickListener {
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
                setResult(RESULT_OK);
                finish();
            }
        }
    }


}

