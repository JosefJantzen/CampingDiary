package com.jochef2.campingdiary.data.entities;

import android.location.Address;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jochef2.campingdiary.data.models.Cords;

@Entity(tableName = "places_table")
public class Place {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "placeId")
    public String mPlaceId;

    @ColumnInfo(name = "placeName")
    public String mPlaceName;

    @ColumnInfo(name = "address_string")
    public String mAddressString;

    @ColumnInfo(name = "address_object")
    public Address mAddressObject;

    @Embedded
    public Cords mCords;
}
