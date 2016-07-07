package com.kocapplication.pixeleye.kockocapp.navigation.notice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Neighbor;
import com.kocapplication.pixeleye.kockocapp.model.NoticeItem;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by pixeleye02 on 2016-06-30.
 */
public class NoticeActivity extends BaseActivityWithoutNav {
    private RecyclerView noticeRecyclerView;
    private int userNo = BasicValue.getInstance().getUserNo();
    private View containView;
    private NoticeRecyclerAdapter noticeRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("알림", Color.WHITE);

        container.setLayoutResource(R.layout.activity_notice);
        containView = container.inflate();

        View noticeView = containView.findViewById(R.id.recycler_layout_notice);
        noticeRecyclerView = (RecyclerView) noticeView.findViewById(R.id.recycler_view);

        ArrayList<NoticeItem> notice = JsonParser.getNoticeItem(JspConn.notice(userNo));
        noticeRecyclerAdapter = new NoticeRecyclerAdapter(notice, NoticeActivity.this, new NoticeClickListener());
        noticeRecyclerView.setAdapter(noticeRecyclerAdapter);
        LinearLayoutManager manager_follower = new LinearLayoutManager(NoticeActivity.this , LinearLayoutManager.VERTICAL,false);
        noticeRecyclerView.setLayoutManager(manager_follower);
        noticeRecyclerView.setHasFixedSize(true);
    }

    private class NoticeClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = noticeRecyclerView.getChildLayoutPosition(v);
            int boardNo = noticeRecyclerAdapter.getNotices().get(position).getBoardNo();

            Intent detail_intent = new Intent(NoticeActivity.this, DetailActivity.class);
            detail_intent.putExtra("boardNo",boardNo);
            startActivity(detail_intent);
        }
    }
}
