package com.example.pc.testeverything;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

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
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void uncaughtException(Thread thread, final Throwable ex) {
            String exMessage = "";
            try {
                exMessage = printExceptionInfo(ex);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    JSONArray s = new JSONArray(ex.getStackTrace());
                    exMessage = s.toString();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            String exType = ex.toString() != null ? ex.toString() : "unknow exType";
            Log.e("应用Crash", exType + "|" + exMessage);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initUncaughtException();
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

    /**
     * init uncaught exception
     */
    private void initUncaughtException() {
        // 程序崩溃时触发线程
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
    }


    private String printExceptionInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        return writer.toString();
    }
}