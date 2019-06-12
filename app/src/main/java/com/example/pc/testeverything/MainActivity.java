package com.example.pc.testeverything;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * public static final int ACTION_DOWN             = 0;
 * public static final int ACTION_UP               = 1;
 * public static final int ACTION_MOVE             = 2;
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* TextView tv= (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(this);
        tv.setOnTouchListener(this);*/
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
            Log.i("test_1", "dispatchTouchEvent-Action" + action + "," + ev.getAction());
        }
        return super.dispatchTouchEvent(ev);
    }


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
            Log.e("test_1", "onTouchEvent-Action:" + action + "," + event.getAction());
        }
        return super.onTouchEvent(event);
    }

/*    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String action = "";
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                action="ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                action="ACTION_UP";
                break;
            case MotionEvent.ACTION_MOVE:
                action="ACTION_MOVE";
                break;
        }
        Log.e("test_1","onTouch,Action:"+action+","+event.getAction());
        return false;
    }

    @Override
    public void onClick(View v) {
        Log.e("test_1","onclick,Action:");
    }*/
}