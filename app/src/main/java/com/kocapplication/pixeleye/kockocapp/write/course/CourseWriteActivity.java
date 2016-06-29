package com.kocapplication.pixeleye.kockocapp.write.course;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-06-29.
 */
public class CourseWriteActivity extends BaseActivityWithoutNav {
    private String courseTitle;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    private EditText courseInput;
    private Button dateButton;
    private Button timeButton;
    private Button addButton;
    private Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseTitle = getIntent().getStringExtra("COURSE_TITLE");
        init();
        actionBarTitleSet(courseTitle, Color.WHITE);

        container.setLayoutResource(R.layout.activity_course_write);
        View containView = container.inflate();

        declare(containView);
    }

    private void declare(View containView) {
        courseInput = (EditText) containView.findViewById(R.id.course_name_input);
        dateButton = (Button) containView.findViewById(R.id.course_date_set);
        timeButton = (Button) containView.findViewById(R.id.course_time_set);
        addButton = (Button) containView.findViewById(R.id.add_button);
        confirm = (Button) containView.findViewById(R.id.confirm);

        View.OnClickListener listener = new ButtonListener();
        dateButton.setOnClickListener(listener);
        timeButton.setOnClickListener(listener);
        addButton.setOnClickListener(listener);
        confirm.setOnClickListener(listener);

        View recyclerLayout = containView.findViewById(R.id.recycler_layout);
        recyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(new ArrayList<Course>());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(dateButton)) {

            } else if (v.equals(timeButton)) {

            } else if (v.equals(addButton)) {

            } else if (v.equals(confirm)) {

            }
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private class DeleteListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView courseName;
        private Button dateButton;
        private Button timeButton;
        private Button delete;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            this.courseName = (TextView) itemView.findViewById(R.id.course_name);
            this.dateButton = (Button) itemView.findViewById(R.id.course_date);
            this.timeButton = (Button) itemView.findViewById(R.id.course_time);
            this.delete = (Button) itemView.findViewById(R.id.delete);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<Course> items;

        public RecyclerAdapter(List<Course> data) {
            super();
            if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
            this.items = data;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course_add, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            Course item = items.get(position);

            holder.courseName.setText(item.getTitle());
            holder.dateButton.setText(item.getDate());
            holder.timeButton.setText(item.getTime());

            holder.delete.setOnClickListener(new DeleteListener());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
