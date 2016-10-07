package com.kocapplication.pixeleye.kockocapp.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor.NeighborActivity;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;
import com.kocapplication.pixeleye.kockocapp.model.ProfileData;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by pixeleye02 on 2016-06-28.
 */
public class UserActivity extends BaseActivityWithoutNav {
    private final String TAG = "UserActivity";
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;

    private ImageView profileImage;
    private TextView nickName;
    private ToggleButton followButton;

    private TextView scrapCount;
    private TextView neighborCount;
    private TextView courseCount;

    private LinearLayout scrapButton;
    private LinearLayout neighborButton;
    private LinearLayout courseButton;

    View containView;

    private int userNo;
    private String followChk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        this.userNo = intent.getIntExtra("userNo",-1);

        container.setLayoutResource(R.layout.activity_user);
        containView = container.inflate();
        actionBarTitleSet("유저 정보", Color.WHITE);

        getComponent();

        Handler handler = new ProfileHandler();
        Thread thread = new GetUserInfoThread(handler,userNo);
        thread.start();

        Handler handler1 = new BoardHandler();
        Thread thread1 = new ProfileBoardThread(handler1,userNo);
        thread1.start();
    }

    private void getComponent(){
        View profile = containView.findViewById(R.id.user_profile_container);
        View recycler = findViewById(R.id.recycler_layout_user);

        //profile
        profileImage = (ImageView) profile.findViewById(R.id.user_profile_image);
        nickName = (TextView) profile.findViewById(R.id.user_nickname);
        followButton = (ToggleButton) profile.findViewById(R.id.btn_follow);

        scrapButton = (LinearLayout) profile.findViewById(R.id.user_scrap_button);
        neighborButton = (LinearLayout) profile.findViewById(R.id.user_neighbor_button);
        courseButton = (LinearLayout) profile.findViewById(R.id.user_course_button);

        scrapCount = (TextView) profile.findViewById(R.id.user_scrap_count);
        neighborCount = (TextView) profile.findViewById(R.id.user_neighbor_count);
        courseCount = (TextView) profile.findViewById(R.id.user_course_count);

        //recyclerView
        recyclerView = (RecyclerView) recycler.findViewById(R.id.recycler_view);
        adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new ItemClickListener(),UserActivity.this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(UserActivity.this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
        //본인이면 팔로우버튼 숨김
        if(userNo == BasicValue.getInstance().getUserNo())
            followButton.setVisibility(View.INVISIBLE);

        //팔로우 버튼 초기값 설정
        if(JspConn.checkFollow(userNo).trim().equals("exist")){ // 체크 팔로우는 DB값을 조사해서 친구 등록이 되어있으면 exist 반환
            followChk = "exist";
            followButton.setChecked(false);
        }else{
            followChk = "not_exist";
            followButton.setChecked(true);
        }

        listenerSet();
    }


    private void listenerSet() {
        View.OnClickListener count_listener = new CountClickListener();
        scrapButton.setOnClickListener(count_listener);
        neighborButton.setOnClickListener(count_listener);
        courseButton.setOnClickListener(count_listener);

        followButton.setOnClickListener(new FollowClickListener());
    }

    private class CountClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(scrapButton)) {
                Toast.makeText(UserActivity.this, "비공개 입니다", Toast.LENGTH_SHORT).show();
//                Intent scrap_intent = new Intent(UserActivity.this, ScrapActivity.class);
//                startActivity(scrap_intent);
            } else if (v.equals(neighborButton)) {
                Intent neighbor_intent = new Intent(UserActivity.this, NeighborActivity.class);
                neighbor_intent.putExtra("userNo",userNo);
                startActivity(neighbor_intent);
            } else if (v.equals(courseButton)) {
                Toast.makeText(UserActivity.this, "비공개 입니다", Toast.LENGTH_SHORT).show();
//                Intent course_intent = new Intent(UserActivity.this, CourseActivity.class);
//                startActivity(course_intent);
            }
        }
    }

    //친구 버튼 리스너
    private class FollowClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
//            if(followChk.equals("not_exist")){
//                int set =1; //add
//                JspConn.setFollower(userNo,set);
//            }
//            else{
//                int set =0;//delete
//                JspConn.setFollower(userNo,set);
//            }

            // check 되어있으면 follow 중
            if (followButton.isChecked()) JspConn.setFollower(userNo, 0);
            else JspConn.setFollower(userNo, 1);

        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            Board board = adapter.getItems().get(position);

            Intent intent = new Intent(UserActivity.this, DetailActivity.class);
            intent.putExtra("boardNo", board.getBasicAttributes().getBoardNo());
            intent.putExtra("courseNo", board.getBasicAttributes().getCourseNo());
            intent.putExtra("board_userNo",board.getBasicAttributes().getUserNo());
            startActivity(intent);
        }
    }

    private class ProfileHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ProfileData data = (ProfileData) msg.getData().getSerializable("THREAD");

            nickName.setText(data.getNickName());
            Glide.with(UserActivity.this).load(BasicValue.getInstance().getUrlHead()+"board_image/"+ userNo + "/profile.jpg")
                    .error(R.drawable.default_profile).bitmapTransform(new CropCircleTransformation(Glide.get(UserActivity.this).getBitmapPool())).into(profileImage);

            scrapCount.setText(data.getCourseCount() + "");
            neighborCount.setText(data.getNeighborCount() + "");
            courseCount.setText(data.getCourseCount() + "");
        }
    }

    private class BoardHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<BoardWithImage> boards = (ArrayList<BoardWithImage>) msg.getData().getSerializable("THREAD");

            adapter.setItems(boards);
            adapter.notifyDataSetChanged();
        }
    }
}
