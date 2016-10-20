package com.kocapplication.pixeleye.kockocapp.main.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivityWithoutNav {
    final static String TAG = "SearchActivity";
    EditText editText;
    private Thread searchThread;
    private SearchRun searchRun;
    private ListView searchList;
    private Button searchBtn;
    private String intentKeyword = "";
    private View containView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container.setLayoutResource(R.layout.activity_search);
        containView = container.inflate();
        actionBarTitleSet("검색", Color.WHITE);
        getComponents(containView);
        setEvents();
    }

    void getComponents(View containView){

        editText = (EditText)containView.findViewById(R.id.searchText);
        searchList = (ListView)containView.findViewById(R.id.searchList);
        searchBtn = (Button)containView.findViewById(R.id.searchBtn);
        //코스에서 키워드 넘겨주는지 확인
        try {
            intentKeyword = getIntent().getStringExtra("keyword");
            if (!intentKeyword.equals("")){
                Keyword.mainStr = "#"+intentKeyword;
                startActivity(new Intent(SearchActivity.this, SearchBoardActivity.class));
                finish();
            }
        } catch (NullPointerException e) {Log.d(TAG,"intentKeyword null"+e.getMessage());}
    }
    void setEvents(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                makeSearchThread(s.toString());
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyword.mainStr = editText.getText().toString();

                if (!Keyword.mainStr.contains("#")) {
                    Keyword.mainStr = "#" + Keyword.mainStr;
                }
                startActivity(new Intent(SearchActivity.this, SearchBoardActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    void makeSearchThread(String keyword){
        if(searchThread == null){       //처음 텍스트를 입력하면 스레드를 시작한다.
            searchRun = new SearchRun(keyword,searchList,this);
            searchThread= new Thread(searchRun);
            searchThread.start();
        }
        else {      //검색한 것의 연관된 태그를 보여준다.
            searchRun.setKeyword(keyword);
        }
    }

    public class SearchRun implements Runnable {
        String keyword = "";
        boolean searched;
        boolean threadEnd = false;
        ListView searchList;
        SearchAdapter searchAdapter;
        SearchActivity searchActivity;

        SearchRun(String keyword,ListView searchList,SearchActivity searchActivity){
            this.keyword = keyword;
            this.searchActivity = searchActivity;
            this.searchList = searchList;
            searched = false;
        }


        @Override
        public void run() {
            while (!threadEnd){
                if(!searched){
                    if(!keyword.equals("")){
                        //검색 resultArr 갱신
                        String result = searchSeverTable(keyword);
                        Keyword.str.clear();
                        searchParser(result);
                        updateUI();
                        searched = true;
                    }else{
                        Keyword.str.clear();
                        updateUI();
                    }
                }
            }
        }

        void updateUI(){
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    renewListView();//listview 배열 전달후 갱신
                }
            });
        }
        public void setKeyword(String keyword){
            if(!this.keyword.equals(keyword)){
                searched = false;
                this.keyword = keyword;
            }
        }

        private void renewListView(){
            if (Keyword.str!=null){
                if(searchAdapter == null){
                    searchAdapter = new SearchAdapter(this.searchActivity,Keyword.str);//resultArr을 이용하여 어댑터 생성
                    searchList.setAdapter(searchAdapter);
                }else{
                    //adapter가 초기화 되어있고 갱신된경우
                    searchAdapter.notifyDataSetChanged();
                }
            }
        }

        private void searchParser(String result){
            try{
                JSONObject obj = new JSONObject(result);
                JSONArray arr = obj.getJSONArray("result");
                int size = arr.length();
                for(int i= 0; i<size;i++){
                    Keyword.str.add(arr.getString(i));
                }
            }catch (Exception e){
            }
        }
        private String searchSeverTable(String keyword){
            String result="";
            try
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpClient client = new DefaultHttpClient();
                String postURL = BasicValue.getInstance().getUrlHead()+"Board/search.jsp";
                HttpPost post = new HttpPost(postURL);
                List<NameValuePair> params = new ArrayList<>();

                params.add(new BasicNameValuePair("keyword",keyword));

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);
                HttpResponse response = client.execute(post);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
                String line;

                while((line = bufferedReader.readLine())!=null){
                    result+=line;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }
    }
}