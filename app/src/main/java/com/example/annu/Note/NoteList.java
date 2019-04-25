package com.example.annu.Note;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.annu.Dictionary.Dictionary_search;
import com.example.annu.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteList extends AppCompatActivity {
    String path = "/data/data/com.example.annu/files";//파일이 저장된 경로
    Button newmemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        ListView list = (ListView) findViewById(R.id.note_list);//파일 목록이 저장될 리스트
        newmemo = (Button) findViewById(R.id.note_bt_new);

        File[] files = (new File(path).listFiles());//

        final List<String> filesNameList = new ArrayList<>();

        for(int i = 0;i<files.length;i++)//생성된 리스트에 경로 내에 있는 파일 목록을 불러온다
            filesNameList.add(files[i].getName());


        newmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Note.class);//인텐트 지정
                intent.putExtra("filename","");//전달할 데이터
                startActivityForResult(intent,2);//액티비티 출력
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,filesNameList);
        list.setAdapter(adapter);//리스너뷰에 목록 넣기

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),filesNameList.get(position),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Note.class);//인텐트 지정
                intent.putExtra("filename",filesNameList.get(position));//전달할 데이터
                startActivityForResult(intent,2);//액티비티 출력
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),filesNameList.get(position),Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}