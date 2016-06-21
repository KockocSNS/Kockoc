package com.kocapplication.pixeleye.kockocapp.detail;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;

import com.kocapplication.pixeleye.kockocapp.model.DetailPageData;

/**
 * Created by hp on 2016-06-21.
 */
public class DetailFragment extends Fragment {
    DetailPageData detailPageData;

    public DetailFragment(){super();}

    @SuppressLint("ValidFragment")
    public DetailFragment(DetailPageData detailPageData) {
        this.detailPageData = detailPageData;
    }
}
