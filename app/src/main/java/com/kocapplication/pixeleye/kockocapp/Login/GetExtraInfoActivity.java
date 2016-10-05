package com.kocapplication.pixeleye.kockocapp.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.model.User;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

import java.util.Calendar;

/**
 * Created by hp on 2016-06-22.
 */
public class GetExtraInfoActivity extends AppCompatActivity {
    final static String TAG = "GetExtraInfoActivity";
    final int DATE_DIALOG_ID = 0;
    private Context mContext;

    int year, month, date;
    private EditText et_nickname, et_tel, et_birth;
    private Button btn_nickname, btn_join;
    private String nickname, tel, birth, flag, gender;
    private RadioButton radioMale, radioFemale;
    private CheckBox check1, check2;
    private boolean nicknamechk = false;
    private int userNo;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_extra_info);

        userNo = BasicValue.getInstance().getUserNo();
        user = new User();

        init();
        GetExtraInfoActivity();
        setOnClickEvent();
    }

    private void init() {
        setYMD();
        mContext = this;
        et_nickname = (EditText) findViewById(R.id.getuser_nickname);
        et_tel = (EditText) findViewById(R.id.getuser_tel);
        et_birth = (EditText) findViewById(R.id.getuser_birth);
        btn_nickname = (Button) findViewById(R.id.getuser_nickname_btn);
        btn_join = (Button) findViewById(R.id.getuser_ok);
        radioMale = (RadioButton) findViewById(R.id.join_radioMale);
        radioFemale = (RadioButton) findViewById(R.id.join_radioFemale);
        check1 = (CheckBox) findViewById(R.id.getuser_check1);
        check2 = (CheckBox) findViewById(R.id.getuser_check2);
    }

    private void GetExtraInfoActivity() {
        Intent getIntent = getIntent();
        try {
            Bundle user_bundle;
            user_bundle = getIntent.getBundleExtra("user");
            user = (User) user_bundle.getSerializable("user");
        } catch (NullPointerException e) {
            Log.d(TAG, "user_bundle null");
        }
        flag = getIntent.getStringExtra("flag");
    }

    private void setOnClickEvent() {
        //생일 다이얼로그
        et_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        //닉네임 중복 체크
        btn_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = et_nickname.getText().toString();
                if (nickname.length() > 0 && JspConn.checkDuplNickname(nickname) == true) {
                    Toast.makeText(GetExtraInfoActivity.this, "사용하셔도 좋습니다.", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("inputValue", nickname);
                    nicknamechk = true;
                } else {
                    Toast.makeText(GetExtraInfoActivity.this, "이미 있는 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_allPass()) {
                    if (flag.equals("naver"))
                        BasicValue.getInstance().setUserNo(JspConn.recordMember(user));
                    else if (flag.equals("kakao"))
                        BasicValue.getInstance().setUserNo(JspConn.recordMember(user)); // 카카오 사인업에서 만든 User 업데이트
                    else if (flag.equals("facebook"))
                        BasicValue.getInstance().setUserNo(JspConn.recordMember(user));
                    startActivity(new Intent(GetExtraInfoActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, year, month, date);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear += 1;
            String YMD = "";
            YMD += year + "-";
            if (monthOfYear > 9) YMD += monthOfYear + "-";
            else YMD += "0" + monthOfYear + "-";
            if (dayOfMonth > 9) YMD += dayOfMonth;
            else YMD += "0" + dayOfMonth;
            et_birth.setText(YMD);
        }
    };

    private void setYMD() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        date = c.get(Calendar.DATE);
    }

    //모든 항목 체크
    private boolean chk_allPass() {
        nickname = et_nickname.getText().toString();
        birth = et_birth.getText().toString();
        tel = et_tel.getText().toString();
        if (nickname.equals("")) {
            Toast.makeText(GetExtraInfoActivity.this, "닉네임을 입력 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!nicknamechk) {
            Toast.makeText(GetExtraInfoActivity.this, "닉네임 중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (birth.equals("")) {
            Toast.makeText(GetExtraInfoActivity.this, "생일을 입력 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tel.equals("")) {
            Toast.makeText(GetExtraInfoActivity.this, "전화번호를 입력 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!check1.isChecked()) {
            Toast.makeText(GetExtraInfoActivity.this, "개인정보 수집/이용 약관에 동의해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (radioMale.isChecked()) {
            user.setUserGender("Male");
            gender = "Male";
        } else if (radioFemale.isChecked()) {
            user.setUserGender("Female");
            gender = "Female";
        }
        user.setUserNickname(nickname);
        user.setUserTel(tel);
        user.setUserYMD(birth);
        return true;
    }

}
