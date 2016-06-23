package com.kocapplication.pixeleye.kockocapp.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;
import com.kocapplication.pixeleye.kockocapp.model.DetailPageData;

import org.w3c.dom.Text;

/**
 * Created by hp on 2016-06-20.
 */
public class DetailActivity extends BaseActivity {
    final static String TAG = "DetailActivity";

    private DetailFragment mdetailFragment;
    private DetailPageData mDetailPageData;

    private EditText comment_et;
    private Button commentSend_btn;
    private Button courseCopy_btn;
    private Button scrap_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        actionBarSetting();
    }


    protected void init() {
        super.init();
        // TODO: 2016-06-21 DB 연동 후 값 가져오기
//        mDetailPageArr = JsonParser.detailPageLoad(JspConn.loadDetailPage(String.valueOf(mBoardNo)));
//        course = JsonParser.readCourse(JspConn.readCourseByCourseNo(mCourseNo));

        comment_et = (EditText) findViewById(R.id.edit_comment);
        commentSend_btn = (Button) findViewById(R.id.btn_send_comment);


        commentSend_btn.setOnClickListener(new CommentSendListener());
    }

    private void actionBarSetting() {
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.back_btn);

        View view = getLayoutInflater().inflate(R.layout.actionbar_detail, null);

        TextView title = (TextView) view.findViewById(R.id.actionbar_text_title);
        courseCopy_btn = (Button) view.findViewById(R.id.btn_detail_course_copy);
        scrap_btn = (Button) view.findViewById(R.id.btn_detail_interest);

        actionBar.setCustomView(view);

        courseCopy_btn.setOnClickListener(new CourseCopyListener());
        scrap_btn.setOnClickListener(new ScrapListener());
    }

    /**
     * CommentSendListener
     * 댓글 입력
     */
    private class CommentSendListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            String commentString = comment_et.getText().toString();
//
//            JspConn.WriteComment(commentString, mBoardNo, BasicValue.mUserNo);
//            JspConn.pushGcm(editText.getText().toString()+"|"+mBoardNo+"&"+mCourseNo, mDetailPageArr.get(vp_detail_page.getCurrentItem()).getUserNo()); //gcm
//
//            softKeyboardHide(comment_et);
        }
    }
    /**
     * CourseCopyListener
     * 코스 복사
     */
    private class CourseCopyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
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
//            JspConn.addScrap(mBoardNo);
//            Toast.makeText(v.getContext(), "관심글 등록되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private class BackListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    /**
     * onCreateOptionsMenu
     * 글 작성자면 삭제,수정 타인이면 신고
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: 2016-06-21 userNo값 받기
//        if (Integer.parseInt(mDetailPageData.getUserNo()) == BasicValue.mUserNo)
//            getMenuInflater().inflate(R.menu.menu_detail_page, menu);
//        else
//            getMenuInflater().inflate(R.menu.menu_detail_page_report, menu);
//
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.menu_delete)
//            openDeleteDialog();
//        else if(id == R.id.menu_edit)
//             TODO: 2016-06-21 수정 기능 구현 필요함
//                editBoard();
//             TODO: 2016-06-21 신고 기능 구현 필요함
//        else if (id == R.id.menu_report)
//            Toast.makeText(this, "너 고소", Toast.LENGTH_SHORT).show();

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
//                                JspConn.delete(mDetailPageArr.get(vp_detail_page.getCurrentItem()).getBoardNo(),BasicValue.mUserNo);
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
}