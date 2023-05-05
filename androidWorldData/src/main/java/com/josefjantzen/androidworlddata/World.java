package com.josefjantzen.androidworlddata;

import android.content.Context;

import androidx.annotation.Nullable;

import com.josefjantzen.androidworlddata.models.Continent;
import com.josefjantzen.androidworlddata.models.Country;
import com.josefjantzen.androidworlddata.models.Currency;
import com.josefjantzen.androidworlddata.models.Language;
import com.josefjantzen.androidworlddata.models.LanguageScope;
import com.josefjantzen.androidworlddata.models.LanguageType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

public class World {

    private final List<Continent> mContinents;

    private final List<Country> mCountries;

    private final List<Currency> mCurrencies;

    private final List<Language> mLanguages;

    private final List<Language> mAllLanguages;

    private final Context mContext;

    public World(Context context) {
        mContext = context;
        WorldData worldData = new WorldData(context);
        mContinents = worldData.mContinents;
        mCountries = worldData.mCountries;
        mCurrencies = worldData.mCurrencies;
        mLanguages = worldData.mLanguages;
        mAllLanguages = worldData.mAllLanguages;
    }

    /**
     * Creates a List of {@link Language} out of Languages(ISO639-1) that have the given type.
     *
     * @param type {@link LanguageType} for the query.
     * @return List of {@link Language} with the given type.
     */
    @Nullable
    public List<Language> getLanguagesByType(LanguageType type) {
        List<Language> languages = new ArrayList<>();
        for (Language language : mLanguages) {
            if (type == language.getLanguageType()) languages.add(language);
        }
        return languages;
    }

    /**
     * Creates a List of {@link Language} out of AllLanguages(ISO639-2) that have the given type.
     *
     * @param type {@link LanguageType} for the query.
     * @return List of {@link Language} with the given type.
     */
    @Nullable
    public List<Language> getAllLanguagesByType(LanguageType type) {
        List<Language> languages = new ArrayList<>();
        for (Language language : mAllLanguages) {
            if (type == language.getLanguageType()) languages.add(language);
        }
        return languages;
    }

    /**
     * Creates a List of {@link Language} out of Languages(ISO639-1) that have the given scope.
     *
     * @param scope {@link LanguageScope} for the query.
     * @return List of {@link Language} with the given scope.
     */
    @Nullable
    public List<Language> getLanguagesByScope(LanguageScope scope) {
        List<Language> languages = new ArrayList<>();
        for (Language language : mLanguages) {
            if (scope == language.getLanguageScope()) languages.add(language);
        }
        return languages;
    }

    /**
     * Creates a List of {@link Language} out of AllLanguages(ISO639-2) that have the given scope.
     *
     * @param scope {@link LanguageScope} for the query.
     * @return List of {@link Language} with the given scope.
     */
    @Nullable
    public List<Language> getAllLanguagesByScope(LanguageScope scope) {
        List<Language> languages = new ArrayList<>();
        for (Language language : mAllLanguages) {
            if (scope == language.getLanguageScope()) languages.add(language);
        }
        return languages;
    }

