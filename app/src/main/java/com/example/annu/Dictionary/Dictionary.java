package com.example.annu.Dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dictionary_search.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });

    }
}
