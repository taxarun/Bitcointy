package com.ermakov.bitcointy;

import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ServiceHelper.BitcointyResultListener {

    static {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .penaltyLog()
                .penaltyDeath()
                .build()
        );
    }

    private int mRequestId;
    private TextView mResultTextView;
    private TextView mStatusTextView;

    private final View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_settings:
                    final Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_check_course:
                    MainActivity.this.startUpdate();
                    break;
            }
        }
    };

    public void startUpdate() {
        if (mRequestId == 0) {
            mStatusTextView.setText("Loading data...");
            String currency = BitcointyStorage.getInstance(this).getCurrency();
            mRequestId = ServiceHelper.getInstance().
                    getCourseForCurrency(this, currency, this);
        } else {
            Snackbar.make(this.findViewById(R.id.main_content),
                    "Too much pending requests", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_settings).setOnClickListener(onButtonClick);
        findViewById(R.id.btn_check_course).setOnClickListener(onButtonClick);

        mResultTextView = (TextView) findViewById(R.id.yoad_result);
        mStatusTextView = (TextView) findViewById(R.id.status);
        startUpdate();

    }

    @Override
    protected void onStop() {
        ServiceHelper.getInstance().removeListener(mRequestId);
        super.onStop();
    }

    @Override
    public void onBitcointyResult(final boolean success, final String result) {
        mRequestId = 0;
        String currency = BitcointyStorage.getInstance(this).getCurrency();
        if (success) {
            mStatusTextView.setText("Course for " + currency + " was successfully updated");
            mResultTextView.setText(String.format("Bitcoin cost in %s: %s", currency, result));
            BitcointyStorage.getInstance(this).cacheCourse(Float.parseFloat(result));
        } else {
            Float course = BitcointyStorage.getInstance(this).getCourse();
            if (course == -1.0) {
                mStatusTextView.setText("Failed to update course for " + currency +
                        ", cached value is missing. \nError description: " + result);
                mResultTextView.setText("");
            }
            else {
                mStatusTextView.setText("Failed to update course for " + currency +
                        ", cached value was loaded. \nError description: " + result);
                mResultTextView.setText(String.format("Bitcoin cost in %s: %f", currency, course));
            }
            Snackbar.make(MainActivity.this.findViewById(R.id.main_content),
                    String.format("Error: %s", result), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
