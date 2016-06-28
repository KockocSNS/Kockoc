package com.kocapplication.pixeleye.kockocapp.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_1;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_10;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_2;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_3;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_4;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_5;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_6;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_7;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_8;
import com.kocapplication.pixeleye.kockocapp.main.help.advice_page_9;

/**
 * Created by Nam on 2016-06-27.
 */
public class AdviceActivity extends FragmentActivity {

    private int MAX_PAGE=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new adapter(getSupportFragmentManager()));

    }


    private class adapter extends FragmentPagerAdapter {

        public adapter(FragmentManager fm) {
            super(fm);
        }

        public int getCount() {
            return MAX_PAGE;
        }

        public Fragment getItem(int position) {
            if(position<0 || MAX_PAGE <= position)
                return  null;
            Fragment fragment = new Fragment();
            switch (position) {
                case 0:
                    fragment = new advice_page_1();
                    break;
                case 1:
                    fragment = new advice_page_2();
                    break;
                case 2:
                    fragment = new advice_page_3();
                    break;
                case 3:
                    fragment = new advice_page_4();
                    break;
                case 4:
                    fragment = new advice_page_5();
                    break;
                case 5:
                    fragment = new advice_page_6();
                    break;
                case 6:
                    fragment = new advice_page_7();
                    break;
                case 7:
                    fragment = new advice_page_8();
                    break;
                case 8:
                    fragment = new advice_page_9();
                    break;
                case 9:
                    fragment = new advice_page_10();
                    break;

            }
            return fragment;
        }

    }
}

