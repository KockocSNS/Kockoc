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

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.BoardBasicAttr;
import com.kocapplication.pixeleye.kockocapp.model.Coordinate;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
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
    public static final int MAP_REQUESTCODE = 50233;

    private EditText boardText;
    private LinearLayout imageContainer;
    private LinearLayout mapContainer;

    private TextView tagText;
    private EditText tagInput;

    private Button confirm;
    private Button tagConfirm;
    private Button photoAdd;
    private Button mapAdd;

    private Board newWriteBoard;
    private Coordinate coordinate;
    private ArrayList<String> imagePaths;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        View titleView = getLayoutInflater().inflate(R.layout.actionbar_new_write_activity, null);
        confirm = (Button) titleView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new ButtonListener());
        actionBarTitleSet(titleView);

        container.setLayoutResource(R.layout.activity_new_write);
        View containView = container.inflate();

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

            if (imagePaths == null || tagList.isEmpty() || text.isEmpty()) {
                Snackbar.make(boardText, "빈칸이 있습니다. (사진, 태그, 본문)", Snackbar.LENGTH_SHORT).show();
                return;
            }

            BoardBasicAttr attributes = new BoardBasicAttr(BasicValue.getInstance().getUserNo());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            String date = dateFormat.format(new Date());
            String time = timeFormat.format(new Date());

            newWriteBoard = new Board(attributes, coordinate, text, date, time, imagePaths, tagList);

            Log.i(TAG, newWriteBoard.toString());

            Handler handler = new Handler();

            // TODO: 2016-06-29 작성된 내용을 Server에 보내는 기능 만들어야한다.
            Thread thread = new NewWriteThread(handler, newWriteBoard);
            thread.start();
        }

        private void photoAddClicked() {
            new Picker.Builder(NewWriteActivity.this, new ImagePickListener(), R.style.MIP_theme)
                    .build()
                    .startActivity();
        }

        private void mapAddClicked() {
            Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
            startActivityForResult(mapIntent, MAP_REQUESTCODE);
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

        if (requestCode == MAP_REQUESTCODE && resultCode == RESULT_OK) {
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
    }
}
