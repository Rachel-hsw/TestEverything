package com.example.pc.testeverything;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by PC on 2018/5/30.
 */

public class ActivityDialog extends Activity {


    private TextView bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_dialog);
        bt = (TextView) findViewById(R.id.bt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityDialog.this,"后台下载中",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


}