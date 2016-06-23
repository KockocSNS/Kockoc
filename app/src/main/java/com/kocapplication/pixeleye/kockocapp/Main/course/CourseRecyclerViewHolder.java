package com.kocapplication.pixeleye.kockocapp.main.course;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Han_ on 2016-06-22.
 */
public class CourseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView boardTitles;
    private TextView title;

    public CourseRecyclerViewHolder(View itemView) {
        super(itemView);

        this.boardTitles = (TextView) itemView.findViewById(R.id.course_tags);
        this.title = (TextView) itemView.findViewById(R.id.course_title);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getBoardTitles() {
        return boardTitles;
    }
}
