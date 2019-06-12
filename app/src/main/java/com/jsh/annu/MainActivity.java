package com.jsh.annu;

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

import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;

import com.jsh.annu.Calendar.Calendar;
import com.jsh.annu.Dictionary.Dictionary;
import com.jsh.annu.EyeDetected.EyeService;
import com.jsh.annu.Note.NoteList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private static final int RC_HANDLE_CAMERA_PERM = 2;

    ImageButton note, dictionary, setting, face;
    Intent intent;//서비스 인텐트

    private AdView mAdView;//광고

    Chronometer timer;//타이머
    Button reset, calendar;
    long stop_time = 0;//정지한 시간을 계산

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent loding_intent = new Intent(this, LodingActivity.class);
        startActivity(loding_intent);
        setContentView(R.layout.study_select);


        MobileAds.initialize(this, "ca-app-pub-8347262987394620~3286481735");//광고 설정정
        mAdView = findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        note = (ImageButton) findViewById(R.id.study_bt_note);
        dictionary = (ImageButton) findViewById(R.id.study_bt_dictionary);
        face = (ImageButton) findViewById(R.id.study_bt_face);
        intent = new Intent(this, EyeService.class);
        setting = (ImageButton) findViewById(R.id.setting);

        timer = (Chronometer) findViewById(R.id.study_timer);//타이머 id 선언 (final 지우면 정지,시작,리셋 메서드 실행 안됨)
        reset = (Button) findViewById(R.id.study_bt_reset);////타이머 리셋버튼 선언
        calendar = (Button) findViewById(R.id.study_bt_calendar);//켈린더 test

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
                    if (isServiceRunning() == false) {//버튼을 눌렀을때 서비스가 동작중이 아니면
                        face.setImageResource(R.drawable.face_on);
                        startService(intent);//서비스 실행
                        Toast.makeText(getApplicationContext(), "졸음 방지 ON", Toast.LENGTH_SHORT).show();

                        timer.setBase(SystemClock.elapsedRealtime() + stop_time);//타이머 실행
                        timer.start();
                        reset.setClickable(false);//타이머 작동중에는 리셋버튼 누르지 못함

                    } else {//동작중이면
                        stopService(intent);//서비스 종료
                        face.setImageResource(R.drawable.face_off);
                        Toast.makeText(getApplicationContext(), "졸음 방지 OFF", Toast.LENGTH_SHORT).show();

                        stop_time = timer.getBase() - SystemClock.elapsedRealtime();//타이머 정지
                        timer.stop();
                        reset.setClickable(true);//타이머가 멈추면 리셋 가능
                    }
                } else {
                    requestCameraPermission();
                }


            }
        });

        if (isServiceRunning() == true) {//서비스가 실행중이면
            face.setImageResource(R.drawable.face_on);//스위치를 ON 상태로 바꾼다
        } else
            face.setImageResource(R.drawable.face_off);


        reset.setOnClickListener(new Button.OnClickListener() {//타이머 리셋 함수
            public void onClick(View v) {
                timer.setBase(SystemClock.elapsedRealtime());
                stop_time = 0;
                timer.stop();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Calendar.class);//인텐트 지정
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}

