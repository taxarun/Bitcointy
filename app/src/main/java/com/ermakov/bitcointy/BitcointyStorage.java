package com.ermakov.bitcointy;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class BitcointyStorage {
    private final static String KEY_CACHED_COURSE = "course";
    private static final String KEY_CACHED_CURRENCY = "currency";
    private static final String DEFAULT_CURRENCY = "USD";

    private static BitcointyStorage INSTANCE;
    private SharedPreferences preferences;

    public static synchronized BitcointyStorage getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new BitcointyStorage(context.getApplicationContext());
        }

        return INSTANCE;
    }

    private BitcointyStorage(Context context) {
        this.preferences = context.getSharedPreferences("PREF_NAME", MODE_PRIVATE);
    }

    public Float getCourse() {
        return preferences.getFloat(KEY_CACHED_COURSE + getCurrency(), -1f);
    }

    public String getCurrency() {
        String currency = preferences.getString(KEY_CACHED_CURRENCY, "");
        if (currency.equals("")) {
            currency = DEFAULT_CURRENCY;
            cacheCurrency(currency);
        }
        return currency;
    }

    public void cacheCourse(Float course) {
        SharedPreferences.Editor cache = preferences.edit();
        cache.putFloat(KEY_CACHED_COURSE + getCurrency(), course);
        cache.apply();
    }

    public void cacheCurrency(String currency) {
        SharedPreferences.Editor cache = preferences.edit();
        cache.putString(KEY_CACHED_CURRENCY, currency);
        cache.apply();
    }

}
