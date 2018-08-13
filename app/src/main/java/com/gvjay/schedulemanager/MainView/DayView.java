package com.gvjay.schedulemanager.MainView;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gvjay.schedulemanager.Database.*;
import com.gvjay.schedulemanager.R;

import java.util.Calendar;
import java.util.Date;

public class DayView extends MainFragContent {

    private View view;
    private int offset;
    private ViewGroup container;
    private LayoutInflater inflater;
    private Calendar calendar;
    private Date currentDate;
    private DayViewAdapter adapter;
    private ScheduledEvent[] data;
    private EventDBHandler dbHandler;

    public DayView(ViewGroup container, int offset, LayoutInflater inflater){
        this.container = container;
        this.offset = offset;
        this.inflater = inflater;
        this.calendar = Calendar.getInstance();
        this.currentDate = this.calendar.getTime();
        dbHandler = new EventDBHandler(container.getContext());
    }

    private void loadData(){
        data = dbHandler.getEventsOnDate(currentDate);
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

        RecyclerView recyclerView = view.findViewById(R.id.eventsView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        loadData();
        adapter = new DayViewAdapter();
        recyclerView.setAdapter(adapter);

        return view;
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

        }

        @Override
        public int getItemCount() {
            return 24;
        }
    }
}
