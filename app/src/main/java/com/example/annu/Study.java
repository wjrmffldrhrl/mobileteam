package com.example.annu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.annu.Dictionary.Dictionary;
import com.example.annu.Note.Note;

public class Study extends AppCompatActivity {

    ImageButton note,dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_select);

        note = (ImageButton) findViewById(R.id.study_bt_note);
        dictionary = (ImageButton) findViewById(R.id.study_bt_dictionary);


        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Note.class);//인텐트 지정
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
    }
}
