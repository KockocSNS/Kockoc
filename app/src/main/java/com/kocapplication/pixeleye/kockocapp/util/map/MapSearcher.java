package com.kocapplication.pixeleye.kockocapp.util.map;

/**
 * Created by Han_ on 2016-06-27.
 */
public class MapSearcher {
    /**
     * category codes
     * MT1 대형마트
     * CS2 편의점
     * PS3 어린이집, 유치원
     * SC4 학교
     * AC5 학원
     * PK6 주차장
     * OL7 주유소, 충전소
     * SW8 지하철역
     * BK9 은행
     * CT1 문화시설
     * AG2 중개업소
     * PO3 공공기관
     * AT4 관광명소
     * AD5 숙박
     * FD6 음식점
     * CE7 카페
     * HP8 병원
     * PM9 약국
     */

    // http://dna.daum.net/apis/local
    public static final String DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/keyword.json?query=%s&location=%f,%f&radius=%d&page=%d&apikey=%s";
    public static final String DAUM_MAPS_LOCAL_CATEGORY_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/category.json?code=%s&location=%f,%f&radius=%d&page=%d&apikey=%s";
    public static final String DAUM_MAPS_KEYWORD_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/keyword.json?query=%s&page=%d&apikey=%s";

    private static final String HEADER_NAME_X_APPID = "x-appid";
    private static final String HEADER_NAME_X_PLATFORM = "x-platform";
    private static final String HEADER_VALUE_X_PLATFORM_ANDROID = "android";

    private static final int resultObjectSize = 10;


}
