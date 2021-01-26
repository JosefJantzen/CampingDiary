package com.jochef2.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.values.EventCategory;

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

    public Event(int reiseId, String name, Calendar begin, EventCategory cat) {
        mReiseId = reiseId;
        mName = name;
        mBegin = begin;
        mCat = cat;
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
}
