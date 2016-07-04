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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;


import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Han_ on 2016-06-21.
 */
public class MyKocKocFragment extends Fragment {
    private final String TAG = "MyKocKocFragment";
    private final int PROFILE_IMG_SET = 1001;
    private final int NICKNAME_UPDATE = 1002;
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
        View recycler = view.findViewById(R.id.recycler_layout);

        //profile
        profileImage = (ImageView) profile.findViewById(R.id.profile_image);
        nickName = (TextView) profile.findViewById(R.id.nick_name);
        followButton = (Button) profile.findViewById(R.id.btn_follow);

        scrapButton = (LinearLayout) profile.findViewById(R.id.scrap_button);
        neighborButton = (LinearLayout) profile.findViewById(R.id.neighbor_button);
        courseButton = (LinearLayout) profile.findViewById(R.id.course_button);

        scrapCount = (TextView) profile.findViewById(R.id.scrap_count);
        neighborCount = (TextView) profile.findViewById(R.id.neighbor_count);
        courseCount = (TextView) profile.findViewById(R.id.course_count);

        listenerSet();

        //recyclerView
        recyclerView = (RecyclerView) recycler.findViewById(R.id.recycler_view);
        adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new BoardItemClickListener());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    private void listenerSet() {
        View.OnClickListener count_listener = new CountClickListener();
        scrapButton.setOnClickListener(count_listener);
        neighborButton.setOnClickListener(count_listener);
        courseButton.setOnClickListener(count_listener);
        nickName.setOnClickListener(new NicknameClickListener());

        View.OnClickListener profile_listener = new ProfileImgClickListener();
        profileImage.setOnClickListener(profile_listener);
    }

    private class CountClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(scrapButton)) {
                Intent scrap_intent = new Intent(getContext(), ScrapActivity.class);
                startActivity(scrap_intent);
            } else if (v.equals(neighborButton)) {
                Intent neighbor_intent = new Intent(getContext(), NeighborActivity.class);
                neighbor_intent.putExtra("userNo",BasicValue.getInstance().getUserNo());
                startActivity(neighbor_intent);
            } else if (v.equals(courseButton)) {
                Intent course_intent = new Intent(getContext(), CourseActivity.class);
                startActivity(course_intent);
            }
        }
    }

    /**
     * ProfileClickListener
     * 프로필 사진 변경 -> 내장 갤러리
     */
    private class ProfileImgClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PROFILE_IMG_SET); // 메인 액티비티로 result 보냄
        }
    }
    private class NicknameClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(getActivity(), NicknameChangeActivity.class),NICKNAME_UPDATE);
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
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PROFILE_IMG_SET:
                //ProfileImgReceiveHandler 에서 서버로 프로필 이미지 전송
                Handler handler = new ProfileImgReceiveHandler();
                Thread thread = new MyProfileImgThread(handler,data,getActivity());
                thread.start();
                break;
            case NICKNAME_UPDATE:
                Handler userinfo_handler = new GetUserInfoHandler();
                Thread userinfo_thread = new GetUserInfoThread(userinfo_handler);
                userinfo_thread.start();
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
            Glide.with(getContext()).load(BasicValue.getInstance().getUrlHead()+"board_image/"+ BasicValue.getInstance().getUserNo() + "/profile.jpg")
                    .error(R.drawable.default_profile).bitmapTransform(new CropCircleTransformation(Glide.get(getContext()).getBitmapPool())).into(profileImage);
            ((MainActivity)getActivity()).set_navProfileName(data.getNickName());
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
    private class ProfileImgReceiveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Glide.with(getActivity())
                    .load(BasicValue.getInstance().getUrlHead()+"board_image/"+ BasicValue.getInstance().getUserNo() + "/profile.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // glide 캐시 초기화
                    .skipMemoryCache(true)
                    .error(R.drawable.default_profile)
                    .bitmapTransform(new CropCircleTransformation(Glide.get(getContext()).getBitmapPool())).into(profileImage);
            ((MainActivity)getActivity()).set_navProfileImg();
        }
    }
}