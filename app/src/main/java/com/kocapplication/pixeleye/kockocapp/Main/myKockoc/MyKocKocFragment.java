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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.intro.IntroActivity;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.ProfileData;
import com.kocapplication.pixeleye.kockocapp.neighbor.NeighborActivity;
import com.kocapplication.pixeleye.kockocapp.scrap.ScrapActivity;

import java.util.ArrayList;

/**
 * Created by Han_ on 2016-06-21.
 */
public class MyKocKocFragment extends Fragment {
    private final String TAG = "MyKocKocFragment";
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

        Handler handler = new ProfileHandler();
        Thread thread = new MyKocKocProfileThread(handler);
        thread.start();

        Handler handler1 = new BoardHandler();
        Thread thread1 = new MyKocKocBoardThread(handler1);
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
        adapter = new BoardRecyclerAdapter(new ArrayList<Board>(), new ItemClickListener());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    private void listenerSet() {
        View.OnClickListener listener = new CountClickListener();
        scrapButton.setOnClickListener(listener);
        neighborButton.setOnClickListener(listener);
        courseButton.setOnClickListener(listener);
    }

    private class CountClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(scrapButton)) {
                Intent scrap_intent = new Intent(getContext(), ScrapActivity.class);
                startActivity(scrap_intent);
            } else if (v.equals(neighborButton)) {
                Intent neighbor_intent = new Intent(getContext(), NeighborActivity.class);
                startActivity(neighbor_intent);
            } else if (v.equals(courseButton)) {

            }
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            Board board = adapter.getItems().get(position);

            Intent intent = new Intent(getActivity(), DetailActivity.class);
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

            scrapCount.setText(data.getCourseCount() + "");
            neighborCount.setText(data.getNeighborCount() + "");
            courseCount.setText(data.getCourseCount() + "");
        }
    }

    private class BoardHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<Board> boards = (ArrayList<Board>) msg.getData().getSerializable("THREAD");

            adapter.setItems(boards);
            adapter.notifyDataSetChanged();
        }
    }
}