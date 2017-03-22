package com.ermakov.bitcointy;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {

    private static final String KEY_CACHED_CURRENCY = "currency";
    private static final String CURRENCY_RUB = "RUB";
    private static final String CURRENCY_USD = "USD";
    private static final String SETTINGS_TAG = "PREF_NAME";

    public void onRadioButtonClicked(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_TAG, MODE_PRIVATE).edit();
        switch(view.getId()) {
            case R.id.radio_RUB:
                editor.putString(KEY_CACHED_CURRENCY, CURRENCY_RUB);
                break;
            case R.id.radio_USD:
                editor.putString(KEY_CACHED_CURRENCY, CURRENCY_USD);
                break;
        }
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        RadioButton radio;
        SharedPreferences prefs = getSharedPreferences(SETTINGS_TAG, MODE_PRIVATE);
        String currency = prefs.getString(KEY_CACHED_CURRENCY, "");
        if (currency.equals(CURRENCY_RUB)) {
            radio = (RadioButton) findViewById(R.id.radio_RUB);
            radio.setChecked(true);
        }
        else if (currency.equals(CURRENCY_USD)) {
            radio = (RadioButton) findViewById(R.id.radio_USD);
            radio.setChecked(true);
        }
    }
}
