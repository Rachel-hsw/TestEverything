package com.example.pc.testeverything;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class ClickableLinearLayout extends LinearLayout {

    public ClickableLinearLayout(Context context) {
        super(context);
    }

    public ClickableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClickableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        String action = "";
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
//            case MotionEvent.ACTION_MOVE:
//                action="ACTION_MOVE";
//                break;
        }
        if (!action.equals("")) {
            Log.e("test_3", "dispatchTouchEvent:" + ",Action:" + action + "," + ev.getAction());
        }
        return super.dispatchTouchEvent(ev);
    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        String action = "";
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                action="ACTION_DOWN";
//                break;
//            case MotionEvent.ACTION_UP:
//                action="ACTION_UP";
//                break;
//            case MotionEvent.ACTION_MOVE:
//                action="ACTION_MOVE";
//                break;
//        }
//        Log.i("test_3","onInterceptTouchEvent:"+super.onInterceptTouchEvent(ev)+",Action:"+action+","+ev.getAction());
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
//            case MotionEvent.ACTION_MOVE:
//                action="ACTION_MOVE";
//                break;
        }
        if (!action.equals("")) {
            Log.e("test_3", "onTouchEvent-Action:" + action + "," + event.getAction());
        }
        return super.onTouchEvent(event);
    }
}
