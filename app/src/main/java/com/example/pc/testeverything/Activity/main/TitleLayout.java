package com.example.pc.testeverything.Activity.main;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.pc.testeverything.R;

/**
 * Created by PC on 2018/7/17.
 */

public class TitleLayout  extends LinearLayout implements View.OnClickListener {


    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_actionbar_layout,this);
        ImageButton back= (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((Activity)getContext()).finish();
    }
}
