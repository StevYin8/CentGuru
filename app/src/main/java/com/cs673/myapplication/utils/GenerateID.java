package com.cs673.myapplication.utils;


import java.text.SimpleDateFormat;
import java.util.Date;


/*This class is to generate an id by the format of current time with three random digits
*
* */
public class GenerateID {

    public static String getDate(String format){
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String dateString =dateFormat.format(currentTime);
        return dateString;
    }

    public static String getRandomNum(int num){
        String numStr = "";
        for (int i = 0; i <num; i++) {
            numStr +=(int)(10*Math.random());
        }
        return numStr;
    }

    public static Long getGenerateID(){
        String format = "yyyyMMdd";
        int num = 3;
        String idStr = getDate(format)+getRandomNum(num);
        Long id = Long.valueOf(idStr);
        return id;
    }
}
