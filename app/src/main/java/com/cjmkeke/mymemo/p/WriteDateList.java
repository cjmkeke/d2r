package com.cjmkeke.mymemo.p;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WriteDateList {

    public String getTimeFormatAdapterView(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        sd.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String result = sd.format(date);
        return result;
    }
    public String getTimeFormatAdapterList() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddhhmmss");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        sd.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String result = sd.format(date);
        return result;
    }
}
