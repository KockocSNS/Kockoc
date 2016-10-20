package com.kocapplication.pixeleye.kockocapp.write.newWrite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.kocapplication.pixeleye.kockocapp.detail.DetailPageData;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.BoardBasicAttr;
import com.kocapplication.pixeleye.kockocapp.model.Coordinate;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.FilePopUp;
import com.kocapplication.pixeleye.kockocapp.write.newWrite.map.MapActivity;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import org.apmem.tools.layouts.FlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Han_ on 2016-06-24.
 */
public class NewWriteActivity extends BaseActivityWithoutNav {
    private final String TAG = "NEW_WRITE_ACTIVITY";
    public static final int MAP_REQUEST_CODE = 50233;
    public static final int IMAGE_TRANSMISSION = 1022;
    public static final int CONTINUOUS_EDIT_FLAG = 1241;
    public static final int CONTINUOUS_FLAG = 387;
    public static final int DEFAULT_FLAG = 5326;
    public static final int EDIT_FLAG = 2222;
    private int flag;

    private EditText boardText;
    private LinearLayout imageContainer;
    private LinearLayout mapContainer;
    private FlowLayout tagContainer;

    private TextView tagTemp;
    private EditText tagInput;
    private ImageView mapImage;

    private Button confirm;
    private Button tagConfirm;
    private Button photoAdd;
    private Button mapAdd;

    private Coordinate coordinate;
    private ArrayList<String> imagePaths;
    private DetailPageData data;
    private ProgressDialog dialog;
    Board newWriteBoard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();
        init();

        container.setLayoutResource(R.layout.activity_new_write);
        View containView = container.inflate();

