package com.jsh.annu.Dictionary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsh.annu.R;

import java.util.ArrayList;
import java.util.List;

public class Dictionary_history extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button cls;
    TextView hsnum;
    ListView item;

    final List<String> search = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_history_layout);

        pref = getSharedPreferences("history", Activity.MODE_PRIVATE);//검색기록 자료
        editor = pref.edit();

        hsnum = (TextView) findViewById(R.id.history_tv_hsnum);
        cls = (Button) findViewById(R.id.history_bt_cls);
        item = (ListView) findViewById(R.id.history_list_item);


        setlist();

        item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), Dictionary_search.class);//인텐트 지정
                intent.putExtra("history_to_search", search.get(position));//전달할 데이터
                startActivityForResult(intent, 3);//액티비티 출력
            }
        });


        cls.setOnClickListener(new View.OnClickListener() {//검색 기록 초기화
            @Override
            public void onClick(View v) {
                for (int i = 0; i < pref.getInt("hsnum", 0); i++)//검색한 횟수만큼 기록 초기화
                    editor.putString("hs" + i, "");

                editor.putInt("hsnum", 0);//검색한 횟수 초기화
                editor.apply();
                Toast.makeText(getApplicationContext(), "검색기록 초기화!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    void setlist() {
        hsnum.setText("검색 기록 수 : "+pref.getInt("hsnum",0));
        for (int i = 0; i < pref.getInt("hsnum", 0); i++)
            search.add(pref.getString("hs" + i, ""));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, search);//검색목록

        item.setAdapter(adapter);//리스너뷰에 목록 넣기
    }
}
