package com.example.annu.Dictionary;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.annu.EyeDetected.EyeService;
import com.example.annu.OCR.OcrCaptureActivity;
import com.example.annu.R;

public class Dictionary extends AppCompatActivity {

    ImageButton bt_camera, bt_search, bt_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_layout);

        bt_camera = (ImageButton) findViewById(R.id.dictionary_bt1);
        bt_search = (ImageButton) findViewById(R.id.dictionary_bt2);
        bt_history = (ImageButton) findViewById(R.id.dictionary_bt3);

        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//ocr 기능 사용

                if (isServiceRunning() == true)
                    Toast.makeText(getApplicationContext(), "졸음방지 동작중에는\n문자인식 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);//인텐트 지정
                    startActivity(intent);//액티비티 출력
                }
            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//검색기능 사용
                Intent intent = new Intent(getApplicationContext(), Dictionary_search.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });

    }

    public boolean isServiceRunning() {//서비스가 작동중인지 알아보는 메서드
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (EyeService.class.getName().equals(service.service.getClassName())) return true;
        }
        return false;
    }
}
