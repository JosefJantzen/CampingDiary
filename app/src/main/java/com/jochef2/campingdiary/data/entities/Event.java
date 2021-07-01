package com.jochef2.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.values.EventCategory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "events_table")
public class Event {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int mId;

    @NonNull
    @ColumnInfo(name = "reiseId")
    public int mReiseId;

    @ColumnInfo(name = "name")
    public String mName;

    @ColumnInfo(name = "description")
    public String mDescription;

    @ColumnInfo(name = "begin")
    public Calendar mBegin;

    @ColumnInfo(name = "end")
    public Calendar mEnd;

    @ColumnInfo(name = "cat")
    public EventCategory mCat;

    @Embedded
    public Price mPrice;

    @ColumnInfo(name = "placeId")
    public int mPlaceId;

    public Event(int reiseId, String name, Calendar begin, Calendar end, Price price) {
        mReiseId = reiseId;
        mName = name;
        mBegin = begin;
        mEnd = end;
        mPrice = price;
    }

    public EventCategory getCat() {
        return mCat;
    }

    public void setCat(EventCategory cat) {
        mCat = cat;
    }

    public Price getPrice() {
        return mPrice;
    }

    public void setPrice(Price price) {
        mPrice = price;
    }

    public int getReiseId() {
        return mReiseId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Calendar getBegin() {
        return mBegin;
    }

    public void setBegin(Calendar begin) {
        mBegin = begin;
    }

    public Calendar getEnd() {
        return mEnd;
    }

    public void setEnd(Calendar end) {
        mEnd = end;
    }

    public void setReiseId(int reiseId) {
        mReiseId = reiseId;
    }

    public int getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(int placeId) {
        mPlaceId = placeId;
    }

    public String getBeginDateTime() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        return simpleDate.format(mBegin.getTime());
    }

    public String getBeginDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY");
        return simpleDate.format(mBegin.getTime());
    }

    public String getBeginTime() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm");
        return simpleDate.format(mBegin.getTime());
    }

    public String getEndDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY");
        return simpleDate.format(mEnd.getTime());
    }

    public String getEndTime() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm");
        return simpleDate.format(mEnd.getTime());
    }

    public String getEndDateTime() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY HH:mm");
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