    /**
     * Searches for a {@link Language} that matches the given name in one available language or it's native name <br> The case is irrelevant.
     *
     * @param name Name of the {@link Language} in one supported language or the native name.
     * @return Found {@link Language} or null.
     */
    @Nullable
    public Language getLanguageByName(@Nullable String name) {
        if (name != null) {
            name = name.toLowerCase();
            for (Language language : mAllLanguages) {
                Collection<String> names = language.getNames(mContext);
                for (String string : names) {
                    string = string.toLowerCase();
                    if (string.equals(name)) {
                        return language;
                    }
                }
                for (String string : language.getNativeNames()) {
                    string = string.toLowerCase();
                    if (string.equals(name)) {
                        return language;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches the {@link Language} with the given ISO code.
     *
     * @param code Accepts ISO639-1 two digit and ISO639-2 three digit codes. <br>
     *             All spaces get removed and the case is irrelevant.
     * @return Found {@link Language} or null.
     */
    @Nullable
    public Language getLanguage(@Nullable String code) {
        if (code != null) {
            code = code.toLowerCase().replace(" ", "");

            if (code.length() == 2) {
                for (Language language : mAllLanguages) {
                    if (code.equals(language.getISO1())) return language;
                }
            } else if (code.length() == 3) {
                for (Language language : mAllLanguages) {
                    if (code.equals(language.getISO2())) return language;
                }
            }
        }
        return null;
    }

    /**
     * @return List of all {@link Language} in ISO639-1
     */
    public List<Language> getLanguages() {
        return mLanguages;
    }

    /**
     * Searches for a {@link Currency} that matches the given name in one available language. <br> The case is irrelevant.
     *
     * @param name Name of the {@link Currency} in one supported language
     * @return Found {@link Currency} or null.
     */
    @Nullable
    public Currency getCurrencyByName(@Nullable String name) {
        if (name != null) {
            name = name.toLowerCase();
            for (Currency currency : mCurrencies) {
                Collection<String> names = currency.getNames(mContext);
                for (String string : names) {
                    string = string.toLowerCase();
                    if (string.equals(name)) {
                        return currency;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches {@link Currency} based on the given code.
     *
     * @param code Accepted values are ISO4217 Alpha 3 and the numeric code as String. The case is irrelevant and all spaces get removed.
     * @return Found {@link Currency} or null if not found
     */
    @Nullable
    public Currency getCurrency(@Nullable String code) {
        if (code != null) {
            code = code.toUpperCase().replace(" ", "");

            try {
                return getCurrency(Integer.parseInt(code));
            } catch (NumberFormatException e) {
                if (code.length() == 3) {
                    for (Currency currency : mCurrencies) {
                        if (currency.getISO().equals(code)) {
                            return currency;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches {@link Currency} based on the given code.
     *
     * @param code Accepts ISO4217 Numeric code.
     * @return Found {@link Currency} or null if not found
     */
    @Nullable
    public Currency getCurrency(int code) {
        for (Currency currency : mCurrencies) {
            if (currency.getISONum() == code) {
                return currency;
            }
        }
        return null;
    }

    /**
     * @return List of all available Currencies
     */
    public List<Currency> getCurrencies() {
        return mCurrencies;
    }

    /**
     * Searches {@link Country} based on the given code.
     *
     * @param code Accepted values are ISO3166-1 Alpha 2, 3 and the numeric code as String. The case is irrelevant and all spaces get removed.
     * @return found Country or null if not found
     */
    @Nullable
    public Country getCountry(@Nullable String code) {
        if (code != null) {
            code = code.toLowerCase().replace(" ", "");

            try {
                return getCountry(Integer.parseInt(code));
            } catch (NumberFormatException e) {
                if (code.length() == 2) {
                    for (Country country : mCountries) {
                        if (country.getISO2().equals(code)) {
                            return country;
                        }
                    }
                } else if (code.length() == 3) {
                    for (Country country : mCountries) {
                        if (country.getISO3().equals(code)) {
                            return country;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches {@link Country} based on the given code.
     *
     * @param code Accepts ISO3166-1 Numeric code.
     * @return found Country or null if not found
     */
    @Nullable
    public Country getCountry(int code) {
        for (Country country : mCountries) {
            if (country.getISONum() == code) {
                return country;
            }
        }
        return null;
    }

    /**
     * Searches for a {@link Country} that matches the given name in one available language and the local names. <br> The case is irrelevant.
     *
     * @param name String of name for the query.
     * @return Found {@link Country} or null if not found
     */
    @Nullable
    public Country getCountryByName(@Nullable String name) {
        if (name != null) {
            name = name.toLowerCase();
            for (Country country : mCountries) {
                Collection<String> names = country.getNames(mContext);
                for (String string : names) {
                    string = string.toLowerCase();
                    if (string.equals(name)) {
                        return country;
                    }
                }
                for (String string : country.getLocalNames()) {
                    string = string.toLowerCase();
                    if (string.equals(name)) {
                        return country;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches for a {@link Country} with the given TLD. <br> The case is irrelevant and all spaces get removed.
     *
     * @param tld String with the TLD. The dot is added if it's missing.
     * @return Found {@link Country} or null if not found
     */
    @Nullable
    public Country getCountryByTLD(@Nullable String tld) {
        if (tld != null) {
            tld = tld.toLowerCase().replace(" ", "");
            if (tld.charAt(0) != '.') {
                tld = "." + tld;
            }
            for (Country country : mCountries) {
                if (country.getTLD().equals(tld)) {
                    return country;
                }
            }
        }
        return null;
    }

    /**
     * Searches for {@link Country} with the given calling code. <br> The case is irrelevant and all spaces get removed.
     *
     * @param code String with the calling code. The plus is added if it's missing.
     * @return Found {@link Country} or null if not found
     */
    @Nullable
    public Country getCountryByCallingCode(@Nullable String code) {
        if (code != null) {
            code = code.toLowerCase().replace(" ", "");
            if (code.charAt(0) != '+') {
                code = "." + code;
            }
            for (Country country : mCountries) {
                if (country.getCallingCode().equals(code)) {
                    return country;
                }
            }
        }
        return null;
    }

    /**
     * Searches for all Countries with the given timezone.
     *
     * @param timeZone TimeZone for the query
     * @return List of Found {@link Country}
     */
    public List<Country> getCountriesByTimezone(TimeZone timeZone) {
        List<Country> countries = new ArrayList<>();
        for (Country country : mCountries) {
            if (country.getTimeZone().hasSameRules(timeZone)) {
                countries.add(country);
            }
        }
        return countries;
    }

    /**
     * @return all available Countries
     */
    public List<Country> getCountries() {
        return mCountries;
    }

    /**
     * Searches in the List of {@link Continent} for the one with the given code
     *
     * @param code Two digit code of the {@link Continent} to identify it. <br> The case of the code is irrelevant and all spaces will be removed
     * @return {@link Continent} with the given code or null if not found
     */
    @Nullable
    public Continent getContinent(@Nullable String code) {
        if (code != null) {
            code = code.toLowerCase().replace(" ", "");
            if (code.length() == 2) {
                for (Continent continent : mContinents) {
                    if (continent.getCode().equals(code)) {
                        return continent;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches for a {@link Continent} that matches the given name in every language. <br> The case is irrelevant.
     *
     * @param name String of name for the search
     * @return Found {@link Continent} or null if not found
     */
    @Nullable
    public Continent getContinentByName(@Nullable String name) {
        if (name != null) {
            name = name.toLowerCase();
            for (Continent continent : mContinents) {
                Collection<String> names = continent.getNames(mContext);
                for (String string : names) {
                    string = string.toLowerCase();
                    if (string.equals(name)) {
                        return continent;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return all Continents
     */
    public List<Continent> getContinents() {
        return mContinents;
    }
}
