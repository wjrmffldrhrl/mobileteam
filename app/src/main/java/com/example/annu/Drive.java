package com.example.annu;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.annu.EyeDetected.EyeService;

public class Drive extends AppCompatActivity {

    Switch face;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive_select);

        face = (Switch) findViewById(R.id.drive_switch_face);
        intent = new Intent(this, EyeService.class);



        face.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//스위치로 설정
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isServiceRunning() == false) {
                    if (isChecked == true)
                        startService(intent);
                }

                if(isChecked == false)
                    stopService(intent);
            }
        });

        if(isServiceRunning()==true){
            face.setChecked(true);
        }
    }


    public boolean isServiceRunning() {//서비스가 작동중인지 알아보는 메서드
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (EyeService.class.getName().equals(service.service.getClassName())) return true;
        }
        return false;
    }
}