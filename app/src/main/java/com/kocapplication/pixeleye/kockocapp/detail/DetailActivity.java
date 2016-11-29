package com.kocapplication.pixeleye.kockocapp.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.util.GlobalApplication;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course.JspConn_ReadCourseByCourseNo;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_AddScrap;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_DeleteScrap;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_IsScrap;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_PushGcm;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_WriteComment;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseTitleActivity;
import com.kocapplication.pixeleye.kockocapp.write.newWrite.NewWriteActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hp on 2016-06-20.
 */
public class DetailActivity extends AppCompatActivity {
    final static String TAG = "DetailActivity";
    public static final int EDIT_FLAG = 2222;
    public static final int DELETE_FLAG = 21512;
    DetailFragment detailFragment;

    private EditText comment_et;
    private Button commentSend_btn;
    private Button courseCopy_btn;
    private ToggleButton scrap_btn;
    private ImageButton back_btn;
    private Spinner course_spinner;
    private ArrayList<String> course;

    private int boardNo;
    private int courseNo;
    private String courseTitle;
    private int board_userNo; // 글 작성자 유저번호
    private int position;

    private boolean isTheUser = false; //글의 작성자와 보고있는 유저가 동일인물인지


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("상세 보기");

        getIntentValue();
        init();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
    }

    protected void init() {
        detailFragment = new DetailFragment(boardNo, courseNo, board_userNo);

        comment_et = (EditText) findViewById(R.id.edit_comment);
        commentSend_btn = (Button) findViewById(R.id.btn_send_comment);
        courseCopy_btn = (Button) findViewById(R.id.btn_detail_course_copy);
        scrap_btn = (ToggleButton) findViewById(R.id.btn_detail_interest);
        back_btn = (ImageButton) findViewById(R.id.btn_detail_back);
        course_spinner = (Spinner) findViewById(R.id.course_spinner);

        setSupportActionBar((Toolbar)findViewById(R.id.tool_bar));

        commentSend_btn.setOnClickListener(new CommentSendListener());
        courseCopy_btn.setOnClickListener(new CourseCopyListener());
        scrap_btn.setOnClickListener(new ScrapListener());
        back_btn.setOnClickListener(new BackListener());

        //작성자와 유저번호가 같으면 코스 복사와 관심글 숨김
        Log.i(TAG, board_userNo + " / " + BasicValue.getInstance().getUserNo());
        if (board_userNo == BasicValue.getInstance().getUserNo()) {
            courseCopy_btn.setVisibility(View.INVISIBLE);
            scrap_btn.setVisibility(View.INVISIBLE);
        } else {
            String isScrap = JspConn_IsScrap.isScrap(boardNo, BasicValue.getInstance().getUserNo());

            scrap_btn.setText("관심글 등록");
            scrap_btn.setTextOff("관심글 등록");
            scrap_btn.setTextOn("관심글 해제");

            if (isScrap.isEmpty()) scrap_btn.setChecked(false);
            else scrap_btn.setChecked(true);
        }

        //코스가 있을때만 스피너 띄움 + 코스없으면 코스복사버튼 GONE
        if (courseNo > 0) set_spinner();
        else {
            course_spinner.setVisibility(View.GONE);
            courseCopy_btn.setVisibility(View.INVISIBLE);
        }
        courseTitle = JspConn.getCourseTitle(courseNo);


        //글 작성자 유저넘버와 보고있는 유저의 넘버를 비교하여 글 주인 구별
        changeIsTheUser();
    }
    // TODO: 2016-07-26 키보드가 올라와있을때 화면 다른 부분이 눌리면 키보드가 내려가도록 하기.


    private void getIntentValue() {
        Intent intent = getIntent();
        boardNo = intent.getIntExtra("boardNo", 0);
        courseNo = intent.getIntExtra("courseNo", 0);
        board_userNo = intent.getIntExtra("board_userNo", 0);
        position = intent.getIntExtra("position",0); // 글 삭제시 쓰기 위해 스토리 프래그먼트 포지션
    }

    private void changeIsTheUser() {
        if (board_userNo == BasicValue.getInstance().getUserNo())
            isTheUser = true;
        else
            isTheUser = false;
    }


    /**
     * 댓글 입력
     */
    private class CommentSendListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String commentString = comment_et.getText().toString();
            if (commentString.isEmpty()) {
                Snackbar.make(comment_et, "댓글을 작성해 주세요.", Snackbar.LENGTH_SHORT).show();
                return;
            }

            JspConn_WriteComment.writeComment(commentString, boardNo, BasicValue.getInstance().getUserNo());
            JspConn_PushGcm.pushGcm(commentString + "|" + boardNo + "&" + courseNo, board_userNo); //gcm

            detailFragment.addComment();
            GlobalApplication.getInstance().softKeyboardHide(comment_et);
        }
    }

    /**
     * 코스 복사
     */
    private class CourseCopyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO: 2016-07-22 Refactoring이 해야한다.
            int courseNo = DetailActivity.this.courseNo;
            String courseTitle = DetailActivity.this.courseTitle;
            List<String> course = JsonParser.readCourse(JspConn_ReadCourseByCourseNo.readCourseByCourseNo(courseNo));

            if (courseNo == 0) return;

            List<Course> courseList = new ArrayList<>();
            for (int i = 0; i < course.size(); i++) {
                String courseName = course.get(i).split("/")[0];
                if (courseName.equals("null")) break;
                Log.i(TAG, courseName);
                courseList.add(new Course(courseName, new Date(), i));
            }

            try {
                Courses courses = new Courses(courseList);
                Intent intent = new Intent(DetailActivity.this, CourseTitleActivity.class);
                intent.putExtra("courses",courses);
                startActivity(intent);
                Toast.makeText(DetailActivity.this, "코스가 복사 되었습니다.\n제목을 입력 해주세요", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DetailActivity.this, "복사 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 관심글 등록
     */
    private class ScrapListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (scrap_btn.isChecked()) {
                JspConn_AddScrap.addScrap(boardNo);
                Toast.makeText(v.getContext(), "관심글 등록되었습니다.", Toast.LENGTH_SHORT).show();
                detailFragment.addScrapCount(1);
            } else {
                JspConn_DeleteScrap.deleteScrap(boardNo);
                Toast.makeText(v.getContext(), "관심글 해제되었습니다.", Toast.LENGTH_SHORT).show();
                detailFragment.addScrapCount(-1);
            }
        }
    }

    private class BackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    public void openOptionsMenu() {

        Configuration config = getResources().getConfiguration();

        if((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                > Configuration.SCREENLAYOUT_SIZE_LARGE) {

            int originalScreenLayout = config.screenLayout;
            config.screenLayout = Configuration.SCREENLAYOUT_SIZE_LARGE;
            super.openOptionsMenu();
            config.screenLayout = originalScreenLayout;

        } else {
            super.openOptionsMenu();
        }
    }

    //스피너 리스너
    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        String courseBoardNo = "";

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                courseBoardNo = JspConn.getBoardNoForEdit(courseNo, course.get(position - 1).split("/")[0]);
                Log.e(TAG, "코스넘버 코스이름 :" + courseNo + "/" + course.get(position - 1).split("/")[0] + "/" + courseBoardNo);
            } catch (Exception e) {}

            switch (position) {
                case 0:
                    break;
                default:
                    if (!courseBoardNo.equals("")) {
                        detailFragment = new DetailFragment(Integer.parseInt(courseBoardNo), courseNo, board_userNo);
                        DetailActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
                    } else
                        Toast.makeText(DetailActivity.this, "해당 코스에 글이 없습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    /**
     * 액션바 메뉴
     * 글 작성자면 삭제,수정 타인이면 신고
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(isTheUser==true) inflater.inflate(R.menu.menu_detail_page, menu);
        else inflater.inflate(R.menu.menu_detail_page_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_delete)
            openDeleteDialog();
        else if (id == R.id.menu_edit) {
            Intent edit_intent = new Intent(DetailActivity.this, NewWriteActivity.class);
            edit_intent.putExtra("DATA", detailFragment.detailPageData);
            edit_intent.putExtra("FLAG", EDIT_FLAG);
            startActivity(edit_intent);
            finish();
        } else if (id == R.id.menu_report)
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
                                JspConn.boardDelete(boardNo, BasicValue.getInstance().getUserNo());
                                Intent intent = new Intent();
                                intent.putExtra("position",position);
                                setResult(MainActivity.DETAIL_ACTIVITY_REQUEST_CODE,intent);
                                finish();
                            }
                        })
                .setNegativeButton(R.string.menu_delete_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                            }
                        })
                .show();
    }

    private void set_spinner() {
        course_spinner.setOnItemSelectedListener(new SpinnerListener());
        //스피너 리스트 값 설정
        List<String> list = new ArrayList<String>();
        list.add("코스선택");
        course = JsonParser.readCourse(JspConn_ReadCourseByCourseNo.readCourseByCourseNo(courseNo));
        for (int i = 0; i < course.size(); i++) {
            if (course.get(i).equals("null")) break;
            list.add(course.get(i));
        }

        Log.i("detailActivity.course",list.toString());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DetailActivity.this, R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        course_spinner.setAdapter(dataAdapter);
        course_spinner.setSelection(0);
    }
}




