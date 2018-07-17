package com.example.pc.testeverything.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pc.testeverything.MyApplication;
import com.example.pc.testeverything.R;
import com.example.pc.testeverything.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 测试约束布局
 */

public class TestLayoutActivity extends AppCompatActivity {
    final static String SP_SET_CONFIG = "config";
    public static final String CURRENT_MODEL = "current_work";
    private SharedPreferences sp;
    private boolean click=false;
    private Button button;
    private String URL="http://192.168.3.5:2222/api/v1/order";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_two_constraintlayout);
     /*   sp = this.getSharedPreferences(SP_SET_CONFIG, Context.MODE_PRIVATE);*/
        sp=PreferenceManager.getDefaultSharedPreferences(this);
     /*   sp=this.getPreferences(MODE_MULTI_PROCESS);*/
        Log.i("当前值：",""+String.valueOf(sp.getInt(CURRENT_MODEL,6)));
        button= (Button) findViewById(R.id.img1);
        button.setText(String.valueOf(sp.getInt(CURRENT_MODEL,6)));
        //设置字体
        button.setTypeface(MyApplication.getTypefaceCaiYun());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.putInt(CURRENT_MODEL, 3);
                    editor.apply();
                    button.setText(String.valueOf(sp.getInt(CURRENT_MODEL,6)));
                    Log.i("当前值：",""+String.valueOf(sp.getInt(CURRENT_MODEL,6)));
                    click=false;
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.putInt(CURRENT_MODEL, 2);
                    editor.apply();
                    button.setText(String.valueOf(sp.getInt(CURRENT_MODEL,6)));
                    Log.i("当前值：",""+String.valueOf(sp.getInt(CURRENT_MODEL,6)));
                    click=true;
                }

            }
        });
/*        setContentView(R.layout.test_constraintlayout);*/
    }


}
