package com.kocapplication.pixeleye.kockocapp.main.tour;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeongpil on 2016-09-21.
 */
public class TourRepo {
    @SerializedName("response") response response;

    public class response {
        @SerializedName("header")
        Result header;
        @SerializedName("body")
        body body;

        public class Result {
            @SerializedName("resultCode") String resultCode;
            @SerializedName("resultMsg") String resultMsg;

            public String getResultCode() {return resultCode;}
            public String getResultMsg() {return resultMsg;}
        }
        public class body {
            @SerializedName("items") items items;
            @SerializedName("numOfRows") String numOfRows;
            @SerializedName("pageNo") String pageNo;
            @SerializedName("totalCount") String totalCount;

            public class items{
                public List<item> item = new ArrayList<>();

                public class item {
                    @SerializedName("title") String title; //이름
                    @SerializedName("addr1") String addr; // 주소
                    @SerializedName("addr2") String detailAddr; // 상세 주소
                    @SerializedName("firstimage") String img; // 원본 대표 이미지
                    @SerializedName("firstimage2") String thumbImg; //썸네일 이미지
                    @SerializedName("mapx") String longitude; // 경도
                    @SerializedName("mapy") String latitude; //위도

                    public String getTitle() {return title;}
                    public String getAddr() {return addr;}
                    public String getDetailAddr() {return detailAddr;}
                    public String getImg() {return img;}
                    public String getThumbImg() {return thumbImg;}
                    public String getLongitude() {return longitude;}
                    public String getLatitude() {return latitude;}
                }
                public List<TourRepo.response.body.items.item> getItem() {return item;}
            }
            public TourRepo.response.body.items getItems() {return items;}
            public String getNumOfRows() {return numOfRows;}
            public String getPageNo() {return pageNo;}
            public String getTotalCount() {return totalCount;}
        }
        public Result getHeader() {return header;}
        public TourRepo.response.body getBody() {return body;}
    }

    public TourRepo.response getResponse() {return response;}

    public interface areaApiInterface {
        @GET("openapi/service/rest/KorService/areaBasedList")
        Call<TourRepo> get_area_retrofit(@Query("ServiceKey") String apiKey,
                                         @Query("arrange") String arrange,
                                         @Query("MobileOS") String os,
                                         @Query("MobileApp") String appName,
                                         @Query("contentTypeId") String content,
                                         @Query("areaCode") String area,
                                         @Query("cat1") String category,
                                         @Query("_type") String type,
                                         @Query("pageNo") String pageNo);
    }
    public interface keywordApiInterface {
        @GET("openapi/service/rest/KorService/searchKeyword")
        Call<TourRepo> get_keyword_retrofit(@Query("ServiceKey") String apiKey,
                                            @Query("arrange") String arrange,
                                            @Query("MobileOS") String os,
                                            @Query("MobileApp") String appName,
                                            @Query("keyword") String keyword,
                                            @Query("_type") String type,
                                            @Query("pageNo") String pageNo);
    }

}
