package com.example.pc.testeverything.Activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pc.testeverything.R;

/**
 * Created by PC on 2018/7/26.
 */

public class BraodCastActivity extends Activity {

    private IntentFilter intentFilter;

    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter();
    /*    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");*/
        intentFilter.addCategory("android.intent.category.APP_CALCULATOR");
//        intentFilter.addCategory(Intent.CATEGORY_APP_EMAIL);
        networkChangeReceiver = new NetworkChangeReceiver();
        intentFilter.addAction("android.intent.category.APP_EMAIL");
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }


    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "network changes", Toast.LENGTH_SHORT).show();
            Log.i("hsw", "dfdsf");
        }

    }

}
