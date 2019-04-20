package com.example.annu.Note;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextFileManager {
    private static final String FILE_NAME=".txt";

    Context mContext = null;

    public TextFileManager(Context context) {
        mContext=context;
    }

    public void save(String strData,String title) {
        if (strData == null || strData.equals("")) {
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
