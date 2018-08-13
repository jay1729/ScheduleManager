package com.gvjay.schedulemanager;

import com.gvjay.schedulemanager.Database.ScheduledEvent;

public class CalendarData {
    public class DrawData {

        public int position;
        public DrawEvent[] events;

        public DrawData(int position, float[] startPointers, float[] endPointers, String[] titles){
            this.position = position;
            int len = startPointers.length;
            events = new DrawEvent[len];
            for(int i=0;i<len;i++){
                events[i] = new DrawEvent(startPointers[i], endPointers[i], titles[i]);
            }
        }
    }

    public class DrawEvent {
        public float startPointer;
        public float endPointer;
        public String eventTitle;

        public DrawEvent(float startPointer, float endPointer, String title){
            this.startPointer = startPointer;
            this.endPointer = endPointer;
            this.eventTitle = title;
        }
    }

    public static DrawData[] convertScheduledEvents(ScheduledEvent[] events){
        return null;
    }
}
