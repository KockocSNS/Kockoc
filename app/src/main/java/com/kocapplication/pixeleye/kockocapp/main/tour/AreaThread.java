package com.kocapplication.pixeleye.kockocapp.main.tour;

import android.content.Context;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
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
public class AreaThread extends Thread {
    final static String TAG = "AreaThread";
    private Context mContext;
    private AreaRepo areaRepo;
    private String apiKey = GlobalApplication.getGlobalApplicationContext().getResources().getString(R.string.tour_api_key);
    private String arrange = "B"; //정렬 구분 (A = 제목순, B = 조회순, C = 수정일순, D = 생성일순)
    private String os = "AND"; // 운영체제 (IOS = 아이폰, AND = 안드로이드, WIN = 윈도우폰)
    private String appName = "Kockoc";
    private String content = "12"; // 관광 타입 ID (12 = 관광지, 14 = 문화시설, 15 = 축제공연행사, 29 = 레포츠)
    private String area = "33"; //지역코드 (1 = 서울, 2 = 인천, 3 = 대전, 4 = 대구, 5 = 광주, 6= 부산, 7 = 울산, 8 =세종시, 31 = 경기도, 32 = 강원도, 33 = 충북, 34 = 충남, 35 = 경북, 36 = 경남, 37 = 전북, 38 = 전남, 39 = 제주)
    private String category = ""; // 대분류1 (A01 = 자연, A02 = 인문, A03 = 레포츠, A04 = 쇼핑, A05 = 음식, B02 = 숙박, C01 = 추천코스)
    private String type = "json"; // 지우면  xml로 받아옴

    public AreaThread(Context mContext, String content, String area, String category) {
        this.mContext = mContext;
        this.content = content;
        this.area = area;
        this.category = category;
    }

    @Override
    public void run() {
        super.run();

        try {apiKey = URLDecoder.decode(apiKey,"UTF-8");} catch (UnsupportedEncodingException e) {e.printStackTrace();}

        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/").addConverterFactory(GsonConverterFactory.create()).build();
        AreaRepo.areaApiInterface service = client.create(AreaRepo.areaApiInterface.class);
        Call<AreaRepo> call = service.get_area_retrofit(apiKey,arrange,os,appName,content,area,category,type);
        call.enqueue(new Callback<AreaRepo>() {
            @Override
            public void onResponse(Call<AreaRepo> call, Response<AreaRepo> response) {
                Log.e(TAG,"raw :"+response.raw());
                if(response.isSuccessful()){
                    areaRepo = response.body();
                    AreaRepo.response.Result header = areaRepo.getResponse().getHeader();
                    AreaRepo.response.body body= areaRepo.getResponse().getBody();

                    if(header.getResultCode().equals("0000")){
                        Log.e(TAG,"성공");
                    }else{
                        Log.e(TAG,"지역정보 검색 실패");
                        Log.e(TAG,""+header.getResultCode()+"/"+header.getResultMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<AreaRepo> call, Throwable t) {
                Log.e(TAG,"관광 지역 가져오기 실패");
                Log.e(TAG,""+call.request());
            }
        });
    }
}
