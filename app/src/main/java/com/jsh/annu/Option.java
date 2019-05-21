package com.jsh.annu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Option extends AppCompatActivity {

    private AdView mAdView;//광고

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    RadioGroup group1, group2, group3;
    RadioButton[] aram = new RadioButton[4];
    RadioButton[] warning = new RadioButton[4];
    RadioButton[] time = new RadioButton[3];

    int[] aram_id = {R.id.option_radiobutton_beep, R.id.option_radiobutton_symbal, R.id.option_radiobutton_clap, R.id.option_radiobutton_bell};
    int[] warning_id = {R.id.option_radiobutton_auto, R.id.option_radiobutton_bellandvibe,R.id.option_radiobutton_vibe, R.id.option_radiobutton_flash};
    int[] time_id = {R.id.option_radiobutton_30, R.id.option_radiobutton_15, R.id.option_radiobutton_5};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);




        mAdView = findViewById(R.id.option_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pref = getSharedPreferences("options", Activity.MODE_PRIVATE);
        editor = pref.edit();


        group1 = (RadioGroup) findViewById(R.id.option_radiogroup_set1);
        group2 = (RadioGroup) findViewById(R.id.option_radiogroup_set2);
        group3 = (RadioGroup) findViewById(R.id.option_radiogroup_set3);

        for (int i = 0; i < aram.length; i++) {
            aram[i] = (RadioButton) findViewById(aram_id[i]);
            if (pref.getInt("aram", 0) == i)
                aram[i].setChecked(true);
        }
        for (int i = 0; i < warning.length; i++) {
            warning[i] = (RadioButton) findViewById(warning_id[i]);
            if (pref.getInt("warning", 0) == i)
                warning[i].setChecked(true);
        }
        for (int i = 0; i < time.length; i++) {
            time[i] = (RadioButton) findViewById(time_id[i]);
            if (pref.getInt("time", 0) == i)
                time[i].setChecked(true);
        }

        Log.e("aram "," : "+(pref.getInt("aram", 0)+1));
        Log.e("warning "," : "+(pref.getInt("warning", 0)+1));
        Log.e("time "," : "+(pref.getInt("time", 0)+1));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (int i = 0; i < aram.length; i++) {
            if (aram[i].isChecked() == true)
                editor.putInt("aram",i);
        }
        for (int i = 0; i < warning.length; i++) {

            if (warning[i].isChecked() == true)
                editor.putInt("warning",i);
        }
        for (int i = 0; i < time.length; i++) {

            if (time[i].isChecked() == true)
               editor.putInt("time",i);
        }

        editor.apply();

        Log.e("aram set"," : "+(1+pref.getInt("aram", 0)));
        Log.e("warning set"," : "+(1+pref.getInt("warning", 0)));
        Log.e("time set"," : "+(1+pref.getInt("time", 0)));

    }
}
