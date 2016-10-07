package com.kocapplication.pixeleye.kockocapp.navigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Setting.JspConn_ChangePwd;

/**
 * Created by pixeleye02 on 2016-06-30.
 */
public class PasswordChangeActivity extends BaseActivityWithoutNav {
    private EditText existingPassword;
    private EditText newPassword;
    private EditText confirmPassword;

    private Button passSubmit;


    private String existingPass;
    private String newPass;
    private String confirmPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("비밀번호 변경", Color.WHITE);

        container.setLayoutResource(R.layout.acitivity_password_change);
        View containView = container.inflate();

        existingPassword = (EditText) containView.findViewById(R.id.existing_password);
        newPassword = (EditText) containView.findViewById(R.id.new_password);
        confirmPassword = (EditText) containView.findViewById(R.id.confirm_password);

        passSubmit = (Button) containView.findViewById(R.id.pass_submit);

//        listener();

        passSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingPass = existingPassword.getText().toString();
                newPass = newPassword.getText().toString();
                confirmPass = confirmPassword.getText().toString();

                if(newPass.equals(confirmPass)){
                    Toast.makeText(PasswordChangeActivity.this,"새로운 비밀번호로 다시 로그인 해주세요",Toast.LENGTH_LONG).show();
                    JspConn_ChangePwd.changePwd(existingPass, newPass);
                    UserManagement.requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                        }
                    });
                    Intent intent = new Intent(PasswordChangeActivity.this, LoginActivity.class);
                    intent.putExtra("logout", 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    try{
                        LoginManager.getInstance().logOut();
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(PasswordChangeActivity.this,"비밀번호가 맞지않거나 새로운 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(newPass.equals(confirmPass)){
                Toast.makeText(PasswordChangeActivity.this,"같다",Toast.LENGTH_SHORT).show();
                JspConn_ChangePwd.changePwd(existingPass, newPass);
            } else{
                Toast.makeText(PasswordChangeActivity.this,"다르다",Toast.LENGTH_SHORT).show();
            }


        }
    }
}
