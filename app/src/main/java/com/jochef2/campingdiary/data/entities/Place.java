package com.jochef2.campingdiary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jochef2.campingdiary.data.models.Address;
import com.jochef2.campingdiary.data.models.Cords;

import java.util.Objects;

@Entity(tableName = "places_table")
public class Place implements SortedListAdapter.ViewModel {

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

    @Override
    public <T> boolean isSameModelAs(@NonNull T model) {
        if (model instanceof Place) {
            final Place place = (Place) model;
            return place.mId == mId;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T model) {
        if (model instanceof Place) {
            final Place other = (Place) model;
            if (mId != other.mId) {
                return false;
            }
            return Objects.equals(mPlaceName, other.mPlaceName);
        }
        return false;
    }
}
