package com.kocapplication.pixeleye.kockocapp.main.tour.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hyeongpil on 2016-09-21.
 */
public class TourImageRepo {
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
                    @SerializedName("originimgurl") String img; // 원본
                    @SerializedName("smallimageurl") String thumnail; // 썸네일

                    public String getImg() {return img;}
                    public String getThumnail() {return thumnail;}
                }
                public List<TourImageRepo.response.body.items.item> getItem() {return item;}
            }
            public TourImageRepo.response.body.items getItems() {return items;}
            public String getNumOfRows() {return numOfRows;}
            public String getPageNo() {return pageNo;}
            public String getTotalCount() {return totalCount;}
        }
        public Result getHeader() {return header;}
        public TourImageRepo.response.body getBody() {return body;}
    }

    public TourImageRepo.response getResponse() {return response;}

    public interface tourImageApiInterface {
        @GET("openapi/service/rest/KorService/detailImage")
        Call<TourImageRepo> get_tour_image_retrofit(@Query("ServiceKey") String apiKey,
                                                     @Query("MobileOS") String os,
                                                     @Query("MobileApp") String appName,
                                                     @Query("contentId") String contentId,
                                                     @Query("contentTypeId") String contentType,
                                                     @Query("_type") String type);
    }

}
