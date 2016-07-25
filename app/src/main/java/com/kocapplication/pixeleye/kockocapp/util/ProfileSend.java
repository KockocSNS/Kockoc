package com.kocapplication.pixeleye.kockocapp.util;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProfileSend extends Thread {
    final static String TAG = "ProfileSend";
    FTPClient ftpClient = null;
    String IPAddress = "115.68.14.27";
    String ConnID = "kocserver";
    String PWD = "vlrtpfdkdl0505";
    String Path = "";
    String targetName = "";
    String PathAndName = "";
    int userNo = 0;

    public ProfileSend(int userNo, String path){
        ftpClient = new FTPClient();
        this.userNo  = userNo;
        this.Path = path;
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

                    String sendFilePath = Path;
                    File file;
                    file = new File(sendFilePath);
                    if (file.isFile()) {
                        FileInputStream ifile = new FileInputStream(file);
                        ftpClient.makeDirectory("/kockoc1/boardImage/" + userNo);//유저별로 폴더가 필요하기 때문에 유저 번호를 이용하여 폴더를 생성한다.
                        ftpClient.cwd("/kockoc1/boardImage/" + userNo);
                        ftpClient.rest(file.getName());  // ftp에 해당 파일이있다면 이어쓰기
                        ftpClient.appendFile(file.getName(), ifile); // ftp 해당 파일이 없다면 새로쓰기
                        ftpClient.rename(file.getName(), "profile.jpg"); //이름 변경
                        Log.e("FTP", "file rename " + file.getName());
                    } else {
                        Log.d(TAG, "ftp fail");

                }//전송 항목이 비었으므로 ftp를 마무리 한다.
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            Log.e(TAG,""+e.getMessage());
        }
        Log.d(TAG,"프로필 사진 성공");
    }
}
