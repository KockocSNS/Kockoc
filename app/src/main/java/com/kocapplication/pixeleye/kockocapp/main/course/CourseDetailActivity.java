package com.kocapplication.pixeleye.kockocapp.main.course;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.CommentRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.detail.DetailPageData;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.Course.JspConn_CourseExpression;
import com.kocapplication.pixeleye.kockocapp.util.connect.Jsp.DetailPage.JspConn_DeleteComment;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Hyeongpil on 2016-11-09.
 */
public class CourseDetailActivity extends BaseActivityWithoutNav {
    final static String TAG = CourseDetailActivity.class.getSimpleName();
    public final static int COURSE_DETAIL_FLAG = 123141;
    private View containView;
    private RecyclerView rv_course_detail;
    private RecyclerView rv_course_comment;
    private CourseWriteRecyclerAdapter course_adapter;
    private CommentRecyclerAdapter comment_adapter;
    private ToggleButton btn_like;
    private Courses courses;
    private ArrayList<DetailPageData.Comment> comments;

    // TODO: 2016-11-10 좋아요 및 댓글 표시 구현, 댓글 삭제 및 등록 구현 필요
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("코스 상세보기", Color.WHITE);
        container.setLayoutResource(R.layout.activity_course_detail);
        containView = container.inflate();

        getComponent();
        setCourseData();
        getData();
    }

    private void getComponent(){
        rv_course_detail = (RecyclerView)containView.findViewById(R.id.rv_course_detail);
        rv_course_comment = (RecyclerView)containView.findViewById(R.id.rv_course_comment);
        btn_like = (ToggleButton)containView.findViewById(R.id.toggle_course_content_like);
        btn_like.setOnClickListener(new LikeClickListener());
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
        //좋아요 수 체크
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
            comment_adapter = new CommentRecyclerAdapter(comments, CourseDetailActivity.this, new CommentClickListener());
            rv_course_comment.setAdapter(comment_adapter);
            LinearLayoutManager manager = new LinearLayoutManager(CourseDetailActivity.this, LinearLayoutManager.VERTICAL, false);
            rv_course_comment.setLayoutManager(manager);
            rv_course_comment.setHasFixedSize(true);

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
    private class CommentClickListener implements View.OnClickListener {
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
//                            JspConn_DeleteComment.deleteComment(comments.get(position).getComment_No());
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
}
