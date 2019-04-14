package com.example.annu.Dictionary;

import android.content.res.AssetManager;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.annu.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Dictionary_search extends AppCompatActivity {

    myDBHelper myHelper;
    EditText search, edtNameResult, edtNumberResult;
    Button btnSelect1,btncls;
    SQLiteDatabase sqlDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_sarch_layout);

        search = (EditText) findViewById(R.id.search);
        edtNameResult = (EditText) findViewById(R.id.edtNameResult);
        edtNumberResult = (EditText) findViewById(R.id.edtNumberResult);
        btnSelect1 = (Button) findViewById(R.id.btnSelect1);
        btncls = (Button) findViewById(R.id.btncls);

/////////////////////////파일 database로 옮겨주기/////////////////////////////////////

        File folder = new File("/data/data/com.example.annu/databases");
        folder.mkdirs();

        File outfile = new File("/data/data/com.example.annu/databases" + "/" + "dictionary");

        if (outfile.length() <= 0) {//파일이 없으면 생성
            AssetManager assetManager = getResources().getAssets();
            try {
                InputStream is = assetManager.open("dictionary.db", AssetManager.ACCESS_BUFFER);
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


        myHelper = new myDBHelper(this); //데이터 베이스 생성

        btnSelect1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String search_word = search.getText().toString();//검색어를 저장
                myHelper.openDataBase();
                myHelper.close();


                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;

                if(search_word.getBytes().length <= 0||search_word.charAt(0)<'a' || search_word.charAt(0)> 'z'){//범위 밖의 단어
                    Toast.makeText(getApplicationContext(),"영단어만 넣어주세요",Toast.LENGTH_SHORT).show();
                    search.setText("");//edittext 초기화
                }
                else {
                    cursor = sqlDB.rawQuery("SELECT * FROM " + search_word.charAt(0) + "_word", null);
                    //검색어의 첫번째 문자를 가지고 해당 테이블의 커서를 움직인다.

                    String dic_word = "단어" + "\r\n" + "--------" + "\r\n";
                    String dic_mean = "뜻" + "\r\n" + "--------" + "\r\n";

                    while (cursor.moveToNext()) {//커서를 계속 진행시킨다.

                        if (search_word.equals(cursor.getString(0))) {//검색어와 일치하면
                            dic_word += cursor.getString(0) + "\r\n";
                            break;//반복 종료
                        }

                    }

                    if (cursor.isAfterLast()) {
                        dic_word += "단어를 찾지";
                        dic_mean += "못했습니다.";
                    } else
                        dic_mean += cursor.getString(1) + "\r\n";


                    edtNameResult.setText(dic_word);//찾은 단어로 입력
                    edtNumberResult.setText(dic_mean);


                    cursor.close();
                    sqlDB.close();
                }
            }
        });

        btncls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });


    }

    public class myDBHelper extends SQLiteOpenHelper {


        private SQLiteDatabase mDataBase;


        public myDBHelper(Context context) {
            super(context, "dictionary", null, 1);// 1은 데이터베이스 버젼
        }


        public boolean openDataBase() throws SQLException {//databases에 있는 데이터 읽어오기
            String mPath = "/data/data/com.example.annu/databases" + "/" + "dictionary";
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
