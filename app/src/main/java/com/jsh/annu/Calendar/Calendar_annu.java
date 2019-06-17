package com.jsh.annu.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jsh.annu.Calendar.decorators.*;
import com.jsh.annu.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Calendar_annu extends AppCompatActivity {

    DBHelper helper;
    SQLiteDatabase db;//일정을 넣기위한 데이터베이스

    String time, kcal, menu;
    //private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    MaterialCalendarView materialCalendarView;
    List<String> plan = new ArrayList<>();
    List<String> study_time_list = new ArrayList<>();
    AddPlan up_paln;

    ImageButton addplan, delplan;
    EditText doo;
    TextView txt1, txt2, txt3;
    int Year = 0, Month = 0, Day = 0;// 선택한 날자
    String shot_Day;//선택한 날자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        addplan = (ImageButton) findViewById(R.id.calendar_imgbt_addplan);
        delplan = (ImageButton) findViewById(R.id.calendar_imgbt_delplan);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        doo = (EditText) findViewById(R.id.calendar_edt_do);

        doo.setVisibility(View.INVISIBLE);//계획 안보이게
        addplan.setVisibility(View.INVISIBLE);
        delplan.setVisibility(View.INVISIBLE);

        helper = new DBHelper(this);//데이터 베이스
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = helper.getReadableDatabase();
        }

        Cursor cursor;
        cursor = db.rawQuery("SELECT day, do FROM schedule", null); //데이터 베이스 내부에 있는 일정 가져오기기
        while (cursor.moveToNext()) {
            plan.add(cursor.getString(0));
            plan.add(cursor.getString(0));
        }

        cursor = db.rawQuery("SELECT day, time FROM study_time", null);// 공부한 시간 가져오기
        while (cursor.moveToNext()){
            study_time_list.add(cursor.getString(0));
        }

            materialCalendarView.state().edit()
                .setFirstDayOfWeek(java.util.Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)//달력 보기 방식
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                //oneDayDecorator
                new OneDayDecorator()
        );

        //plan.add(shot_Day);
        new AddPlan(plan).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {//날자 누를시 발생 이벤트
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Year = date.getYear();
                Month = date.getMonth() + 1;
                Day = date.getDay();
                shot_Day = Year + "," + Month + "," + Day;//클릭한 날자


                materialCalendarView.clearSelection();//선택모션 초기화
                Toast.makeText(getApplicationContext(), Year + "년 " + Month + "월 " + Day + "일", Toast.LENGTH_SHORT).show();


                for (int i = 0; i < plan.size(); i++) {//선택한 날자에 계획이 있는지 확인
                    if (plan.get(i).equals(shot_Day)) {//계획이 있다면

                        Cursor cursor;
                        cursor = db.rawQuery("SELECT day, do FROM schedule WHERE day= '"+shot_Day+"'; ", null);


                       // txt1.setText("YES Plan");

                        cursor.moveToNext();
                        txt2.setText(cursor.getString(1));

                        Log.e("plan", "same");
                        doo.setText("");
                        doo.setVisibility(View.INVISIBLE);
                        addplan.setVisibility(View.INVISIBLE);
                        delplan.setVisibility(View.VISIBLE);

                        break;
                    } else {//계획이 없다면

                       // txt1.setText("NO Plan");
                        txt2.setText("no plan");
                        Log.e("plan", "different");
                        doo.setVisibility(View.VISIBLE);
                        addplan.setVisibility(View.VISIBLE);
                        delplan.setVisibility(View.INVISIBLE);

                    }
                }
                for(int i = 0 ; i < study_time_list.size(); i++){
                    if(study_time_list.get(i).equals(shot_Day)){//그날 공부한 시간이 있다면.
                        Cursor cursor;
                        cursor = db.rawQuery("SELECT day, time FROM study_time WHERE day = '"+shot_Day+"';",null);
                        cursor.moveToNext();
                        txt1.setText(cursor.getString(1));
                    }
                    else {//공부한 시간이 없다면
                        txt1.setText("you don't study ");
                    }
                }

            }
        });


        addplan.setOnClickListener(new View.OnClickListener() {//선택한 날자에 계획 추가
            @Override
            public void onClick(View v) {
                String schedule_plan = doo.getText().toString();
                db.execSQL("INSERT INTO schedule VALUES (null, '" + shot_Day + "', '" + schedule_plan + "');");
                doo.setText("");
                Toast.makeText(getApplicationContext(), "계획 추가 완료!", Toast.LENGTH_SHORT).show();
            }
        });
        delplan.setOnClickListener(new View.OnClickListener() {//선택한 날자 계획 제거
            @Override
            public void onClick(View v) {

                db.execSQL("DELETE FROM schedule WHERE day='" + shot_Day + "';");
                Toast.makeText(getApplicationContext(), "계획 삭제 완료!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private class AddPlan extends AsyncTask<Void, Void, List<CalendarDay>> {


        List<String> day_plan;

        AddPlan(List<String> day_plan) {
            this.day_plan = day_plan;

        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for (int i = 0; i < day_plan.size(); i++) {
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = day_plan.get(i).split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year, month - 1, dayy);
            }


            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            calendarDays.remove(0);

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, Calendar_annu.this));

        }
    }


    public class DBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "calender.db";
        private static final int DATABASE_VERSION = 2;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE schedule( _id INTEGER PRIMARY KEY AUTOINCREMENT, day TEXT, do TEXT);");
            db.execSQL("CREATE TABLE study_time( _id INTEGER PRIMARY KEY AUTOINCREMENT, day TEXT, time TEXT);");
            db.execSQL("INSERT INTO schedule VALUES (null, '2019,3,2', '조승현 생일');");
            db.execSQL("INSERT INTO study_time VALUES (null, '2019,6,15','97 분 24 초 ');");
            /**
             * 초기 데이터가 없을때 오늘날자에 표시하는것을
             * 방지하기위해 리스트를 하나 지우는것에서
             * 에러가 나므로 초기 데이터 하나 삽입
             **/
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS schedule");
            onCreate(db);
        }

    }

}