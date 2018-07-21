package com.example.pc.testeverything;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.example.pc.testeverything.Activity.main.SpeackUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by PC on 2018/5/24.
 */

public class MyApplication extends Application {

    private static Context context;
    private static Typeface simkai;
    private static Typeface simhei;
    private static Typeface simsun;
    private static Typeface simsiyuan;
    private static Typeface simcaiyun;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
       //得到AssetManager
        AssetManager mgr = getAssets();
        /* //根据路径得到Typeface-楷体
        simkai = Typeface.createFromAsset(mgr, "simkai.ttf");
        //根据路径得到Typeface-黑体
        simhei = Typeface.createFromAsset(mgr, "simhei.ttf");
        //根据路径得到Typeface-宋体
        simsun = Typeface.createFromAsset(mgr, "simsun.ttc");*/
        //根据路径得到Typeface-思源黑体
        simsiyuan = Typeface.createFromAsset(mgr, "simsiyuan.ttf");
        //根据路径得到Typeface-华文彩云
        simcaiyun = Typeface.createFromAsset(mgr, "simcaiyun.ttf");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("simkai.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        SpeackUtils.getInstance().init();

    }
    public static Context getContext() {
        return context;
    }
    public static Typeface getTypefaceKai() {
        return simkai;
    }
    public static Typeface getTypefaceHei() {
        return simhei;
    }
    public static Typeface getTypefaceSun() {
        return simsun;
    }
    public static Typeface getTypefaceSiYuan() {
        return simsiyuan;
    }
    public static Typeface getTypefaceCaiYun() {
        return simcaiyun;
    }

}