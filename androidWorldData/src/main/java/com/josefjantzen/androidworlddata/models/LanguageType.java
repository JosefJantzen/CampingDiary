package com.josefjantzen.androidworlddata.models;

import android.content.Context;

/**
 * The LanguageType identifies how the Language is used today.
 */
public enum LanguageType {

    /**
     * Language that's extinct since ancient times.
     */
    ANCIENT,

    /**
     * A constructed language is a language whose phonology, grammar, and vocabulary, instead of having developed naturally, are consciously devised.
     */
    CONSTRUCTED,

    /**
     * Language that's extinct in recent time.
     */
    EXTINCT,

    /**
     * Language that's distinct from it's modern from.
     */
    HISTORICAL,

    /**
     * Language that's still spoken.
     */
    LIVING,

    /**
     * Language with unknown type.
     */
    UNKNWON;

    /**
     * @param context to call getString
     * @return Translated String with name of th {@link LanguageType}
     */
    public String getName(Context context) {
        return context.getString(getResNameId(context));
    }

    /**
     * @param context to call getResources()
     * @return Resource id of the name of this {@link LanguageType}
     */
    public int getResNameId(Context context) {
        return context.getResources().getIdentifier(toString(), "string", context.getPackageName());
    }
}