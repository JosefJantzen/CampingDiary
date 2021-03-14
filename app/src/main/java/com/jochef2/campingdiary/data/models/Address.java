package com.jochef2.campingdiary.data.models;


import android.os.Bundle;

import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A class representing an Address, i.e, a set of Strings describing a location.
 * <p>
 * The address format is a simplified version of xAL (eXtensible Address Language)
 * http://www.oasis-open.org/committees/ciq/ciq.html#6
 */

public class Address {

    private Locale mLocale;

    private String mFeatureName;
    private List<String> mAddressLines = new ArrayList<>();
    private String mAdminArea;
    private String mSubAdminArea;
    private String mLocality;
    private String mSubLocality;
    private String mThoroughfare;
    private String mSubThoroughfare;
    private String mPremises;
    private String mPostalCode;
    private String mCountryCode;
    private String mCountryName;
    private String mPhone;
    private String mUrl;
    private Bundle mExtras = null;

    @Ignore
    public Address(android.location.Address address) {

        mLocale = address.getLocale();
        mFeatureName = address.getFeatureName();
        mAdminArea = address.getAdminArea();
        mSubAdminArea = address.getSubAdminArea();
        mLocality = address.getLocality();
        mSubLocality = address.getSubLocality();
        mThoroughfare = address.getThoroughfare();
        mSubThoroughfare = address.getSubThoroughfare();
        mPremises = address.getPremises();
        mPostalCode = address.getPostalCode();
        mCountryCode = address.getCountryCode();
        mCountryName = address.getCountryName();
        mPhone = address.getPhone();
        mUrl = address.getUrl();
        mExtras = address.getExtras();

        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            mAddressLines.add(address.getAddressLine(i));
        }
    }

    /**
     * Constructs a new Address object set to the given Locale and with all
     * other fields initialized to null or false.
     */

    public Address(Locale locale) {
        mLocale = locale;
    }

    public List<String> getAddressLines() {
        return mAddressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        mAddressLines = addressLines;
    }

    /**
     * Returns the Locale associated with this address.
     */
    public Locale getLocale() {
        return mLocale;
    }

    public void setLocale(Locale locale) {
        mLocale = locale;
    }

    /**
     * Returns a line of the address numbered by the given index
     * (starting at 0), or null if no such line is present.
     *
     * @throws IllegalArgumentException if index < 0
     */
    public String getAddressLine(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index = " + index + " < 0");
        }
        return mAddressLines == null ? null : mAddressLines.get(index);
    }

    /**
     * Sets the line of the address numbered by index (starting at 0) to the
     * given String, which may be null.
     *
     * @throws IllegalArgumentException if index < 0
     */
    public void setAddressLine(int index, String line) {
        if (index < 0) {
            throw new IllegalArgumentException("index = " + index + " < 0");
        }
        if (mAddressLines == null) {
            mAddressLines = new ArrayList<>();
        }
        mAddressLines.set(index, line);
    }

    /**
     * Returns the feature name of the address, for example, "Golden Gate Bridge", or null
     * if it is unknown
     */
    public String getFeatureName() {
        return mFeatureName;
    }

    /**
     * Sets the feature name of the address to the given String, which may be null
     */
    public void setFeatureName(String featureName) {
        mFeatureName = featureName;
    }

    /**
     * Returns the administrative area name of the address, for example, "CA", or null if
     * it is unknown
     */
    public String getAdminArea() {
        return mAdminArea;
    }

    /**
     * Sets the administrative area name of the address to the given String, which may be null
     */
    public void setAdminArea(String adminArea) {
        this.mAdminArea = adminArea;
    }

    /**
     * Returns the sub-administrative area name of the address, for example, "Santa Clara County",
     * or null if it is unknown
     */
    public String getSubAdminArea() {
        return mSubAdminArea;
    }

    /**
     * Sets the sub-administrative area name of the address to the given String, which may be null
     */
    public void setSubAdminArea(String subAdminArea) {
        this.mSubAdminArea = subAdminArea;
    }

    /**
     * Returns the locality of the address, for example "Mountain View", or null if it is unknown.
     */
    public String getLocality() {
        return mLocality;
    }

    /**
     * Sets the locality of the address to the given String, which may be null.
     */
    public void setLocality(String locality) {
        mLocality = locality;
    }

    /**
     * Returns the sub-locality of the address, or null if it is unknown.
     * For example, this may correspond to the neighborhood of the locality.
     */
    public String getSubLocality() {
        return mSubLocality;
    }

    /**
     * Sets the sub-locality of the address to the given String, which may be null.
     */
    public void setSubLocality(String sublocality) {
        mSubLocality = sublocality;
    }

    /**
     * Returns the thoroughfare name of the address, for example, "1600 Ampitheater Parkway",
     * which may be null
     */
    public String getThoroughfare() {
        return mThoroughfare;
    }

    /**
     * Sets the thoroughfare name of the address, which may be null.
     */
    public void setThoroughfare(String thoroughfare) {
        this.mThoroughfare = thoroughfare;
    }

    /**
     * Returns the sub-thoroughfare name of the address, which may be null.
     * This may correspond to the street number of the address.
     */
    public String getSubThoroughfare() {
        return mSubThoroughfare;
    }

    /**
     * Sets the sub-thoroughfare name of the address, which may be null.
     */
    public void setSubThoroughfare(String subthoroughfare) {
        this.mSubThoroughfare = subthoroughfare;
    }

    /**
     * Returns the premises of the address, or null if it is unknown.
     */
    public String getPremises() {
        return mPremises;
    }

    /**
     * Sets the premises of the address to the given String, which may be null.
     */
    public void setPremises(String premises) {
        mPremises = premises;
    }

    /**
     * Returns the postal code of the address, for example "94110",
     * or null if it is unknown.
     */
    public String getPostalCode() {
        return mPostalCode;
    }

    /**
     * Sets the postal code of the address to the given String, which may
     * be null.
     */
    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }

    /**
     * Returns the country code of the address, for example "US",
     * or null if it is unknown.
     */
    public String getCountryCode() {
        return mCountryCode;
    }

    /**
     * Sets the country code of the address to the given String, which may
     * be null.
     */
    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    /**
     * Returns the localized country name of the address, for example "Iceland",
     * or null if it is unknown.
     */
    public String getCountryName() {
        return mCountryName;
    }

    /**
     * Sets the country name of the address to the given String, which may
     * be null.
     */
    public void setCountryName(String countryName) {
        mCountryName = countryName;
    }

    /**
     * Returns the phone number of the address if known,
     * or null if it is unknown.
     *
     * @throws IllegalStateException if this Address has not been assigned
     *                               a phone number.
     */
    public String getPhone() {
        return mPhone;
    }

    /**
     * Sets the phone number associated with this address.
     */
    public void setPhone(String phone) {
        mPhone = phone;
    }

    /**
     * Returns the public URL for the address if known,
     * or null if it is unknown.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Sets the public URL associated with this address.
     */
    public void setUrl(String Url) {
        mUrl = Url;
    }

    /**
     * Returns additional provider-specific information about the
     * address as a Bundle.  The keys and values are determined
     * by the provider.  If no additional information is available,
     * null is returned.
     * <p>
     * <!--
     * <p> A number of common key/value pairs are listed
     * below. Providers that use any of the keys on this list must
     * provide the corresponding value as described below.
     *
     * <ul>
     * </ul>
     * -->
     */
    public Bundle getExtras() {
        return mExtras;
    }

    /**
     * Sets the extra information associated with this fix to the
     * given Bundle.
     */
    public void setExtras(Bundle extras) {
        mExtras = (extras == null) ? null : new Bundle(extras);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Address[addressLines=[");
        for (int i = 0; i <= mAddressLines.size() - 1; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(i);
            sb.append(':');
            String line = mAddressLines.get(i);
            if (line == null) {
                sb.append("null");
            } else {
                sb.append('\"');
                sb.append(line);
                sb.append('\"');
            }
        }
        sb.append(']');
        sb.append(",feature=");
        sb.append(mFeatureName);
        sb.append(",admin=");
        sb.append(mAdminArea);
        sb.append(",sub-admin=");
        sb.append(mSubAdminArea);
        sb.append(",locality=");
        sb.append(mLocality);
        sb.append(",thoroughfare=");
        sb.append(mThoroughfare);
        sb.append(",postalCode=");
        sb.append(mPostalCode);
        sb.append(",countryCode=");
        sb.append(mCountryCode);
        sb.append(",countryName=");
        sb.append(mCountryName);
        sb.append(",phone=");
        sb.append(mPhone);
        sb.append(",url=");
        sb.append(mUrl);
        sb.append(",extras=");
        sb.append(mExtras);
        sb.append(']');
        return sb.toString();
    }

    public int describeContents() {
        return (mExtras != null) ? mExtras.describeContents() : 0;
    }
}
