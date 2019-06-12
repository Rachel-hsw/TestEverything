package com.example.pc.testeverything;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ClickeableTextView extends AppCompatTextView {

    public ClickeableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

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
            Log.e("test_2", "onTouchEvent-Action:" + action + "," + event.getAction());
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
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
            Log.e("test_2", "dispatchTouchEvent-Action:" + action + "," + event.getAction());

        }

        return super.dispatchTouchEvent(event);
    }

}
