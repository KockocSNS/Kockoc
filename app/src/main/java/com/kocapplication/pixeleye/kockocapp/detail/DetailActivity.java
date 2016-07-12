package com.kocapplication.pixeleye.kockocapp.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;
import com.kocapplication.pixeleye.kockocapp.write.newWrite.NewWriteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016-06-20.
 */
public class DetailActivity extends AppCompatActivity {
    final static String TAG = "DetailActivity";
    public static final int EDIT_FLAG = 2222;
    DetailFragment detailFragment;

    private EditText comment_et;
    private Button commentSend_btn;
    private Button courseCopy_btn;
    private ToggleButton scrap_btn;
    private ImageButton back_btn;
    private ImageView menu_btn;
    private Spinner course_spinner;
    private ArrayList<String> course;

    private int boardNo;
    private int courseNo;
    private int board_userNo; // 글 작성자 유저번호

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("상세 보기");

        getIntentValue();
        init();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,detailFragment).commit();
    }

    protected void init(){
        detailFragment = new DetailFragment(boardNo,courseNo,board_userNo);

        comment_et = (EditText) findViewById(R.id.edit_comment);
        commentSend_btn = (Button) findViewById(R.id.btn_send_comment);
        courseCopy_btn = (Button)findViewById(R.id.btn_detail_course_copy);
        scrap_btn = (ToggleButton)findViewById(R.id.btn_detail_interest);
        back_btn = (ImageButton)findViewById(R.id.btn_detail_back);
        menu_btn = (ImageView)findViewById(R.id.detail_menu);
        course_spinner = (Spinner) findViewById(R.id.course_spinner);


        commentSend_btn.setOnClickListener(new CommentSendListener());
        courseCopy_btn.setOnClickListener(new CourseCopyListener());
        scrap_btn.setOnClickListener(new ScrapListener());
        back_btn.setOnClickListener(new BackListener());
        menu_btn.setOnClickListener(new MenuListener());
        //작성자와 유저번호가 같으면 코스 복사와 관심글 숨김
        if(board_userNo == BasicValue.getInstance().getUserNo()){
            courseCopy_btn.setVisibility(View.INVISIBLE);
            scrap_btn.setVisibility(View.INVISIBLE);
        } else{
            scrap_btn.setChecked(true);
            scrap_btn.setTextOff("관심글 등록");
            scrap_btn.setTextOn("관심글 해제");
            scrap_btn.setText("관심글 등록");
        }
        //코스가 있을때만 스피너 띄움
        if(courseNo>0) {set_spinner();}
        else course_spinner.setVisibility(View.GONE);

    }

    private void getIntentValue(){
        Intent intent = getIntent();
        boardNo = intent.getIntExtra("boardNo",0);
        courseNo = intent.getIntExtra("courseNo",0);
        board_userNo = intent.getIntExtra("board_userNo",0);
    }

    /**
     * CommentSendListener
     * 댓글 입력
     */
    private class CommentSendListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String commentString = comment_et.getText().toString();

            JspConn.WriteComment(commentString, boardNo, BasicValue.getInstance().getUserNo());
            JspConn.pushGcm(commentString+"|"+boardNo+"&"+courseNo, board_userNo); //gcm

            detailFragment.addComment();
            softKeyboardHide(comment_et);
        }
    }
    /**
     * CourseCopyListener
     * 코스 복사
     */
    private class CourseCopyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(DetailActivity.this, "코스 복사 구현해야함", Toast.LENGTH_SHORT).show();
//            int index;
//            ArrayList<Courses> courseArr = (JsonParser.readCourseToCopy(JspConn.readCourseAll()));
//            for (index = 0; index < courseArr.size(); index++) {
//                if (courseArr.get(index).courseNo == mCourseNo) {
//                    break;
//                }
//            }
//            Courses courses = new Courses();
//            courses.courseArr = courseArr.get(index).courseArr;
//            courses.courseNo = courseArr.get(index).courseNo;
//            Intent intent = new Intent(getApplicationContext(), GetTitleActivity.class);
//            intent.putExtra("courses",courses);
//            intent.putExtra("courseArr",courseArr);
//            startActivity(intent);
//            finish();
        }
    }
    /**
     * CourseCopyListener
     * 관심글 등록
     */
    private class ScrapListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (scrap_btn.isChecked()) {
                JspConn.addScrap(boardNo);
                Toast.makeText(v.getContext(), "관심글 등록되었습니다.", Toast.LENGTH_SHORT).show();
                detailFragment.addScrapCount(1);
            } else{
                JspConn.deleteScrap(boardNo);
                Toast.makeText(v.getContext(), "관심글 해제되었습니다.", Toast.LENGTH_SHORT).show();
                detailFragment.addScrapCount(-1);
            }
        }
    }
    private class BackListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {finish();}
    }
    private class MenuListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            DetailActivity.this.openOptionsMenu();
        }
    }

    //스피너 리스너
    private class SpinnerListener implements AdapterView.OnItemSelectedListener{
        String courseBoardNo = "";

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                courseBoardNo = JspConn.getBoardNoForEdit(courseNo, course.get(position-1).split("/")[0]);
                Log.e(TAG,"코스넘버 코스이름 :"+courseNo+"/"+course.get(position-1).split("/")[0]+"/"+courseBoardNo);
            } catch (Exception e){}

            switch (position){
                case 0:
                    break;
                default:
                    if(!courseBoardNo.equals("")) {
                        detailFragment = new DetailFragment(Integer.parseInt(courseBoardNo), courseNo, board_userNo);
                        DetailActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
                    } else Toast.makeText(DetailActivity.this, "해당 코스에 글이 없습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /**
     * onCreateOptionsMenu
     * 글 작성자면 삭제,수정 타인이면 신고
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (board_userNo == BasicValue.getInstance().getUserNo())
            getMenuInflater().inflate(R.menu.menu_detail_page, menu);
        else
            getMenuInflater().inflate(R.menu.menu_detail_page_report, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_delete)
            openDeleteDialog();
        else if(id == R.id.menu_edit) {
            Intent edit_intent = new Intent(DetailActivity.this, NewWriteActivity.class);
            edit_intent.putExtra("DATA",detailFragment.detailPageData);
            edit_intent.putExtra("FLAG",EDIT_FLAG);
            startActivity(edit_intent);
            finish();
        }
        else if (id == R.id.menu_report)
            Toast.makeText(this, "신고 기능 구현 해야함", Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    private void openDeleteDialog() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_delete_title)
                .setMessage(R.string.menu_delete_message)
                .setPositiveButton(R.string.menu_delete_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                JspConn.boardDelete(boardNo,BasicValue.getInstance().getUserNo());
                                finish();
                            }
                        })
                .setNegativeButton(R.string.menu_delete_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {}
                        })
                .show();
    }
    //키보드 숨김
    protected void softKeyboardHide(EditText editText){
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.setText("");
    }
    private void set_spinner(){
        course_spinner.setOnItemSelectedListener(new SpinnerListener());
        //스피너 리스트 값 설정
        List<String> list = new ArrayList<String>();
        list.add("코스선택");
        course = JsonParser.readCourse(JspConn.readCourseByCourseNo(courseNo));
        for (int i = 0; i < 10; i++) {
            if (course.get(i).equals("null"))
                break;
            list.add(course.get(i).split("/")[0] + "(" + i + ")");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DetailActivity.this, R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        course_spinner.setAdapter(dataAdapter);
        course_spinner.setSelection(0);
    }

}