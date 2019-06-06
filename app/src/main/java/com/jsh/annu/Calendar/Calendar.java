package com.jsh.annu.Calendar;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class Calendar extends AppCompatActivity {

    String time, kcal, menu;
    //private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;
    List<String> plan = new ArrayList<>();
    AddPlan up_paln;
    Button set;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        set = (Button) findViewById(R.id.set);
        txt = (TextView) findViewById(R.id.txt);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(java.util.Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)//달력 보기 방식
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                //oneDayDecorator
                new OneDayDecorator()
        );
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {//날자 누를시 발생 이벤트
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                String shot_Day = Year + "," + Month + "," + Day;
                plan.add(shot_Day);
                plan.add(shot_Day);
                materialCalendarView.clearSelection();//선택모션 초기화

                Toast.makeText(getApplicationContext(), shot_Day, Toast.LENGTH_SHORT).show();
                new AddPlan(plan).executeOnExecutor(Executors.newSingleThreadExecutor());


            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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
            /*
            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }
            */


            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
           calendarDays.remove(0);

             set.setText("" + calendarDays.size());
            String text = "";
            for (int i = 0; i < calendarDays.size(); i++)
                text += calendarDays.get(i);
            txt.setText(text);

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, Calendar.this));
            plan.remove(plan.size()-1);
        }
    }
}