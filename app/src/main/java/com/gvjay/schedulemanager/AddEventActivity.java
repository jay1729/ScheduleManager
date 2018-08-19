package com.gvjay.schedulemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gvjay.schedulemanager.Database.EventDBHandler;
import com.gvjay.schedulemanager.Database.ScheduledEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {

    private Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    TextView changeDate;
    TextView fromTime;
    TextView toTime;
    Button addEvent;
    Spinner frequencySpinner;
    CheckBox attendanceBox;
    EditText classNameET;

    Date fromDate = myCalendar.getTime();
    Date toDate = myCalendar.getTime();
    Date eventDate = myCalendar.getTime();
    String frequency = ScheduledEvent.FREQUENCY_OPTIONS.DAILY[1];
    int attendance = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        classNameET = findViewById(R.id.editText);

        changeDate = findViewById(R.id.set_date);
        changeDate.setText(eventDate.getDate()+"-"+eventDate.getMonth()+"-"+eventDate.getYear());

        fromTime = findViewById(R.id.from_time);
        fromTime.setText(fromDate.getHours()+":"+fromDate.getMinutes());

        toTime = findViewById(R.id.to_time);
        toTime.setText(toDate.getHours()+":"+toDate.getMinutes());

        frequencySpinner = findViewById(R.id.add_freq_spinner);

        ArrayList<String[]> temp = ScheduledEvent.FREQUENCY_OPTIONS.getAllOptions();
        int len = temp.size();
        String[] items = new String[len];
        for(int i=0;i<len;i++) items[i] = temp.get(i)[0];
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);

        frequencySpinner.setAdapter(arrayAdapter);

        addEvent = findViewById(R.id.add_event_button);

        attendanceBox = findViewById(R.id.add_attendance);
        attendanceBox.setChecked(true);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                changeDate.setText(String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(year));
                eventDate = new Date(year-1900, monthOfYear, dayOfMonth, 0, 0, 0);
            }
        };

        classNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("text", editable.toString());
            }
        });

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

        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Item Selected", i + " " + l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        attendanceBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                attendance = b ? 1 : 0;
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDBHandler dbHandler = new EventDBHandler(AddEventActivity.this);
                ScheduledEvent scheduledEvent = new ScheduledEvent("class", "Wololo", fromDate, toDate, eventDate, ScheduledEvent.FREQUENCY_OPTIONS.DAILY[1], 1);
                if(checkObjection(view.getContext(), scheduledEvent)){
                    Log.i("Objection", "Objection");
                    return;
                }
                dbHandler.addEvent(scheduledEvent);
                dbHandler.close();

                Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                AddEventActivity.this.startActivity(intent);
                AddEventActivity.this.finish();
            }
        });
    }

    private boolean checkObjection(Context context, ScheduledEvent scheduledEvent){
        ScheduledEvent blockingEvent = ScheduledEvent.Utils.checkTimingClash(scheduledEvent, context);
        if(blockingEvent != null){
            new AlertDialog.Builder(context).setTitle(context.getString(R.string.time_clash_title))
                    .setMessage(context.getString(R.string.time_clash_message)+" "+blockingEvent.title)
                    .create().show();
            return true;
        }
        return false;
    }
}
