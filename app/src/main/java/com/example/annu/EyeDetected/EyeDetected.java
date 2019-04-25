package com.example.annu.EyeDetected;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.annu.R;

public class EyeDetected extends AppCompatActivity {

    ImageView img;
    Button start,stop;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_detected);

        start = (Button) findViewById(R.id.eye_bt_start);
        stop = (Button) findViewById(R.id.eye_bt_stop);
        img = (ImageView) findViewById(R.id.img);
        intent = new Intent(this,EyeService.class);

        //권한 설정
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Grant Permission and restart app", Toast.LENGTH_SHORT).show();
        } else {//권한 설정 완료 시

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startService(intent);

                }
            });
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopService(intent);
                }
            });
        }
    }

}