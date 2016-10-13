package com.kocapplication.pixeleye.kockocapp.main.tour;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.tour.retrofit.TourDetailThread;
import com.kocapplication.pixeleye.kockocapp.model.TourData;
import com.kocapplication.pixeleye.kockocapp.model.TourDetailData;


/**
 * Created by Hyeongpil on 2016-10-06.
 */
public class TourDetailActivity extends BaseActivityWithoutNav {
    final static String TAG = "TourDetailActivity";
    private View containView;
    private LinearLayout ll_tour_img_container;
    private TextView tour_text;
    private TextView tour_title;
    private TourData id_data;
    private TourDetailData data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container.setLayoutResource(R.layout.activity_tour_detail);
        containView = container.inflate();

        actionBarTitleSet("", Color.WHITE);

        getComponent(containView);
        getIdData();
        getDetailData();
    }

    private void getComponent(View containView){
        tour_text = (TextView)containView.findViewById(R.id.tv_tour_detail_text);
        tour_title = (TextView)containView.findViewById(R.id.tv_tour_detail_title);
        ll_tour_img_container = (LinearLayout)containView.findViewById(R.id.ll_tour_detail_image_container);
    }

    private void getIdData(){
        id_data = (TourData) getIntent().getSerializableExtra("data");
    }
    private void getDetailData(){
        Handler handler = new TourDetailReceiveHandler();
        Thread thread = new TourDetailThread(this, id_data.getContentID(), id_data.getContentTypeId(), handler);
        thread.start();
    }
    private void setData(){
        //이미지
        for (int i = 0; i < data.getImgList().size(); i++) {
            //ImageView 생성
            ImageView temp = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            temp.setLayoutParams(params);
            temp.setAdjustViewBounds(true);
            temp.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Glide.with(this).load(data.getImgList().get(i)).into(temp);
            ll_tour_img_container.addView(temp);
        }

        actionBarTitleSet(data.getTitle(), Color.WHITE);
        tour_title.setText(data.getTitle());
        tour_text.setText(Html.fromHtml(data.getText()));
    }


    private class TourDetailReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            data = (TourDetailData) msg.getData().getSerializable("TourDetailThread");
            setData();
        }
    }
}
