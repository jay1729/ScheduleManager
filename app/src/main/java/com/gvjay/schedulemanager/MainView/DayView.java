package com.gvjay.schedulemanager.MainView;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gvjay.schedulemanager.CalendarData;
import com.gvjay.schedulemanager.Database.*;
import com.gvjay.schedulemanager.EventDialog.DeleteEventDialog;
import com.gvjay.schedulemanager.EventDialog.DescriptiveDialog;
import com.gvjay.schedulemanager.NotifyDataChanged;
import com.gvjay.schedulemanager.R;
import com.gvjay.schedulemanager.RVDecorator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DayView extends MainFragContent implements TouchCallBack, NotifyDataChanged {

    private View view;
    private int offset;
    private ViewGroup container;
    private LayoutInflater inflater;
    private Calendar calendar;
    private Date currentDate;
    private DayViewAdapter adapter;
    private ArrayList<ScheduledEvent> data;
    private EventDBHandler dbHandler;
    private TouchHandler touchListener;
    private RecyclerView recyclerView;
    private RVDecorator itemDecorator;

    public DayView(ViewGroup container, int offset, LayoutInflater inflater){
        this.container = container;
        this.offset = offset;
        this.inflater = inflater;
        this.calendar = Calendar.getInstance();
        long offsetDays = offset;
        offsetDays *= DateUtils.DAY_IN_MILLIS;
        this.calendar.setTimeInMillis(this.calendar.getTimeInMillis()+offsetDays);
        this.currentDate = this.calendar.getTime();
        dbHandler = new EventDBHandler(container.getContext());
        touchListener = new TouchHandler(this);
    }

    private void loadData(){
        data = dbHandler.getEventsOnDate(currentDate);
        int len = data.size();
        for(int i=0;i<len;i++){
            Log.i("id", data.get(i).ID+"");
            Log.i("frequency", data.get(i).frequency);
            Log.i("from", data.get(i).fromDate.toString());
            Log.i("to", data.get(i).toDate.toString());
            Log.i("day", data.get(i).classDayOfWeek + "");
        }
    }

    private ScheduledEvent findEventById(int ID){
        int len = data.size();
        for(int i=0;i<len;i++){
            if(data.get(i).ID == ID){
                return data.get(i);
            }
        }
        return null;
    }

    @Override
    public View getView() {
        view = inflater.inflate(R.layout.day_fragment, container, false);
        TextView dayValue = view.findViewById(R.id.day_value);
        TextView dateValue = view.findViewById(R.id.date_value);

        String[] DAY_VALUES = container.getResources().getStringArray(R.array.day_values);

        dayValue.setText(DAY_VALUES[calendar.get(Calendar.DAY_OF_WEEK)-1]);
        dateValue.setText(calendar.getTime().toString().substring(4,10));

        recyclerView = view.findViewById(R.id.eventsView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        loadData();
        adapter = new DayViewAdapter();
        recyclerView.setAdapter(adapter);
        CalendarData cd = new CalendarData();
        itemDecorator = new RVDecorator(cd.convertScheduledEvents(this.data));
        recyclerView.addItemDecoration(itemDecorator);
        touchListener.setRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void touchCallBack(float x, float y, int action, int adapterPos) {
        Log.i("Touch Position", x+" "+y);
        int eventID = itemDecorator.isEventTouched(x, y, adapterPos, recyclerView.getChildAt(adapterPos));
        if(eventID > 0){
            ScheduledEvent event = findEventById(eventID);
            Log.i("Event is", event.fromDate.toString());
            if(action == TouchHandler.ACTION_CLICK){
                DescriptiveDialog descriptiveDialog = new DescriptiveDialog(view.getContext(), event);
                descriptiveDialog.show();
            }else if(action == TouchHandler.ACTION_LONG_CLICK){
                DeleteEventDialog eventDialog = new DeleteEventDialog(view.getContext(), event, this);
                eventDialog.show();
            }

        }
    }

    @Override
    public void notifyDataChanged() {
        loadData();
        recyclerView.removeItemDecoration(itemDecorator);
        CalendarData cd = new CalendarData();
        itemDecorator = new RVDecorator(cd.convertScheduledEvents(this.data));
        recyclerView.addItemDecoration(itemDecorator);
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
    void touchCallBack(float x, float y, int action, int adapterPos);
}
