package com.kocapplication.pixeleye.kockocapp.util;

/**
 * Created by Administrator on 2015-07-13.
 */

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pixeleye02 on 2015-06-29.
 */
public class FileSend implements Runnable {
    FTPClient ftpClient = null;
    String IPAddress = "115.68.14.27";
    String ConnID = "kocserver";
    String PWD = "vlrtpfdkdl0505";
    String targetName = "";
    int userNo = 0;

    Bitmap thumbnail;

    public class FileQueue{
        ArrayList<String> queueArr;
        FileQueue(){
            queueArr = new ArrayList<>();
        }
        public boolean put(String filePath){
            return queueArr.add(filePath);
        }
        public  String get(){
            String result = queueArr.get(0);
            queueArr.remove(0);
            return result;
        }
    }

    public FileQueue Queue;


    public FileSend(int userNo){
        ftpClient = new FTPClient();
        this.userNo  = userNo;
        this.thumbnail = ThumbnailUtils.createVideoThumbnail(targetName, MediaStore.Images.Thumbnails.MICRO_KIND);
        Queue = new FileQueue();
    }



    @Override
    public void run() {//업로드 실행
        try {
            //ftp파일전송을 윈하 기본적인 세팅
            ftpClient.connect(IPAddress, 21);
            ftpClient.login(ConnID, PWD);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(5 * 1024 * 1024);
            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();

            //세팅후에 서버로부터 연결 여부를 받아와 전송 할 것인지를 판단 한다.
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }else{//아무런 문제없이 잘 세팅 된경우
                while (Queue.queueArr.size() > 0) {
                    String sendFilePath = Queue.get();
                    File file;
                    file = new File(sendFilePath);
                    if (file.isFile()) {
                        FileInputStream ifile = new FileInputStream(file);
                        ftpClient.makeDirectory("/kockoc1/boardImage/" + userNo);//유저별로 폴더가 필요하기 때문에 유저 번호를 이용하여 폴더를 생성한다.
                        ftpClient.cwd("/kockoc1/boardImage/" + userNo);
                        ftpClient.rest(file.getName());  // ftp에 해당 파일이있다면 이어쓰기
                        ftpClient.appendFile(file.getName(), ifile); // ftp 해당 파일이 없다면 새로쓰기
                    } else {
                        Log.d("FileSend", "fail");
                    }
                }//전송 항목이 비었으므로 ftp를 마무리 한다.
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            Log.e("FTP",""+e.getMessage());
        }
    }
}
