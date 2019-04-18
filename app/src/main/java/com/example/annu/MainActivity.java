package com.example.annu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.annu.Dictionary.Dictionary;
import com.example.annu.EyeDetected.EyeDetected;
import com.example.annu.OCR.OcrCaptureActivity;

public class MainActivity extends AppCompatActivity {



    Button bt1,bt2,bt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt1 = (Button) findViewById(R.id.main_bt1);//OCR
        bt2 = (Button) findViewById(R.id.main_bt2);//eye detected
        bt3 = (Button) findViewById(R.id.main_bt3); //dictionary

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EyeDetected.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dictionary.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });

    }
}
