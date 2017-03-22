package com.ermakov.bitcointy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

@SuppressLint("ParcelCreator")
class BitcointyResultReceiver extends ResultReceiver {
    private final int requestId;
    private ServiceHelper.BitcointyResultListener mListener;

    public BitcointyResultReceiver(int requestId, final Handler handler) {
        super(handler);
        this.requestId = requestId;
    }

    void setListener(final ServiceHelper.BitcointyResultListener listener) {
        mListener = listener;
    }

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData) {
        if (mListener != null) {
            final boolean success = (resultCode == BitcointyIntentService.RESULT_SUCCESS);
            final String result = (resultData.getString(BitcointyIntentService.EXTRA_BITCOINTY_RESULT));
            mListener.onBitcointyResult(success, result);
        }
        ServiceHelper.getInstance().removeListener(requestId);
    }
}
