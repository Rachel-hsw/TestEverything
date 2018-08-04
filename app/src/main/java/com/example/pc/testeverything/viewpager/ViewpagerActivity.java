package com.example.pc.testeverything.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.example.pc.testeverything.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */

public class ViewpagerActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_layout);
        setVp();
    }

    private void setVp() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add("第" + i + "个View");
        }

        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new MyPagerAdapter(this, list));
    }
}
