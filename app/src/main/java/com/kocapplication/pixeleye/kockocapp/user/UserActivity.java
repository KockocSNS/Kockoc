package com.kocapplication.pixeleye.kockocapp.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.course.CourseActivity;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor.NeighborActivity;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.scrap.ScrapActivity;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;
import com.kocapplication.pixeleye.kockocapp.model.ProfileData;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by pixeleye02 on 2016-06-28.
 */
public class UserActivity extends BaseActivity {
    private final String TAG = "UserActivity";
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;

    private ImageView profileImage;
    private TextView nickName;
    private Button followButton;

    private TextView scrapCount;
    private TextView neighborCount;
    private TextView courseCount;

    private LinearLayout scrapButton;
    private LinearLayout neighborButton;
    private LinearLayout courseButton;

    private int userNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        this.userNo = intent.getIntExtra("userNo",-1);

        View profile = findViewById(R.id.user_profile_container);
        View recycler = findViewById(R.id.recycler_layout_user);

        //profile
        profileImage = (ImageView) profile.findViewById(R.id.user_profile_image);
        nickName = (TextView) profile.findViewById(R.id.user_nickname);
        followButton = (Button) profile.findViewById(R.id.btn_follow);

        scrapButton = (LinearLayout) profile.findViewById(R.id.user_scrap_button);
        neighborButton = (LinearLayout) profile.findViewById(R.id.user_neighbor_button);
        courseButton = (LinearLayout) profile.findViewById(R.id.user_course_button);

        scrapCount = (TextView) profile.findViewById(R.id.user_scrap_count);
        neighborCount = (TextView) profile.findViewById(R.id.user_neighbor_count);
        courseCount = (TextView) profile.findViewById(R.id.user_course_count);

        listenerSet();

        //recyclerView
        recyclerView = (RecyclerView) recycler.findViewById(R.id.recycler_view);
        adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new ItemClickListener());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(UserActivity.this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);

        Handler handler = new ProfileHandler();
        Thread thread = new ProfileImageThread(handler,userNo);
        thread.start();

        Handler handler1 = new BoardHandler();
        Thread thread1 = new ProfileBoardThread(handler1,userNo);
        thread1.start();
    }

    private void listenerSet() {
        View.OnClickListener count_listener = new CountClickListener();
        scrapButton.setOnClickListener(count_listener);
        neighborButton.setOnClickListener(count_listener);
        courseButton.setOnClickListener(count_listener);

        View.OnClickListener profile_listenrer = new ProfileClickListener();
        profileImage.setOnClickListener(profile_listenrer);
    }

    private class CountClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(scrapButton)) {
                Intent scrap_intent = new Intent(UserActivity.this, ScrapActivity.class);
                startActivity(scrap_intent);
            } else if (v.equals(neighborButton)) {
                Intent neighbor_intent = new Intent(UserActivity.this, NeighborActivity.class);
                startActivity(neighbor_intent);
            } else if (v.equals(courseButton)) {
                Intent course_intent = new Intent(UserActivity.this, CourseActivity.class);
                startActivity(course_intent);
            }
        }
    }

    private class ProfileClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(UserActivity.this, "사진선택", Toast.LENGTH_SHORT).show();
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
