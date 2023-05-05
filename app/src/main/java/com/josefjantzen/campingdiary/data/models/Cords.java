package com.josefjantzen.campingdiary.data.models;

import android.location.Address;
import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.android.gms.maps.model.LatLng;

public class Cords {

    @ColumnInfo(name = "latitude")
    public double mLatitude;

    @ColumnInfo(name = "longitude")
    public double mLongitude;

    @ColumnInfo(name = "altitude")
    public double mAltitude;

    public Cords(double latitude, double longitude, double altitude) {
        mLatitude = latitude;
        mLongitude = longitude;
        mAltitude = altitude;
    }

    @Ignore
    public Cords(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        mAltitude = location.getAltitude();
    }

    @Ignore
    public Cords(LatLng latLng) {
        mLatitude = latLng.latitude;
        mLongitude = latLng.longitude;
    }

    @Ignore
    public Cords(Address address) {
        if (address.hasLatitude() && address.hasLongitude()) {
            mLatitude = address.getLatitude();
            mLongitude = address.getLongitude();
        }
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

    public double getAltitude() {
        return mAltitude;
    }

    public void setAltitude(double altitude) {
        mAltitude = altitude;
    }

    public LatLng toLatLng() {
        return new LatLng(mLatitude, mLongitude);
    }
}
