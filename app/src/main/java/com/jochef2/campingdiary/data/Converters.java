package com.jochef2.campingdiary.data;

import android.os.Bundle;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jochef2.campingdiary.data.values.EventCategory;
import com.jochef2.campingdiary.data.values.Events;
import com.jochef2.campingdiary.data.values.NightCategory;
import com.jochef2.campingdiary.data.values.SADCategory;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Converters {

    /**
     * converts long to Calendar
     *
     * @param value millis
     * @return Calendar
     */
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

    /**
     * converts calendar to long
     *
     * @param calendar Calendar
     * @return millis
     */
    @TypeConverter
    public static Long toTimestamp(Calendar calendar) {
        return calendar == null ? null : calendar.getTimeInMillis() / 1000L;
    }

    /**
     * converts EventCategory Enum to String
     *
     * @param eventCategory category
     * @return string
     */
    @TypeConverter
    public static String eventCatToString(EventCategory eventCategory) {
        return eventCategory == null ? null : eventCategory.toString();
    }

    /**
     * converts string to EventCategory Enum
     *
     * @param cat string
     * @return EventCategory
     */
    @TypeConverter
    public static EventCategory stringToEventCat(String cat) {
        return cat == null ? null : EventCategory.valueOf(cat);
    }

    /**
     * converts NightCategory Enum to string
     *
     * @param nightCategory category
     * @return string
     */
    @TypeConverter
    public static String nightCatToString(NightCategory nightCategory) {
        return nightCategory == null ? null : nightCategory.toString();
    }

    /**
     * converts string to NightCategory Enum
     *
     * @param cat category
     * @return NightCategory Enum
     */
    @TypeConverter
    public static NightCategory stringToNightCategory(String cat) {
        return cat == null ? null : NightCategory.valueOf(cat);
    }

    /**
     * converts list of sadCategory Enum to Json String
     *
     * @param sadCategories categories
     * @return Json String
     */
    @TypeConverter
    public static String sadCatListToString(List<SADCategory> sadCategories) {
        return sadCategories == null ? null : new Gson().toJson(sadCategories);
    }

    /**
     * converts Json String to sadCategory Enum
     *
     * @param cats categories
     * @return sadCategory Enum
     */
    @TypeConverter
    public static List<SADCategory> stringToSADCatList(String cats) {
        return cats == null ? null : new Gson().fromJson(cats, new TypeToken<ArrayList<SADCategory>>() {
        }.getType());
    }

    @TypeConverter
    public static String EventsToString(Events events) {
        return events == null ? null : events.toString();
    }

    @TypeConverter
    public static Events StringToEvents(String events) {
        return events == null ? null : Events.valueOf(events);
    }

    @TypeConverter
    public static String LocaleToString(Locale locale) {
        return locale == null ? null : locale.getISO3Language();
    }

    @TypeConverter
    public static Locale StringToLocale(String locale) {
        return locale == null ? null : new Locale(locale);
    }

    @TypeConverter
    public static String ListToString(List<String> list) {
        return list == null ? null : new Gson().toJson(list);
    }

    @TypeConverter
    public static List<String> StringToHashMap(String list) {
        return list == null ? null : new Gson().fromJson(list, List.class);
    }

    @TypeConverter
    public static String BundleToString(Bundle bundle) {
        return bundle == null ? null : new Gson().toJson(bundle);
    }

    @TypeConverter
    public static Bundle StringToBundle(String bundle) {
        return bundle == null ? null : new Gson().fromJson(bundle, Bundle.class);
    }

    /**
     * converts ExtendedCurrency to it's name
     *
     * @param currency ExtendedCurrency
     * @return String name of currency
     */
    @TypeConverter
    public static String extendedCurrencyToString(ExtendedCurrency currency) {
        return currency == null ? null : currency.getName();
    }

    /**
     * converts currency string name to ExtendedCurrency
     *
     * @param name currency string name
     * @return ExtendedCurrency
     */
    @TypeConverter
    public static ExtendedCurrency stringToExtendedCurrency(String name) {
        return name == null ? null : ExtendedCurrency.getCurrencyByName(name);
    }
}
