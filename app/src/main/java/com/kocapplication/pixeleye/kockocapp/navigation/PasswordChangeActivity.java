package com.kocapplication.pixeleye.kockocapp.navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;

/**
 * Created by pixeleye02 on 2016-06-30.
 */
public class PasswordChangeActivity extends BaseActivityWithoutNav {
    private EditText existingPassword;
    private EditText newPassword;
    private EditText confirmPassword;

    private Button passSubmit;

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

        listenerset();
    }

    private void listenerset(){
        EditTextListener editTextListener = new EditTextListener();
        existingPassword.setOnClickListener(editTextListener);
        newPassword.setOnClickListener(editTextListener);
        confirmPassword.setOnClickListener(editTextListener);
        ButtonListener buttonListener = new ButtonListener();
        passSubmit.setOnClickListener(buttonListener);
    }

    private class EditTextListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String existingPass = existingPassword.getText().toString();
            String newPass = newPassword.getText().toString();
            String confirmPass = confirmPassword.getText().toString();
        }
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(PasswordChangeActivity.this,"확인",Toast.LENGTH_LONG).show();
        }
    }
}
