package com.gvjay.schedulemanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.gvjay.schedulemanager.Database.EventDBHandler;
import com.gvjay.schedulemanager.Database.ScheduledEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    CalendarPagerAdapter pagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity", "onCreate");
        pagerAdapter = new CalendarPagerAdapter(this.getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis()+ DateUtils.DAY_IN_MILLIS);

        EventDBHandler dbHandler = new EventDBHandler(this);
        CalendarData cd = new CalendarData();
        ArrayList<CalendarData.DrawData> data = cd.convertScheduledEvents(dbHandler.getEventsOnDate(calendar.getTime()));

        int len = data.size();
        for(int i=0;i<len;i++){
            if(data.get(i).events.size()>0){
                Log.i("id", data.get(i).events.get(0).ID+"");
                Log.i("frequency", data.get(i).events.get(0).startPointer+"");
                Log.i("from", data.get(i).events.get(0).endPointer+"");
                Log.i("to", data.get(i).events.get(0).eventTitle);
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume");
    }
}

