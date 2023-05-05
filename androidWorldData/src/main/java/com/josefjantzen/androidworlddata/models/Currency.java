package com.josefjantzen.androidworlddata.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.josefjantzen.androidworlddata.Utils;
import com.josefjantzen.androidworlddata.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Currency {

    /**
     * ISO4217 Alpha 3
     */
    private final String mISO;

    /**
     * ISO4217 Numeric code
     */
    private final int mISONum;

    /**
     * Symbol this {@link Currency}
     */
    private final String mSymbol;

    /**
     * Subunit
     */
    private final String mSubunit;

    /**
     * Decimal places
     */
    private final int mDecimals;

    /**
     * List of ISO3166-1 Alpha 2 codes of the {@link Country} using this {@link Currency}
     */
    private final List<String> mCountries;

    public Currency(String ISO, int ISONum, String symbol, String subunit, int decimals, List<String> countries) {
        mISO = ISO;
        mISONum = ISONum;
        mSymbol = symbol;
        mSubunit = subunit;
        mDecimals = decimals;
        mCountries = countries;
    }

    /**
     * @param context to call getResources()
     * @return integer with resource id of the name string
     */
    public int getResNameId(Context context) {
        return context.getResources().getIdentifier(mISO, "string", context.getPackageName());
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
     * @return Collection of String with the names of the currency in all available languages
     */
    public Collection<String> getNames(Context context) {
        return Utils.getStrings(context, getResNameId(context)).values();
    }

    /**
     * @param context to call getResources()
     * @return integer with resource id of th flag
     */
    public int getResFlagId(Context context) {
        try {
            return context.getResources().getIdentifier("c" + mISO, "drawable", context.getPackageName());
        } catch (Exception e) {
            return context.getResources().getIdentifier("unknwon_flag", "drawable", context.getPackageName());
        }
    }

    /**
     * @param context to call getDrawable()
     * @return Drawable with the flag of this {@link Currency}
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public Drawable getFlag(Context context) {
        return context.getDrawable(getResFlagId(context));
    }

    /**
     * @return ISO4217 Alpha 3
     */
    public String getISO() {
        return mISO;
    }

    /**
     * @return ISO4217 Numeric code
     */
    public int getISONum() {
        return mISONum;
    }

    /**
     * @return String with the Symbol this {@link Currency}
     */
    public String getSymbol() {
        return mSymbol;
    }

    /**
     * @return String with the name of the subunit
     */
    public String getSubunit() {
        return mSubunit;
    }

    /**
     * @return integer with number of decimals
     */
    public int getDecimals() {
        return mDecimals;
    }

    /**
     * @return List of ISO3166-1 Alpha 2 codes of the {@link Country} using this {@link Currency}
     */
    public List<String> getCountries() {
        return mCountries;
    }

    /**
     * Creates List of {@link Country} using this {@link Currency}
     *
     * @param world to get Country objects
     * @return List of {@link Country} using this {@link Currency}
     */
    public List<Country> getCountries(World world) {
        List<Country> countries = new ArrayList<>();
        for (String country : mCountries) {
            countries.add(world.getCountry(country));
        }
        return countries;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        return obj != null && getISO().equals(((Currency) obj).getISO()) &&
                getISONum() == ((Currency) obj).getISONum();
    }
}
