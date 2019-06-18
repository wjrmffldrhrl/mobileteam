package com.jsh.annu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ExplainActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private AdView mAdView;//광고

    ImageView explain_img;
    ImageButton imgbtn1, imgbtn2, imgbtn3, out;
    CheckBox noshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explain_layout);

        explain_img = (ImageView) findViewById(R.id.explain_img_explain);
        imgbtn1 = (ImageButton) findViewById(R.id.explain_btn1);
        imgbtn2 = (ImageButton) findViewById(R.id.explain_btn2);
        imgbtn3 = (ImageButton) findViewById(R.id.explain_btn3);
        out = (ImageButton) findViewById(R.id.explain_btn_out);
        noshow = (CheckBox) findViewById(R.id.explain_check_noshow);

        MobileAds.initialize(this, "ca-app-pub-8347262987394620~3286481735");//광고 설정정
        mAdView = findViewById(R.id.explain_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pref = getSharedPreferences("times", Activity.MODE_PRIVATE);
        editor = pref.edit();

        imgbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explain_img.setImageResource(R.drawable.explain1);
            }
        });

        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explain_img.setImageResource(R.drawable.explain2);
            }
        });
        imgbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explain_img.setImageResource(R.drawable.explain3);
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (noshow.isChecked()) {
            editor.putBoolean("noshow", false);//공부 시간 저장
            editor.apply();
        }
    }
}
