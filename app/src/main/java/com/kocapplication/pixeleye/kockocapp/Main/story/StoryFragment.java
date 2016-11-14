package com.kocapplication.pixeleye.kockocapp.main.story;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.course.CourseActivity;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        init(view);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (writeContainer.getVisibility() == View.VISIBLE) {
                    buttonLayoutDownAnimation();
                    return true;
                }
                return false;
            }
        });

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

        View includeView = view.findViewById(R.id.course_recycler_layout);
        refreshLayout = (SwipeRefreshLayout) includeView.findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) includeView.findViewById(R.id.recycler_view);

        refreshLayout.setOnRefreshListener(new RefreshListener());

        if (initialData == null)
            adapter = new BoardRecyclerAdapter(new ArrayList<BoardWithImage>(), new ItemClickListener(),getActivity());
        else adapter = new BoardRecyclerAdapter(initialData, new ItemClickListener(),getActivity());

        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setOnScrollListener(new BottomRefreshListener());
    }

    public void refresh() {
        if (refreshLayout == null) return;
        refreshLayout.setRefreshing(true);
        new StoryThread(new StoryDataReceiveHandler()).start();
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

    public void deleteItem(int position){
        adapter.deleteItems(position);
        adapter.notifyDataSetChanged();
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            if (writeContainer.getVisibility() == View.INVISIBLE) {
                BoardWithImage boardWithImage = adapter.getItems().get(position);

                Log.i(TAG, boardWithImage.getBasicAttributes().getCourseNo() + "");

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("boardNo", boardWithImage.getBasicAttributes().getBoardNo());
                intent.putExtra("courseNo", boardWithImage.getBasicAttributes().getCourseNo());
                intent.putExtra("board_userNo", boardWithImage.getBasicAttributes().getUserNo());
                intent.putExtra("position",position);
                getActivity().startActivityForResult(intent, MainActivity.DETAIL_ACTIVITY_REQUEST_CODE);
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

    private class StoryDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                Snackbar.make(refreshLayout, "데이터를 불러오는데 실패하였습니다. 새로고침을 해주세요", Snackbar.LENGTH_SHORT).show();
                return;
            }

            ArrayList<BoardWithImage> boardWithImages = (ArrayList<BoardWithImage>) msg.getData().getSerializable("THREAD");

            ((MainActivity)getActivity()).getMainFragment().setStoryData(boardWithImages);

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

            if (msg.what == 0) {
                Snackbar.make(refreshLayout, "데이터를 불러오는데 실패하였습니다. 새로고침을 해주세요", Snackbar.LENGTH_SHORT).show();
                return;
            }

            ArrayList<BoardWithImage> boardWithImages = (ArrayList<BoardWithImage>) msg.getData().getSerializable("THREAD");

            List<BoardWithImage> initialData = adapter.getItems();
            initialData.addAll(boardWithImages);

            adapter.setItems(initialData);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

}
