package com.example.annu.Note;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * 파일 관리 클래스
 * 파일의 저장 불러오기 삭제를 담당한다
 * 해당 클래스의 객체를 Note 클래스에서 생성하여
 * 기능들을 활용함
* */
public class TextFileManager {
    private static final String FILE_NAME=".txt";

    Context mContext = null;

    public TextFileManager(Context context) {
        mContext=context;
    }

    public void save(String strData,String title) {
        if (strData == null || strData.equals("")) {//파일이 공백일때 저장 안함
            return;
        }
        FileOutputStream fosMemo = null;

        try {
            fosMemo = mContext.openFileOutput(title+FILE_NAME, Context.MODE_PRIVATE);
            fosMemo.write(strData.getBytes());
            fosMemo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String load(String title) {
        try {
            FileInputStream fisMemo = mContext.openFileInput(title+FILE_NAME);
            byte[] memoData = new byte[fisMemo.available()];

            while (fisMemo.read(memoData) != -1) {
            }

            return new String(memoData);
        } catch (IOException e) {}
        return "";
    }

    public void delete(String title) {
        mContext.deleteFile(title+FILE_NAME);
    }
}
