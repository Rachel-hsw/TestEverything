package com.example.pc.testeverything.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by PC on 2018/7/13.
 */

public class RootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "hsww Complete", Toast.LENGTH_LONG).show();
        Log.i("hsw", "dfdsf");
    }

}