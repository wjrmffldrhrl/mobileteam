package com.example.annu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Option extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);

        t1 = (TextView) findViewById(R.id.tx1);
        t2 = (TextView) findViewById(R.id.tx2);
        t3 = (TextView) findViewById(R.id.tx3);
        t4 = (TextView) findViewById(R.id.tx4);
        t5 = (TextView) findViewById(R.id.tx5);

        SharedPreferences pref = getSharedPreferences("options", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("op1",5124125);
        editor.putInt("op2", 1);
        editor.putInt("op3", 6);
        editor.putInt("op4", 9);
        editor.apply();

        t1.setText(""+pref.getInt("op1",0));
        t2.setText(""+pref.getInt("op2",0));
        t3.setText(""+pref.getInt("op3",0));
        t4.setText(""+pref.getInt("op4",0));
        t5.setText(""+pref.getInt("op5",0));





    }
}
