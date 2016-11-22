package com.kocapplication.pixeleye.kockocapp.main.tour.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.TourDetailData;
import com.kocapplication.pixeleye.kockocapp.util.GlobalApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Hyeongpil on 2016-09-23.
 */
public class TourDetailThread extends Thread {
    final static String TAG = "TourDetailThread";
    private Context mContext;
    private Handler handler;
    private TourDetailRepo tourDetailRepo;
    private TourImageRepo tourImageRepo;
    private TourDetailData data;
    private String apiKey = GlobalApplication.getInstance().getResources().getString(R.string.tour_api_key);
    private String os = "AND"; // 운영체제 (IOS = 아이폰, AND = 안드로이드, WIN = 윈도우폰)
    private String appName = "Kockoc";
    private String contentId = "";
    private String contentTypeId = "";
    private String type = "json"; // 지우면  xml로 받아옴
    private String overviewYN = "Y";
    private String defaultYN = "Y";
    private boolean detail = false; // 상세정보 스레드 끝나면 true
    private boolean img = false; // 이미지 스레드 끝나면 true

    public TourDetailThread(Context mContext, String contentId, String contentTypeId, Handler handler) {
        this.mContext = mContext;
        this.contentId = contentId;
        this.contentTypeId = contentTypeId;
        this.handler = handler;
    }
    @Override
    public void run() {
        super.run();
        data = new TourDetailData();
        try {apiKey = URLDecoder.decode(apiKey,"UTF-8");} catch (UnsupportedEncodingException e) {e.printStackTrace();}

        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/").addConverterFactory(GsonConverterFactory.create()).build();
        TourDetailRepo.tourDetailApiInterface service = client.create(TourDetailRepo.tourDetailApiInterface.class);
        Call<TourDetailRepo> call = service.get_tour_detail_retrofit(apiKey,os,appName,contentId,contentTypeId,overviewYN,defaultYN,type);
        call.enqueue(new Callback<TourDetailRepo>() {
            @Override
            public void onResponse(Call<TourDetailRepo> call, Response<TourDetailRepo> response) {
                Log.e(TAG,"상세정보 raw :"+response.raw());
                if(response.isSuccessful()){
                    tourDetailRepo = response.body();
                    TourDetailRepo.response.Result header = tourDetailRepo.getResponse().getHeader();
                    TourDetailRepo.response.body.items items = tourDetailRepo.getResponse().getBody().getItems();
                    if(header.getResultCode().equals("0000")){
                        data.setText(items.getItem().getOverview());
                        data.setTitle(items.getItem().getTitle());
                        detail = true;
                        sendMsg();
                    }else{
                        Log.e(TAG,"상세정보 검색 실패");
                        Log.e(TAG,""+header.getResultCode()+"/"+header.getResultMsg());
                        Toast.makeText(mContext, "데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<TourDetailRepo> call, Throwable t) {
                Log.e(TAG,"상세정보 가져오기 실패");
                Log.e(TAG,""+call.request());
                Log.e(TAG,""+t.getMessage());
                Toast.makeText(mContext, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Retrofit client2 = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/").addConverterFactory(GsonConverterFactory.create()).build();
        TourImageRepo.tourImageApiInterface service2 = client2.create(TourImageRepo.tourImageApiInterface.class);
        Call<TourImageRepo> call2 = service2.get_tour_image_retrofit(apiKey,os,appName,contentId,contentTypeId,type);
        call2.enqueue(new Callback<TourImageRepo>() {
            @Override
            public void onResponse(Call<TourImageRepo> call, Response<TourImageRepo> response) {
                Log.e(TAG, "사진 raw :" + response.raw());
                if (response.isSuccessful()) {
                    tourImageRepo = response.body();
                    TourImageRepo.response.Result header = tourImageRepo.getResponse().getHeader();
                    TourImageRepo.response.body.items items = tourImageRepo.getResponse().getBody().getItems();
                    if (header.getResultCode().equals("0000")) {
                        for(int i = 0; i < items.getItem().size(); i++){
                            data.getImgList().add(items.getItem().get(i).getImg());
                            data.getThumbList().add(items.getItem().get(i).getThumnail());
                        }
                        img = true;
                        sendMsg();
                    } else {
                        Log.e(TAG, "사진정보 검색 실패");
                        Log.e(TAG, "" + header.getResultCode() + "/" + header.getResultMsg());
                        Toast.makeText(mContext, "사진 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TourImageRepo> call, Throwable t) {
                Log.e(TAG,"사진정보 가져오기 실패");
                Log.e(TAG,""+call.request());
                Log.e(TAG,""+t.getMessage());
                Toast.makeText(mContext, "사진 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void sendMsg(){
        if(detail && img){
            Bundle bundle = new Bundle();
            Message msg = Message.obtain();
            bundle.putSerializable("TourDetailThread",data);
            msg.setData(bundle);
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }
}
