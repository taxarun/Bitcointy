package com.ermakov.bitcointy;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class BitcointyRest {
    private final static String API_KEY = "RanNJNqVF1mshPAErtKiRXBjLEAQp1bk9ijjsn8nRgMbdpJ0qY";
    private final static String METHOD_URL = "https://community-bitcointy.p.mashape.com/average/";
    private final static String API_HEADER = "X-Mashape-Key";

    private final OkHttpClient httpClient = new OkHttpClient();

    BitcointyRest() {

    }

    public String getValueForCurrency(final String currency) throws IOException {
        Request request = (new Request.Builder())
                .url(METHOD_URL + currency)
                .addHeader(API_HEADER, API_KEY)
                .build();
        Response response = this.httpClient.newCall(request).execute();
        String result = null;
        try {
            if (!response.isSuccessful()) {
                throw new IOException("Wrong status: " + response.code() + "; body: " + response.body().string());
            }
            Gson gson = new Gson();
            DataModel data = gson.fromJson(response.body().string(), DataModel.class);
            result = data.value + "";

        } finally {
            response.body().close();
        }

        return result;
    }

    private static class DataModel {
        String currency;
        float value;

        private DataModel() {
            currency = null;
        }
    }
}