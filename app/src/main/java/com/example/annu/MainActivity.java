package com.example.annu;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.annu.Dictionary.Dictionary;
import com.example.annu.EyeDetected.EyeService;
import com.example.annu.Note.NoteList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private static final int RC_HANDLE_CAMERA_PERM = 2;

    ImageButton note, dictionary, setting, face;
    Intent intent;//서비스 인텐트

    private AdView mAdView;//광고

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent loding_intent = new Intent(this, LodingActivity.class);
        startActivity(loding_intent);
        setContentView(R.layout.study_select);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");//광고 설정정
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        note = (ImageButton) findViewById(R.id.study_bt_note);
        dictionary = (ImageButton) findViewById(R.id.study_bt_dictionary);
        face = (ImageButton) findViewById(R.id.study_bt_face);
        intent = new Intent(this, EyeService.class);
        setting = (ImageButton) findViewById(R.id.setting);

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NoteList.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });

        dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dictionary.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceRunning() == true)
                    Toast.makeText(getApplicationContext(), "서비스 실행중 설정변경 불가", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getApplicationContext(), Option.class);//인텐트 지정
                    startActivity(intent);//액티비티 출력
                }
            }
        });


        final Context context = getApplicationContext();
        face.setOnClickListener(new View.OnClickListener() {//서비스 시작 버튼
            @Override
            public void onClick(View v) {


                int rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA);//권한설정

                if (rc == PackageManager.PERMISSION_GRANTED) {// 권한 체크
                    Log.e("permission", "ok");
                } else {
                    face.setImageResource(R.drawable.face_off);//on된 스위치를 off함
                    requestCameraPermission();
                }

                if (isServiceRunning() == false) {//버튼을 눌렀을때 서비스가 동작중이 아니면
                    face.setImageResource(R.drawable.face_on);
                    startService(intent);//서비스 실행
                    Toast.makeText(getApplicationContext(), "졸음 방지 ON", Toast.LENGTH_SHORT).show();
                } else {//동작중이면
                    stopService(intent);//서비스 종료
                    face.setImageResource(R.drawable.face_off);
                    Toast.makeText(getApplicationContext(), "졸음 방지 OFF", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (isServiceRunning() == true) {//서비스가 실행중이면
            face.setImageResource(R.drawable.face_on);//스위치를 ON 상태로 바꾼다
        } else
            face.setImageResource(R.drawable.face_off);
    }


    public boolean isServiceRunning() {//서비스가 작동중인지 알아보는 메서드
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (EyeService.class.getName().equals(service.service.getClassName())) return true;
        }
        return false;
    }

    private void requestCameraPermission() {//카메라 권한 요청 처리

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };


    }
}
