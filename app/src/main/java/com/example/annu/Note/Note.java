package com.example.annu.Note;


import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.annu.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Note extends AppCompatActivity {
    EditText mMemoEdit = null, title;
    TextFileManager mTextFileManager = new TextFileManager(this);//TextFileManger 객체 생성
    Button save, load, delete;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);
        mMemoEdit = (EditText) findViewById(R.id.note_edt);
        title = (EditText) findViewById(R.id.note_title);
        save = (Button) findViewById(R.id.note_bt_save);
        //load = (Button) findViewById(R.id.note_bt_load);
        delete = (Button) findViewById(R.id.note_bt_delete);

        Intent choice_file = getIntent();//NoteList에서 넘어왔을때 데이터 받기
        String name = choice_file.getStringExtra("filename");
        name = name.replace(".txt", "");
        title.setText(name);//받은 데이터로 파일 이름 설정

        String memoData = mTextFileManager.load(title.getText().toString());//선택한 파일 불러오기
        mMemoEdit.setText(memoData);

        save.setOnClickListener(new View.OnClickListener() {//저장버튼 클릭
            @Override
            public void onClick(View v) {


                String memoData = mMemoEdit.getText().toString();//작성한 문서 저장
                mTextFileManager.save(memoData, title.getText().toString());
                Toast.makeText(getApplicationContext(), "저장", Toast.LENGTH_SHORT).show();


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {//삭제 버튼 클릭
            @Override
            public void onClick(View v) {

                mTextFileManager.delete(title.getText().toString());//저장한 메모 삭제
                mMemoEdit.setText("");
                Toast.makeText(getApplicationContext(), "삭제", Toast.LENGTH_SHORT).show();
                finish();//엑티비티 종료

            }
        });

    }


}

