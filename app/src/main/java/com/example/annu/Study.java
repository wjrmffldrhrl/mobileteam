package com.example.annu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.annu.Dictionary.Dictionary;
import com.example.annu.EyeDetected.EyeDetected;
import com.example.annu.EyeDetected.EyeService;
import com.example.annu.Note.Note;
import com.example.annu.Note.NoteList;

public class Study extends AppCompatActivity {

    ImageButton note,dictionary;
    Switch face;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        face.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//스위치로 설정
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    startService(intent);
                else
                    stopService(intent);
            }
        });
    }
}
