package com.kocapplication.pixeleye.kockocapp.write;

import android.os.Handler;

import com.kocapplication.pixeleye.kockocapp.model.Board;

/**
 * Created by Han_ on 2016-06-27.
 */
public class NewWriteThread extends Thread {
    private Handler handler;
    private Board board;

    public NewWriteThread(Handler handler, Board board) {
        super();
        this.handler = handler;
        this.board = board;
    }

    @Override
    public void run() {
        super.run();


    }
}
