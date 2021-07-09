package com.jochef2.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "routes_table")
public class Route {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int mId;

    @NonNull
    @ColumnInfo(name = "reiseId")
    public int mReiseId;

    @NonNull
    @ColumnInfo(name = "name")
    public String mName;

    @ColumnInfo(name = "time")
    public Calendar mTime;

    @ColumnInfo(name = "mileage")
    public double mMileage;

    public Route(int reiseId, @NonNull String name, Calendar time) {
        mReiseId = reiseId;
        mName = name;
        mTime = time;
    }

    public int getId() {
        return mId;
    }

    public int getReiseId() {
        return mReiseId;
    }

    public void setReiseId(int reiseId) {
        mReiseId = reiseId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }

    public Calendar getTime() {
        return mTime;
    }

    public void setTime(Calendar time) {
        mTime = time;
    }

    public double getMileage() {
        return mMileage;
    }

    public void setMileage(double mileage) {
        mMileage = mileage;
    }

    public String getTimeString() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        return simpleDate.format(mTime.getTime());
    }
}
