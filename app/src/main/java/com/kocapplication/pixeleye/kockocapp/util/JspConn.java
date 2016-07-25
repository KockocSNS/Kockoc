package com.kocapplication.pixeleye.kockocapp.util;

import android.os.StrictMode;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.User;
import com.kocapplication.pixeleye.kockocapp.write.course.MemoWriteThread;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016-06-23.
 */
public class JspConn {
    final static String TAG = "JspConn";

    /**
     * DetailPage
     */
    //상세보기 데이터 가져오기
    static public String loadDetailPage(String boardNo) {
        Log.e(TAG, "boardNo :" + boardNo);
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Board/LoadDetailPage.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("boardNo", "" + boardNo));
        String result = "";
        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "loadDetailPage result :" + result);
        return result;
    }

    //댓글 쓰기
    static public String WriteComment(String comment, int boardNo, int userNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Board/Comment/WriteComment.jsp";
        HttpPost post = new HttpPost(postURL);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("comment", "" + comment));
        params.add(new BasicNameValuePair("boardNo", "" + String.valueOf(boardNo)));
        params.add(new BasicNameValuePair("userNo", "" + String.valueOf(userNo)));
        String result = "";

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //gcm 메시지 보내기
    static public String pushGcm(String msg, int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead() + "GCM/GCM.jsp"; //테스트 빼기
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + userNo));
            params.add(new BasicNameValuePair("msg", "" + msg));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "pushGCM result :" + result);
        return result;
    }

    //댓글 삭제
    static public String DeleteComment(int commentNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Board/Comment/DeleteComment.jsp";
        HttpPost post = new HttpPost(postURL);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("commentNo", "" + commentNo));
        String result = "";
        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //보드넘버를 받아 좋아요 카운트
    static public String checkExpression(int boardNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Board/Expression/checkExpression.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("boardNo", "" + boardNo));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //좋아요
    static public String writeExpression(int boardNo, int Status) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Board/Expression/writeExpression.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("userNo", "" + String.valueOf(BasicValue.getInstance().getUserNo())));
        params.add(new BasicNameValuePair("boardNo", "" + String.valueOf(boardNo)));
        params.add(new BasicNameValuePair("status", "" + String.valueOf(Status)));
        String result = "";
        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //관심글 추가
    static public String addScrap(int boardNo) {

        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Scrap/AddScrap.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("boardNo", "" + String.valueOf(boardNo)));
        params.add(new BasicNameValuePair("userNo", "" + String.valueOf(BasicValue.getInstance().getUserNo())));
        String result = "";

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static public String isScrap(int boardNo, int userNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Scrap/isScrap.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userNo", String.valueOf(userNo)));
        params.add(new BasicNameValuePair("boardNo", String.valueOf(boardNo)));
        String result = "";

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static public boolean deleteScrap(int boardNo) {   //read first = boardNo = -1
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Scrap/DeleteScrap.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("boardNo", "" + boardNo));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Course
     */
    //코스 넘버를 받아 course 반환
    static public String readCourseByCourseNo(int courseNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Course/readCourseByCourseNo.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("courseNo", "" + courseNo));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //코스 업로드
    static public String uploadCourse(String title, List<Course> Arr) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Course/InsertCourse.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("courseNum", "" + String.valueOf(Arr.size())));
            params.add(new BasicNameValuePair("title", title));

            int i = 0;
            for (Course temp : Arr) {
                params.add(new BasicNameValuePair("course" + i++, "" + temp.getTitle() + "/" + temp.getDataByMilSec()));
            }
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.e(TAG, "UploadCourse error :" + e.getMessage());
        }

        Log.d(TAG, "UploadCourse result :" + result);
        return result;
    }

    //코스랑 메모 업로드
    static public void uploadCourseAndMemo(String title, List<Course> Arr,int memoNum){
        String result="";
        try
        {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Course/insertCourseWithMemo.jsp";
            HttpPost post = new HttpPost(postURL);


            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userNo",""+ BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("courseNum", "" + String.valueOf(Arr.size())));
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("memoNo", String.valueOf(memoNum)));


            int i=0;
            for(Course temp:Arr){
                params.add(new BasicNameValuePair("course"+i++,""+temp.getTitle()+"/"+temp.getDataByMilSec()));
            }

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while((line = bufferedReader.readLine())!=null){
                result+=line;
            }
        } catch (Exception e) {
            Log.e(TAG, "UploadCourse error :" + e.getMessage());
        }

        Log.d(TAG, "UploadCourse result :" + result);

        String[] array;
        array = result.split(" ");
        MemoWriteThread.resultcourseNo = Integer.parseInt(array[1]);
        MemoWriteThread.resultuserNo = Integer.parseInt(array[2]);
        MemoWriteThread.message = array[3];
        MemoWriteThread.resultmemoNo= Integer.parseInt(array[4]);

        Log.d("return courseNo = ", ""+MemoWriteThread.resultcourseNo);
        Log.d("return userNo = ", ""+MemoWriteThread.resultuserNo);
        Log.d("return message = ", ""+MemoWriteThread.message);
        Log.d("return memoNo = ", ""+MemoWriteThread.resultmemoNo);
    }



    /**
     * Board
     */
    //글 삭제
    static public String boardDelete(int boardNo, int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead() + "Board/HPdeleteBoard.jsp"; //deleteBoard로 변경
            // String postURL = BasicValue.getInstance().getUrlHead() + "Board/deleteBoard.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("boardNo", "" + boardNo));
            params.add(new BasicNameValuePair("userNo", "" + userNo));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Debug", "boardDelete complete");
        return result;
    }

    //팔로우 목록
    static public String getFollowInfo(int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/HPgetNeighborInfo.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + userNo));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "" + result);
        return result;
    }

    //나를 팔로우한 사람 목록
    static public String getFollowerInfo(int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/getFollowerInfo.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + userNo));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //글 쓰기
    static public String writeBoard(Board board) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead() + "Board/new_writeBoard.jsp";
            String map = Double.toString(board.getCoordinate().getmLatitude()) + " " + Double.toString(board.getCoordinate().getmLongitude());
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("text", "" + board.getText()));
            params.add(new BasicNameValuePair("courseName", board.getBasicAttributes().getCourseName()));// 베이직 밸류 애트리뷰트에 추가해야함
            params.add(new BasicNameValuePair("mainImg", "" + board.getMainImg()));

            //send ImageArr
            List<String> ImageArr = board.getImageNames();
            int i = 0;
            for (String temp : ImageArr) {
                params.add(new BasicNameValuePair("imgPath" + (i++), temp));
            }
            params.add(new BasicNameValuePair("imgPathCount", "" + ImageArr.size()));

            //Send Hash Arr
            i = 0;
            List<String> HashArr = board.getHashTags();
            for (String temp : HashArr) {
                params.add(new BasicNameValuePair("hashTag" + (i++), temp));
                Log.d("해시배열", "arr : " + temp);
            }
            params.add(new BasicNameValuePair("hashNum", "" + HashArr.size()));

            params.add(new BasicNameValuePair("courseNo", "" + board.getBasicAttributes().getCourseNo()));
            params.add(new BasicNameValuePair("map", "" + map));


            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Jspconn", "writeBoard result :" + result.trim());
        return result.trim();
    }

    static public String editBoard(Board board) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead() + "Board/new_editBoard.jsp";
            String map = Double.toString(board.getCoordinate().getmLatitude()) + " " + Double.toString(board.getCoordinate().getmLongitude());
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("boardNo", "" + board.getBoardNo()));
            params.add(new BasicNameValuePair("text", "" + board.getText()));
            params.add(new BasicNameValuePair("mainImg", "" + board.getMainImg()));

            //send ImageArr
            List<String> ImageArr = board.getImageNames();
            int i = 0;
            for (String temp : ImageArr) {
                params.add(new BasicNameValuePair("imgPath" + (i++), temp));
            }
            params.add(new BasicNameValuePair("imgPathCount", "" + ImageArr.size()));

            //Send Hash Arr
            i = 0;
            List<String> HashArr = board.getHashTags();
            for (String temp : HashArr) {
                params.add(new BasicNameValuePair("hashTag" + (i++), temp));
                Log.d("해시배열", "arr : " + temp);
            }
            params.add(new BasicNameValuePair("hashNum", "" + HashArr.size()));

            params.add(new BasicNameValuePair("map", "" + map));


            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Jspconn", "editBoard result :" + result);
        return result;
    }

    /**
     * login
     */
    //아이디 중복 체크
    static public boolean checkDuplID(String id) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/CheckDuplID.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("E_mail_ID", id));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("duplication", "ID:" + resultStr);
        if (resultStr.equals(" no duplication")) return true;
        else return false;
    }

    //별명 중복 체크 ( 한글 안되는거같음)
    static public boolean checkDuplNickname(String nickname) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/CheckDuplNickname.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Nickname", nickname));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("duplication", "nickname:" + resultStr);
        if (resultStr.equals(" no duplication")) {
            return true;
        } else {
            return false;
        }
    }


    //회원 가입
    static public int recordMember(User user) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/recordUser3.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("nickname", user.nickName));
            params.add(new BasicNameValuePair("id", user.e_mail));
            params.add(new BasicNameValuePair("pwd", user.pwd));
            params.add(new BasicNameValuePair("ymd", user.ymd));
            params.add(new BasicNameValuePair("gender", user.gender));
            params.add(new BasicNameValuePair("name", user.name));
            params.add(new BasicNameValuePair("tel", user.tel));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
            }

            Log.i(TAG, "Insert User" + resultStr);
            return Integer.parseInt(resultStr.trim());
        } catch (Exception e) {
            Log.e(TAG, "recordMember error" + e.getMessage());
            return 0;
        }
    }

    //로그인 시 비밀번호 대조
    static public int checkPwd(String id, String pwd) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/HPloginCheck.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", id));
            params.add(new BasicNameValuePair("PWD", pwd));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Log.d("loginCheck result", resultStr);
            if ((!resultStr.equals("not exist ID")) && !resultStr.equals("false")) {
                JSONObject jsonResult = new JSONObject(resultStr);
                if (jsonResult.get("result").equals("true")) {
                    return jsonResult.getInt("No");
                }
            }
        } catch (Exception e) {
        }
        return 0;
    }

    //ID로 유저넘버 반환
    static public int getUserNo(String emailID) { // 이메일 아이디를 받아 유저넘버 반환
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead() + "Member/getUserNo.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("emailID", "" + emailID));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("TAG", "getUserNo result :" + result);
        result = result.trim();
        return Integer.parseInt(result);
    }

    //카카오 회원가입
    static public boolean kakaoRecordUser(String kakaoID, String kakaoNickname) {
        String resultStr = "";
        Log.d("JSP", "Called kakaoRecordUser");
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/kakaoRecordUser.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("kakaoNickname", kakaoNickname));
            params.add(new BasicNameValuePair("kakaoID", kakaoID));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //카카오 로그인값이 있는지 체크
    static public int kakaoCheck(String kakaoID, String kakaoNickname) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/kakaoCheck.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("kakaoID", kakaoID));
            params.add(new BasicNameValuePair("kakaoNickname", kakaoNickname));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
            }
        } catch (Exception e) {
            Log.e(TAG, "kakaoCheck 캐치진입 :" + e.getMessage());
        }
        try {
            Log.d(TAG, "kakaoCheck result :" + resultStr);
            if ((!resultStr.equals("not exist ID")) && !resultStr.equals("false")) {
                JSONObject jsonResult = new JSONObject(resultStr);
                if (jsonResult.get("result").equals("true")) {
                    return jsonResult.getInt("No");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "kakaoCheck 캐치진입 :" + e.getMessage());
        }
        return -1;
    }

    /**
     * Setting
     */
    //비밀번호 변경
    static public String changePwd(String password, String newPass) {
        String result = "";

        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/changePwd.jsp";    //어디감????
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("UserNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("PWD", password));
            params.add(new BasicNameValuePair("newPWD", newPass));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //닉네임 변경
    static public String changeNickname(String nickname) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/updateNickname.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("nickname", nickname));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * UserInfo
     */

    //set이 1이면 팔로우, 0이면 언팔로우
    static public String setFollower(int userNo, int set) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/setFollower.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("followNo", "" + userNo));
            params.add(new BasicNameValuePair("set", "" + set));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //팔로우가 존재하면 exist, 존재하지 않는다면 not_exist 반환
    static public String checkFollow(int follower) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Member/CheckFollow.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("follower", "" + follower));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static public String notice(int userNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "Board/Comment/test.jsp";  //Notice로 바꾸기
        HttpPost post = new HttpPost(postURL);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("UserNo", "" + userNo));
        String result = "";

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 보드넘버를 받아 스크랩한 유저넘버 반환
    static public String getScrapUser(int boardNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            //getScrapUser.jsp에 이미지경로 불러오는 부분 빼기 => 추후수정
            String postURL = BasicValue.getInstance().getUrlHead() + "Scrap/getScrapUser.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("boardNo", "" + boardNo));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 코스 삭제
    static public String deleteCourse(int userNo, int courseNo, String title) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course/DeleteCourse.jsp";
        HttpPost post = new HttpPost(postURL);

        Log.e("deletecourseno",""+courseNo);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courseNo", "" + courseNo));
        params.add(new BasicNameValuePair("userNo", "" + userNo));
        params.add(new BasicNameValuePair("title", title));
        String result = "";

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //코스 수정
    static public String editCourse(int courseNo, String title, List<Course> courses) {
        String result = "";
        try {
            passiveMethod();

            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Course/editCourse.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();

            Log.i("JSPCONN", BasicValue.getInstance().getUserNo() + " / " + courses.size() + " / " + courseNo);

            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("courseNum", "" + String.valueOf(courses.size())));
            params.add(new BasicNameValuePair("courseNo", "" + courseNo));
            int i = 1;
            for (Course course : courses)
                params.add(new BasicNameValuePair("course" + i++, course.getTitle() + "/" + course.getDateTime().getTime()));

            params.add(new BasicNameValuePair("title", title));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //코스와 메모 수정
    static public void editCourseAndMemo(int courseNo, String title, List<Course> courses, String memo) {
        String result = "";
        try {
            passiveMethod();

            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Course/editCourseWithMemo.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();

            Log.i("JSPCONN", BasicValue.getInstance().getUserNo() + " / " + courses.size() + " / " + courseNo);

            params.add(new BasicNameValuePair("userNo", "" + BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("courseNum", "" + String.valueOf(courses.size())));
            params.add(new BasicNameValuePair("courseNo", "" + courseNo));
            params.add(new BasicNameValuePair("memo", memo));
            int i = 1;
            for (Course course : courses)
                params.add(new BasicNameValuePair("course" + i++, course.getTitle() + "/" + course.getDateTime().getTime()));

            params.add(new BasicNameValuePair("title", title));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            // Log.e("editCourse_Test rs: ", "" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public String getBoardNoForEdit(int courseNo, String courseName) { // 코스넘버와 코스 이름을 받아 보드넘버 반환
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead() + "Board/getBoardNoForEdit.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("courseNo", "" + courseNo));
            params.add(new BasicNameValuePair("courseName", courseName));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("TAG", "getBoardNoForEdit result :" + result);
        return result;
    }

    //코스 번호와 코스 이름을 받아 Course_i 의 i값 리턴
    static public int getCoursePo(int Course_No, String Course) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course/getCoursePo.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courseNo", "" + Course_No));
        Course = Course.trim();
        Course = Course + "%'"; // 자바스크립트에서 뒤에 문자가 안붙어서 안드로이드에서 붙여서 넘겨줌
        params.add(new BasicNameValuePair("course", Course));
        String result = "";

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result = result.trim();     // DB에서 값을 받아올 때 공백을 제거함
        Log.e("jspconn", "getCoursePo result :" + result);

        int coursePo = 0;
        try {
            coursePo = Integer.parseInt(result);
        } catch (NumberFormatException e) {
            Log.e(TAG, "THIS BOARD IS NOT COURSE");
        }
        return coursePo; // 코스포지션은 0부터 시작하므로 1을 지워줌
    }

    static public String getCourseName(int Board_No) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course/getCourseName.jsp";
        HttpPost post = new HttpPost(postURL);
        Log.e("jspconn", "getCourseName Board_No :" + Board_No);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("board_No", "" + Board_No));
        String result = "";

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = result.substring(4); // DB에서 값을 받아올 때 공백을 제거함
        Log.e("jspconn", "getCourseName result :" + result);
        return result;
    }


    static public String getCourseTitle(int courseNo) {
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course/getCourseTitle.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courseNo", "" + courseNo));
        String result = "";

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("jspconn", "서버서버 :" + result);
        return result;
    }


    static public void passiveMethod() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
