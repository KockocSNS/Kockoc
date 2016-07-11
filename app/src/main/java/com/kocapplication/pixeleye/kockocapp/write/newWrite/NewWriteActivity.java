package com.kocapplication.pixeleye.kockocapp.write.newWrite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.BoardBasicAttr;
import com.kocapplication.pixeleye.kockocapp.model.Coordinate;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.FilePopUp;
import com.kocapplication.pixeleye.kockocapp.write.newWrite.map.MapActivity;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Han_ on 2016-06-24.
 */
public class NewWriteActivity extends BaseActivityWithoutNav {
    private final String TAG = "NEW_WRITE_ACTIVITY";
    public static final int MAP_REQUEST_CODE = 50233;
    public static final int IMAGE_TRANSMISSION = 1022;

    public static final int CONTINUOUS_FLAG = 387;
    public static final int DEFAULT_FLAG = 5326;
    private int flag;

    private EditText boardText;
    private LinearLayout imageContainer;
    private LinearLayout mapContainer;

    private TextView tagText;
    private EditText tagInput;

    private Button confirm;
    private Button tagConfirm;
    private Button photoAdd;
    private Button mapAdd;

    private Coordinate coordinate;
    private ArrayList<String> imagePaths;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFlag();
        init();

        container.setLayoutResource(R.layout.activity_new_write);
        View containView = container.inflate();

