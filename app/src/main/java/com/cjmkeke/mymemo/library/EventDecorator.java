package com.cjmkeke.mymemo.library;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;
    private final int dotColor;

    public EventDecorator(int dotColor, Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
        this.dotColor = dotColor;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, dotColor)); // 도트 크기와 색상 설정
    }
}