package com.kocapplication.pixeleye.kockocapp.write.continuousWrite;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by pixeleye03 on 2016-07-05.
 */
public class CourseSelectRecyclerViewHolder extends RecyclerView.ViewHolder{
    private TextView courseName;
    private Button dateButton;
    private Button timeButton;
    private Button memoButton;
    private LinearLayout course_ll;

    public CourseSelectRecyclerViewHolder(View itemView){
        super(itemView);
        this.courseName = (TextView)itemView.findViewById(R.id.course_select_name);
        this.course_ll = (LinearLayout)itemView.findViewById(R.id.course_select_layout);
        this.dateButton = (Button)itemView.findViewById(R.id.course_select_date);
        this.timeButton = (Button)itemView.findViewById(R.id.course_select_time);
        this.memoButton = (Button)itemView.findViewById(R.id.course_select_memo);
    }
    public TextView getCourseName() {return courseName;}

    public LinearLayout getCourse_ll() {return course_ll;}

    public Button getDateButton() {return dateButton;}

    public Button getTimeButton() {return timeButton;}

    public Button getMemoButton() {return memoButton;}
}
