package com.kocapplication.pixeleye.kockocapp.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kocapplication.pixeleye.kockocapp.login.LoginActivity;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.myKockoc.neighbor.NeighborActivity;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Han_ on 2016-06-20.
 */
public class BaseActivity extends AppCompatActivity {
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
        nav_profile_img = (ImageView)nav_header.findViewById(R.id.header_profile_image);
        nav_profile_name = (TextView)nav_header.findViewById(R.id.header_profile_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);


        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.nav_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        navigationView.setNavigationItemSelectedListener(navigationListener);
        Glide.with(this).load(BasicValue.getInstance().getUrlHead()+"board_image/"+ BasicValue.getInstance().getUserNo() + "/profile.jpg")
                .error(R.drawable.default_profile).bitmapTransform(new CropCircleTransformation(Glide.get(this).getBitmapPool())).into(nav_profile_img);
        nav_profile_name.setText(BasicValue.getInstance().getUserNickname());
    }

    protected void onRefresh() {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_search:

                return true;
            case R.id.menu_neighbor:
                Intent neighbor_intent = new Intent(BaseActivity.this, NeighborActivity.class);
                startActivity(neighbor_intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
     * @param editText
     */
    protected void softKeyboardHide(EditText editText){
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

                    return true;
                case R.id.nav_setting:

                    return true;
                case R.id.nav_logout:
                    UserManagement.requestLogout(new LogoutResponseCallback() {@Override public void onCompleteLogout() {}}); // 카카오 로그아웃
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.putExtra("logout", 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    BaseActivity.this.startActivity(intent);
                    BaseActivity.this.finish();
                    try{
                        LoginManager.getInstance().logOut();
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    return true;
            }
            return false;
        }
    }
}
