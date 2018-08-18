package com.gvjay.schedulemanager.Database;

import java.util.Date;

public class AttendanceEvent {
    public int ID;
    public String className;
    public Date classDate;
    public String status;

    public static String TABLE_NAME = "attendance_records";

    public static String COLUMN_ID = "_id";
    public static String COLUMN_CLASS_NAME = "class_name";
    public static String COLUMN_DATE = "class_date";
    public static String COLUMN_STATUS = "status";

    public static class STATUS_OPTIONS {
        public static String ATTENDED = "Attended";
        public static String NOT_ATTENDED = "Not Attended";
        public static String NEUTRAL = "Class Not Conducted";
    }

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CLASS_NAME + " TEXT,"
            + COLUMN_DATE + " INTEGER,"
            + COLUMN_STATUS + " TEXT"
            + ")";

    public AttendanceEvent(int ID, String className, Date classDate, String status){
        this.ID = ID;
        this.className = className;
        this.classDate = classDate;
        this.status = status;
    }

    public AttendanceEvent(int ID, String className, long classDate, String status){
        this.ID = ID;
        this.className = className;
        this.classDate = new Date(classDate);
        this.status = status;
    }

    public AttendanceEvent(String className, Date classDate, String status){
        this.className = className;
        this.classDate = classDate;
        this.status = status;
    }

    public AttendanceEvent(String className, long classDate, String status){
        this.className = className;
        this.classDate = new Date(classDate);
        this.status = status;
    }
}
