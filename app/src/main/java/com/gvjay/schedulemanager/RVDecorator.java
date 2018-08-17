package com.gvjay.schedulemanager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class RVDecorator extends RecyclerView.ItemDecoration {

    private ArrayList<CalendarData.DrawData> calendarData;
    private float elementHeight, elementWidth;
    private static final double EVENT_WIDTH_LEFT = 0.2;
    private static final double EVENT_WIDTH_RIGHT = 0.8;

    public RVDecorator(ArrayList<CalendarData.DrawData> data){
        calendarData = data;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        int count = parent.getChildCount();
        elementHeight = parent.getChildAt(0).getHeight();
        elementWidth = parent.getChildAt(0).getWidth();

        for(int i=0;i<count;i++){
            View childView = parent.getChildAt(i);
            int adapterPos = parent.getChildAdapterPosition(childView);
            if(calendarData.get(adapterPos).events.size() > 0){
                int n = calendarData.get(adapterPos).events.size();
                for(int j=0;j<n;j++){
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
                    c.drawRect((int) (elementWidth*EVENT_WIDTH_LEFT),
                            (int) (elementHeight*calendarData.get(adapterPos).events.get(j).startPointer) + childView.getTop(),
                            (int) (elementWidth*EVENT_WIDTH_RIGHT),
                            (int) (elementHeight*calendarData.get(adapterPos).events.get(j).endPointer + childView.getTop()),
                            paint);
                }
            }
        }
    }

    public int isEventTouched(float x, float y, int adapterPos, View childView){
        int len = calendarData.get(adapterPos).events.size();

        for(int i=0;i<len;i++){
            CalendarData.DrawEvent drawEvent = calendarData.get(adapterPos).events.get(i);
            if((x > (elementWidth*EVENT_WIDTH_LEFT))
                    && (x < (elementWidth*EVENT_WIDTH_RIGHT))
                    && (y > ((elementHeight*drawEvent.startPointer)))
                    && (y < (elementHeight*drawEvent.endPointer))){
                return drawEvent.ID;
            }
        }
        return -1;
    }
}
