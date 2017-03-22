package com.ermakov.bitcointy;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.Hashtable;
import java.util.Map;

class ServiceHelper {
    private int mIdCounter = 1;
    private final Map<Integer, BitcointyResultReceiver> mResultReceivers = new Hashtable<>();

    private static ServiceHelper instance;

    private ServiceHelper() {
    }

    synchronized static ServiceHelper getInstance() {
        if (instance == null) {
            instance = new ServiceHelper();
        }
        return instance;
    }

    int getCourseForCurrency(final Context context, final String text, final BitcointyResultListener listener) {
        final BitcointyResultReceiver receiver = new BitcointyResultReceiver(mIdCounter, new Handler());
        receiver.setListener(listener);
        mResultReceivers.put(mIdCounter, receiver);

        Intent intent = new Intent(context, BitcointyIntentService.class);
        intent.setAction(BitcointyIntentService.ACTION_BITCOINTY);
        intent.putExtra(BitcointyIntentService.EXTRA_BITCOINTY_TEXT, text);
        intent.putExtra(BitcointyIntentService.EXTRA_BITCOINTY_RESULT_RECEIVER, receiver);
        context.startService(intent);

        return mIdCounter++;
    }

    void removeListener(final int id) {
        BitcointyResultReceiver receiver = mResultReceivers.remove(id);
        if (receiver != null) {
            receiver.setListener(null);
        }
    }

    interface BitcointyResultListener {
        void onBitcointyResult(final boolean success, final String result);
    }

}
