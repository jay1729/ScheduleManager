package com.gvjay.schedulemanager.Database;

import android.content.Context;
import android.util.Log;

import com.gvjay.schedulemanager.DateUtils;
import com.gvjay.schedulemanager.R;

import java.util.ArrayList;
import java.util.Date;

public class ScheduledEvent {
    public String type;
    public String title;
    public Date eventDate;
    public Date fromDate;
    public Date toDate;
    public int ID;
    public String frequency;
    public int attendance;
    public int classDayOfWeek;
    public int classDayOfMonth;
    public int classMonth;
    public int classYear;

    public static String TABLE_NAME = "scheduled_event";

    public static String COLUMN_ID = "_id";
    public static String COLUMN_TYPE = "type";
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_FROM_DATE = "from_date";
    public static String COLUMN_TO_DATE = "to_date";
    public static String COLUMN_EVENT_DATE = "event_date";
    public static String COLUMN_CLASS_DAY_OF_WEEK = "class_day";
    public static String COLUMN_CLASS_DAY_OF_MONTH = "class_day_of_month";
    public static String COLUMN_CLASS_MONTH = "class_month";
    public static String COLUMN_CLASS_YEAR = "class_year";
    public static String COLUMN_FREQUENCY = "frequency";
    public static String COLUMN_ATTENDANCE = "attendance";

    public static final class FREQUENCY_OPTIONS {
        public static final String[] DAILY = {"Daily", "daily"};
        public static String[] WEEKLY = {"Weekly", "weekly"};
        public static String[] MONTHLY = {"Monthly", "monthly"};
        public static String[] YEARLY = {"Yearly", "yearly"};
        public static String[] ONE_TIME = {"One Time", "one_time"};

        public static ArrayList<String[]> getAllOptions(){
            ArrayList<String[]> output = new ArrayList<>();
            output.add(DAILY);
            output.add(WEEKLY);
            output.add(MONTHLY);
            output.add(YEARLY);
            output.add(ONE_TIME);

            return output;
        }

        public static String findValueByName(String name){
            ArrayList<String[]> temp = getAllOptions();
            int len = temp.size();
            for(int i=0;i<len;i++) if(temp.get(i)[0].equals(name)) return temp.get(i)[1];
            return null;
        }
    }

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_FROM_DATE + " INTEGER,"
            + COLUMN_TO_DATE + " INTEGER,"
            + COLUMN_EVENT_DATE + " INTEGER,"
            + COLUMN_CLASS_DAY_OF_WEEK + " INTEGER,"
            + COLUMN_CLASS_DAY_OF_MONTH + " INTEGER,"
            + COLUMN_CLASS_MONTH + " INTEGER,"
            + COLUMN_CLASS_YEAR + " INTEGER,"
            + COLUMN_FREQUENCY + " TEXT,"
            + COLUMN_ATTENDANCE + " INTEGER"
            +")";

    public ScheduledEvent(int ID, String type, String title, Date fromDate, Date toDate, Date eventDate, String frequency, int attendance){
        this.ID = ID;
        this.type = type;
        this.title = title;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.eventDate = eventDate;
        this.frequency = frequency;
        this.attendance = attendance;

        this.classDayOfWeek = eventDate.getDay();
        this.classDayOfMonth = eventDate.getDate();
        this.classMonth = eventDate.getMonth();
        this.classYear = eventDate.getYear()+1900;
    }

    public ScheduledEvent(String type, String title, Date fromDate, Date toDate, Date eventDate, String frequency, int attendance){
        this.type = type;
        this.title = title;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.eventDate = eventDate;
        this.frequency = frequency;
        this.attendance = attendance;

        this.classDayOfWeek = eventDate.getDay();
        this.classDayOfMonth = eventDate.getDate();
        this.classMonth = eventDate.getMonth();
        this.classYear = eventDate.getYear()+1900;
    }

    public static class Utils {
        public static ScheduledEvent checkTimingClash(ScheduledEvent scheduledEvent, Context context){
            EventDBHandler dbHandler = new EventDBHandler(context);

            ArrayList<ScheduledEvent> allEvents = dbHandler.getAllEvents();
            int len = allEvents.size();

            ScheduledEvent blockingEvent = null;

            for(int i=0;i<len;i++){
                ScheduledEvent currentEvent = allEvents.get(i);
                if(currentEvent.frequency.equals(FREQUENCY_OPTIONS.ONE_TIME)){
                    if((currentEvent.eventDate.getTime() < scheduledEvent.eventDate.getTime()) && (DateUtils.doDatesClash(currentEvent.eventDate, scheduledEvent.eventDate))){
                        continue;
                    }
                } else if(scheduledEvent.frequency.equals(FREQUENCY_OPTIONS.ONE_TIME)){
                    if((scheduledEvent.eventDate.getTime() < currentEvent.eventDate.getTime()) && (DateUtils.doDatesClash(currentEvent.eventDate, scheduledEvent.eventDate))){
                        continue;
                    }
                }
                if(currentEvent.frequency.equals(FREQUENCY_OPTIONS.DAILY[1]) || scheduledEvent.frequency.equals(FREQUENCY_OPTIONS.DAILY[1])){
                    if(DateUtils.doTimesClash(currentEvent.fromDate, currentEvent.toDate, scheduledEvent.fromDate, scheduledEvent.toDate)){
                        blockingEvent = currentEvent;
                        break;
                    }
                }else if(currentEvent.frequency.equals(FREQUENCY_OPTIONS.WEEKLY[1]) || scheduledEvent.frequency.equals(FREQUENCY_OPTIONS.WEEKLY[1])){
                    Log.i("it is weekly", "weekly it is "+currentEvent.classDayOfWeek+" "+scheduledEvent.classDayOfWeek);
                    if(DateUtils.doTimesClash(currentEvent.fromDate, currentEvent.toDate, currentEvent.classDayOfWeek, scheduledEvent.fromDate, scheduledEvent.toDate, scheduledEvent.classDayOfWeek)){
                        blockingEvent = currentEvent;
                        break;
                    }
                }else if(currentEvent.frequency.equals(FREQUENCY_OPTIONS.MONTHLY[1]) || scheduledEvent.frequency.equals(FREQUENCY_OPTIONS.MONTHLY[1])){
                    if(DateUtils.doTimesClash(currentEvent.fromDate, currentEvent.toDate, currentEvent.classDayOfMonth, scheduledEvent.fromDate, scheduledEvent.toDate, scheduledEvent.classDayOfMonth)){
                        blockingEvent = currentEvent;
                        break;
                    }
                }
            }
            if(blockingEvent != null) return blockingEvent;
            return null;
        }
    }
}
