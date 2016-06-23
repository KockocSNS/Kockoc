package com.kocapplication.pixeleye.kockocapp.main.myKockoc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.story.BoardRecyclerAdapter;

/**
 * Created by Han_ on 2016-06-21.
 */
public class MyKocKocFragment extends Fragment {
    private RecyclerView recyclerView;
    private BoardRecyclerAdapter adapter;

    private ImageView profileImage;
    private TextView nickName;
//    private Button
//
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mykockoc, container, false);

        init(view);

        return view;
    }

    private void init(View view) {

    }
}