        getComponent(containView);
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            flag = intent.getIntExtra("FLAG", DEFAULT_FLAG);
            data = (DetailPageData) intent.getSerializableExtra("DATA");
        } catch (NullPointerException e) {
            Log.e(TAG, "getIntentdata null :" + e.getMessage());
        }
    }

    private void getComponent(View containView) {
        View titleView = getLayoutInflater().inflate(R.layout.actionbar_new_write_activity, null);
        confirm = (Button) titleView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new ButtonListener());
        actionBarTitleSet(titleView);

        imageContainer = (LinearLayout) containView.findViewById(R.id.image_container);
        mapContainer = (LinearLayout) containView.findViewById(R.id.map_container);
        tagContainer = (FlowLayout) containView.findViewById(R.id.fl_new_write_tag_container);

        boardText = (EditText) containView.findViewById(R.id.board_text);
        tagInput = (EditText) containView.findViewById(R.id.tag_input);
        tagTemp = (TextView) containView.findViewById(R.id.tv_new_write_tag_temp);

        tagConfirm = (Button) containView.findViewById(R.id.tag_confirm);
        photoAdd = (Button) containView.findViewById(R.id.photo_add);
        mapAdd = (Button) containView.findViewById(R.id.map_add);

        tagConfirm.setOnClickListener(new ButtonListener());
        photoAdd.setOnClickListener(new ButtonListener());
        mapAdd.setOnClickListener(new ButtonListener());
        coordinate = new Coordinate(0, 0);
        mapImage = new ImageView(getApplicationContext());
        mapImage.setImageDrawable(getResources().getDrawable(R.drawable.map_image));

        imagePaths = new ArrayList<>();

        if (flag == EDIT_FLAG || flag == CONTINUOUS_EDIT_FLAG) {
            set_editData();
        }
    }

    /**
     * set_editData
     * 수정 데이터 set
     */
    private void set_editData() {
        ArrayList<String> paths = new ArrayList<>(); // 이미지 경로 temp 초기화
        boardText.setText(data.getBoardText()); // 글

        for (int i = 0; i < data.getHashTagArr().size(); i++) { // 해시태그
            TextView t = addTagTextView(data.getHashTagArr().get(i));
            t.setText(data.getHashTagArr().get(i));
            tagContainer.removeView(tagTemp);
            tagContainer.addView(t);
        }

        for (int i = 0; i < data.getBoardImgArr().size(); i++) { // 사진 목록
            paths.add(BasicValue.getInstance().getUrlHead() + "board_image/" + BasicValue.getInstance().getUserNo() + "/" + data.getBoardImgArr().get(i));
        }
        imagePaths = paths;
        coordinate = new Coordinate(data.getLatitude(), data.getLongitude()); //지도
        mapContainer.addView(mapImage);
        setImg(paths); // 하단 작은 사진
    }

    @Override
    protected void init() {
        super.init();
        coordinate = null;
        newWriteBoard = new Board();
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
            confirm.setEnabled(false);
            String text = boardText.getText().toString();
            Log.e(TAG,"imagePaths :"+imagePaths);
            if (imagePaths.size() == 0 || newWriteBoard.getHashSize() == 0 || text.isEmpty()) {    //태그가 없어도 글이 들어감
                Snackbar.make(boardText, "빈칸이 있습니다. (사진, 태그, 본문)", Snackbar.LENGTH_SHORT).show();
                confirm.setEnabled(true);
                return;
            }

            dialog = ProgressDialog.show(NewWriteActivity.this, "", "잠시만 기다려주세요");

            BoardBasicAttr attributes = new BoardBasicAttr(BasicValue.getInstance().getUserNo());

            if (flag == CONTINUOUS_FLAG || flag == CONTINUOUS_EDIT_FLAG)
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

            newWriteBoard.setBasicAttributes(attributes);
            newWriteBoard.setCoordinate(coordinate);
            newWriteBoard.setText(text);
            newWriteBoard.setDate(date);
            newWriteBoard.setTime(time);
            newWriteBoard.setImagePaths(imagePaths);


            // 이미지 삽입
            if (!newWriteBoard.getImagePaths().isEmpty()) {
                for (int i = 0; i < newWriteBoard.getImagePaths().size(); i++) {

                    String path = newWriteBoard.getImagePaths().get(i).toString();
                    int split = path.lastIndexOf("/");

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
            if (flag == EDIT_FLAG || flag == CONTINUOUS_EDIT_FLAG) { //수정 플래그
                writeBoard.putExtra("flag", "edit");
                newWriteBoard.setBoardNo(data.getBoardNo()); // 수정 시 보드넘버 추가
            } else writeBoard.putExtra("flag", "default");

            startActivityForResult(writeBoard, IMAGE_TRANSMISSION);
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

        /**
         * tagConfirmClicked
         * 태그 추가 버튼 시 (태그를 추가해주세요) 를 없애고 텍스트뷰를 추가
         */
        private void tagConfirmClicked() {
            if (!(tagInput.getText().length() == 0)) {
                Log.e(TAG, "tagInput.getText().toString() :" + tagInput.getText().toString());
                TextView t = addTagTextView("#" + tagInput.getText().toString());
                t.setText("#" + tagInput.getText().toString());
                tagContainer.removeView(tagTemp);
                tagContainer.addView(t);
                Log.e(TAG, "tagContainer :" + tagContainer.toString());
                tagInput.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "태그를 추가해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ImagePickListener implements Picker.PickListener {
        @Override
        public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
            if (!images.isEmpty()) {
                for (ImageEntry image : images) {
                    String path = image.path;
                    imagePaths.add(path);
                }
                setImg(imagePaths);
            }
        }

        @Override
        public void onCancel() {
            Snackbar.make(photoAdd, "이미지 불러오기를 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void setImg(ArrayList<String> paths) {
        imageContainer.removeAllViews();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            double latitude = data.getDoubleExtra("LATITUDE", 0);
            double longitude = data.getDoubleExtra("LONGITUDE", 0);

            coordinate = new Coordinate(latitude, longitude);

            mapContainer.removeAllViews();
            mapContainer.addView(mapImage);

            mapImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    coordinate = null;
                    return false;
                }
            });

        } else if (requestCode == IMAGE_TRANSMISSION) {
            int boardNo = data.getIntExtra("result_boardNo", 0);
            int courseNo = getIntent().getIntExtra("COURSE_NO", 0);
            int userNo = BasicValue.getInstance().getUserNo();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("boardNo", boardNo);
            resultIntent.putExtra("courseNo", courseNo);
            resultIntent.putExtra("board_userNo", userNo);
            setResult(RESULT_OK, resultIntent);

            dialog.cancel();
            finish();
        }
    }

    /**
     * addTagTextView
     * 보드에 hashtag를 추가하고 텍스트뷰를 삽입
     *
     * @param text
     * @return TextView
     */
    private TextView addTagTextView(String text) {

        newWriteBoard.hashAdd(text);
        final TextView t = new TextView(this);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        margin.setMargins(15, 0, 15, 0);
        t.setLayoutParams(new LinearLayout.LayoutParams(margin));
        t.setTextColor(getResources().getColor(R.color.innerTagColor));
        t.setTextSize(20);
        //클릭 시 텍스트뷰를 지우고 보드의 해시태그를 지움
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagContainer.removeView(v);
                newWriteBoard.hashRemove(t.getText().toString());
            }
        });

        return t;
    }
}
