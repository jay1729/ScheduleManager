package com.gvjay.schedulemanager;

import android.util.Log;

import com.gvjay.schedulemanager.Database.ScheduledEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CalendarData {
    public class DrawData {

        public int position;
        public ArrayList<DrawEvent> events;

        public DrawData(int position){
            this.position = position;
            this.events = new ArrayList<DrawEvent>();
        }

        public DrawData(int position, double[] startPointers, double[] endPointers, String[] titles){
            this.position = position;
            int len = startPointers.length;
            for(int i=0;i<len;i++){
                events.add(new DrawEvent(startPointers[i], endPointers[i], titles[i]));
            }
        }
    }

    public class DrawEvent {
        public double startPointer;
        public double endPointer;
        public String eventTitle;

        public DrawEvent(double startPointer, double endPointer, String title){
            this.startPointer = startPointer;
            this.endPointer = endPointer;
            this.eventTitle = title;
        }
    }

    public ArrayList<DrawData> convertScheduledEvents(ArrayList<ScheduledEvent> events){
        ArrayList<DrawData> output = new ArrayList<DrawData>();
        for(int i=0;i<24;i++) output.add(new DrawData(i));
        Collections.sort(events, new EventsComprtr());
        int n_events = events.size();
        for(int i=0;i<n_events;i++){
            ScheduledEvent event = events.get(i);
            long fromDate = event.fromDate.getTime() - (event.fromDate.getTimezoneOffset()*60*1000);
            long toDate = event.toDate.getTime() - (event.toDate.getTimezoneOffset()*60*1000);
            int position = (int) ((fromDate % DateUtils.DAY_IN_MILLIS) / DateUtils.HOUR_IN_MILLIS);
            while(true){
                double endPosition = ((double) (toDate % DateUtils.HOUR_IN_MILLIS))/((double) DateUtils.HOUR_IN_MILLIS);
                double startPosition = ((double) (fromDate % DateUtils.HOUR_IN_MILLIS))/((double) DateUtils.HOUR_IN_MILLIS);
                if((toDate - fromDate) > DateUtils.HOUR_IN_MILLIS){
                    endPosition = 1.0;
                }
                output.get(position).events.add(new DrawEvent(startPosition, endPosition, event.title));
                if((toDate - fromDate) > DateUtils.HOUR_IN_MILLIS){
                    fromDate += DateUtils.HOUR_IN_MILLIS - (fromDate % DateUtils.HOUR_IN_MILLIS);
                    position++;
                    continue;
                }
                break;
            }
        }
        return output;
    }

    private class EventsComprtr implements Comparator<ScheduledEvent>{

        @Override
        public int compare(ScheduledEvent e1, ScheduledEvent e2) {

            return (e1.fromDate.getTime() % DateUtils.DAY_IN_MILLIS) < (e2.fromDate.getTime() % DateUtils.DAY_IN_MILLIS) ? -1 : 1;
        }
    }
}
