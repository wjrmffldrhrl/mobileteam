package com.example.annu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

public class Light extends AppCompatActivity {

    private WindowManager.LayoutParams params;
    private float brightness;// 화면 밝기 제어


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);

        params = getWindow().getAttributes();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("light","off");

        params.screenBrightness = brightness;
        getWindow().setAttributes(params);//기존 밝기로 변경

    }

    @Override
    protected void onResume(){
        super.onResume();

        brightness = params.screenBrightness; // 기존밝기 저장

        params.screenBrightness = 1f;
        getWindow().setAttributes(params);//밝기 최대로 설정정
    }
}