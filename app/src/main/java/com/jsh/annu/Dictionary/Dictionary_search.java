package com.jsh.annu.Dictionary;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.jsh.annu.EyeDetected.EyeService;
import com.jsh.annu.OCR.OcrCaptureActivity;

import com.jsh.annu.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.toLowerCase;

public class Dictionary_search extends AppCompatActivity {

    private TextToSpeech tts;
    myDBHelper myHelper;
    EditText search;
    ImageButton btn_cls;


    ImageButton imgbtn_camera,imgbtn_history,imgbtn_search,sound;

    TextView text_word, text_mean;
    SQLiteDatabase sqlDB;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private AdView mAdView;//광고

    char fisrt_word;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_search_layout);


        mAdView = findViewById(R.id.diction_search_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pref = getSharedPreferences("history", Activity.MODE_PRIVATE);//검색 기록을 남기기 위한 변수
        editor = pref.edit();

        search = (EditText) findViewById(R.id.edt_search);

        imgbtn_search = (ImageButton) findViewById(R.id.imgbtn_search);

        btn_cls = (ImageButton) findViewById(R.id.diction_imgbtn_cls);
        imgbtn_camera = (ImageButton) findViewById(R.id.diction_imgbtn_camera) ;
        imgbtn_history= (ImageButton) findViewById(R.id.diction_imgbtn_history);
        sound = (ImageButton)findViewById(R.id.diction_imgbtn_sound);

        text_word = (TextView) findViewById(R.id.text_word);
        text_mean = (TextView) findViewById(R.id.text_mean);

        Intent ocr_result = getIntent();//OCR에서 넘어왔을때 데이터 받기
        String data_OCR = ocr_result.getStringExtra("OCR");

        Intent history_result = getIntent();
        String data_history = history_result.getStringExtra("history_to_search");

        if (data_OCR != null)//ocr에서 데이터를 받아도 history에서 초기화 되기때문에 조건문 설정
            search.setText(data_OCR);//받아온 데이터를 edittext에 넣는다
        else if(data_history != null)
            search.setText(data_history);

/////////////////////////파일 database로 옮겨주기/////////////////////////////////////

        File folder = new File("/data/data/com.jsh.annu/databases");
        folder.mkdirs();

        File outfile = new File("/data/data/com.jsh.annu/databases" + "/" + "dictionary");

        if (outfile.length() <= 0) {//파일이 없으면 생성
            AssetManager assetManager = getResources().getAssets();
            try {
                InputStream is = assetManager.open("dictionary.db"
                        , AssetManager.ACCESS_BUFFER);
                long filesize = is.available();
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                FileOutputStream fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//////////////////////////////////////////////////////////////////////////////////////////
//단어 말해주기 설정
        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("OnInitListener", "Text to speech engine started successfully.");
                            tts.setLanguage(Locale.US);
                        } else {
                            Log.d("OnInitListener", "Error starting the text to speech engine.");
                        }
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(), listener);

///////////////////////////////////////////////////////////////////////////////////////
        myHelper = new myDBHelper(this); //데이터 베이스 생성

        //검색어로 검색
        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String search_word = search.getText().toString();//검색어를 저장

                myHelper.openDataBase();
                myHelper.close();


                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;

                if (search_word.getBytes().length > 0)
                    fisrt_word = search_word.charAt(0); // 단어의 첫번째 알파벳을 알아낸다
                // 이후 검색할 단어 테이블을 결정해줌

                if (search_word.getBytes().length > 0 && !isLowerCase(fisrt_word))//첫 알파벳이 소문자가 아니면
                    fisrt_word = toLowerCase(fisrt_word);//소문자로 바꿔줌


                if (search_word.getBytes().length <= 0 || fisrt_word < 'a' || fisrt_word > 'z') {//범위 밖의 단어
                    Toast.makeText(getApplicationContext(), "영단어만 넣어주세요", Toast.LENGTH_SHORT).show();
                    search.setText("");//edittext 초기화
                } else {
                    cursor = sqlDB.rawQuery("SELECT * FROM " + fisrt_word + "_word", null);
                    //검색어의 첫번째 문자를 가지고 해당 테이블의 커서를 움직인다.

                    String dic_word = "";
                    String dic_mean = "\r\n" + "----------------------------------------" + "\r\n";

                    while (cursor.moveToNext()) {//커서를 계속 진행시킨다.

                        if (search_word.equalsIgnoreCase(cursor.getString(0))) {//검색어와 일치하면
                            //대소문자 구분하지 않게 변경
                            dic_word += cursor.getString(0);
                            break;//반복 종료
                        }

                    }

                    if (cursor.isAfterLast()) {//데이터 베이스를 모두 봐도 찾는 단어가 없을때
                        dic_word += "";
                        dic_mean += "단어를 찾지못했습니다.";
                    } else {//찾는 단어가 있을때
                        editor.putInt("hsnum", pref.getInt("hsnum", 0) + 1);//검색 기록 갯수
                        editor.putString("hs" + pref.getInt("hsnum", 0), dic_word);//단어를 넣는다.
                        editor.apply();
                        dic_mean += cursor.getString(1) + "\r\n";//찾는 단어가 있으면 그 단어 뜻 넣기

                    }

                    text_word.setText(dic_word);//찾은 단어로 입력
                    text_mean.setText(dic_mean);


                    cursor.close();
                    sqlDB.close();
                }
            }
        });


        imgbtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//ocr 기능 사용

                if (isServiceRunning() == true)
                    Toast.makeText(getApplicationContext(), "졸음방지 동작중에는\n문자인식 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);//인텐트 지정
                    startActivity(intent);//액티비티 출력
                }
            }
        });
        imgbtn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dictionary_history.class);//인텐트 지정
                startActivity(intent);//액티비티 출력
            }
        });

        btn_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(text_word.getText(), TextToSpeech.QUEUE_ADD, null, "DEFAULT");// 찾은 단어 발음 들려주기
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

    public class myDBHelper extends SQLiteOpenHelper {


        private SQLiteDatabase mDataBase;


        public myDBHelper(Context context) {
            super(context, "dictionary", null, 1);// 1은 데이터베이스 버젼
        }


        public boolean openDataBase() throws SQLException {//databases에 있는 데이터 읽어오기
            String mPath = "/data/data/com.jsh.annu/databases" + "/" + "dictionary";
            mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            return mDataBase != null;
        }

        @Override
        public synchronized void close() {
            if (mDataBase != null)
                mDataBase.close();
            super.close();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {//apk 설치시 실행
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
