package com.example.annu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.annu.Dictionary.Dictionary;
import com.example.annu.EyeDetected.EyeDetected;
import com.example.annu.OCR.OcrCaptureActivity;

public class MainActivity extends AppCompatActivity {



    ImageButton study,drive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_select);

    study = (ImageButton) findViewById(R.id.main_bt_study);
    drive = (ImageButton) findViewById(R.id.main_bt_drive);


    study.setOnClickListener(new View.OnClickListener() {//공부 메뉴 선택
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Study.class);//인텐트 지정
            startActivity(intent);//액티비티 출력
        }
    });

    drive.setOnClickListener(new View.OnClickListener() {// 운전 메뉴 선택
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Drive.class);//인텐트 지정
            startActivity(intent);//액티비티 출력
        }
    });

    }
}
