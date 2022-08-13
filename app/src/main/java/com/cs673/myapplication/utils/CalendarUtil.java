package com.cs673.myapplication.utils;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CalendarUtil {

    public static String getTargetDate(int days){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH,days);
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

    public static String getTargetDate(String dateStr,int days){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar  = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DAY_OF_MONTH,days);

        String targetDate = dateFormat.format(calendar.getTime());
        return targetDate;
    }

    public static String getTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

}
