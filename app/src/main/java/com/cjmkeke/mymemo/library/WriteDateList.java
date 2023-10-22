package com.cjmkeke.mymemo.library;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WriteDateList {

    public String getTimeFormatAdapterViewSS(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        sd.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String result = sd.format(date);
        return result;
    }

    public String getTimeFormatAdapterView(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy. MM. dd");
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

    public long getTimeFormatAdapterListLong() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        sd.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String formattedDate = sd.format(date);

        try {
            // 포맷된 문자열을 long으로 파싱
            return Long.parseLong(formattedDate);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return 0L; // 기본값 또는 오류 처리를 원하는 대로 변경하세요.
    }

}