        getComponent(containView);


    }

    private void getFlag() {
        Intent intent = getIntent();
        flag = intent.getIntExtra("FLAG", DEFAULT_FLAG);
    }

    private void getComponent(View containView) {
        View titleView = getLayoutInflater().inflate(R.layout.actionbar_new_write_activity, null);
        confirm = (Button) titleView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new ButtonListener());
        actionBarTitleSet(titleView);

        imageContainer = (LinearLayout) containView.findViewById(R.id.image_container);
        mapContainer = (LinearLayout) containView.findViewById(R.id.map_container);

        boardText = (EditText) containView.findViewById(R.id.board_text);
        tagInput = (EditText) containView.findViewById(R.id.tag_input);
        tagText = (TextView) containView.findViewById(R.id.tag_text);

        tagConfirm = (Button) containView.findViewById(R.id.tag_confirm);
        photoAdd = (Button) containView.findViewById(R.id.photo_add);
        mapAdd = (Button) containView.findViewById(R.id.map_add);

        tagConfirm.setOnClickListener(new ButtonListener());
        photoAdd.setOnClickListener(new ButtonListener());
        mapAdd.setOnClickListener(new ButtonListener());
        coordinate = new Coordinate(0,0);
    }

    @Override
    protected void init() {
        super.init();
        coordinate = null;
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(confirm)) confirmClicked();
            else if (v.equals(photoAdd)) photoAddClicked();
            else if (v.equals(mapAdd)) mapAddClicked();
            else if (v.equals(tagConfirm)) tagConfirmClicked();
        }

        private void confirmClicked() {
            String _tags = tagText.getText().toString();
            String[] tags = _tags.split(", ");

            List<String> tagList = new ArrayList<>(Arrays.asList(tags));
            String text = boardText.getText().toString();

            if (imagePaths == null || tagList.isEmpty() || text.isEmpty()) {    //태그가 없어도 글이 들어감
                Snackbar.make(boardText, "빈칸이 있습니다. (사진, 태그, 본문)", Snackbar.LENGTH_SHORT).show();
                return;
            }

            BoardBasicAttr attributes = new BoardBasicAttr(BasicValue.getInstance().getUserNo());

            if (flag == CONTINUOUS_FLAG)
                attributes =
                        new BoardBasicAttr(
                                /*userNo*/          BasicValue.getInstance().getUserNo(),
                                /*boardNo*/         0,
                                /*courseNo*/        getIntent().getIntExtra("COURSE_NO", 0),
                                /*coursePosition*/  getIntent().getIntExtra("COURSE_PO", 0),
                                /*courseCount*/     0,
                                /*courseName*/      getIntent().getStringExtra("COURSE_NAME"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            String date = dateFormat.format(new Date());
            String time = timeFormat.format(new Date());

            Board newWriteBoard = new Board(attributes, coordinate, text, date, time, imagePaths, tagList);

            Log.i(TAG, newWriteBoard.toString());

            // 이미지 삽입
            if (newWriteBoard.getImagePaths() != null) {
                for (int i = 0; i < newWriteBoard.getImagePaths().size(); i++) {

                    String path = newWriteBoard.getImagePaths().get(i).toString();
                    int split = newWriteBoard.getImagePaths().get(i).toString().lastIndexOf("/");

                    String name = path.substring(split + 1);
                    path = path.substring(0, split + 1);

                    newWriteBoard.imageAdd(path, name);
                    if (i == 0) {
                        newWriteBoard.setMainImg(name);
                    }
                }
                newWriteBoard.setImageNo(newWriteBoard.getImagePaths().size());
            } else
                newWriteBoard.setImageNo(0);


            /**
             * FilePopUp
             * 사진을 ftp로 서버에 전송하고 글을 올림
             */
            Intent writeBoard = new Intent(NewWriteActivity.this, FilePopUp.class);
            writeBoard.putExtra("board", newWriteBoard);

            startActivityForResult(writeBoard,IMAGE_TRANSMISSION);

//            Handler handler = new Handler();
//            // TODO: 2016-06-29 작성된 내용을 Server에 보내는 기능 만들어야한다.
//            Snackbar.make(confirm, "Server로 보내는 기능 작성", Snackbar.LENGTH_SHORT).show();
//            Thread thread = new NewWriteThread(handler, newWriteBoard);
//            thread.start();
        }

        private void photoAddClicked() {
            new Picker.Builder(NewWriteActivity.this, new ImagePickListener(), R.style.MIP_theme)
                    .build()
                    .startActivity();
        }

        private void mapAddClicked() {
            Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
            startActivityForResult(mapIntent, MAP_REQUEST_CODE);
        }

        private void tagConfirmClicked() {
            String tag = tagText.getText().toString();
            String inputTag = tagInput.getText().toString();

            if (tag.contains("#'태그'를"))
                tag = "#" + inputTag;

            else if (tag.contains(inputTag))
                Snackbar.make(photoAdd, "이미 등록되어 있는 태그입니다.", Snackbar.LENGTH_SHORT).show();

            else
                tag = tag + ", #" + inputTag;

            tagText.setText(tag);
            tagInput.setText("");
        }
    }

    private class ImagePickListener implements Picker.PickListener {
        @Override
        public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
            ArrayList<String> paths = new ArrayList<>();
            if (!images.isEmpty()) {

                for (ImageEntry image : images) {
                    String path = image.path;
                    paths.add(path);
                }

                imagePaths = paths;
                int displayWidth = getWindowManager().getDefaultDisplay().getWidth() / 4;

                for (String path : paths) {
                    ImageView picture = new ImageView(NewWriteActivity.this);

                    ViewGroup.MarginLayoutParams margin =
                            new ViewGroup.MarginLayoutParams(new LinearLayout.LayoutParams(displayWidth, displayWidth));

                    margin.setMargins(10, 5, 10, 5);
                    picture.setLayoutParams(new LinearLayout.LayoutParams(margin));

                    Glide.with(NewWriteActivity.this).load(path).into(picture);
                    imageContainer.addView(picture);

                    picture.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int position = imageContainer.indexOfChild(v);
                            imageContainer.removeView(v);
                            imagePaths.remove(position);
                            return true;
                        }
                    });
                }
            }
        }

        @Override
        public void onCancel() {
            Snackbar.make(photoAdd, "이미지 불러오기를 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            double latitude = data.getDoubleExtra("LATITUDE", 0);
            double longitude = data.getDoubleExtra("LONGITUDE", 0);

            coordinate = new Coordinate(latitude, longitude);

            ImageView mapImage = new ImageView(getApplicationContext());
            mapImage.setImageDrawable(getResources().getDrawable(R.drawable.map_image));

            mapContainer.removeAllViews();
            mapContainer.addView(mapImage);

            mapImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    coordinate = null;
                    return false;
                }
            });
        }
        else if (requestCode == IMAGE_TRANSMISSION){
            Toast.makeText(NewWriteActivity.this, "완료 후 디테일 페이지로 보내야함", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
