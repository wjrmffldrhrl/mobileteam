package com.example.annu;
import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.annu.Dictionary.Dictionary;
import com.example.annu.EyeDetected.EyeService;
import com.example.annu.Note.NoteList;

public class MainActivity extends AppCompatActivity {

    private static final int RC_HANDLE_CAMERA_PERM = 2;

    ImageButton note, dictionary;
    Switch face;
    Intent intent;//서비스 인텐트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent loding_intent = new Intent(this, LodingActivity.class);
        startActivity(loding_intent);
        setContentView(R.layout.study_select);

        note = (ImageButton) findViewById(R.id.study_bt_note);
        dictionary = (ImageButton) findViewById(R.id.study_bt_dictionary);
        face = (Switch) findViewById(R.id.study_switch_face);
        intent = new Intent(this, EyeService.class);


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

        final Context context = getApplicationContext();
        face.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//스위치로 설정
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isServiceRunning() == false) {//어플이 실행중이 아닐때(중복 실행되는 경우 방지)
                    if (isChecked == true)//스위치를 키면
                        startService(intent);//서비스 실행
                }
                if (isChecked == false)
                    stopService(intent);

                int rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

                if (rc == PackageManager.PERMISSION_GRANTED) {// 권한 체크
                    Log.e("permission", "ok");
                } else {
                    stopService(intent); //권한 설정이 안됐을때 실행된 서비스를 종료시킨다.
                    face.setChecked(false);//on된 스위치를 off함
                    requestCameraPermission();
                }
            }
        });

        if (isServiceRunning() == true) {//서비스가 실행중이면
            face.setChecked(true);//스위치를 ON 상태로 바꾼다
        }
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
