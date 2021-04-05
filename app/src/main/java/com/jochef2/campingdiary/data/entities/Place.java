package com.jochef2.campingdiary.data.entities;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.jochef2.campingdiary.data.models.Address;
import com.jochef2.campingdiary.data.models.Cords;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

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

    @Ignore
    public double mDistance = -1;

    public Place(String placeName) {
        mPlaceName = placeName;
    }

    @Ignore
    public Place(String placeName, Cords cords) {
        mPlaceName = placeName;
        mCords = cords;
    }

    public void setByPlace(com.google.android.libraries.places.api.model.Place place, Context context) {
        mPlaceId = place.getId();
        setCords(new Cords(place.getLatLng()));
        setAddressString(place.getAddress());

        predictAddress(context);
    }

    public String distanceTo(Place place) {
        Location one = new Location("");
        one.setLatitude(this.getCords().getLatitude());
        one.setLongitude(this.getCords().getLongitude());
        Location two = new Location("");
        one.setLatitude(place.getCords().getLatitude());
        one.setLongitude(place.getCords().getLongitude());
        double distance = one.distanceTo(two);
        if (distance >= 1000) {
            return new BigDecimal(distance / 1000).setScale(1, BigDecimal.ROUND_HALF_EVEN) + " km";
        } else {
            return new BigDecimal(distance).setScale(2, BigDecimal.ROUND_HALF_EVEN) + " m";
        }
    }

    public String distanceTo(Location two) {
        Location one = new Location("");
        one.setLatitude(this.getCords().getLatitude());
        one.setLongitude(this.getCords().getLongitude());

        if (two != null) {
            double distance = one.distanceTo(two);
            if (distance >= 1000) {
                return (new BigDecimal(distance / 1000).setScale(1, BigDecimal.ROUND_HALF_EVEN) + " km").replace(".", ",");
            } else {
                return (new BigDecimal(distance).setScale(2, BigDecimal.ROUND_HALF_EVEN) + " m").replace(".", ",");
            }
        } else {
            return "NaN";
        }
    }

    public boolean setDistanceTo(Location two) {
        Location one = new Location("");
        one.setLatitude(this.getCords().getLatitude());
        one.setLongitude(this.getCords().getLongitude());

        if (two != null) {
            setDistance(one.distanceTo(two));
            return true;
        } else {
            setDistance(-1);
            return false;
        }
    }

    public String getDistanceString() {
        if (getDistance() != -1) {
            double distance = getDistance();
            if (distance >= 1000) {
                return (new BigDecimal(distance / 1000).setScale(1, BigDecimal.ROUND_HALF_EVEN) + " km").replace(".", ",");
            } else {
                return (new BigDecimal(distance).setScale(2, BigDecimal.ROUND_HALF_EVEN) + " m").replace(".", ",");
            }
        } else {
            return "NaN";
        }
    }

    public void predictAddress(Context context) {
        Location location = new Location("");
        location.setLatitude(getCords().getLatitude());
        location.setLongitude(getCords().getLongitude());
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<android.location.Address> addresse = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresse.isEmpty()) {
                this.setAddressObject(new Address(addresse.get(0)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateAddressString() {
        if (this.getAddressString() == null || this.getAddressString().isEmpty()) {
            String address = "";
            if (this.getAddressObject() != null) {
                if (this.getAddressObject().getThoroughfare() != null && this.getAddressObject().getSubThoroughfare() != null && this.getAddressObject().getLocality() != null) {
                    address = this.getAddressObject().getThoroughfare() + " " + this.getAddressObject().getSubThoroughfare() + ", " + this.getAddressObject().getLocality();
                } else if (this.getAddressObject().getAddressLine(0) != null) {
                    address = this.getAddressObject().getAddressLine(0);
                }
            } else if (this.getCords() != null) {
                address = this.getCordsString();
            }
            setAddressString(address);
        }
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

    public String getCordsString() {
        return getCords().getLatitude() + ", " + getCords().getLongitude() + " (lat, lng)";
    }

    public void setCords(Cords cords) {
        mCords = cords;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }
}
