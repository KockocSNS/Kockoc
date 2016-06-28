package com.kocapplication.pixeleye.kockocapp.detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

/**
 * Created by hp on 2016-06-20.
 */
public class DetailActivity extends AppCompatActivity {
    final static String TAG = "DetailActivity";
    DetailFragment detailFragment;

    private Toolbar toolbar;
    private EditText comment_et;
    private Button commentSend_btn;
    private Button courseCopy_btn;
    private Button scrap_btn;
    private ImageButton back_btn;
    private ImageView menu_btn;

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
        getFragmentManager().beginTransaction().replace(R.id.container,detailFragment).commit();
    }

    protected void init(){
        detailFragment = new DetailFragment(boardNo,courseNo);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        comment_et = (EditText) findViewById(R.id.edit_comment);
        commentSend_btn = (Button) findViewById(R.id.btn_send_comment);
        courseCopy_btn = (Button)findViewById(R.id.btn_detail_course_copy);
        scrap_btn = (Button)findViewById(R.id.btn_detail_interest);
        back_btn = (ImageButton)findViewById(R.id.btn_detail_back);
        menu_btn = (ImageView)findViewById(R.id.detail_menu);

        commentSend_btn.setOnClickListener(new CommentSendListener());
        courseCopy_btn.setOnClickListener(new CourseCopyListener());
        scrap_btn.setOnClickListener(new ScrapListener());
        back_btn.setOnClickListener(new BackListener());
        menu_btn.setOnClickListener(new MenuListener());
        //작성자와 유저번호가 같으면 코스 복사와 관심글 숨김
        if(board_userNo == BasicValue.getInstance().getUserNo()){
            courseCopy_btn.setVisibility(View.GONE);
            scrap_btn.setVisibility(View.GONE);
        }
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
            JspConn.addScrap(boardNo);
            Toast.makeText(v.getContext(), "관심글 등록되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private class BackListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {finish();}
    }
    private class MenuListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.e(TAG,"클릭");
            DetailActivity.this.openOptionsMenu();
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
        else if(id == R.id.menu_edit)
            Toast.makeText(DetailActivity.this, "글 수정 구현해야함", Toast.LENGTH_SHORT).show();
//                editBoard();
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

}