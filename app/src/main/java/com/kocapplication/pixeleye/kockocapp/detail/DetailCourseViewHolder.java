package com.kocapplication.pixeleye.kockocapp.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Han_ on 2016-07-05.
 */
public class DetailCourseViewHolder extends RecyclerView.ViewHolder {
    private View lineTop;
    private View lineBottom;

    private ImageView courseImage;

    private TextView courseName;
    private TextView courseTime;

    public DetailCourseViewHolder(View itemView) {
        super(itemView);

        lineTop = itemView.findViewById(R.id.line_top);
        lineBottom = itemView.findViewById(R.id.line_bottom);

        courseImage = (ImageView) itemView.findViewById(R.id.course_image);

        courseName = (TextView) itemView.findViewById(R.id.course_name);
        courseTime = (TextView) itemView.findViewById(R.id.course_time);
    }

    public View getLineTop() {
        return lineTop;
    }

    public View getLineBottom() {
        return lineBottom;
    }

    public ImageView getCourseImage() {
        return courseImage;
    }

    public TextView getCourseName() {
        return courseName;
    }

    public TextView getCourseTime() {
        return courseTime;
    }
}
