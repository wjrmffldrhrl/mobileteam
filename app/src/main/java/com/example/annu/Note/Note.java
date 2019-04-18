package com.example.annu.Note;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.annu.R;

public class Note extends AppCompatActivity {
    EditText mMemoEdit = null;
    TextFileManager mTextFileManager = new TextFileManager(this);//TextFileManger 객체 생성
    Button save, load, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);
        mMemoEdit = (EditText) findViewById(R.id.note_edt);
        save = (Button) findViewById(R.id.note_bt_save);
        load = (Button) findViewById(R.id.note_bt_load);
        delete = (Button) findViewById(R.id.note_bt_delete);



        save.setOnClickListener(new View.OnClickListener() {//저장버튼 클릭
            @Override
            public void onClick(View v) {


                String memoData = mMemoEdit.getText().toString();//작성한 문서 저장
                mTextFileManager.save(memoData);
                Toast.makeText(getApplicationContext(), "저장", Toast.LENGTH_SHORT).show();


            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String memoData = mTextFileManager.load();//이전에 저장한 목록 가져오기
                mMemoEdit.setText(memoData);
                Toast.makeText(getApplicationContext(), "불러오기", Toast.LENGTH_SHORT).show();


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTextFileManager.delete();//저장한 메모 삭제
                mMemoEdit.setText("");
                Toast.makeText(getApplicationContext(), "삭제", Toast.LENGTH_SHORT).show();

            }
        });

    }


}

