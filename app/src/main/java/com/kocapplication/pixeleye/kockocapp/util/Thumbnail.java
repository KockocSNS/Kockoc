package com.kocapplication.pixeleye.kockocapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.StringTokenizer;

public class Thumbnail {
    final static String TAG = "Thumnail";
    public static int width1280=1280, width720 = 720;
    int mainimg;
    public static String createThumbnail(String targetPath,String targetName)
    {
        Log.e("Thumbnail","targetPath :"+targetPath);
        Log.e("Thumbnail","targetName :"+targetName);
        String result="";
        int degree =0;
        String makePath=null;
        makeKocDir(); // 핸드폰에 kockoc 디렉토리 생성
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;


        Bitmap bitmap = BitmapFactory.decodeFile(targetPath + targetName,options);
        File f = new File(targetPath,targetName);
        if(f.exists()) {
            FileOutputStream out = null;//파일을 쓰기위한 FileOutputStream 생성
            try {
                makePath = String.valueOf(Environment.getExternalStorageDirectory()) + "/KocKoc/";
                StringTokenizer stk = new StringTokenizer(targetName, ".");
                out = new FileOutputStream(makePath + targetName);//thumbnail 파일이 생길 위치를 지정하며 메모리 할당

                if(bitmap.getWidth() > bitmap.getHeight()) {//눕혀진 이미지
                    if (bitmap.getWidth() > width1280) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, width1280, (bitmap.getHeight() * width1280) / bitmap.getWidth(), true);//비트맵이미지 썸네일
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);//파일 쓰기
                    }
                    else {
                        bitmap.compress(Bitmap.CompressFormat.JPEG,80,out);
                    }
                }
                else {
                    if (bitmap.getWidth() > width720) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, width720, (bitmap.getHeight() * width720) / bitmap.getWidth(), true);//비트맵이미지 썸네일
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    }
                    else {
                        bitmap.compress(Bitmap.CompressFormat.JPEG,80,out);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            Log.e("Image","No Exist Image Name & Path");
        }
        return makePath + targetName;
    }

    public static String profileImgThum (String imgPath,String imgName){

        String makePath=null;
        makeKocDir();// \\ /storage

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = false;

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath+imgName,options);
        File f = new File(imgPath,imgName);

        if(f.exists()){
            FileOutputStream out = null;//파일을 쓰기위한 FileOutputStream 생성
            try {
                makePath = String.valueOf(Environment.getExternalStorageDirectory()) + "/KocKoc/";
                StringTokenizer stk = new StringTokenizer(imgName, ".");
                out = new FileOutputStream(makePath + imgName);//thumbnail 파일이 생길 위치를 지정하며 메모리 할당

                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/4, bitmap.getHeight()/4, true);//비트맵이미지 썸네일
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);//파일 쓰기

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            Log.e("Image","No Exist Image Name & Path");
        }
        return makePath + imgName;
    }

    static public void makeKocDir(){
        File tempDir = new File(String.valueOf(Environment.getExternalStorageDirectory())+"/KocKoc");
        tempDir.mkdirs();
    }
    public void setMainimg(){mainimg=1;}
}