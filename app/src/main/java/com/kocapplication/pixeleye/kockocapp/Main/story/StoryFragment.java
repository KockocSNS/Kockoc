package com.kocapplication.pixeleye.kockocapp.main.story;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import java.util.ArrayList;

/**
 * Created by Han_ on 2016-06-20.
 */
public class StoryFragment extends Fragment {
    private final String TAG = "STORY_FRAGMENT";

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;

    private Animation up,down;

    private FrameLayout mainContainer;
    private TextView writeButton;   //글쓰기 버튼
    private TextView courseAdd;     //코스짜기 버튼
    private LinearLayout writeContainer;
    private TextView boardAdd;      //새글 버튼
    private TextView continuousAdd; //이어쓰기

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        init(view);

        Handler handler = new StoryDataReceiveHandler();
        Thread thread = new StoryThread(handler, BasicValue.getInstance().getUrlHead() + "News/readNews.jsp");
        thread.start();

        return view;
    }

    private void init(View view) {
        up = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_bottom_menu_up);
        down = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_bottom_menu_up);

        mainContainer = (FrameLayout) view.findViewById(R.id.story_main_container);
        writeButton = (TextView) view.findViewById(R.id.story_write_button);
        writeContainer = (LinearLayout) view.findViewById(R.id.story_write_container);
        boardAdd = (TextView) view.findViewById(R.id.story_board_add);
        courseAdd = (TextView) view.findViewById(R.id.story_course_add);
        continuousAdd = (TextView) view.findViewById(R.id.story_continuous_add);
        buttonListenerSet();

        View includeView = view.findViewById(R.id.recycler_layout);
        refreshLayout = (SwipeRefreshLayout) includeView.findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) includeView.findViewById(R.id.recycler_view);

        refreshLayout.setOnRefreshListener(new RefreshListener());

        adapter = new BoardRecyclerAdapter(new ArrayList<Board>(), new ItemClickListener());
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
    }

    private void buttonListenerSet() {
        View.OnClickListener listener = new FloatingMenuListener();
        writeButton.setOnClickListener(listener);
        boardAdd.setOnClickListener(listener);
        courseAdd.setOnClickListener(listener);
        continuousAdd.setOnClickListener(listener);
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            Toast.makeText(getActivity(), "asdasd", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
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

    private class FloatingMenuListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(writeButton)) {
                if (writeContainer.getVisibility() == View.INVISIBLE) {
                    writeButton.setBackgroundResource(R.drawable.story_close_button);
                    mainContainer.setBackgroundResource(R.color.translucent_80);
                    writeContainer.setVisibility(View.VISIBLE);
                    writeContainer.startAnimation(up);
                } else {
                    writeButton.setBackgroundResource(R.drawable.story_write_button);
                    mainContainer.setBackgroundResource(R.color.transparency);
                    writeContainer.setVisibility(View.INVISIBLE);
                    writeContainer.startAnimation(down);
                }
            }

            else if (v.equals(boardAdd)) {
//                Intent intent = new Intent(getActivity(), );
//                writeButton.callOnClick();
//                startActivityForResult(intent);
            }

            else if (v.equals(courseAdd)) {
//                Intent intent = new Intent();
//                startActivity(intent);
            }

            else if (v.equals(continuousAdd)) {
//                Intent intent = new Intent(getActivity(), );
//                writeButton.callOnClick();
//                startActivityForResult(intent);
            }
        }

    }
    private class StoryDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<Board> boards = (ArrayList<Board>) msg.getData().getSerializable("THREAD");

            adapter.setItems(boards);
            adapter.notifyDataSetChanged();
        }

    }
}
