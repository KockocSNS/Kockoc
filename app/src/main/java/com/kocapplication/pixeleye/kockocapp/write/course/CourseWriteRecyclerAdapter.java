package com.kocapplication.pixeleye.kockocapp.write.course;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.course.CourseDetailActivity;
import com.kocapplication.pixeleye.kockocapp.main.search.SearchActivity;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.util.GlobalApplication;
import com.kocapplication.pixeleye.kockocapp.util.StringUtil;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.connect.JspConn;
import com.kocapplication.pixeleye.kockocapp.write.continuousWrite.CourseSelectActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-06-30.
 */
public class CourseWriteRecyclerAdapter extends RecyclerView.Adapter<CourseWriteRecyclerViewHolder> {
    final static String TAG = "CourseWriteAdapter";
    private List<Course> items;
    private Activity activity;
    private View.OnClickListener itemClickListener;
    private int flag;

    public CourseWriteRecyclerAdapter(List<Course> data, Activity activity, int flag) {
        super();
        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
        this.activity = activity;
        this.flag = flag;
    }

    public CourseWriteRecyclerAdapter(List<Course> data, Activity activity, View.OnClickListener itemClickListener, int flag) {
        this(data, activity, flag);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public CourseWriteRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_course_add, parent, false);
        if (itemClickListener != null) itemView.setOnClickListener(itemClickListener);
        return new CourseWriteRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseWriteRecyclerViewHolder holder, int position) {
        Course item = items.get(position);
        View.OnClickListener listener = new ItemButtonListener(holder, position);

        //이어쓰기 시
        if (flag == CourseSelectActivity.CONTINUOUS_FLAG || flag == CourseDetailActivity.COURSE_DETAIL_FLAG) {
            holder.getDelete().setVisibility(View.GONE); // 삭제버튼 지움
        }else{ // 코스 작성, 수정 시
            holder.getUploadIcon().setVisibility(View.GONE);
            holder.getDateButton().setOnClickListener(listener);
            holder.getTimeButton().setOnClickListener(listener);
            holder.getDelete().setOnClickListener(listener);
        }

        if (position == 0) holder.getLineTop().setVisibility(View.GONE);
        else holder.getLineTop().setVisibility(View.VISIBLE);

        if (position == (items.size() - 1)) {
            holder.getLineBottom().setVisibility(View.GONE);
        } else holder.getLineBottom().setVisibility(View.VISIBLE);

        holder.getCourseName().setText("# " + item.getTitle());
        holder.getDateButton().setText(item.getDate());
        holder.getTimeButton().setText(item.getTime());
        holder.getMemo().setOnClickListener(listener);
        holder.getSearch().setOnClickListener(listener);

        int boardNo = 0;

        //중복 체크
        if(new StringUtil().findDuplicateValue(items)){ // 코스 이름에 중복이 있을 경우 stopoverIndex로 검색
            boardNo = Integer.parseInt(JspConn.getBoardNo(items.get(position).getCourseNo(), items.get(position).getCoursePosition()));
        }else{ //중복이 없을 경우 이름으로 검색 (기존 글들과 호환성을 위해 나눔)
            try {
                boardNo = Integer.parseInt(JspConn.getBoardNoForEdit(items.get(position).getCourseNo(), items.get(position).getTitle()));
                Log.e(TAG, "" + boardNo + "/" + items.get(position).getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //경유지글이 업로드되면 업로드아이콘 표시
        if (boardNo > 0) {
            holder.getUploadIcon().setText("수정");
        } else {
            holder.getUploadIcon().setText("작성");
            holder.getUploadIcon().setBackground(GlobalApplication.getInstance().getDrawable(GlobalApplication.getInstance(),R.drawable.bg_round_shape_maincolor));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Course> getItems() {
        return items;
    }

    public void setItems(List<Course> items) {
        this.items = items;
    }

    public boolean contain(Course course) {
        for (Course item : items)
            if (item.equals(course)) return true;
        return false;
    }

    private class ItemButtonListener implements View.OnClickListener {
        private CourseWriteRecyclerViewHolder holder;
        private int position;

        public ItemButtonListener(CourseWriteRecyclerViewHolder holder, int position) {
            super();
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (v.equals(holder.getDateButton())) {
                Calendar currentDate = Calendar.getInstance();
                new DatePickerDialog(activity, new TimeSetListener(holder, position),
                        currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();

            } else if (v.equals(holder.getTimeButton())) {
                String time = holder.getTimeButton().getText().toString();
                String[] _time = time.split(":");
                new TimePickerDialog(activity, new TimeSetListener(holder, position), Integer.parseInt(_time[0]), Integer.parseInt(_time[1]), false).show();

            } else if (v.equals(holder.getDelete())) {
                // 해당하는 코스에 글이 있다면 삭제 할것인지 확인  삭제한다면 글은 그대로 두고 코스만 지움
                Log.e(TAG,"time :"+items.get(position).getTime());
                if(JspConn.getBoardNoForEdit(items.get(position).getCourseNo(),items.get(position).getTitle()).equals("")){
                    items.remove(position);
                    notifyDataSetChanged();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("코스 삭제")
                    .setMessage("코스에 작성된 글이 있습니다.\n삭제 하시겠습니까?\n (삭제해도 글은 남아있습니다)")
                    .setCancelable(true) // 뒤로가기버튼
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            items.remove(position);
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } else if (v.equals(holder.getSearch())) {
                Intent searchIntent = new Intent(activity, SearchActivity.class);
                searchIntent.putExtra("keyword", items.get(position).getTitle());
                activity.startActivity(searchIntent);
            } else if(v.equals(holder.getMemo())){
                Intent memoIntent = new Intent(activity, CourseMemoActivity.class);
                memoIntent.putExtra("courseNo",items.get(position).getCourseNo());
                memoIntent.putExtra("coursePo",items.get(position).getCoursePosition());
                memoIntent.putExtra("memo",items.get(position).getMemo());
                memoIntent.putExtra("FLAG",flag);
                memoIntent.putExtra("position",position);//어댑터 포지션
                if(flag == CourseWriteActivity.DEFAULT_FLAG || flag == CourseWriteActivity.ADJUST_FLAG)
                    activity.startActivityForResult(memoIntent,CourseWriteActivity.DEFAULT_FLAG);
                else
                    activity.startActivity(memoIntent);
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CourseWriteActivity.DEFAULT_FLAG) {
            try {
                String memo = data.getStringExtra("memo");
                int position = data.getIntExtra("position", 0);
                items.get(position).setMemo(memo);
                Log.i(TAG,"memo :"+memo);
            }catch (NullPointerException e){Log.e(TAG,"memo null");}
        }
    }

    private class TimeSetListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
        private CourseWriteRecyclerViewHolder holder;
        private int position;

        public TimeSetListener(CourseWriteRecyclerViewHolder holder, int position) {
            super();
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String _year = String.valueOf(year).substring(2);
            String _month = String.valueOf(monthOfYear + 1);
            String _day = String.valueOf(dayOfMonth);

            _year = _year.length() < 2 ? "0" + _year : _year;
            _month = _month.length() < 2 ? "0" + _month : _month;
            _day = _day.length() < 2 ? "0" + _day : _day;

            String date = _year + "/" + _month + "/" + _day;
            String time = holder.getTimeButton().getText().toString();

            try {
                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
                Date selectedDate = format.parse(date + " " + time);
                items.get(position).setDateTime(selectedDate);
            } catch (ParseException e) {
                Snackbar.make(view, "날짜를 다시 설정해주세요.", Snackbar.LENGTH_SHORT).show();
            }

            holder.getDateButton().setText(date);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String _minute = String.valueOf(minute);
            String _hour = String.valueOf(hourOfDay);

            _hour = _hour.length() < 2 ? "0" + _hour : _hour;
            _minute = _minute.length() < 2 ? "0" + _minute : _minute;

            String date = holder.getDateButton().getText().toString();
            String time = _hour + ":" + _minute;

            try {
                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
                Date selectedDate = format.parse(date + " " + time);
                items.get(position).setDateTime(selectedDate);
            } catch (ParseException e) {
                Snackbar.make(view, "시간을 다시 설정해주세요.", Snackbar.LENGTH_SHORT).show();
            }

            holder.getTimeButton().setText(time);
        }
    }
}

