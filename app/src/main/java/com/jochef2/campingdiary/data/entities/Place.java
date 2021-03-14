package com.jochef2.campingdiary.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.jochef2.campingdiary.data.models.Address;
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

    @Embedded
    public Address mAddressObject;

    @Embedded
    public Cords mCords;

    public Place(String placeName) {
        mPlaceName = placeName;
    }

    @Ignore
    public Place(String placeName, Cords cords) {
        mPlaceName = placeName;
        mCords = cords;
    }

    public void setByPlace(com.google.android.libraries.places.api.model.Place place) {
        mPlaceId = place.getId();
        setCords(new Cords(place.getLatLng()));
        setAddressString(place.getAddress());
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String placeName) {
        mPlaceName = placeName;
    }

    public String getAddressString() {
        return mAddressString;
    }

    public void setAddressString(String addressString) {
        mAddressString = addressString;
    }

    public Address getAddressObject() {
        return mAddressObject;
    }

    public void setAddressObject(Address addressObject) {
        mAddressObject = addressObject;
    }

    public Cords getCords() {
        return mCords;
    }

    public void setCords(Cords cords) {
        mCords = cords;
    }
}
