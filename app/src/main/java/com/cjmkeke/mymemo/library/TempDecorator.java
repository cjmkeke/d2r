package com.cjmkeke.mymemo.library;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

public class TempDecorator implements DayViewDecorator {
    private HashSet<CalendarDay> dates;

    public TempDecorator() {
        this.dates = new HashSet<>();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates != null && dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }

    // "CalendarDay{yyyy-MM-dd}" 형식의 문자열을 CalendarDay로 변환하여 추가
    public void addDate(String date) {
        CalendarDay calendarDay = convertToCalendarDay(date);
        if (calendarDay != null) {
            dates.add(calendarDay);
        }
    }

    // 기존의 HashSet을 대체하기 위한 메서드
    public void setDates(HashSet<CalendarDay> dates) {
        this.dates = dates;
    }

    private CalendarDay convertToCalendarDay(String calendarDayString) {
        String dateString = parseDateString(calendarDayString);
        if (dateString != null) {
            int indexOfOpeningBrace = dateString.indexOf("{");
            int indexOfClosingBrace = dateString.indexOf("}");

            if (indexOfOpeningBrace != -1 && indexOfClosingBrace != -1) {
                // Remove "{", "}" and split the remaining string
                String[] dateComponents = dateString.substring(indexOfOpeningBrace + 1, indexOfClosingBrace).split("-");

                if (dateComponents.length == 3) {
                    int year = Integer.parseInt(dateComponents[0]);
                    int month = Integer.parseInt(dateComponents[1]) - 1;
                    int day = Integer.parseInt(dateComponents[2]);
                    return CalendarDay.from(year, month, day);
                }
            }
        }
        return null;
    }


    // "CalendarDay{yyyy-MM-dd}" 형식의 문자열에서 "yyyy-MM-dd" 부분을 추출하는 함수
    private String parseDateString(String calendarDayString) {
        int startIndex = calendarDayString.indexOf("{") + 1;
        int endIndex = calendarDayString.indexOf("}");
        if (startIndex != -1 && endIndex != -1) {
            return calendarDayString.substring(startIndex, endIndex);
        }
        return null;
    }

    // ... 이하 생략
}
