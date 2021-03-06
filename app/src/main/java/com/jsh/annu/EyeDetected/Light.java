package com.jsh.annu.EyeDetected;


import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
//import android.hardware.camera2.CameraAccessException;
//import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jsh.annu.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Timer;
import java.util.TimerTask;

public class Light extends AppCompatActivity {

    private AdView mAdView;//광고

    private WindowManager.LayoutParams params;
    private float brightness;// 화면 밝기 제어

    boolean flashon = false;//플래시 사용 변수
   // private CameraManager mCameraManager;
    //private String mCameraId;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    Button get_up;
    LinearLayout color;

    final Handler handler = new Handler(){//색상 변경 핸들러
        public void handleMessage(Message msg){
            if(!flashon)
            color.setBackgroundColor(Color.rgb(255,255,0));
            else
                color.setBackgroundColor(Color.rgb(255,255,255));
        }
    };

    Timer my_timer = new Timer();//플래시 반복 실행을 위한 타이머
    TimerTask my_task = new TimerTask() {
        @Override
        public void run() {

            Message msg = handler.obtainMessage();//화면 색상 변경을 위한 메세지
            handler.sendMessage(msg);

          //  mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);//카메라 프래시 제어어
          /*  try {
              //  mCameraId = mCameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
             e.printStackTrace();
            }
           */
            if (!flashon) {
               // color.setBackgroundColor(Color.rgb(255,255,0));
                flashon = true;
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//버젼이 낮을시 실행 x
                    try {
                        mCameraManager.setTorchMode(mCameraId, true);

                        Log.e("flashon", "can use");
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        Log.e("flashon", "can't use");
                    }
                }*/
            } else {
                //color.setBackgroundColor(Color.rgb(255,255,255));
                flashon = false;
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        mCameraManager.setTorchMode(mCameraId, false);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        Log.e("flashoff", "can't use");
                    }
                }*/
            }
        }
    };

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);// 잠금화면 위로 화면표시

        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "mywake:Lock");

        wakeLock.acquire();


        mAdView = findViewById(R.id.light_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        get_up = (Button) findViewById(R.id.light_bt_getup);
        color = (LinearLayout) findViewById(R.id.light_layout_color);

        params = getWindow().getAttributes();
        intent = new Intent(this, EyeService.class);
        stopService(intent);//진행중인 서비스 정지

        my_timer.schedule(my_task, 1000, 500); // 타이머 실행

        get_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {//액티비티 종료시
        super.onDestroy();
        Log.e("light", "off");
        wakeLock.release();
        params.screenBrightness = brightness;
        getWindow().setAttributes(params);//기존 밝기로 변경
/*
        if (flashon) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    mCameraManager.setTorchMode(mCameraId, false);
                    flashon = false;
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    Log.e("flashoff", "can't use");
                }
            }

        }
        */
        my_timer.cancel();//타이머 정지

        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        brightness = params.screenBrightness; // 기존밝기 저장
        params.screenBrightness = 1f;
        getWindow().setAttributes(params);//밝기 최대로 설정정
    }


}