package com.example.annu;


import android.content.Context;
import android.content.Intent;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.annu.EyeDetected.EyeService;

import java.util.Timer;
import java.util.TimerTask;

public class Light extends AppCompatActivity {

    private WindowManager.LayoutParams params;
    private float brightness;// 화면 밝기 제어

    boolean flashon = false;//플래시 사용 변수
    private CameraManager mCameraManager;
    private String mCameraId;

    Button get_up;

    Timer my_timer = new Timer();//플래시 반복 실행을 위한 타이머
    TimerTask my_task = new TimerTask() {
        @Override
        public void run() {
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);//카메라 프래시 제어어
            try {
                mCameraId = mCameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            if (!flashon) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//버젼이 낮을시 실행 x
                    try {
                        mCameraManager.setTorchMode(mCameraId, true);
                        flashon = true;
                        Log.e("flashon", "can use");
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        Log.e("flashon", "can't use");
                    }
                }
            } else {
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
        }
    };

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);

        get_up = (Button) findViewById(R.id.light_bt_getup);
        params = getWindow().getAttributes();
        intent = new Intent(this, EyeService.class);
        stopService(intent);//진행중인 서비스 정지

        my_timer.schedule(my_task, 1000, 1000); // 타이머 실행

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

        params.screenBrightness = brightness;
        getWindow().setAttributes(params);//기존 밝기로 변경

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