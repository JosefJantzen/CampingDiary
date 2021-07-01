package com.jochef2.androidworlddata.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.jochef2.androidworlddata.Utils;
import com.jochef2.androidworlddata.World;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Continent {

    /**
     * Two digit code
     */
    private final String mCode;

    /**
     * List with ISO3166-1 Alpha2 codes of the {@link Country} on this {@link Continent}
     */
    private final List<String> mCountries;

    /**
     * @param code      two digit code to identify this {@link Continent}
     * @param countries List with ISO3166-1 Alpha-2 code of the {@link Country} in on this {@link Continent}
     */
    public Continent(String code, List<String> countries) {
        mCode = code;
        mCountries = countries;
    }

    /**
     * @return String with two digit code of this {@link Continent}
     */
    public String getCode() {
        return mCode;
    }

    /**
     * @return List with ISO3166-1 Alpha-2 code of the {@link Country} in on this {@link Continent}
     */
    public List<String> getCountriesISOs() {
        return mCountries;
    }

    /**
     * Creates List with all {@link Country} on the {@link Continent}
     *
     * @param world to get Country objects
     * @return List of {@link Country} on the {@link Continent}
     */
    public List<Country> getCountries(World world) {
        List<Country> countries = new ArrayList<>();
        for (String code : mCountries) {
            countries.add(world.getCountry(code));
        }
        return countries;
    }

    /**
     * @param context to call getResources()
     * @return integer with resource id of the name value
     */
    public int getResNameId(Context context) {
        return context.getResources().getIdentifier(mCode + mCode, "string", context.getPackageName());
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
     * @return Collection of String with the names of this {@link Continent} in all available languages
     */
    public Collection<String> getNames(Context context) {
        return Utils.getStrings(context, getResNameId(context)).values();
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        return obj != null && getCode().equals(((Continent) obj).getCode());
    }
}
