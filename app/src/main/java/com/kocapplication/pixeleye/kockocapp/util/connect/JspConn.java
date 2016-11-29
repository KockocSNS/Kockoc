package com.kocapplication.pixeleye.kockocapp.util.connect;

import android.os.StrictMode;
import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.model.Board;
import com.kocapplication.pixeleye.kockocapp.model.Course;
import com.kocapplication.pixeleye.kockocapp.model.User;
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
     * Board
     */
    //글 삭제
    static public String boardDelete(int boardNo, int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead() + "Board/deleteBoard.jsp"; //deleteBoard로 변경
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
        Log.d("jspconn boardDelete", "complete");
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

            String postURL = BasicValue.getInstance().getUrlHead() + "Board/writeBoard.jsp";
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
            params.add(new BasicNameValuePair("stopoverNo", "" + getStopoverIndex(board.getBasicAttributes().getCourseNo(),board.getBasicAttributes().getCoursePosition())));


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
    //코스넘버와 경유지이름으로 코스 중복 검색
    static public boolean checkDuplBoard(String stopverName, String time, int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Board/CheckDuplBoardNo.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("StopoverName", stopverName));
            params.add(new BasicNameValuePair("UserNo", String.valueOf(userNo)));
            Log.e(TAG,"time :"+time);
            params.add(new BasicNameValuePair("Time", time));

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

        Log.d("duplication", "Stopover:" + stopverName);
        Log.d("duplication", "BoardNo:" + result);
        Log.d("duplication", "UserNo:" + BasicValue.getInstance().getUserNo());
        if (result.equals(" no duplication")) {
            return false;
        } else {
            return true;
        }

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

    //별명 중복 체크
    static public boolean checkDuplNickname(String nickname) {
        String result = "";
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
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("duplication", "nickname:" + result);
        if (result.equals(" no duplication")) {
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

            Log.i(TAG, "Insert User :" + resultStr);
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
            Log.d("TAG",e.toString());
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
        String postURL = BasicValue.getInstance().getUrlHead() + "Board/Comment/Notice.jsp";
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
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course_V2/deleteCourse.jsp";
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
    static public String editCourse(List<Course> arr, boolean publicity) {
        String result = "";
        try {
            passiveMethod();

            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead() + "Course_V2/editCourse.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("courseNo",String.valueOf(arr.get(0).getCourseNo())));
            params.add(new BasicNameValuePair("courseNum", "" + String.valueOf(arr.size()))); //stopover 갯수
            int i = 0;

            if (publicity) {
                params.add(new BasicNameValuePair("publicity", "0"));
            } else {
                params.add(new BasicNameValuePair("publicity", "1"));
            }

            for (Course temp : arr) {
//                + "/" + temp.getDataByMilSec()
                params.add(new BasicNameValuePair("course" + i +"Name", "" + temp.getTitle()));
                params.add(new BasicNameValuePair("course" + i +"Position", "" + temp.getCoursePosition()));
                params.add(new BasicNameValuePair("course" + i +"Memo", "" + temp.getMemo()));
                params.add(new BasicNameValuePair("course" + i +"DateTime", "" +temp.getDateTime()));
                Log.i("editCourse","DateTime :"+"" + temp.getDate()+" "+temp.getTime());
                i++;
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
            e.printStackTrace();
        }
        Log.d(TAG,"editCourse result :"+result);

        return result;
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
        if (result.equals("")) result = "0";
        Log.d("TAG", "getBoardNoForEdit result :" + result);
        return result;
    }

    static public String getBoardNo(int courseNo, int coursePo) { // 코스넘버와 코스 포지션을 받아 보드넘버 반환
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead() + "Course_V2/getBoardNo.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("courseNo", "" + courseNo));
            params.add(new BasicNameValuePair("coursePo", "" + coursePo));
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
        Log.d("TAG", "getBoardNo result :" + result);
        if(result.equals("")) result = "0";
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
        params.add(new BasicNameValuePair("courseName", Course));
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
        Log.d("jspconn", "getCoursePo result :" + result);

        int coursePo = 0;
        try {
            coursePo = Integer.parseInt(result);
        } catch (NumberFormatException e) {
            Log.e(TAG, "THIS BOARD IS NOT COURSE");
        }
        return coursePo;
    }
    //보드 넘버를 받아 코스 포지션 리턴
    static public int getCoursePo_use_boardNo(int boardNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course/getCoursePo_use_boardNo.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("boardNo", ""+boardNo));
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
        Log.d("jspconn", "getCoursePo_use_boardNo result :" + result);

        int coursePo = 0;
        try {
            coursePo = Integer.parseInt(result);
        } catch (NumberFormatException e) {
            Log.e(TAG, "THIS BOARD IS NOT COURSE");
        }
        return coursePo;
    }

    static public String getCourseName(int Board_No) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course/getCourseName.jsp";
        HttpPost post = new HttpPost(postURL);
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
        result = result.trim(); // DB에서 값을 받아올 때 공백을 제거함
        return result;
    }


    static public String getCourseTitle(int courseNo) {
        Log.e("jspconn getcourseTitle","courseNo :"+courseNo);
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
        Log.e("jspconn getcourseTitle","result :"+result);
        return result;
    }
    static public String setCourseMemo(String memo, int courseNo, int coursePo) {
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course_V2/setCourseMemo.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courseNo", "" + courseNo));
        params.add(new BasicNameValuePair("coursePo", "" + coursePo));
        params.add(new BasicNameValuePair("memo", "" + memo));
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
        Log.i("setCourseMemo"," :"+result);
        return result;
    }

    static public String getCourseMemo(int courseNo, int coursePo) {
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course_V2/getCourseMemo.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courseNo", "" + courseNo));
        params.add(new BasicNameValuePair("coursePo", "" + coursePo));
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
    static public String getStopoverIndex(int courseNo, int coursePo) {
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead() + "/Course_V2/getStopoverIndex.jsp";
        HttpPost post = new HttpPost(postURL);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courseNo", "" + courseNo));
        params.add(new BasicNameValuePair("coursePo", "" + coursePo));
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
        Log.e(TAG,"getStopoverIndex result :"+result);
        return result;
    }


    static public void passiveMethod() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
