package com.gvjay.schedulemanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class EventDBHandler extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "scheduled_events.db";
    public static int DATABASE_VERSION = 1;

    public EventDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long addEvent(ScheduledEvent scheduledEvent){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ScheduledEvent.COLUMN_TYPE, scheduledEvent.type);
        cv.put(ScheduledEvent.COLUMN_TITLE, scheduledEvent.title);
        cv.put(ScheduledEvent.COLUMN_FROM_DATE, scheduledEvent.fromDate.getTime());
        cv.put(ScheduledEvent.COLUMN_TO_DATE, scheduledEvent.toDate.getTime());
        cv.put(ScheduledEvent.COLUMN_EVENT_DATE, scheduledEvent.eventDate.getTime());
        cv.put(ScheduledEvent.COLUMN_FREQUENCY, scheduledEvent.frequency);

        long id = database.insert(ScheduledEvent.TABLE_NAME, null, cv);

        database.close();

        return id;
    }

    public ScheduledEvent[] getEventsOnDate(Date currentDate){
        String query = "SELECT * FROM "+ScheduledEvent.TABLE_NAME;
//                + " WHERE " + ScheduledEvent.COLUMN_FREQUENCY + " = " + ScheduledEvent.FREQUENCY_OPTIONS.DAILY
 //               + " OR " + "(" + ScheduledEvent.COLUMN_FREQUENCY + " = " + ScheduledEvent.FREQUENCY_OPTIONS.ONE_TIME + " AND " + ScheduledEvent.COLUMN_EVENT_DATE
   //             + " = " + currentDate.getTime() + ")";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        int rowCount = cursor.getCount();
        ScheduledEvent[] output = new ScheduledEvent[rowCount];
        for(int i=0;i<rowCount;i++){
            output[i] = new ScheduledEvent(cursor.getString(0),
                    cursor.getString(1),
                    new Date(cursor.getLong(2)),
                    new Date(cursor.getLong(3)),
                    new Date(cursor.getLong(4)),
                    cursor.getString(5));

            cursor.moveToNext();
        }
        database.close();
        cursor.close();
        return output;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ScheduledEvent.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ScheduledEvent.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
