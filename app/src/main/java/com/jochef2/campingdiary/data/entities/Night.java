package com.jochef2.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.values.NightCategory;

import org.jetbrains.annotations.NotNull;

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

    public Night(int reiseId, String name, Calendar begin, NightCategory cat) {
        mReiseId = reiseId;
        mName = name;
        mBegin = begin;
        mCat = cat;
    }

    @Ignore
    public Night(int reiseId, String name, Calendar begin, NightCategory cat, Price price) {
        mReiseId = reiseId;
        mName = name;
        mBegin = begin;
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

    public Price getPrice() {
        return mPrice;
    }

    public void setPrice(Price price) {
        mPrice = price;
    }
}
