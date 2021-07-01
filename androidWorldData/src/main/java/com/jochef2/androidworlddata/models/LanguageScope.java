package com.jochef2.androidworlddata.models;

import android.content.Context;

/**
 * Enum to show the scope of the defined Language.  <br>
 * e.g. English has the scope INDIVIDUAL and Arabic is an MACROLANGUAGE.
 */
public enum LanguageScope {

    /**
     * Individual Language
     */
    INDIVIDUAL,

    /**
     * Collects small Languages that are very rare
     */
    COLLECTIVE,

    /**
     * Collects Languages that could be grouped by one advanced Language, by it's writing or their identity <br>
     * For example Chinese is a MACROLANGUAGE because of it's consistent writing
     */
    MACROLANGUAGE,

    /**
     * A Language with missing scope
     */
    UNKNOWN;

    /**
     * @param context to call getString
     * @return Translated String with name of th {@link LanguageScope}
     */
    public String getName(Context context) {
        return context.getString(getResNameId(context));
    }

    /**
     * @param context to call getResources()
     * @return Resource id of the name of this {@link LanguageScope}
     */
    public int getResNameId(Context context) {
        return context.getResources().getIdentifier(toString(), "string", context.getPackageName());
    }
}