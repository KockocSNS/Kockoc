package com.kocapplication.pixeleye.kockocapp.main.course;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Han_ on 2016-07-04.
 */
public class CourseViewRecyclerViewHolder extends RecyclerView.ViewHolder {
    private ImageView courseIcon;
    private TextView title;
    private TextView date;

    public CourseViewRecyclerViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.title);
        date = (TextView) itemView.findViewById(R.id.date);
    }

    public TextView getDate() {
        return date;
    }

    public TextView getTitle() {
        return title;
    }
}
