package com.kocapplication.pixeleye.kockocapp.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor.NeighborActivity;
import com.kocapplication.pixeleye.kockocapp.main.search.SearchActivity;
import com.kocapplication.pixeleye.kockocapp.navigation.notice.NoticeActivity;
import com.kocapplication.pixeleye.kockocapp.navigation.SettingActivity;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.SharedPreferenceHelper;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Han_ on 2016-06-20.
 */
public class BaseActivity extends AppCompatActivity {
    private final String TAG = "BASE_ACTIVITY";

    public static int SETTING_ACTIVITY_RESULT = 1003;

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBar actionBar;
    protected ImageView nav_profile_img;
    protected TextView nav_profile_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void init() {
        NavigationListener navigationListener = new NavigationListener();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View nav_header = navigationView.inflateHeaderView(R.layout.navigation_header);
        nav_profile_img = (ImageView) nav_header.findViewById(R.id.header_profile_image);
        nav_profile_name = (TextView) nav_header.findViewById(R.id.header_profile_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.nav_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        navigationView.setNavigationItemSelectedListener(navigationListener);
        //네비게이션 프로필 이미지
        Glide.with(this).load(BasicValue.getInstance().getUrlHead() + "board_image/" + BasicValue.getInstance().getUserNo() + "/profile.jpg")
                .error(R.drawable.default_profile).bitmapTransform(new CropCircleTransformation(Glide.get(this).getBitmapPool())).into(nav_profile_img);
    }

    /**
     * set_navProfile
     * 프로필 사진과 이름 설정
     */
    protected void set_navProfileImg() {
        Glide.with(BaseActivity.this)
                .load(BasicValue.getInstance().getUrlHead() + "board_image/" + BasicValue.getInstance().getUserNo() + "/profile.jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE) // glide 캐시 초기화
                .skipMemoryCache(true)
                .error(R.drawable.default_profile)
                .bitmapTransform(new CropCircleTransformation(Glide.get(BaseActivity.this).getBitmapPool())).into(nav_profile_img);
    }

    protected void set_navProfileName(String name) {
        nav_profile_name.setText(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_search:
                startActivity(new Intent(BaseActivity.this, SearchActivity.class));
                return true;
            case R.id.menu_neighbor:
                Intent neighbor_intent = new Intent(BaseActivity.this, NeighborActivity.class);
                neighbor_intent.putExtra("userNo", BasicValue.getInstance().getUserNo());
                startActivity(neighbor_intent);
                return true;
            case R.id.menu_alarm:
                Intent notice_intent = new Intent(BaseActivity.this, NoticeActivity.class);
                startActivity(notice_intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawers();
        else super.onBackPressed();

    }

    protected void actionBarTitleSet(String title, int color) {
        actionBar.setDisplayShowTitleEnabled(false);
        View view = getLayoutInflater().inflate(R.layout.actionbar_text_title, null);
        TextView titleView = (TextView) view.findViewById(R.id.actionbar_text_title);
        titleView.setText(title);
        titleView.setTextColor(color);

        actionBar.setCustomView(view);
    }

    protected void actionBarTitleSet() {
        actionBar.setDisplayShowTitleEnabled(false);
        View view = getLayoutInflater().inflate(R.layout.actionbar_image_title, null);

        actionBar.setCustomView(view);

    }

    /**
     * softKeyboardHide
     * 키보드를 숨기고 edittext 초기화
     *
     * @param editText
     */
    protected void softKeyboardHide(EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.setText("");
    }

    private class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_help:
                    Intent adviceActivity = new Intent(getApplicationContext(), AdviceActivity.class);
                    startActivity(adviceActivity);
                    return true;
                case R.id.nav_alarm:
                    Intent notice_intent = new Intent(BaseActivity.this, NoticeActivity.class);
                    startActivity(notice_intent);
                    return true;
                case R.id.nav_setting:
                    Intent setting_intent = new Intent(BaseActivity.this, SettingActivity.class);
                    startActivityForResult(setting_intent, SETTING_ACTIVITY_RESULT);
                    return true;
                case R.id.nav_logout:
                    UserManagement.requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                        }
                    });
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.putExtra("logout", 0);
                    autoLoginDisavleNaver();
                    autoLoginDisavleFacebook();
                    Log.i("naverauto","disable");
                    startActivity(intent);
                    finish();
                    try {
                        LoginManager.getInstance().logOut();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
            }
            return false;
        }
    }
    //페이스북 자동로그인 해제
    public  void autoLoginDisavleFacebook () {
        SharedPreferences facebookLoginState = getSharedPreferences("facebookLoginState", MODE_PRIVATE);
        SharedPreferences.Editor editor = facebookLoginState.edit();
        editor.putBoolean("isFacebookLogin",false);
        editor.commit();
    }
    //네이버 자동로그인 해제
    public void autoLoginDisavleNaver () {
        SharedPreferences naverLoginState = getSharedPreferences("naverLoginState", MODE_PRIVATE);
        SharedPreferences.Editor editor = naverLoginState.edit();
        editor.putBoolean("isNaverLogin",false);
        editor.commit();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTING_ACTIVITY_RESULT && resultCode == RESULT_OK) {
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                }
            });

            SharedPreferenceHelper helper = new SharedPreferenceHelper(BaseActivity.this, "pref");
            helper.clear();

            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            intent.putExtra("logout", 0);
            startActivity(intent);
            finish();
            try {
                LoginManager.getInstance().logOut();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();
        }
    }
}
