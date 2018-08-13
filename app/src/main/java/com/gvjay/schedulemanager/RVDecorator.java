package com.gvjay.schedulemanager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class RVDecorator extends RecyclerView.ItemDecoration {

    private CalendarData.DrawData[] calendarData;
    private float elementHeight, elementWidth;
    private static final double EVENT_WIDTH_LEFT = 0.2;
    private static final double EVENT_WIDTH_RIGHT = 0.8;

    public RVDecorator(CalendarData.DrawData[] data){
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
            if(calendarData[adapterPos].events.length > 0){
                int n = calendarData[adapterPos].events.length;
                for(int j=0;j<n;j++){
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
                    c.drawRect((int) (elementWidth*EVENT_WIDTH_LEFT),
                            (int) (elementHeight*calendarData[adapterPos].events[j].startPointer),
                            (int) (elementWidth*EVENT_WIDTH_RIGHT),
                            (int) (elementHeight*calendarData[adapterPos].events[j].endPointer),
                            paint);
                }
            }
        }
    }
}