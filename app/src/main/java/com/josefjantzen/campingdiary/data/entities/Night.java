package com.josefjantzen.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.josefjantzen.campingdiary.data.models.Price;
import com.josefjantzen.campingdiary.data.values.NightCategory;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "nights_table")
public class Night {

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

    @ColumnInfo(name = "begin")
    public Calendar mBegin;

    @ColumnInfo(name = "end")
    public Calendar mEnd;

    @ColumnInfo(name = "cat")
    public NightCategory mCat;

    @Embedded
    public Price mPrice;

    @ColumnInfo(name = "placeId")
    public int mPlaceId;

    public Night(int reiseId, @NotNull String name, Calendar begin, Calendar end, NightCategory cat) {
        mReiseId = reiseId;
        mName = name;
        mBegin = begin;
        mEnd = end;
        mCat = cat;
    }

    @Ignore
    public Night(int reiseId, @NotNull String name, Calendar begin, Calendar end, NightCategory cat, Price price) {
        mReiseId = reiseId;
        mName = name;
        mBegin = begin;
        mEnd = end;
        mCat = cat;
        mPrice = price;
    }

    public NightCategory getCat() {
        return mCat;
    }

    public void setCat(NightCategory cat) {
        mCat = cat;
    }

    @NonNull
    public int getReiseId() {
        return mReiseId;
    }

    public void setReiseId(int reiseId) {
        mReiseId = reiseId;
    }

    @NotNull
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

    public Calendar getBegin() {
        return (Calendar) mBegin.clone();
    }

    public void setBegin(Calendar begin) {
        mBegin = begin;
    }

    public Calendar getEnd() {
        return (Calendar) mEnd.clone();
    }

    public void setEnd(Calendar end) {
        mEnd = end;
    }

    public Price getPrice() {
        return mPrice;
    }

    public void setPrice(Price price) {
        mPrice = price;
    }

    public void setPriceNumber(double priceNumber) {
        mPrice.setPrice(priceNumber);
    }

    public void setCurrency(String ISO) {
        mPrice.setCurrency(ISO);
    }

    public int getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(int placeId) {
        mPlaceId = placeId;
    }

    public int getId() {
        return mId;
    }

    public String getBeginDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY");
        return simpleDate.format(mBegin.getTime());
    }

    public String getEndDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY");
        return simpleDate.format(mEnd.getTime());
    }

    public String getDates() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY");
        String end = "in die Ewigkeit";
        if (mEnd != null) {
            end = simpleDate.format(mEnd.getTime());
        }
        return simpleDate.format(mBegin.getTime()) + " - " + end;
    }
}
