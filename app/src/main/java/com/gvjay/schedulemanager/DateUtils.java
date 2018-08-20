package com.gvjay.schedulemanager;

import android.util.Log;

import java.util.Date;

public class DateUtils {
    public static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    public static final long HOUR_IN_MILLIS = 1000 * 60 * 60;
    public static final long MIN_IN_MILLIS = 1000 * 60;

    public static long getDateInMillis(int year, int month, int day, int hour, int min, int sec){
        long output = 0;
        output += (sec * 1000);
        output += (min * MIN_IN_MILLIS);
        output += (hour * HOUR_IN_MILLIS);
        output += ((day) * DAY_IN_MILLIS);
        output += ((month + 1) * 30 * android.text.format.DateUtils.DAY_IN_MILLIS);
        output += ((year - 1970) * android.text.format.DateUtils.YEAR_IN_MILLIS);

        return output;
    }

    public static long getTimeFromDate(Date date){
        return (date.getTime() % android.text.format.DateUtils.DAY_IN_MILLIS);
    }

    public static boolean doTimesClash(Date fromDate1, Date toDate1, Date fromDate2, Date toDate2){
        if((getTimeFromDate(fromDate1) < getTimeFromDate(fromDate2)) && (getTimeFromDate(fromDate2) < getTimeFromDate(toDate1))) return true;
        if((getTimeFromDate(fromDate1) < getTimeFromDate(toDate2)) && (getTimeFromDate(toDate2) < getTimeFromDate(toDate1))) return true;
        if((getTimeFromDate(fromDate2) < getTimeFromDate(fromDate1)) && (getTimeFromDate(fromDate1) < getTimeFromDate(toDate2))) return true;
        return false;
    }

    public static boolean doTimesClash(Date fromDate1, Date toDate1, int dayOfWeek1, Date fromDate2, Date toDate2, int dayOfWeek2){
        if((dayOfWeek1 == dayOfWeek2) && doTimesClash(fromDate1, toDate1, fromDate2, toDate2)){
            Log.i("Attention", dayOfWeek1+" "+dayOfWeek2);
            return true;
        }
        return false;
    }

    public static boolean doDatesClash(Date eventDate1, Date eventDate2){
        long a = eventDate1.getTime() - (eventDate1.getTime() % android.text.format.DateUtils.DAY_IN_MILLIS);
        long b = eventDate2.getTime() - (eventDate2.getTime() % android.text.format.DateUtils.DAY_IN_MILLIS);

        return (a == b);
    }
}
