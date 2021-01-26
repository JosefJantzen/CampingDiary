package com.jochef2.campingdiary.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jochef2.campingdiary.data.values.EventCategory;
import com.jochef2.campingdiary.data.values.NightCategory;
import com.jochef2.campingdiary.data.values.SADCategory;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Calendar toCalendar(Long value) {
        if (value != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value * 1000L);
            return calendar;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static Long toTimestamp(Calendar calendar) {
        return calendar == null ? null : calendar.getTimeInMillis() / 1000L;
    }

    @TypeConverter
    public static String eventCatToString(EventCategory eventCategory) {
        return eventCategory == null ? null : eventCategory.toString();
    }

    @TypeConverter
    public static EventCategory stringToEventCat(String cat) {
        return cat == null ? null : EventCategory.valueOf(cat);
    }

    @TypeConverter
    public static String nightCatToString(NightCategory nightCategory) {
        return nightCategory == null ? null : nightCategory.toString();
    }

    @TypeConverter
    public static NightCategory stringToNightCategory(String cat) {
        return cat == null ? null : NightCategory.valueOf(cat);
    }

    @TypeConverter
    public static String sadCatListToString(List<SADCategory> sadCategories) {
        return sadCategories == null ? null : new Gson().toJson(sadCategories);
    }

    @TypeConverter
    public static List<SADCategory> stringToSADCatList(String cats) {
        return cats == null ? null : new Gson().fromJson(cats, new TypeToken<ArrayList<SADCategory>>() {
        }.getType());
    }

    @TypeConverter
    public static String extendedCurrencyToString(ExtendedCurrency currency) {
        return currency == null ? null : currency.getName();
    }

    @TypeConverter
    public static ExtendedCurrency stringToExtendedCurrency(String name) {
        return name == null ? null : ExtendedCurrency.getCurrencyByName(name);
    }
}
