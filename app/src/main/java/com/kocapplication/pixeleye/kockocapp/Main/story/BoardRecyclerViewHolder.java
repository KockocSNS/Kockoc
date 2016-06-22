package com.kocapplication.pixeleye.kockocapp.main.story;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BoardRecyclerViewHolder extends RecyclerView.ViewHolder {
    private ImageView boardImage;
    private ImageView courseImage;

    private TextView courseCount;

    //board bottom | left
    private TextView Title;
    private TextView tag;
    private TextView date;

    //expression
    private TextView like;
    private TextView scrap;
    private TextView comment;

    public BoardRecyclerViewHolder(View itemView) {
        super(itemView);
        boardImage = (ImageView) itemView.findViewById(R.id.board_image);
        courseImage = (ImageView) itemView.findViewById(R.id.board_course_image);

        courseCount = (TextView) itemView.findViewById(R.id.board_course_count);

        Title = (TextView) itemView.findViewById(R.id.board_title);
        tag = (TextView) itemView.findViewById(R.id.board_tag);
        date = (TextView) itemView.findViewById(R.id.board_date);

        like = (TextView) itemView.findViewById(R.id.board_like);
        scrap = (TextView) itemView.findViewById(R.id.board_scrap);
        comment = (TextView) itemView.findViewById(R.id.board_comment);
    }

    public ImageView getBoardImage() {
        return boardImage;
    }

    public ImageView getCourseImage() {
        return courseImage;
    }

    public TextView getComment() {
        return comment;
    }

    public TextView getCourseCount() {
        return courseCount;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getLike() {
        return like;
    }

    public TextView getScrap() {
        return scrap;
    }

    public TextView getTag() {
        return tag;
    }

    public TextView getTitle() {
        return Title;
    }
}
