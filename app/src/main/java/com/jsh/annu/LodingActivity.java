package com.jsh.annu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.MobileAds;

public class LodingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);

        MobileAds.initialize(this, "ca-app-pub-8347262987394620~3286481735");//광고 설정정
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}