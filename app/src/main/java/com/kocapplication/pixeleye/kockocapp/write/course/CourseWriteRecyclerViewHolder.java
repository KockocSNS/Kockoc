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
    private View lineTop;
    private View lineBottom;

    private TextView courseName;
    private TextView uploadIcon;

    private Button dateButton;
    private Button timeButton;
    private Button delete;

    private Button memo;
    private Button search;

    public CourseWriteRecyclerViewHolder(View itemView) {
        super(itemView);
        this.lineTop = itemView.findViewById(R.id.line_top);
        this.lineBottom = itemView.findViewById(R.id.line_bottom);

        this.courseName = (TextView) itemView.findViewById(R.id.course_name);
        this.uploadIcon = (TextView) itemView.findViewById(R.id.upload_icon);
        this.dateButton = (Button) itemView.findViewById(R.id.course_date);
        this.timeButton = (Button) itemView.findViewById(R.id.course_time);
        this.delete = (Button) itemView.findViewById(R.id.delete);

        this.memo = (Button) itemView.findViewById(R.id.course_memo);
        this.search = (Button) itemView.findViewById(R.id.course_search);

    }

    public View getLineTop() {
        return lineTop;
    }

    public View getLineBottom() {
        return lineBottom;
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

    public Button getSearch() {return search;}

    public Button getMemo() {
        return memo;
    }

    public TextView getUploadIcon() { return uploadIcon; }


}