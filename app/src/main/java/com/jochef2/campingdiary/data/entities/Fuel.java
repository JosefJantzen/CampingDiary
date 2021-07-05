package com.jochef2.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jochef2.campingdiary.data.models.Price;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "fuel_table")
public class Fuel {

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

    @ColumnInfo(name = "description")
    public String mDescription;

    @ColumnInfo(name = "time")
    public Calendar mTime;

    @ColumnInfo(name = "liter")
    public double mLiter;

    @Embedded
    public Price mPrice;

    @ColumnInfo(name = "palceId")
    public int mPlaceId;

    public Fuel(int reiseId, @NonNull String name, Calendar time, Price price) {
        mReiseId = reiseId;
        mName = name;
        mTime = time;
        mPrice = price;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Calendar getTime() {
        return mTime;
    }

    public void setTime(Calendar time) {
        mTime = time;
    }

    public double getLiter() {
        return mLiter;
    }

    public void setLiter(double liter) {
        mLiter = liter;
    }

    public Price getPrice() {
        return mPrice;
    }

    public void setPrice(Price price) {
        mPrice = price;
    }

    public int getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(int placeId) {
        mPlaceId = placeId;
    }

    public void setPriceNumber(double price) {
        mPrice.setPrice(price);
    }

    public void setCurrency(String code) {
        mPrice.setCurrency(code);
    }

    public String getTimeString() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        return simpleDate.format(mTime.getTime());
    }
}
