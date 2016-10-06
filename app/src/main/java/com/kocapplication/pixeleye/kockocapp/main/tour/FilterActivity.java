package com.kocapplication.pixeleye.kockocapp.main.tour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;

/**
 * Created by Hyeongpil on 2016-09-29.
 */
public class FilterActivity extends BaseActivityWithoutNav {
    final static String TAG = "FilterFragment";
    private RadioGroup rg_content;
    private RadioGroup rg_area;
    private RadioGroup rg_category;
    private Button btn_keyword_search;
    private Button btn_search;
    private EditText et_keyword;
    private String content = "";
    private String area = "";
    private String category = "";
    private View containView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container.setLayoutResource(R.layout.tour_filter);
        containView = container.inflate();
        actionBarTitleSet("필터", Color.WHITE);

        getComponent(containView);
    }

    private void getComponent(View containView){
        ButtonClickListener btn_clickListener = new ButtonClickListener();
        rg_content = (RadioGroup) containView.findViewById(R.id.tour_radio_content);
        rg_area = (RadioGroup) containView.findViewById(R.id.tour_radio_area);
        rg_category = (RadioGroup) containView.findViewById(R.id.tour_radio_category);
        et_keyword = (EditText) containView.findViewById(R.id.tour_searchText);
        btn_keyword_search = (Button) containView.findViewById(R.id.tour_btn_search_keyword);
        btn_search = (Button) containView.findViewById(R.id.tour_btn_search);
        btn_keyword_search.setOnClickListener(btn_clickListener);
        btn_search.setOnClickListener(btn_clickListener);
        setRadioListener();
    }

    private void setRadioListener(){
        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                content = getTourCode(((RadioButton) findViewById(checkedId)).getText().toString());
            }
        });
        rg_area.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                area = getTourCode(((RadioButton) findViewById(checkedId)).getText().toString());
            }
        });
        rg_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                category = getTourCode(((RadioButton) findViewById(checkedId)).getText().toString());
            }
        });
    }

    private String getTourCode(String content){
        switch (content){
            case "관광지": return "12";
            case "문화시설": return "14";
            case "축제/공연": return "15";
            case "레저": return "29";
            case "전체": return "";
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
    private class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tour_btn_search:
                    Log.d(TAG,"content :"+content);
                    Log.d(TAG,"area :"+area);
                    Log.d(TAG,"category :"+category);

                    if(!content.equals("")){
                        Intent intent = new Intent();
                        intent.putExtra("flag","area");
                        intent.putExtra("content",content);
                        intent.putExtra("area",area);
                        intent.putExtra("category",category);
                        setResult(TourFragment.TOURFRAGMENT,intent);
                        finish();
                    }else{
                        Toast.makeText(FilterActivity.this, "관광 타입을 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.tour_btn_search_keyword:
                    String keyword = et_keyword.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("flag","keyword");
                    intent.putExtra("keyword",keyword);
                    setResult(TourFragment.TOURFRAGMENT,intent);
                    finish();
                    break;
            }
        }
    }
}
