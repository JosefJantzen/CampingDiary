package com.jochef2.campingdiary.data.models;

import androidx.room.ColumnInfo;

public class Cords {

    @ColumnInfo(name = "latitude")
    public double mLatitude;

    @ColumnInfo(name = "longitude")
    public double mLongitude;

    public Cords(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
