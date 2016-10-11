package com.kocapplication.pixeleye.kockocapp.main.tour.retrofit;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hyeongpil on 2016-09-21.
 */
public class TourDetailRepo {
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
                public item item = new item();

                public class item {
                    @SerializedName("title") String title; // 제목
                    @SerializedName("overview") String overview; // 내용
                    public String getTitle() {return title;}
                    public String getOverview() {return overview;}
                }
                public TourDetailRepo.response.body.items.item getItem() {return item;}
            }
            public TourDetailRepo.response.body.items getItems() {return items;}
            public String getNumOfRows() {return numOfRows;}
            public String getPageNo() {return pageNo;}
            public String getTotalCount() {return totalCount;}
        }
        public Result getHeader() {return header;}
        public TourDetailRepo.response.body getBody() {return body;}
    }

    public TourDetailRepo.response getResponse() {return response;}

    public interface tourDetailApiInterface {
        @GET("openapi/service/rest/KorService/detailCommon")
        Call<TourDetailRepo> get_tour_detail_retrofit(@Query("ServiceKey") String apiKey,
                                                      @Query("MobileOS") String os,
                                                      @Query("MobileApp") String appName,
                                                      @Query("contentId") String contentId,
                                                      @Query("contentTypeId") String contentType,
                                                      @Query("overviewYN") String overviewYN,
                                                      @Query("defaultYN") String defaultYN,
                                                      @Query("_type") String type);
    }

}
