package com.kocapplication.pixeleye.kockocapp.main.myKockoc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;

/**
 * Created by Han_ on 2016-06-21.
 */
public class MyKocKocFragment extends Fragment {
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init() {

    }
}
