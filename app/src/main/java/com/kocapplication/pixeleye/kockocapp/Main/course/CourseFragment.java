package com.kocapplication.pixeleye.kockocapp.main.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Han_ on 2016-06-21.
 */
public class CourseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_with_swipe_refresh, container, false);
    }
}
