package com.kocapplication.pixeleye.kockocapp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016-09-29.
 */
public class TourDataList implements Serializable{
    private ArrayList<TourData> tourDataList = new ArrayList<>();

    public TourDataList(ArrayList<TourData> tourDataList) {
        this.tourDataList = tourDataList;
    }

    public ArrayList<TourData> getTourDataList() {
        return tourDataList;
    }

    public void setTourDataList(ArrayList<TourData> tourDataList) {
        this.tourDataList = tourDataList;
    }
}
