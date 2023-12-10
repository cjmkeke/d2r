package com.cjmkeke.mymemo.library;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class OneDayDecorator implements DayViewDecorator {
    private final CalendarDay today = CalendarDay.today();

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return today.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#E91E63")));
        view.addSpan(new StyleSpan(Typeface.BOLD));
    }
}
