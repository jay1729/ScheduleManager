package com.gvjay.schedulemanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gvjay.schedulemanager.Database.EventDBHandler;
import com.gvjay.schedulemanager.Database.ScheduledEvent;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {

    private Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    TextView changeDate;
    TextView fromTime;
    TextView toTime;
    Button addEvent;

    Date fromDate;
    Date toDate;
    Date eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        changeDate = findViewById(R.id.set_date);
        fromTime = findViewById(R.id.from_time);
        toTime = findViewById(R.id.to_time);
        addEvent = findViewById(R.id.add_event_button);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                changeDate.setText(String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(year));
                eventDate = new Date(year-1900, monthOfYear, dayOfMonth, 0, 0, 0);
            }
        };

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fromTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromTime.setText( selectedHour + ":" + selectedMinute);
                        fromDate = new Date(myCalendar.get(Calendar.YEAR)-1900,
                                myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH),
                                selectedHour, selectedMinute, 0);
                        Log.i("from date actual", fromDate.toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        toTime.setText( selectedHour + ":" + selectedMinute);
                        toDate = new Date(myCalendar.get(Calendar.YEAR)-1900,
                                myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH),
                                selectedHour, selectedMinute, 0);
                        Log.i("Actual to date", toDate.toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDBHandler dbHandler = new EventDBHandler(AddEventActivity.this);
                dbHandler.addEvent(new ScheduledEvent("event", "Wololo", fromDate, toDate, eventDate, "daily"));
                dbHandler.close();

                Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                AddEventActivity.this.startActivity(intent);
            }
        });
    }
}
