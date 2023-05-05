package com.josefjantzen.androidworlddata;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josefjantzen.androidworlddata.models.Continent;
import com.josefjantzen.androidworlddata.models.Country;
import com.josefjantzen.androidworlddata.models.Currency;
import com.josefjantzen.androidworlddata.models.Language;

import java.util.List;

class WorldData {

    public List<Continent> mContinents;

    public List<Country> mCountries;

    public List<Currency> mCurrencies;

    public List<Language> mAllLanguages;

    public List<Language> mLanguages;

    public WorldData(Context context) {
        Gson gson = new Gson();

        mContinents = gson.fromJson(Utils.getJsonFromAssets(context, "continents.json"),
                new TypeToken<List<Continent>>() {
                }.getType());

        mCountries = gson.fromJson(Utils.getJsonFromAssets(context, "countries.json"),
                new TypeToken<List<Country>>() {
                }.getType());

        mCurrencies = gson.fromJson(Utils.getJsonFromAssets(context, "currencies.json"),
                new TypeToken<List<Currency>>() {
                }.getType());

        mAllLanguages = gson.fromJson(Utils.getJsonFromAssets(context, "languages.json"),
                new TypeToken<List<Language>>() {
                }.getType());

        mLanguages = gson.fromJson(Utils.getJsonFromAssets(context, "languagesShort.json"),
                new TypeToken<List<Language>>() {
                }.getType());
    }
}
