package com.jsh.annu.Calendar;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    Button addplan,delplan;
    TextView txt1,txt2,txt3;
    int Year = 0, Month = 0, Day = 0;// 선택한 날자
    String shot_Day;//선택한 날자
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        addplan = (Button) findViewById(R.id.calendar_bt_addplan);
        delplan= (Button) findViewById(R.id.calendar_bt_delplan);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);

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
                Year = date.getYear();
                Month = date.getMonth() + 1;
                Day = date.getDay();
                shot_Day = Year + "," + Month + "," + Day;


                materialCalendarView.clearSelection();//선택모션 초기화
                Toast.makeText(getApplicationContext(), Year + "년 " + Month + "월 " + Day + "일", Toast.LENGTH_SHORT).show();


                for(int i = 0 ; i < plan.size() ; i++){//선택한 날자에 계획이 있는지 확인
                    if(plan.get(i).equals(shot_Day)){
                        txt1.setText("YES Plan");
                        Log.e("plan","same");
                        break;
                    }
                    else {
                        txt1.setText("NO Plan");
                        Log.e("plan","different");
                    }
                }
            }
        });
        addplan.setOnClickListener(new View.OnClickListener() {//선택한 날자에 계획 추가
            @Override
            public void onClick(View v) {
                plan.add(shot_Day);
                plan.add(shot_Day);//오류로 인해서 두개씩 추가
                new AddPlan(plan).executeOnExecutor(Executors.newSingleThreadExecutor());

            }
        });
        delplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan.remove(shot_Day);
                new AddPlan(plan).executeOnExecutor(Executors.newSingleThreadExecutor());
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

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, Calendar.this));
            plan.remove(plan.size() - 1);//리스트에 두개씩 저장해야 하는 오류때문에 한개 지우기
        }
    }
}