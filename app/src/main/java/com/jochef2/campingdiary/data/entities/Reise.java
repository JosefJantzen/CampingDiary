package com.jochef2.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "reisen_table")
public class Reise {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "name")
    public String mName;

    @ColumnInfo(name = "land")
    public String mLand;

    @ColumnInfo(name = "description")
    public String mDescription;

    @ColumnInfo(name = "begin")
    public Calendar mBegin;

    @ColumnInfo(name = "end")
    public Calendar mEnd;

    public Reise(String name, Calendar begin, Calendar end) {
        mName = name;
        mBegin = begin;
        mEnd = end;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLand() {
        return mLand;
    }

    public void setLand(String land) {
        mLand = land;
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

    public String getDates() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY");
        String end = "in die Ewigkeit";
        if (mEnd != null) {
            end = simpleDate.format(mEnd.getTime());
        }
        return simpleDate.format(mBegin.getTime()) + " - " + end;
    }

    public String getBeginDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY");
        return simpleDate.format(mBegin.getTime());
    }

    public String getEndDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.YYYY");
        return simpleDate.format(mEnd.getTime());
    }
}
