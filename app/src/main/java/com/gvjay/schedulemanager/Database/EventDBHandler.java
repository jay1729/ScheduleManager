package com.gvjay.schedulemanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class EventDBHandler extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "scheduled_events.db";
    public static int DATABASE_VERSION = 1;

    public EventDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long addEvent(ScheduledEvent scheduledEvent){

        Log.i("test db", scheduledEvent.classDayOfWeek+"");

        if(scheduledEvent.frequency.equals(ScheduledEvent.FREQUENCY_OPTIONS.DAILY[1])){
            scheduledEvent.frequency = ScheduledEvent.FREQUENCY_OPTIONS.WEEKLY[1];
            for(int i=0;i<7;i++){
                scheduledEvent.classDayOfWeek = scheduledEvent.classDayOfWeek + 1;
                if(scheduledEvent.classDayOfWeek > 6) scheduledEvent.classDayOfWeek = 0;
                addEvent(scheduledEvent);
            }
            return 0;
        }
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ScheduledEvent.COLUMN_TYPE, scheduledEvent.type);
        cv.put(ScheduledEvent.COLUMN_TITLE, scheduledEvent.title);
        cv.put(ScheduledEvent.COLUMN_FROM_DATE, scheduledEvent.fromDate.getTime());
        cv.put(ScheduledEvent.COLUMN_TO_DATE, scheduledEvent.toDate.getTime());
        cv.put(ScheduledEvent.COLUMN_EVENT_DATE, scheduledEvent.eventDate.getTime());
        cv.put(ScheduledEvent.COLUMN_CLASS_DAY_OF_WEEK, scheduledEvent.classDayOfWeek);
        cv.put(ScheduledEvent.COLUMN_CLASS_DAY_OF_MONTH, scheduledEvent.classDayOfMonth);
        cv.put(ScheduledEvent.COLUMN_CLASS_MONTH, scheduledEvent.classMonth);
        cv.put(ScheduledEvent.COLUMN_CLASS_YEAR, scheduledEvent.classYear);
        cv.put(ScheduledEvent.COLUMN_FREQUENCY, scheduledEvent.frequency);
        cv.put(ScheduledEvent.COLUMN_ATTENDANCE, scheduledEvent.attendance);

        long id = database.insert(ScheduledEvent.TABLE_NAME, null, cv);

        database.close();

        return id;
    }

    public int updateAttendanceRecord(AttendanceEvent attendanceEvent){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(AttendanceEvent.COLUMN_ID, attendanceEvent.ID);
        cv.put(AttendanceEvent.COLUMN_CLASS_NAME, attendanceEvent.className);
        cv.put(AttendanceEvent.COLUMN_DATE, attendanceEvent.classDate.getTime());
        cv.put(AttendanceEvent.COLUMN_STATUS, attendanceEvent.status);

        return sqLiteDatabase.update(AttendanceEvent.TABLE_NAME, cv, AttendanceEvent.COLUMN_ID + "=?", new String[]{Integer.toString(attendanceEvent.ID)});
    }

    public int deleteAttendanceRecordById(int Id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(AttendanceEvent.TABLE_NAME, AttendanceEvent.COLUMN_ID + "=?", new String[]{Integer.toString(Id)});
    }

    public int deleteAttendanceRecordsByName(String className){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(AttendanceEvent.TABLE_NAME, AttendanceEvent.COLUMN_CLASS_NAME + "=?", new String[]{className});
    }

    public long addAttendanceRecord(AttendanceEvent attendanceEvent){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(AttendanceEvent.COLUMN_CLASS_NAME, attendanceEvent.className);
        cv.put(AttendanceEvent.COLUMN_DATE, attendanceEvent.classDate.getTime());
        cv.put(AttendanceEvent.COLUMN_STATUS, attendanceEvent.status);

        long id = sqLiteDatabase.insert(AttendanceEvent.TABLE_NAME, null, cv);

        sqLiteDatabase.close();

        return id;
    }

    public ArrayList<AttendanceEvent> getAttendanceRecordsByName(String className){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(AttendanceEvent.TABLE_NAME,
                null,
                AttendanceEvent.COLUMN_CLASS_NAME+"=?",
                new String[]{className}, null, null, null);
        ArrayList<AttendanceEvent> output = new ArrayList<AttendanceEvent>();
        cursor.moveToFirst();
        while(true){
            output.add(new AttendanceEvent(cursor.getInt(0), cursor.getString(1), cursor.getLong(2), cursor.getString(3)));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        return output;
    }

    public int deleteEventById(int Id){
        SQLiteDatabase database = this.getWritableDatabase();

        return database.delete(ScheduledEvent.TABLE_NAME, ScheduledEvent.COLUMN_ID+"=?", new String[]{Integer.toString(Id)});
    }

    public int deleteEventByName(String className){
        SQLiteDatabase database = this.getWritableDatabase();

        return database.delete(ScheduledEvent.TABLE_NAME, ScheduledEvent.COLUMN_TITLE+"=?", new String[]{className});
    }

    private static ScheduledEvent getScheduledEventFromCursor(Cursor cursor){
        return new ScheduledEvent(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                new Date(cursor.getLong(3)),
                new Date(cursor.getLong(4)),
                new Date(cursor.getLong(5)),
                cursor.getString(10),
                cursor.getInt(11));
    }

    private static void feedDataIntoArrayList(Cursor cursor, ArrayList<ScheduledEvent> list){
        cursor.moveToFirst();
        if(cursor.getCount() == 0) return;
        while (true){
            list.add(getScheduledEventFromCursor(cursor));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }
        cursor.close();
    }

    public ArrayList<ScheduledEvent> getEventsOnDate(Date currentDate){

        SQLiteDatabase database = this.getWritableDatabase();
        Log.i("GetEventsCalled", currentDate.getDay()+"");

        ArrayList<ScheduledEvent> output = new ArrayList<ScheduledEvent>();

        Cursor cursor = database.query(ScheduledEvent.TABLE_NAME, null,
                ScheduledEvent.COLUMN_FREQUENCY + "=?"+" AND " + ScheduledEvent.COLUMN_CLASS_DAY_OF_WEEK + "=?",
                new String[]{ScheduledEvent.FREQUENCY_OPTIONS.WEEKLY[1], Integer.toString(currentDate.getDay())},
                null, null, null);
        feedDataIntoArrayList(cursor, output);

        cursor = database.query(ScheduledEvent.TABLE_NAME, null,
                ScheduledEvent.COLUMN_FREQUENCY + "=?" + " AND " + ScheduledEvent.COLUMN_CLASS_DAY_OF_MONTH + "=?",
                new String[]{ScheduledEvent.FREQUENCY_OPTIONS.MONTHLY[1], Integer.toString(currentDate.getDate())},
                null, null, null);
        feedDataIntoArrayList(cursor, output);

        cursor = database.query(ScheduledEvent.TABLE_NAME, null,
                ScheduledEvent.COLUMN_FREQUENCY + "=?" + " AND " + ScheduledEvent.COLUMN_CLASS_DAY_OF_WEEK + "=?" +" AND "
                        + ScheduledEvent.COLUMN_CLASS_DAY_OF_MONTH + "=?",
                new String[]{ScheduledEvent.FREQUENCY_OPTIONS.YEARLY[1], Integer.toString(currentDate.getDay()), Integer.toString(currentDate.getDate())},
                null, null, null);
        feedDataIntoArrayList(cursor, output);

        cursor = database.query(ScheduledEvent.TABLE_NAME, null,
                ScheduledEvent.COLUMN_FREQUENCY + "=?" + " AND " + ScheduledEvent.COLUMN_CLASS_DAY_OF_WEEK + "=?" +" AND "
                        + ScheduledEvent.COLUMN_CLASS_DAY_OF_MONTH + "=?" + " AND " + ScheduledEvent.COLUMN_CLASS_YEAR + "=?",
                new String[]{ScheduledEvent.FREQUENCY_OPTIONS.YEARLY[1], Integer.toString(currentDate.getDay()), Integer.toString(currentDate.getDate()),
                        (currentDate.getYear()+1900) + ""},
                null, null, null);
        feedDataIntoArrayList(cursor, output);
        database.close();
        return output;
    }

    public ArrayList<ScheduledEvent> getAllEvents(){
        String query = "SELECT * FROM "+ScheduledEvent.TABLE_NAME;

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        ArrayList<ScheduledEvent> output = new ArrayList<ScheduledEvent>();
        if(cursor.getCount() == 0) return output;
        while (true){
            output.add(getScheduledEventFromCursor(cursor));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }
        database.close();
        cursor.close();
        return output;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ScheduledEvent.CREATE_TABLE);
        sqLiteDatabase.execSQL(AttendanceEvent.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ScheduledEvent.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+AttendanceEvent.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
