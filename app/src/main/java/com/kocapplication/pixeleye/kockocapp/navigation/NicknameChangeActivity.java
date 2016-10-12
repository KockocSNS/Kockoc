package com.kocapplication.pixeleye.kockocapp.navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Setting.JspConn_ChangeNickname;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

import java.io.IOException;

/**
 * Created by pixeleye03 on 2016-07-04.
 */
public class NicknameChangeActivity extends BaseActivityWithoutNav {
    final static String TAG = "NicknameChangeActivity";
    EditText et_nickname;
    Button btn_chkDup;
    Button btn_confirm;
    View containView;

    String nickname;
    boolean nicknamechk = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        actionBarTitleSet("프로필 편집", Color.WHITE);

        container.setLayoutResource(R.layout.activity_nickname_change);
        containView = container.inflate();
        getComponent();
    }

    private void getComponent(){
        et_nickname = (EditText)containView.findViewById(R.id.et_nickname);
        btn_chkDup = (Button) containView.findViewById(R.id.btn_nickname_change_dup);
        btn_confirm = (Button)containView.findViewById(R.id.btn_nickname_change_confirm);

        btn_chkDup.setOnClickListener(new ChkDupClickListener());
        btn_confirm.setOnClickListener(new ConfirmClickListener());

        TextWatcher TWnickname = new TextWatcher() {// 닉네임 변화
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {nicknamechk =false;}
        };
        et_nickname.addTextChangedListener(TWnickname);
    }

    private class ChkDupClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            nickname = et_nickname.getText().toString();
            if (nickname.length() > 0 && JspConn.checkDuplNickname(nickname)==true){
                Toast.makeText(NicknameChangeActivity.this, "사용하셔도 좋습니다.", Toast.LENGTH_SHORT).show();
                nicknamechk = true;
            }
            else{
                Toast.makeText(NicknameChangeActivity.this, "이미 있는 닉네임 입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class ConfirmClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            nickname = et_nickname.getText().toString();
            if(nicknamechk){
                try {
                    JspConn_ChangeNickname.changeNickname(nickname);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(NicknameChangeActivity.this, "변경되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}
