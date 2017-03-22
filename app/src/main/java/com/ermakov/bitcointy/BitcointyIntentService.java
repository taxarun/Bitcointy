package com.ermakov.bitcointy;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.IOException;

public class BitcointyIntentService extends IntentService {
    public final static String ACTION_BITCOINTY = "action.BITCOINTY";
    public final static String EXTRA_BITCOINTY_TEXT = "extra.BITCOINTY_TEXT";
    public final static String EXTRA_BITCOINTY_RESULT_RECEIVER = "extra.EXTRA_BITCOINTY_RESULT_RECEIVER";

    public final static int RESULT_SUCCESS = 1;
    public final static int RESULT_ERROR = 2;
    public final static String EXTRA_BITCOINTY_RESULT = "extra.EXTRA_BITCOINTY_RESULT";

    public BitcointyIntentService() {
        super("BitcointyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_BITCOINTY.equals(action)) {
                final String text = intent.getStringExtra(EXTRA_BITCOINTY_TEXT);
                final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_BITCOINTY_RESULT_RECEIVER);
                handleActionYoda(text, receiver);
            }
        }
    }

    private void handleActionYoda(final String text, final ResultReceiver receiver) {
        final Bundle data = new Bundle();
        try {
            final String result = new BitcointyRest().getValueForCurrency(text);
            if (result != null && !result.isEmpty()) {
                data.putString(EXTRA_BITCOINTY_RESULT, result);
                receiver.send(RESULT_SUCCESS, data);
            } else {
                data.putString(EXTRA_BITCOINTY_RESULT, "result is null");
                receiver.send(RESULT_ERROR, data);
            }
        } catch (IOException ex) {
            data.putString(EXTRA_BITCOINTY_RESULT, ex.getMessage());
            receiver.send(RESULT_ERROR, data);
        }
    }
}