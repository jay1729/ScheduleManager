package com.gvjay.schedulemanager;

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
}
