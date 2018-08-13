package com.gvjay.schedulemanager.Database;

import java.util.Date;

public class ScheduledEvent {
    public String type;
    public String title;
    public Date eventDate;
    public Date fromDate;
    public Date toDate;
    public int ID;
    public String frequency;

    public static String TABLE_NAME = "scheduled_event";

    public static String COLUMN_ID = "_id";
    public static String COLUMN_TYPE = "type";
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_FROM_DATE = "from_date";
    public static String COLUMN_TO_DATE = "to_date";
    public static String COLUMN_EVENT_DATE = "event_date";
    public static String COLUMN_FREQUENCY = "frequency";

    public static class FREQUENCY_OPTIONS {
        public static String DAILY = "daily";
        public static String WEEKLY = "weekly";
        public static String MONTHLY = "monthly";
        public static String YEARLY = "yearly";
        public static String ONE_TIME = "one_time";
    }

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_FROM_DATE + " INTEGER,"
            + COLUMN_TO_DATE + " INTEGER,"
            + COLUMN_EVENT_DATE + " INTEGER,"
            + COLUMN_FREQUENCY + " TEXT"
            +")";

    public ScheduledEvent(int ID, String type, String title, Date fromDate, Date toDate, Date eventDate, String frequency){
        this.ID = ID;
        this.type = type;
        this.title = title;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.eventDate = eventDate;
        this.frequency = frequency;
    }

    public ScheduledEvent(String type, String title, Date fromDate, Date toDate, Date eventDate, String frequency){
        this.type = type;
        this.title = title;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.eventDate = eventDate;
        this.frequency = frequency;
    }
}
