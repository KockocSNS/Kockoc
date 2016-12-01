package com.kocapplication.pixeleye.kockocapp.main.myKockoc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.course.CourseActivity;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor.NeighborActivity;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.scrap.ScrapActivity;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;
import com.kocapplication.pixeleye.kockocapp.model.ProfileData;
import com.kocapplication.pixeleye.kockocapp.navigation.NicknameChangeActivity;
import com.kocapplication.pixeleye.kockocapp.user.ProfileBoardThread;
import com.kocapplication.pixeleye.kockocapp.user.GetUserInfoThread;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseTitleActivity;
import com.kocapplication.pixeleye.kockocapp.write.newWrite.NewWriteActivity;


import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Han_ on 2016-06-21.
 */
public class MyKocKocFragment extends Fragment {
    private final String TAG = "MyKocKocFragment";
    private final int PROFILE_IMG_SET = 1001;
    private final int NICKNAME_UPDATE = 1002;
    private final int SCRAP_REQUEST_CODE = 1003;
    private final int NEIGHBOR_REQUEST_CODE = 1004;
    private final int COURSE_REQUEST_CODE = 1005;

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

    //플로팅 버튼
    private FrameLayout mainContainer;
    private TextView writeButton;   //글쓰기 버튼
    private TextView courseAdd;     //코스짜기 버튼
    private LinearLayout writeContainer;
    private TextView boardAdd;      //새글 버튼
    private TextView continuousAdd; //이어쓰기
    private Animation up, down;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mykockoc, container, false);

        init(view);

        Handler handler = new GetUserInfoHandler();
        Thread thread = new GetUserInfoThread(handler);
        thread.start();

        Handler handler1 = new BoardHandler();
        Thread thread1 = new ProfileBoardThread(handler1);
        thread1.start();

        return view;
    }

    private void init(View view) {
        View profile = view.findViewById(R.id.profile_container);
        View recycler = view.findViewById(R.id.course_recycler_layout);

        //profile
        profileImage = (ImageView) profile.findViewById(R.id.profile_image);
        nickName = (TextView) profile.findViewById(R.id.nick_name);
        followButton = (Button) profile.findViewById(R.id.btn_follow);
        followButton.setVisibility(View.INVISIBLE);

        scrapButton = (LinearLayout) profile.findViewById(R.id.scrap_button);
        neighborButton = (LinearLayout) profile.findViewById(R.id.neighbor_button);
        courseButton = (LinearLayout) profile.findViewById(R.id.course_button);

        scrapCount = (TextView) profile.findViewById(R.id.scrap_count);
        neighborCount = (TextView) profile.findViewById(R.id.neighbor_count);
        courseCount = (TextView) profile.findViewById(R.id.course_count);

        listenerSet();

        //recyclerView
        recyclerView = (RecyclerView) recycler.findViewById(R.id.recycler_view);
        adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new BoardItemClickListener(), getActivity());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
        //플로팅 버튼
        mainContainer = (FrameLayout)view.findViewById(R.id.mykockoc_main_container);
        writeButton = (TextView)view.findViewById(R.id.mykockoc_write_button);
        courseAdd = (TextView)view.findViewById(R.id.mykockoc_course_add);
        writeContainer = (LinearLayout)view.findViewById(R.id.mykockoc_write_container);
        boardAdd = (TextView)view.findViewById(R.id.mykockoc_board_add);
        continuousAdd = (TextView)view.findViewById(R.id.mykockoc_continuous_add);
        up = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_bottom_menu_up);
        down = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_bottom_menu_down);
        buttonListenerSet();

    }

    private void listenerSet() {
        View.OnClickListener count_listener = new CountClickListener();
        scrapButton.setOnClickListener(count_listener);
        neighborButton.setOnClickListener(count_listener);
        courseButton.setOnClickListener(count_listener);
        nickName.setOnClickListener(new NicknameClickListener());
        profileImage.setOnClickListener(new ProfileImgClickListener());
    }

    private class CountClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(scrapButton)) {
                Intent scrap_intent = new Intent(getActivity(), ScrapActivity.class);
                startActivityForResult(scrap_intent, SCRAP_REQUEST_CODE);
            } else if (v.equals(neighborButton)) {
                Intent neighbor_intent = new Intent(getContext(), NeighborActivity.class);
                neighbor_intent.putExtra("userNo", BasicValue.getInstance().getUserNo());
                startActivityForResult(neighbor_intent, NEIGHBOR_REQUEST_CODE);
            } else if (v.equals(courseButton)) {
                Intent course_intent = new Intent(getContext(), CourseActivity.class);
                startActivityForResult(course_intent, COURSE_REQUEST_CODE);
            }
        }
    }

    /**
     * ProfileClickListener
     * 프로필 사진 변경 -> 내장 갤러리
     */
    private class ProfileImgClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PROFILE_IMG_SET); // 메인 액티비티로 result 보냄
        }
    }

    private class NicknameClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(getActivity(), NicknameChangeActivity.class), NICKNAME_UPDATE);
        }
    }

    private class BoardItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            BoardWithImage boardWithImage = adapter.getItems().get(position);

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("boardNo", boardWithImage.getBasicAttributes().getBoardNo());
            intent.putExtra("courseNo", boardWithImage.getBasicAttributes().getCourseNo());
            intent.putExtra("board_userNo", BasicValue.getInstance().getUserNo());

            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler handler;
        Thread thread;

        switch (requestCode) {
            case PROFILE_IMG_SET:
                //ProfileImgReceiveHandler 에서 서버로 프로필 이미지 전송
                handler = new ProfileImgReceiveHandler();
                thread = new MyProfileImgThread(handler, data, getActivity());
                thread.start();
                break;
            case NICKNAME_UPDATE:
                handler = new GetUserInfoHandler();
                thread = new GetUserInfoThread(handler);
                thread.start();
                break;
            case SCRAP_REQUEST_CODE:
                handler = new GetUserInfoHandler();
                thread = new GetUserInfoThread(handler);
                thread.start();
                break;
            case NEIGHBOR_REQUEST_CODE:
                handler = new GetUserInfoHandler();
                thread = new GetUserInfoThread(handler);
                thread.start();
                break;
            case COURSE_REQUEST_CODE:
                handler = new GetUserInfoHandler();
                thread = new GetUserInfoThread(handler);
                thread.start();
                break;
            default:
                break;
        }
    }


    private class GetUserInfoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ProfileData data = (ProfileData) msg.getData().getSerializable("THREAD");
            BasicValue.getInstance().setUserNickname(data.getNickName());

            nickName.setText(data.getNickName());
            scrapCount.setText(data.getScrapCount() + "");
            neighborCount.setText(data.getNeighborCount() + "");
            courseCount.setText(data.getCourseCount() + "");
            Glide.with(getContext()).load(BasicValue.getInstance().getUrlHead() + "board_image/" + BasicValue.getInstance().getUserNo() + "/profile.jpg")
                    .error(R.drawable.default_profile).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .bitmapTransform(new CropCircleTransformation(Glide.get(getContext()).getBitmapPool())).into(profileImage);
            ((MainActivity) getActivity()).set_navProfileName(data.getNickName());
        }
    }

    private class BoardHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<BoardWithImage> boardWithImages = (ArrayList<BoardWithImage>) msg.getData().getSerializable("THREAD");

            adapter.setItems(boardWithImages);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * ProfileImgReceiveHandler
     * 프로필 사진 변경 시 호출
     */
    private class ProfileImgReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Glide.with(getActivity())
                    .load(BasicValue.getInstance().getUrlHead() + "board_image/" + BasicValue.getInstance().getUserNo() + "/profile.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // glide 캐시 초기화
                    .skipMemoryCache(true)
                    .error(R.drawable.default_profile)
                    .bitmapTransform(new CropCircleTransformation(Glide.get(getContext()).getBitmapPool())).into(profileImage);
            ((MainActivity) getActivity()).set_navProfileImg();
        }
    }
    private class FloatingMenuListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(writeButton)) {
                if (writeContainer.getVisibility() == View.INVISIBLE)
                    buttonLayoutUpAnimation();
                else buttonLayoutDownAnimation();
            } else if (v.equals(boardAdd)) {
                Intent intent = new Intent(getActivity(), NewWriteActivity.class);
                writeButton.callOnClick();
                getActivity().startActivityForResult(intent, MainActivity.NEW_WRITE_REQUEST_CODE);
            } else if (v.equals(courseAdd)) {
                Intent intent = new Intent(getActivity(), CourseTitleActivity.class);
                startActivity(intent);
            } else if (v.equals(continuousAdd)) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("flag", CourseActivity.CONTINUOUS_FLAG);
                writeButton.callOnClick();
                getActivity().startActivityForResult(intent, MainActivity.CONTINUOUS_WRITE_REQUEST_CODE);
            }
        }
    }
    private void buttonListenerSet() {
        View.OnClickListener listener = new FloatingMenuListener();
        writeButton.setOnClickListener(listener);
        boardAdd.setOnClickListener(listener);
        courseAdd.setOnClickListener(listener);
        continuousAdd.setOnClickListener(listener);
    }

    private void buttonLayoutUpAnimation() {
        writeButton.setBackgroundResource(R.drawable.story_close_button);
        mainContainer.setBackgroundResource(R.color.translucent_80);
        writeContainer.setVisibility(View.VISIBLE);
        writeContainer.startAnimation(up);
    }

    private void buttonLayoutDownAnimation() {
        writeButton.setBackgroundResource(R.drawable.story_write_button);
        mainContainer.setBackgroundResource(R.color.transparency);
        writeContainer.setVisibility(View.INVISIBLE);
        writeContainer.startAnimation(down);
    }
}