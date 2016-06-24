package com.kocapplication.pixeleye.kockocapp.write;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;

/**
 * Created by Han_ on 2016-06-24.
 */
public class NewWriteActivity extends BaseActivityWithoutNav {
    private final String TAG = "NEW_WRITE_ACTIVITY";

    private EditText boardText;

    private TextView tagText;
    private EditText tagInput;

    private Button confirm;
    private Button tagConfirm;
    private Button photoAdd;
    private Button mapAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        container.setLayoutResource(R.layout.activity_new_write);
        View containView = container.inflate();

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

        View titleView = getLayoutInflater().inflate(R.layout.actionbar_new_write_activity, null);
        confirm = (Button) titleView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new ButtonListener());
        actionBarTitleSet(titleView);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(confirm)) {

            } else if (v.equals(photoAdd)) {

            } else if (v.equals(mapAdd)) {

            } else if (v.equals(tagConfirm)) {
                String tag = tagText.getText().toString();
                String inputTag = tagInput.getText().toString();

                if (tag.contains("#'태그'를"))
                    tag = "#" + inputTag;

                else if (tag.contains(inputTag))
                    Snackbar.make(photoAdd, "이미 등록되어 있는 태그입니다.", Snackbar.LENGTH_SHORT).show();

                else
                    tag = tag + ", #" + inputTag;

                tagText.setText(tag);
                tagInput.clearComposingText();
            }
        }
    }
}
