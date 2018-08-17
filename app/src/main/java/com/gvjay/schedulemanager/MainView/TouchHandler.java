package com.gvjay.schedulemanager.MainView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchHandler implements View.OnTouchListener {

    public static int ACTION_CLICK = 0;
    public static int ACTION_LONG_CLICK = 1;

    private TouchCallBack cb;
    private long downTime;
    private long upTime;

    public TouchHandler(TouchCallBack cb){
        this.cb = cb;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                downTime = motionEvent.getEventTime();
                return true;

            case MotionEvent.ACTION_UP:
                upTime = motionEvent.getEventTime();
                Log.i("Event Time", (upTime-downTime)+"");
                this.cb.touchCallBack(motionEvent.getX(), motionEvent.getY(), ACTION_CLICK);
                return false;
            case MotionEvent.ACTION_CANCEL:
                return false;
            default:
                return false;
        }
    }
}
