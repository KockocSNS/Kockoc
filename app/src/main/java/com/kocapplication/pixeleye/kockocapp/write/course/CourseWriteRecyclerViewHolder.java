package com.kocapplication.pixeleye.kockocapp.write.course;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Han_ on 2016-06-30.
 */
public class CourseWriteRecyclerViewHolder  extends RecyclerView.ViewHolder {
    private TextView courseName;
    private Button dateButton;
    private Button timeButton;
    private Button delete;

    public CourseWriteRecyclerViewHolder(View itemView) {
        super(itemView);
        this.courseName = (TextView) itemView.findViewById(R.id.course_name);
        this.dateButton = (Button) itemView.findViewById(R.id.course_date);
        this.timeButton = (Button) itemView.findViewById(R.id.course_time);
        this.delete = (Button) itemView.findViewById(R.id.delete);
    }

    public TextView getCourseName() {
        return courseName;
    }

    public Button getDateButton() {
        return dateButton;
    }

    public Button getTimeButton() {
        return timeButton;
    }

    public Button getDelete() {
        return delete;
    }
}