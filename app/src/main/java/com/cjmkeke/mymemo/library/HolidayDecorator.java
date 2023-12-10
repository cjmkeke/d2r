package com.cjmkeke.mymemo.library;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class HolidayDecorator implements DayViewDecorator {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private final CalendarDay today = CalendarDay.today();
    private List<Integer> holidayLocalDate = new ArrayList<>();
    private List<String> holidayDataName = new ArrayList<>();

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return holidayLocalDate.contains(getLocalDate(day));
    }

    private int getLocalDate(CalendarDay day) {
        Calendar calendar = day.getCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH는 0부터 시작합니다.
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.parseInt(String.format("%04d%02d%02d", year, month, dayOfMonth));
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")));
    }

    public void fetchHolidayDates() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("calendarDate").child("detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int localDate = dataSnapshot.child("locdate").getValue(Integer.class);
                    String dataName = dataSnapshot.child("dateName").getValue(String.class);
                    holidayLocalDate.add(localDate);
                    holidayDataName.add(dataName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터베이스 오류 처리
            }
        });

    }
}
