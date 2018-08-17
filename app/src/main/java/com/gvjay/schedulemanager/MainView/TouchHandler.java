package com.gvjay.schedulemanager.MainView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchHandler implements View.OnTouchListener {

    public static int ACTION_CLICK = 0;
    public static int ACTION_LONG_CLICK = 1;
    private static long CLICK_VS_LONG_CLICK = 500; // distinguish btw click and long click

    private TouchCallBack cb;
    private long downTime;
    private float upX, upY, downX, downY;
    private long upTime;
    private RecyclerView recyclerView = null;

    public TouchHandler(TouchCallBack cb){
        this.cb = cb;
    }

    public TouchHandler(TouchCallBack cb, RecyclerView recyclerView){
        this.cb = cb;
        this.recyclerView = recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                downTime = motionEvent.getEventTime();
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                return true;

            case MotionEvent.ACTION_UP:
                int currentAction;
                upTime = motionEvent.getEventTime();
                Log.i("Event Time", (upTime-downTime)+"");
                upX = motionEvent.getX();
                upY = motionEvent.getY();
                if((downX != upX) || (downY != upY)) return false;
                if((upTime - downTime) < CLICK_VS_LONG_CLICK){
                    currentAction = ACTION_CLICK;
                }else currentAction = ACTION_LONG_CLICK;
                if(recyclerView != null){
                    cb.touchCallBack(upX, upY, currentAction, recyclerView.getChildAdapterPosition(view));
                }else cb.touchCallBack(upX, upY, currentAction, -1);
                return false;
            case MotionEvent.ACTION_CANCEL:
                return false;
            default:
                return false;
        }
    }
}
