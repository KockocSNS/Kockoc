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
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.course.CourseActivity;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseTitleActivity;
import com.kocapplication.pixeleye.kockocapp.write.newWrite.NewWriteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-06-20.
 */
public class StoryFragment extends Fragment {
    private final String TAG = "STORY_FRAGMENT";

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;

    private Animation up, down;

    private FrameLayout mainContainer;
    private TextView writeButton;   //글쓰기 버튼
    private TextView courseAdd;     //코스짜기 버튼
    private LinearLayout writeContainer;
    private TextView boardAdd;      //새글 버튼
    private TextView continuousAdd; //이어쓰기

    private ArrayList<BoardWithImage> initialData;

    public static final int NEW_WRITE_REQUEST_CODE = 12433;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        init(view);

        JsonParser.getNoticeItem(JspConn.notice(BasicValue.getInstance().getUserNo()));

        if (initialData == null) {
            Handler handler = new StoryDataReceiveHandler();
            Thread thread = new StoryThread(handler);
            refreshLayout.setRefreshing(true);
            thread.start();
        }

        return view;
    }

    private void init(View view) {
        up = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_bottom_menu_up);
        down = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_bottom_menu_down);

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

        if (initialData == null) adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new ItemClickListener());
        else adapter = new BoardRecyclerAdapter(initialData, new ItemClickListener());

        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setOnScrollListener(new BottomRefreshListener());
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

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            new StoryThread(new StoryDataReceiveHandler()).start();
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            if (writeContainer.getVisibility() == View.INVISIBLE) {
                BoardWithImage boardWithImage = adapter.getItems().get(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("boardNo", boardWithImage.getBasicAttributes().getBoardNo());
                intent.putExtra("courseNo", boardWithImage.getBasicAttributes().getCourseNo());
                intent.putExtra("board_userNo", boardWithImage.getBasicAttributes().getUserNo());
                startActivity(intent);
            } else {
                buttonLayoutDownAnimation();
            }
        }
    }

    private class BottomRefreshListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int LastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

            if ((LastVisibleItem) == adapter.getItems().size() - 1 && !refreshLayout.isRefreshing() && adapter.getItems().size() > 6) {
                refreshLayout.setRefreshing(true);
                Handler handler = new BottomRefreshHandler();
                Thread thread = new StoryThread(handler, initialData.get(initialData.size() - 1).getBasicAttributes().getBoardNo());
                thread.start();
            }
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
                startActivityForResult(intent, NEW_WRITE_REQUEST_CODE);
            } else if (v.equals(courseAdd)) {
                Intent intent = new Intent(getActivity(), CourseTitleActivity.class);
                startActivity(intent);
            } else if (v.equals(continuousAdd)) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("flag","이어쓰기");
                writeButton.callOnClick();
                startActivity(intent);
            }
        }

    }

    private class StoryDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<BoardWithImage> boardWithImages = (ArrayList<BoardWithImage>) msg.getData().getSerializable("THREAD");

            initialData = boardWithImages;
            adapter.setItems(boardWithImages);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    private class BottomRefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<BoardWithImage> boardWithImages = (ArrayList<BoardWithImage>) msg.getData().getSerializable("THREAD");

            List<BoardWithImage> initialData = adapter.getItems();
            initialData.addAll(boardWithImages);

            adapter.setItems(initialData);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }
}
