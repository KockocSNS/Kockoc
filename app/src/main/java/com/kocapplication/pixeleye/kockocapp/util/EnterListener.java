package com.kocapplication.pixeleye.kockocapp.util;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

/**
 * Created by Han_ on 2016-06-27.
 */
public abstract class EnterListener implements TextView.OnEditorActionListener {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            onEnter();
            return true;
        }
        return false;
    }

    public void onEnter() {

    }
}
