package com.josefjantzen.androidworlddata.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.josefjantzen.androidworlddata.Utils;
import com.josefjantzen.androidworlddata.World;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

public class Country {

    /**
     * ISO3166-1 Alpha 2
     */
    private final String mISO2;

    /**
     * ISO3166-1 Alpha 3
     */
    private final String mISO3;

    /**
     * ISO3166-1 Numeric code
     */
    private final int mISONum;

    /**
     * Top-Level-Domain
     */
    private final String mTLD;

    private final String mCallingCode;

    /**
     * Latitude of centroid
     */
    private final double mLatitude;

    /**
     * Longitude of centroid
     */
    private final double mLongitude;

    private final String mTimeZone;

    /**
     * List of ISO639-1 or ISO639-3 codes of the languages spoken in the country
     */
    private final List<String> mLanguages;

    private final String mContinent;

    private final String mCurrency;

    private final List<String> mLocalNames;

    public Country(String ISO2, String ISO3, int ISONum, String TLD, String callingCode,
                   double latitude, double longitude, String timeZone, List<String> languages,
                   String continent, String currency, List<String> localNames) {
        mISO2 = ISO2;
        mISO3 = ISO3;
        mISONum = ISONum;
        mTLD = TLD;
        mCallingCode = callingCode;
        mLatitude = latitude;
        mLongitude = longitude;
        mTimeZone = timeZone;
        mLanguages = languages;
        mContinent = continent;
        mCurrency = currency;
        mLocalNames = localNames;
    }

    /**
     * @param context to call getResources()
     * @return integer with resource id of the name string
     */
    public int getResNameId(Context context) {
        return context.getResources().getIdentifier(mISO2.toUpperCase(), "string", context.getPackageName());
    }

    /**
     * @param context to call getString()
     * @return String with the name in current language
     */
    public String getName(Context context) {
        return context.getString(getResNameId(context));
    }

    /**
     * @param context to call getString()
     * @return Collection of String with the names of the country in all available languages
     */
    public Collection<String> getNames(Context context) {
        return Utils.getStrings(context, getResNameId(context)).values();
    }

    /**
     * @param context to call getResources()
     * @return integer with resource id of th flag
     */
    public int getResFlagId(Context context) {
        return context.getResources().getIdentifier("country_" + mISO2, "drawable", context.getPackageName());
    }

    /**
     * @param context to call getDrawable()
     * @return Drawable with the flag of this {@link Country}
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public Drawable getFlag(Context context) {
        return context.getDrawable(getResFlagId(context));
    }

    /**
     * @return String with ISO3166-1 Alpha 2 code
     */
    public String getISO2() {
        return mISO2;
    }

    /**
     * @return String with ISO3166-1 Alpha 3 code
     */
    public String getISO3() {
        return mISO3;
    }

    /**
     * @return integer with ISO3166-1 numeric code
     */
    public int getISONum() {
        return mISONum;
    }

    /**
     * @return String with TLD and dot
     */
    public String getTLD() {
        return mTLD;
    }

    /**
     * @return String with calling code and plus
     */
    public String getCallingCode() {
        return mCallingCode;
    }

    /**
     * @return double with latitude of the centroid
     */
    public double getLatitude() {
        return mLatitude;
    }

    /**
     * @return double with longitude of the centroid
     */
    public double getLongitude() {
        return mLongitude;
    }

    /**
     * @return {@link TimeZone} of the Country
     */
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(mTimeZone);
    }

    /**
     * @return List of String with ISO639-1 or ISO639-3 of the languages
     */
    public List<String> getLanguages() {
        return mLanguages;
    }

    /**
     * @return two digit code of the {@link Continent}
     */
    public String getContinentCode() {
        return mContinent;
    }

    /**
     * @param world to get {@link Continent} object
     * @return {@link Continent} of this Country
     */
    public Continent getContinent(World world) {
        return world.getContinent(mContinent);
    }

    /**
     * @return ISO4217 code of the {@link Currency} of this {@link Country}
     */
    public String getCurrency() {
        return mCurrency;
    }

    /**
     * @param world to get {@link Currency} object
     * @return {@link Currency} of this {@link Country}
     */
    @Nullable
    public Currency getCurrency(World world) {
        return world.getCurrency(mCurrency);
    }

    /**
     * @return List of String with all local names of this {@link Country}
     */
    public List<String> getLocalNames() {
        return mLocalNames;
    }

    /**
     * @param world to get {@link Language} objects
     * @return List of {@link Language} of this {@link Country}
     */
    public List<Language> getLanguages(World world) {
        List<Language> languages = new ArrayList<>();
        for (String code : mLanguages) {
            languages.add(world.getLanguage(code));
        }
        return languages;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        return obj != null && getISO2().equals(((Country) obj).getISO2()) &&
                getISO3().equals(((Country) obj).getISO3()) && getISONum() == ((Country) obj).getISONum();
    }
}
