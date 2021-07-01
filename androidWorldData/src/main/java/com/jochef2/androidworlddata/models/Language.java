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

public class Language {

    /**
     * ISO639-1
     */
    private final String mISO1;

    /**
     * ISO639-2/T
     */
    private final String mISO2;

    private final List<String> mNativeNames;

    private final LanguageScope mLanguageScope;

    private final LanguageType mLanguageType;

    private final List<String> mCountries;

    public Language(String ISO1, String ISO2, List<String> nativeNames, LanguageScope languageScope, LanguageType languageType, List<String> countries) {
        mISO1 = ISO1;
        mISO2 = ISO2;
        mNativeNames = nativeNames;
        mLanguageScope = languageScope;
        mLanguageType = languageType;
        mCountries = countries;
    }

    /**
     * @param context to call getResources()
     * @return integer with resource id of the name string
     */
    public int getResNameId(Context context) {
        return context.getResources().getIdentifier("L" + mISO2.toUpperCase(), "string", context.getPackageName());
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
     * @return ISO639-1
     */
    public String getISO1() {
        return mISO1;
    }

    /**
     * @return ISO639-2
     */
    public String getISO2() {
        return mISO2;
    }

    public List<String> getNativeNames() {
        return mNativeNames;
    }

    public LanguageScope getLanguageScope() {
        return mLanguageScope;
    }

    public LanguageType getLanguageType() {
        return mLanguageType;
    }

    /**
     * @param world to get {@link Country} objects
     * @return List of {@link Country} objects with this {@link Language}
     */
    public List<Country> getCountries(World world) {
        List<Country> countries = new ArrayList<>();
        for (String code : mCountries) {
            countries.add(world.getCountry(code));
        }
        return countries;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        return obj != null && getISO1().equals(((Language) obj).getISO1()) &&
                getISO2().equals(((Language) obj).getISO2());
    }
}
