package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;


/**
 * Created by Hyeongpil on 2016-09-07.
 */
public class CourseMemoActivity extends Activity {
    final static String TAG = "CourseMemoActivity";
    private int courseNo;
    private int coursePo;
    private String memo;
    private int flag;
    private EditText et_memo;
    private Button btn_confirm;
    private Button btn_cancel;


    // TODO: 2016-09-08 메인의 코스 프레그먼트에서는 코스넘버를 받아오지 못해 등록,수정이 안됨 / 새 코스 쓰기 시 메모 수정 구현해야함
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //액션바 없앰
        setContentView(R.layout.activity_course_memo);

        init();
    }

    private void init(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); // 키보드 올리기
        et_memo = (EditText)findViewById(R.id.memo_text);
        btn_cancel = (Button)findViewById(R.id.memo_cancel);
        btn_confirm = (Button)findViewById(R.id.memo_confirm);
        coursePo = getIntent().getIntExtra("coursePo",0); // CourseWriteRecyclerAdapter 에서 받아옴
        courseNo = getIntent().getIntExtra("courseNo",0);
        memo = getIntent().getStringExtra("memo");
        flag = getIntent().getIntExtra("FLAG",0);
        //임시 저장값(코스 작성중일때, 코스 수정할때)
        if(flag == CourseWriteActivity.COURSE_WRITE_ACTIVITY || flag == CourseWriteActivity.DEFAULT_FLAG || flag == 0 || flag == CourseWriteActivity.ADJUST_FLAG){
            et_memo.setText(memo);
        }else{//디비에 기록된 값
            et_memo.setText(JspConn.getCourseMemo(courseNo,coursePo));
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //새로 작성하는 코스일때(하단의 메모버튼 누른 경우) memo 반환 , 코스 수정할때
                if(flag == CourseWriteActivity.COURSE_WRITE_ACTIVITY || flag == CourseWriteActivity.DEFAULT_FLAG || flag == CourseWriteActivity.ADJUST_FLAG){
                    Intent intent = new Intent();
                    intent.putExtra("memo",et_memo.getText().toString());
                    intent.putExtra("position",getIntent().getIntExtra("position",0));
                    setResult(CourseWriteActivity.COURSE_WRITE_ACTIVITY,intent);
                }else{//이어쓰기시
                    JspConn.setCourseMemo(et_memo.getText().toString(),courseNo,coursePo);
                }

                finish();
            }
        });
    }
}
