package com.kocapplication.pixeleye.kockocapp.write.newWrite;

import android.os.Handler;

import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.BoardWithImage;

/**
 * Created by Han_ on 2016-06-27.
 */
public class NewWriteThread extends Thread {
    private Handler handler;
    private Board boardWithImage;

    public NewWriteThread(Handler handler, Board boardWithImage) {
        super();
        this.handler = handler;
        this.boardWithImage = boardWithImage;
    }

    @Override
    public void run() {
        super.run();

    }
}
