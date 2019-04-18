package com.example.annu;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    EditText mMemoEdit = null;
    TextFileManager mTextFileManager = new TextFileManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMemoEdit = (EditText) findViewById(R.id.edit);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load: {
                String memoData = mTextFileManager.load();
                mMemoEdit.setText(memoData);
                Toast.makeText(this, "불러오기 완료", Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.save: {
                String memoData = mMemoEdit.getText().toString();
                mTextFileManager.save(memoData);
                Toast.makeText(this, "저장 완료", Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.delete: {
                mTextFileManager.delete();
                mMemoEdit.setText("");
                Toast.makeText(this, "삭제 완료", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
}
