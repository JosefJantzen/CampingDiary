package com.jochef2.androidworlddata;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {

    /**
     * Reads a Json file.
     *
     * @param context  to call getAssets()
     * @param filename of the file to read
     * @return String with the content of the file
     */
    public static String getJsonFromAssets(Context context, String filename) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(filename);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    /**
     * @return String Array with two digit language codes that the library supports
     */
    public static String[] getSupportedLanguages() {
        return BuildConfig.TRANSLATION_ARRAY;
    }

    /**
     * Creates a Map with all translations of a String value
     *
     * @param context to call getResources() and getSystemService
     * @param resId   of the String value
     * @return Map with all translations of the String value
     */
    public static Map<String, String> getStrings(Context context, int resId) {
        Map<String, String> map = new LinkedHashMap<>();
        String[] languages = getSupportedLanguages();
        Configuration conf = context.getResources().getConfiguration();
        Locale savedLocale = conf.locale;
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);


        for (String lang : languages) {
            conf.locale = new Locale(lang);
            Resources resources = new Resources(context.getAssets(), metrics, conf);
            map.put(lang, resources.getString(resId));
        }
        conf.locale = savedLocale;
        context.getResources().updateConfiguration(conf, null);
        return map;
    }
}
