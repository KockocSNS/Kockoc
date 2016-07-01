package com.kocapplication.pixeleye.kockocapp.util;

import android.os.StrictMode;
import android.util.Log;

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
     * DetailPage
     */
    static public String loadDetailPage(String boardNo) {
        Log.e(TAG,"boardNo :"+boardNo);
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead()+"Board/LoadDetailPage.jsp";
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
        Log.d(TAG,"loadDetailPage result :"+result);
        return result;
    }

    static public String WriteComment(String comment, int boardNo, int userNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead()+"Board/Comment/WriteComment.jsp";
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

            String postURL = BasicValue.getInstance().getUrlHead()+"GCM/GCM.jsp";
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
        Log.d("TAG", "pushGCM result :" + result);
        return result;
    }
    static public String DeleteComment(int commentNo) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead()+"Board/Comment/DeleteComment.jsp";
        HttpPost post = new HttpPost(postURL);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("commentNo", "" +commentNo));
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
    static public String checkExpression(int boardNo){
        String result="";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Board/Expression/checkExpression.jsp";
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
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    static public String writeExpression(int boardNo, int Status) {
        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead()+"Board/Expression/test/writeExpression.jsp";
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
    static public String addScrap(int boardNo) {

        passiveMethod();
        HttpClient client = new DefaultHttpClient();
        String postURL = BasicValue.getInstance().getUrlHead()+"Scrap/AddScrap.jsp";
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

    /**
     * Course
     */
    //코스 넘버를 받아 course 반환
    static public String readCourseByCourseNo(int courseNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Course/readCourseByCourseNo.jsp";
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

    static public String getNeighborInfo(int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/HPgetNeighborInfo.jsp";
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
        Log.e(TAG,""+result);
        return result;
    }

    static public String getFollowerInfo(int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/getFollowerInfo.jsp";
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

    /**
     * Board
     */
    static public String boardDelete(int boardNo, int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL =BasicValue.getInstance().getUrlHead()+"Board/HPdeleteBoard.jsp";
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
    static public String getFollowInfo(int userNo) {
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/HPgetNeighborInfo.jsp";
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
        Log.e(TAG,""+result);
        return result;
    }

    /**
     * login
     */
    static public boolean checkDuplID(String id) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/CheckDuplID.jsp";
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
    static public boolean checkDuplNickname(String nickname) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/CheckDuplNickname.jsp";
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
        if (resultStr.equals(" no duplication")) {return true;}
        else {return false;}
    }
    static public boolean recordMember(User user) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/recordUser2.jsp";
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
        } catch (Exception e) {Log.e(TAG,"recordMember error"+e.getMessage());}
        try {
            if ((!resultStr.equals("not exist ID")) && !resultStr.equals("false")) {
                JSONObject jsonResult = new JSONObject(resultStr);
                if (jsonResult.get("result").equals("true")) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG,"recordMember error :"+ e.getMessage());
        }
        return false;
    }
    static public void updateUser(String nickName, String tel, String gender,String birth) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/updateUser.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("userNo", ""+BasicValue.getInstance().getUserNo()));
            params.add(new BasicNameValuePair("nickname", nickName));
            params.add(new BasicNameValuePair("tel", tel));
            params.add(new BasicNameValuePair("gender", gender));
            params.add(new BasicNameValuePair("birth", birth));

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
        Log.d(TAG,"updateUser result :"+resultStr);
    }
    static public int checkPwd(String id, String pwd) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/HPloginCheck.jsp";
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
        } catch (Exception e) {e.printStackTrace();}
        try {
            Log.d("loginCheck result", resultStr);
            if ((!resultStr.equals("not exist ID")) && !resultStr.equals("false")) {
                JSONObject jsonResult = new JSONObject(resultStr);
                if (jsonResult.get("result").equals("true")) {
                    return jsonResult.getInt("No");
                }
            }
        } catch (Exception e) {}
        return 0;
    }
    static public int getUserNo(String emailID) { // 이메일 아이디를 받아 유저넘버 반환
        String result = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();

            String postURL = BasicValue.getInstance().getUrlHead()+"Member/getUserNo.jsp";
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
    static public int kakaoCheck(String kakaoID, String kakaoNickname) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/kakaoCheck.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("kakaoID",kakaoID));
            params.add(new BasicNameValuePair("kakaoNickname",kakaoNickname));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
            }
        } catch (Exception e) {Log.e(TAG,"kakaoCheck 캐치진입 :"+e.getMessage());}
        try {
            Log.d(TAG, "kakaoCheck result :" + resultStr);
            if ((!resultStr.equals("not exist ID")) && !resultStr.equals("false")) {
                JSONObject jsonResult = new JSONObject(resultStr);
                if (jsonResult.get("result").equals("true")) {
                    return jsonResult.getInt("No");
                }
            }
        } catch (Exception e) {Log.e(TAG,"kakaoCheck 캐치진입 :"+e.getMessage());}
        return -1;
    }
    static public boolean kakaoRecordUser(String kakaoNickname,String kakaoID) {
        String resultStr = "";
        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/kakaoRecordUser.jsp";
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
        } catch (Exception e) {Log.e(TAG,"kakaoRecordUser error :"+e.getMessage());}
        return false;
    }

    static public String test(String password, String newPass) {
        String result = "";

        try {
            passiveMethod();
            HttpClient client = new DefaultHttpClient();
            String postURL = BasicValue.getInstance().getUrlHead()+"Member/test.jsp";
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
        Log.e(TAG,result);
        return result;
    }


    static public void passiveMethod() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
