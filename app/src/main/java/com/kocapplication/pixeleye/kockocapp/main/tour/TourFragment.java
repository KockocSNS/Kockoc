package com.kocapplication.pixeleye.kockocapp.main.tour;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Hyeongpil on 2016-09-27.
 */
public class TourFragment extends Fragment {
    final static String TAG = "TourFragment";
    private RadioGroup rg_content;
    private RadioGroup rg_area;
    private RadioGroup rg_category;
    private Button btn_search;
    private String content = "";
    private String area = "";
    private String category = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour,container,false);

        init(view);

        return view;
    }

    private void init(final View view){
        rg_content = (RadioGroup) view.findViewById(R.id.tour_radio_content);
        rg_area = (RadioGroup) view.findViewById(R.id.tour_radio_area);
        rg_category = (RadioGroup) view.findViewById(R.id.tour_radio_category);
        btn_search = (Button) view.findViewById(R.id.tour_btn_search);
        setRadioListener(view);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //버튼에서 text를 받아 코드로 변환
                Log.d(TAG,"content :"+content);
                Log.d(TAG,"area :"+area);
                Log.d(TAG,"category :"+category);

                if(!content.equals("")){ // Content는 반드시 선택되어야함
                    Handler handler = new Handler(new TourDataHandleCallback());
                    Thread areaThread = new AreaThread(getActivity(), content, area, category, handler); // 뒤의 지역코드를 화면에서 받아와야함
                    areaThread.start();
                }else{
                    Toast.makeText(getActivity(), "대분류를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class TourDataHandleCallback implements Handler.Callback{
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    }

    private void setRadioListener(final View view){
        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                content = getTourCode(((RadioButton) view.findViewById(checkedId)).getText().toString());
            }
        });
        rg_area.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                area = getTourCode(((RadioButton) view.findViewById(checkedId)).getText().toString());
            }
        });
        rg_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                category = getTourCode(((RadioButton) view.findViewById(checkedId)).getText().toString());
            }
        });
    }

    private String getTourCode(String content){
        switch (content){
            case "관광지": return "12";
            case "문화시설": return "14";
            case "축제/공연": return "15";
            case "레저": return "29";
            case "서울": return "1";
            case "인천": return "2";
            case "대전": return "3";
            case "대구": return "4";
            case "광주": return "5";
            case "부산": return "6";
            case "울산": return "7";
            case "세종": return "8";
            case "경기": return "31";
            case "강원": return "32";
            case "충북": return "33";
            case "충남": return "34";
            case "경북": return "35";
            case "경남": return "36";
            case "전북": return "37";
            case "전남": return "38";
            case "제주": return "39";
            case "자연": return "A01";
            case "인문": return "A02";
            case "레포츠": return "A03";
            case "쇼핑": return "A04";
            case "음식": return "A05";
            case "숙박": return "B02";
            case "추천코스": return "C01";
            default: return "";
        }
    }
}
