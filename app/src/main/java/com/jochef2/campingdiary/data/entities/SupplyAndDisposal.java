package com.jochef2.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.values.SADCategory;

import java.util.Calendar;
import java.util.List;

@Entity(tableName = "supplyAndDisposal_table")
public class SupplyAndDisposal {

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

    @ColumnInfo(name = "cats")
    public List<SADCategory> mCats;

    @Embedded
    public Price mPrice;

    public SupplyAndDisposal(int reiseId, @NonNull String name, Calendar begin, List<SADCategory> cats) {
        mReiseId = reiseId;
        mName = name;
        mBegin = begin;
        mCats = cats;
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

    public void addCats(List<SADCategory> cat) {
        mCats.addAll(cat);
    }

    public List<SADCategory> getCats() {
        return mCats;
    }

    public void setCats(List<SADCategory> cats) {
        mCats = cats;
    }
}
