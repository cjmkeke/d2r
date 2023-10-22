package com.cjmkeke.mymemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cjmkeke.mymemo.R;
import com.cjmkeke.mymemo.library.SaturdayDecorator;
import com.cjmkeke.mymemo.library.SunDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class Calendar extends Activity {

    private MaterialCalendarView calendar;
    private EditText inputCalendar;
    private Button commit;
    private java.util.Calendar calendarDate;
    private CalendarDay firstSelectedDate; // 처음 선택한 날짜
    private CalendarDay lastSelectedDate;  // 마지막으로 선택한 날짜
    private String  selectedDateString;
    private String  memoContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar);

        calendar = findViewById(R.id.calendarView);
//        inputCalendar = findViewById(R.id.et_calendar_input);
//        commit = findViewById(R.id.btn_calendar_commit);

        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        SunDayDecorator sunDayDecorator = new SunDayDecorator();
        SaturdayDecorator saturdayDecorator = new SaturdayDecorator();
        calendar.addDecorators(sunDayDecorator, saturdayDecorator);

//        calendar.setOnRangeSelectedListener(new OnRangeSelectedListener() {
//            @Override
//            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
//                // 범위 내에서 처음과 마지막 날짜 추적
//                if (dates.size() >= 2) {
//                    firstSelectedDate = dates.get(0);
//                    lastSelectedDate = dates.get(dates.size() - 1);
//                }
//            }
//        });

//        commit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (firstSelectedDate != null && lastSelectedDate != null) {
//                    // 중간 날짜까지의 범위 계산
//                    List<CalendarDay> selectedDatesInRange = new ArrayList<>();
//                    CalendarDay currentDate = firstSelectedDate;
//                    while (!currentDate.equals(lastSelectedDate)) {
//                        selectedDatesInRange.add(currentDate);
//
//                        // 날짜를 하루 증가시킴
//                        int year = currentDate.getYear();
//                        int month = currentDate.getMonth();
//                        int day = currentDate.getDay() + 1;
//                        currentDate = CalendarDay.from(year, month, day);
//                    }
//
//                    // 마지막 날짜도 추가
//                    selectedDatesInRange.add(lastSelectedDate);
//
//                    // 선택한 범위의 날짜 데이터를 저장
//                    for (CalendarDay date : selectedDatesInRange) {
//                         selectedDateString = date.getDate().toString();
//                         memoContent = inputCalendar.getText().toString();
//
//                        // 여기서 memo 객체를 데이터베이스에 저장하거나 원하는 저장 방법을 사용할 수 있습니다.
//                        // 예를 들어, SQLite 데이터베이스나 SharedPreferences를 사용할 수 있습니다.
//
//                        Log.v("Selected Date", selectedDateString);
//                        Log.v("Memo Content", memoContent);
//                    }
//                    //
//                } else {
//                    // 범위를 선택하지 않았을 때 사용자에게 메시지를 표시
//                    Toast.makeText(Calendar.this, "날짜 범위를 선택하세요.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
