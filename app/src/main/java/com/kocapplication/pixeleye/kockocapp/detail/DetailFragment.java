package com.kocapplication.pixeleye.kockocapp.detail;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import org.apmem.tools.layouts.FlowLayout;

/**
 * Created by hp on 2016-06-21.
 */
public class DetailFragment extends Fragment {
    final static String TAG = "DetailFragment";
    DetailPageData detailPageData;
    DetailRecyclerAdapter adapter;

    LinearLayout ll_profile;
    LinearLayout ll_htmlInfo;
    LinearLayout ll_board_img;
    LinearLayout ll_board_map;
    LinearLayout ll_comment_menu;
    LinearLayout ll_bgLayout;
    FlowLayout fl_board_hashtag;
    RecyclerView rv_comment_list;

    ToggleButton btn_like;
    Spinner course_spinner;

    TextView course_title;
    TextView profile_nickname;
    TextView profile_date;
    TextView board_text;
    TextView html_title;
    TextView html_desc;
    TextView comment_scrap;
    TextView comment_count;
    TextView comment_link;

    ImageView profile_img;
    ImageView html_img;
    ImageView board_mainimg;
    ImageView board_courses;

    private int boardNo;
    private int courseNo;
    LayoutInflater mInflater;


    public DetailFragment(){super();}

    @SuppressLint("ValidFragment")
    public DetailFragment(int boardNo,int courseNo){
        this.boardNo = boardNo;
        this.courseNo = courseNo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_content,container,false);
        this.mInflater = inflater;

        init(view);

        detailPageData = new DetailPageData();

        //DetailThread 에서 데이터를 받아옴
        Handler handler = new DetailDataReceiveHandler();
        Thread thread = new DetailThread(handler,boardNo,courseNo);
        thread.start();

        return view;
    }
    private void init(View view){
        ll_profile = (LinearLayout)view.findViewById(R.id.ll_profile);
        ll_htmlInfo = (LinearLayout)view.findViewById(R.id.ll_htmlInfo);
        ll_board_img = (LinearLayout)view.findViewById(R.id.ll_detail_content_imgViewList);
        ll_board_map = (LinearLayout)view.findViewById(R.id.ll_detail_content_maps);
        ll_comment_menu = (LinearLayout)view.findViewById(R.id.ll_comment_menu);
        ll_bgLayout = (LinearLayout)view.findViewById(R.id.ll_bg_detail_up);

        btn_like = (ToggleButton)view.findViewById(R.id.toggle_detail_content_like);
        course_spinner = (Spinner)view.findViewById(R.id.course_spinner);
        course_title = (TextView)view.findViewById(R.id.course_title);
        profile_nickname = (TextView)view.findViewById(R.id.tv_detail_inner_up_nickname);
        profile_date = (TextView)view.findViewById(R.id.tv_detail_inner_up_date);
        board_text = (TextView)view.findViewById(R.id.tv_detail_content_text);
        html_title = (TextView)view.findViewById(R.id.tv_detail_content_htmlTitle);
        html_desc = (TextView)view.findViewById(R.id.tv_detail_content_htmlDesc);
        comment_scrap = (TextView)view.findViewById(R.id.tv_detail_comment_scrap);
        comment_count = (TextView)view.findViewById(R.id.tv_detail_comment_count);
        comment_link = (TextView)view.findViewById(R.id.tv_detail_comment_link);
        profile_img = (ImageView)view.findViewById(R.id.img_detail_inner_up_profile);
        html_img = (ImageView)view.findViewById(R.id.tv_detail_content_htmlImg);
        board_mainimg = (ImageView)view.findViewById(R.id.img_detail_content_main_img);
        board_courses = (ImageView)view.findViewById(R.id.iv_detail_content_courses);
        fl_board_hashtag = (FlowLayout)view.findViewById(R.id.fl_detail_content_tag);
        View includeView = view.findViewById(R.id.detail_commentlist_layout);
        rv_comment_list = (RecyclerView)includeView.findViewById(R.id.rv_detail_commentlist);
    }

    private void setData(DetailPageData data) {
        profile_nickname.setText(data.getUserName());
        profile_date.setText(data.getBoardDate());
        board_text.setText(data.getBoardText());
//        comment_scrap.setText(detailPageData.getScrapNumber());
//        comment_count.setText(detailPageData.getCommentArr().size());

        Log.e(TAG,"courseTitle :"+data.getCourse().get(0));
        //해시태그
        for (int l = 0; l < data.getHashTagArr().size(); l++) {
            TextView textTemp = new TextView(getActivity());
            textTemp.setTextColor(mInflater.getContext().getResources().getColor(R.color.innerTagColor));
            textTemp.setText(data.getHashTagArr().get(l));
            fl_board_hashtag.addView(textTemp);
        }
    }

    private void setImg(DetailPageData data) {
        //프로필 이미지
        Glide.with(getActivity()).load(BasicValue.getInstance().getUrlHead()+"board_image/" + data.getUserNo() + "/profile.jpg").into(profile_img);

        //게시글 이미지
        for(int i = 0;i < data.getBoardImgArr().size(); i++ ){
            //ImageView 생성
            ImageView temp = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            temp.setLayoutParams(params);
            temp.setAdjustViewBounds(true);
            temp.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Glide.with(getActivity()).load(BasicValue.getInstance().getUrlHead()+"board_image/" + data.getUserNo() + "/" + data.getBoardImgArr().get(i)).into(temp);
            ll_board_img.addView(temp);
        }
    }
    private void setCommentList(){
        adapter = new DetailRecyclerAdapter(detailPageData.getCommentArr(),getActivity());
        rv_comment_list.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_comment_list.setLayoutManager(manager);
        rv_comment_list.setHasFixedSize(true);
    }

    private class DetailDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            detailPageData = (DetailPageData) msg.getData().getSerializable("THREAD");
            setData(detailPageData);
            setImg(detailPageData);
            setCommentList();
        }
    }
}
