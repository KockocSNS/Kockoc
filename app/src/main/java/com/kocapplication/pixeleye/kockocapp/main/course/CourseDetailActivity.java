package com.kocapplication.pixeleye.kockocapp.main.course;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.CommentRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.detail.DetailPageData;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.util.GlobalApplication;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course.JspConn_CourseExpression;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course.JspConn_ReadCourseByCourseNo;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_DeleteComment;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_WriteComment;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseTitleActivity;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteActivity;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hyeongpil on 2016-11-09.
 */
public class CourseDetailActivity extends AppCompatActivity {
    final static String TAG = CourseDetailActivity.class.getSimpleName();
    public final static int COURSE_DETAIL_FLAG = 123141;
    private RecyclerView rv_course_detail;
    private RecyclerView rv_course_comment;
    private CourseWriteRecyclerAdapter course_adapter;
    private CommentRecyclerAdapter comment_adapter;
    private ToggleButton btn_like;
    private Courses courses;
    private ArrayList<DetailPageData.Comment> comments;
    private EditText et_comment;
    private Button btn_comment;
    private Button btn_copy;
    private ImageButton btn_back;
    private TextView tv_comment_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_course_detail);
        getComponent();
        setCourseData();
        getData();

    }

    private void getComponent(){
        setSupportActionBar((Toolbar)findViewById(R.id.course_detail_tool_bar));
        rv_course_detail = (RecyclerView)findViewById(R.id.rv_course_detail);
        rv_course_comment = (RecyclerView)findViewById(R.id.rv_course_comment);
        et_comment = (EditText)findViewById(R.id.et_course_comment);
        tv_comment_count = (TextView)findViewById(R.id.tv_detail_course_comment_count);
        btn_comment = (Button)findViewById(R.id.btn_course_comment);
        btn_comment.setOnClickListener(new CommentClickListener());
        btn_like = (ToggleButton)findViewById(R.id.toggle_course_content_like);
        btn_like.setOnClickListener(new LikeClickListener());
        btn_copy = (Button)findViewById(R.id.btn_course_detail_copy);
        btn_copy.setOnClickListener(new CourseCopyListener());
        btn_back = (ImageButton)findViewById(R.id.btn_course_detail_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setCourseData() {
        courses = (Courses) getIntent().getSerializableExtra("COURSES"); // CourseFragment에서 코스 데이터를 받아옴
        course_adapter = new CourseWriteRecyclerAdapter(courses.getCourses(), this, COURSE_DETAIL_FLAG);
        rv_course_detail.setAdapter(course_adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        rv_course_detail.setLayoutManager(manager);
        rv_course_detail.setHasFixedSize(true);
    }

    private void getData(){
        //좋아요 및 댓글 데이터 불러옴
        Handler data_handler = new getDataHandler();
        Thread ex_thread = new CourseDetailDataThread(data_handler, courses.getCourseNo());
        ex_thread.start();
    }

    /**
     * 서버에서 데이터를 불러옴
     */
    private class getDataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            setExpression(msg.getData().getString("expression"));
            comments = (ArrayList<DetailPageData.Comment>)msg.getData().getSerializable("comment");
            setComment();
        }
        /**
         * 좋아요 표시
         */
        private void setExpression(String expression_data){
            Log.i(TAG, expression_data);
            try {
                JSONObject upperObj = new JSONObject(expression_data);
                JSONArray array = upperObj.getJSONArray("userArr");
                btn_like.setTextOff(array.length() + "");
                btn_like.setTextOn((array.length() + 1) + "");
                btn_like.setText(array.length() + "");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    if (BasicValue.getInstance().getUserNo() == object.getInt("userNo")) {
                        btn_like.setChecked(true);
                        btn_like.setTextOn(array.length() + "");
                        btn_like.setTextOff((array.length() - 1) + "");
                        btn_like.setText(array.length() + "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 댓글 데이터를 RecyclerView에 붙임
         */
        private void setComment(){
            comment_adapter = new CommentRecyclerAdapter(comments, CourseDetailActivity.this, new CommentListClickListener());
            rv_course_comment.setAdapter(comment_adapter);
            LinearLayoutManager manager = new LinearLayoutManager(CourseDetailActivity.this, LinearLayoutManager.VERTICAL, false);
            rv_course_comment.setLayoutManager(manager);
            rv_course_comment.setHasFixedSize(true);
            tv_comment_count.setText(String.valueOf(comments.size()));
        }
    }

    /**
     * 좋아요 클릭 리스너
     */
    private class LikeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            JspConn_CourseExpression.writeExpression(courses.getCourseNo(), 0);
        }
    }

    /**
     * 댓글 클릭 리스너
     */
    private class CommentListClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final int position = rv_course_comment.getChildLayoutPosition(v);
            try {
                if (comments.get(position).getComment_userNo() == BasicValue.getInstance().getUserNo()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailActivity.this);

                    builder.setTitle("안내");
                    builder.setMessage("삭제하시겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            JspConn_DeleteComment.deleteCourseComment(comments.get(position).getComment_No());
                            comment_adapter.removeItem(position);
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();
                }
            } catch (Exception e) {
                Log.e(TAG, "댓글 삭제 오류" + e.getMessage());
            }
        }
    }
    private class CommentClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String commentString = et_comment.getText().toString();
            if (commentString.isEmpty()) {
                Snackbar.make(et_comment, "댓글을 작성해 주세요.", Snackbar.LENGTH_SHORT).show();
                return;
            }

            JspConn_WriteComment.writeCourseComment(commentString, courses.getCourseNo(), BasicValue.getInstance().getUserNo());
//            JspConn_PushGcm.pushGcm(commentString + "|" + boardNo + "&" + courseNo, board_userNo); //gcm
            getData(); // 댓글 달면 데이터 새로 불러옴
            GlobalApplication.getInstance().softKeyboardHide(et_comment);
        }
    }

    /**
     * 액션바 메뉴
     * 코스 작성자면 삭제,수정 타인이면 신고
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(courses.getUserNo()==BasicValue.getInstance().getUserNo()) inflater.inflate(R.menu.menu_detail_page, menu);
        else inflater.inflate(R.menu.menu_detail_page_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_delete)
            openDeleteDialog();
        else if (id == R.id.menu_edit) {
            Intent edit_intent = new Intent(CourseDetailActivity.this, CourseWriteActivity.class);
            edit_intent.putExtra("FLAG", CourseWriteActivity.ADJUST_FLAG);
            edit_intent.putExtra("COURSES", courses);
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
                                String result = JspConn.deleteCourse(courses.getUserNo(), courses.getCourseNo(), courses.getTitle());
//                                Intent intent = new Intent();
//                                intent.putExtra("position",courses.get);
//                                setResult(MainActivity.DETAIL_ACTIVITY_REQUEST_CODE,intent);
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
    /**
     * 코스 복사
     */
    private class CourseCopyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            List<String> course = JsonParser.readCourse(JspConn_ReadCourseByCourseNo.readCourseByCourseNo(courses.getCourseNo()));

            if (courses.getCourseNo() == 0) return;

            List<Course> courseList = new ArrayList<>();
            for (int i = 0; i < course.size(); i++) {
                String courseName = course.get(i).split("/")[0];
                if (courseName.equals("null")) break;
                Log.i(TAG, courseName);
                courseList.add(new Course(courseName, new Date(), i));
            }

            try {
                Courses courses = new Courses(courseList);
                Intent intent = new Intent(CourseDetailActivity.this, CourseTitleActivity.class);
                intent.putExtra("courses",courses);
                startActivity(intent);
                Toast.makeText(CourseDetailActivity.this, "코스가 복사 되었습니다.\n제목을 입력 해주세요", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CourseDetailActivity.this, "복사 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
