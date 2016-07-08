package com.kocapplication.pixeleye.kockocapp.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Board;

public class FilePopUp extends Activity {
    Thumbnail thumbnail = new Thumbnail();
    boolean end = false;
    Thread sendThread;
    Board board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_file_pop_up);
        Intent intent = getIntent();
        board = (Board)intent.getSerializableExtra("board");
        FileSend fileSend = new FileSend(BasicValue.getInstance().getUserNo());
        for(int i = 0; i<board.getImageNames().size(); i++){
            String temp;
            String mainImgTemp;
            if(i==0){
                thumbnail.setMainimg();
                temp = Thumbnail.createThumbnail(board.getImagePathArr().get(i), board.getImageNames().get(i));
//              Bitmap mainImg = BitmapFactory.decodeFile(board.getImagePathArr().get(i));
//              mainImg=Thumbnail.cropBitmap(mainImg, 150, 150);
            }
            else temp = Thumbnail.createThumbnail(board.getImagePathArr().get(i),board.getImageNames().get(i));
            fileSend.Queue.put(temp);
        }
        sendThread = new Thread(fileSend);
        sendThread.start();
        Thread ThisCheck = new Thread(new thisEnd());
        ThisCheck.start();
        //controlPopupLocation();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_file_pop_up, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    class thisEnd implements Runnable{
        @Override
        public void run() {
            while(sendThread.isAlive());

            ActivityEnd();

            Log.d("FilePopup", "사진 전송 완료");
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //취소버튼시 원하는 동작
            ActivityEnd();
        }
        return false;

    }
    private  void ActivityEnd(){
        String result = JspConn.boardWrite(board);
        Intent intent = new Intent();
        intent.putExtra("result",result);
        setResult(1022,intent);
        finish();
    }
}
