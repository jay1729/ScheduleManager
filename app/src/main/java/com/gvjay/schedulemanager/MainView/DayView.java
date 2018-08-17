package com.gvjay.schedulemanager.MainView;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gvjay.schedulemanager.CalendarData;
import com.gvjay.schedulemanager.Database.*;
import com.gvjay.schedulemanager.R;
import com.gvjay.schedulemanager.RVDecorator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DayView extends MainFragContent implements TouchCallBack {

    private View view;
    private int offset;
    private ViewGroup container;
    private LayoutInflater inflater;
    private Calendar calendar;
    private Date currentDate;
    private DayViewAdapter adapter;
    private ArrayList<ScheduledEvent> data;
    private EventDBHandler dbHandler;
    private View.OnTouchListener touchListener;
    private RecyclerView recyclerView;

    public DayView(ViewGroup container, int offset, LayoutInflater inflater){
        this.container = container;
        this.offset = offset;
        this.inflater = inflater;
        this.calendar = Calendar.getInstance();
        this.currentDate = this.calendar.getTime();
        dbHandler = new EventDBHandler(container.getContext());
        touchListener = new TouchHandler(this);
    }

    private void loadData(){
        data = dbHandler.getEventsOnDate(currentDate);
        int len = data.size();
        for(int i=0;i<len;i++){
            Log.i("index", i+"");
            Log.i("title", data.get(i).title);
            Log.i("from", data.get(i).fromDate.toString());
            Log.i("to", data.get(i).toDate.toString());
        }
    }

    @Override
    public View getView() {
        view = inflater.inflate(R.layout.day_fragment, container, false);
        TextView dayValue = view.findViewById(R.id.day_value);
        TextView dateValue = view.findViewById(R.id.date_value);

        String[] DAY_VALUES = container.getResources().getStringArray(R.array.day_values);

        long dayOffset = offset;
        dayOffset *=86400000;
        calendar.setTimeInMillis(currentDate.getTime()+dayOffset);

        dayValue.setText(DAY_VALUES[calendar.get(Calendar.DAY_OF_WEEK)-1]);
        dateValue.setText(calendar.getTime().toString().substring(4,10));

        recyclerView = view.findViewById(R.id.eventsView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        loadData();
        adapter = new DayViewAdapter();
        recyclerView.setAdapter(adapter);
        CalendarData cd = new CalendarData();
        recyclerView.addItemDecoration(new RVDecorator(cd.convertScheduledEvents(this.data)));

        return view;
    }

    @Override
    public void touchCallBack(float x, float y, int action) {
        Log.i("Touch Position", x+" "+y);
    }

    private class DayViewAdapter extends RecyclerView.Adapter{


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

            View view1 = layoutInflater.inflate(R.layout.event_element,viewGroup, false);

            return new RecyclerView.ViewHolder(view1) {

                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            TextView textView = viewHolder.itemView.findViewById(R.id.element_time);
            textView.setText(i+":00");
            viewHolder.itemView.setOnTouchListener(touchListener);
        }

        @Override
        public int getItemCount() {
            return 24;
        }
    }
}

interface TouchCallBack {
    void touchCallBack(float x, float y, int action);
}