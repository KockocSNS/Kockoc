package com.kocapplication.pixeleye.kockocapp.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.User;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

import java.util.Calendar;

/**
 * Created by hp on 2016-06-29.
 */
public class JoinActivity extends BaseActivityWithoutNav {
    final static String TAG = "JoinActivity";
    RelativeLayout join_container;
    final int DATE_DIALOG_ID =0;
    int year,month,date;
    Button btnJoin,btnDup_name,btnDupl_id;
    EditText editName,editNickname,editID,editTel,editPwd,editPwdConfirm,editYmd;
    RadioButton radioMale,radioFemale;
    CheckBox checkBoxPnl,checkBoxAlarm;
    boolean nicknamechk= false, idchk= false;
     String nickname, tel, birth, gender, pwd, check_pwd, name, id;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        getComponent();

        actionBarTitleSet("회원가입", Color.WHITE);
    }
    protected void getComponent(){
        container.setLayoutResource(R.layout.activity_join);
        View containView = container.inflate();
        View join_container = containView.findViewById(R.id.join_container);
        //Button
        btnJoin = (Button)join_container.findViewById(R.id.join_BtnJoin);
        btnDup_name = (Button)join_container.findViewById(R.id.join_btnDup_name);
        btnDupl_id = (Button)join_container.findViewById(R.id.join_btnDupl_id);
        //EditText
        editName = (EditText)join_container.findViewById(R.id.join_editName);
        editNickname = (EditText)join_container.findViewById(R.id.join_editNickname);
        editID = (EditText)join_container.findViewById(R.id.join_editID);
        editTel = (EditText)join_container.findViewById(R.id.join_editTel);
        editPwd  = (EditText)join_container.findViewById(R.id.join_editPwd);
        editYmd = (EditText)join_container.findViewById(R.id.join_editYmd);
        editPwdConfirm = (EditText)join_container.findViewById(R.id.join_editPwdConfirm);
        //RadioButton
        radioMale = (RadioButton)join_container.findViewById(R.id.join_radioMale);
        radioFemale = (RadioButton)join_container.findViewById(R.id.join_radioFemale);
        //CheckBox
        checkBoxPnl = (CheckBox)join_container.findViewById(R.id.join_checkBoxPnl);
        checkBoxAlarm = (CheckBox)join_container.findViewById(R.id.join_checkBoxAlarm);

        setOnClickEvent();
        setYMD();
    }

    //모든 항목 체크
    private boolean chk_allPass(){
        name = editName.getText().toString();
        nickname = editNickname.getText().toString();
        id = editID.getText().toString();
        birth = editYmd.getText().toString();
        tel = editTel.getText().toString();
        pwd = editPwd.getText().toString();
        check_pwd = editPwdConfirm.getText().toString();
        if(nickname.equals("")){
            Toast.makeText(JoinActivity.this, "닉네임을 입력 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!nicknamechk) {
            Toast.makeText(JoinActivity.this, "닉네임 중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!idchk){
            Toast.makeText(JoinActivity.this, "아이디 중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(birth.equals("")){
            Toast.makeText(JoinActivity.this, "생일을 입력 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(tel.equals("")){
            Toast.makeText(JoinActivity.this, "전화번호를 입력 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!checkBoxPnl.isChecked()){
            Toast.makeText(JoinActivity.this, "개인정보 수집/이용 약관에 동의해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!pwd.equals(check_pwd)){
            Toast.makeText(JoinActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(pwd.equals("")){
            Toast.makeText(JoinActivity.this, "비밀번호를 입력 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(name.equals("")){
            Toast.makeText(JoinActivity.this, "이름을 입력 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(radioMale.isChecked()){ gender = "Male";}
        else if (radioFemale.isChecked()){ gender = "Female";}
        user = new User(name,nickname,tel,id,pwd,birth,gender);
        return true;
    }
    private void setOnClickEvent(){
        //생일 다이얼로그
        editYmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        //닉네임 중복 체크
        btnDup_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = editNickname.getText().toString();
                if (nickname.length() > 0 && JspConn.checkDuplNickname(nickname)==true){
                    Toast.makeText(JoinActivity.this, "사용하셔도 좋습니다.", Toast.LENGTH_SHORT).show();
                    nicknamechk = true;
                }
                else{
                    Toast.makeText(JoinActivity.this, "이미 있는 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDupl_id.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String id = editID.getText().toString();
                if (id.length() >0  && JspConn.checkDuplID(id) == true) {
                    Toast.makeText(JoinActivity.this, "사용하셔도 좋습니다.", Toast.LENGTH_SHORT).show();
                    idchk = true;
                }
                else{
                    Toast.makeText(JoinActivity.this, "이미 있는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    idchk = false;
                }
            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chk_allPass()){
                    Log.d(TAG,"user :"+user.toString());
                    JspConn.recordMember(user);
                    startActivity(new Intent(JoinActivity.this,LoginActivity.class));
                    Toast.makeText(JoinActivity.this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        //edittext에 변화가 생길 때 마다 반응
        final TextWatcher TWID = new TextWatcher() { //id 변화
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {idchk = false;}//아이디에 변화가 생기면 아이디체크를 false로 바꿔줌
        };
        editID.addTextChangedListener(TWID);
        TextWatcher TWnickname = new TextWatcher() {// 닉네임 변화
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {nicknamechk =false;}
        };
        editNickname.addTextChangedListener(TWnickname);
    }
    //생일 설정 다이얼로그
    private void setYMD(){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        date = c.get(Calendar.DATE);
    }
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,mDateSetListener,year,month,date);
        }
        return  null;
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear+=1;
            String YMD="";
            YMD+=year+"-";
            if(monthOfYear>9)YMD+=monthOfYear+"-";
            else YMD+="0"+monthOfYear+"-";
            if(dayOfMonth>9)YMD+=dayOfMonth;
            else YMD+="0"+dayOfMonth;
            editYmd.setText(YMD);
        }
    };
}
