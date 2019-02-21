package com.example.pc.testeverything;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.example.pc.testeverything.Activity.main.SpeackUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePalApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by PC on 2018/5/24.
 */

public class MyApplication extends LitePalApplication {

    private static Context context;
    private static Typeface simkai;
    private static Typeface simhei;
    private static Typeface simsun;
    private static Typeface simsiyuan;
    private static Typeface simcaiyun;
    private static Typeface berirut;
    private static Typeface notosanshans;
    private static Typeface sourcehansanscnbold;
    private static Typeface sourcehansanscnheavy;
    private static Typeface sourcehansanscnnormal;
    //LeakCanary使用详解
    private RefWatcher refWatcher;

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher() {
        MyApplication leakApplication = (MyApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        refWatcher = setupLeakCanary();
       //得到AssetManager
        AssetManager mgr = getAssets();
        //根据路径得到Typeface-楷体
        simkai = Typeface.createFromAsset(mgr, "simkai.ttf");
        //根据路径得到Typeface-黑体
        simhei = Typeface.createFromAsset(mgr, "simhei.ttf");
        //根据路径得到Typeface-宋体
        simsun = Typeface.createFromAsset(mgr, "simsun.ttc");
        //根据路径得到Typeface-思源黑体
        simsiyuan = Typeface.createFromAsset(mgr, "simsiyuan.ttf");
        //根据路径得到Typeface-华文彩云
        simcaiyun = Typeface.createFromAsset(mgr, "simcaiyun.ttf");
        //根据路径得到Typeface-berirut
        berirut = Typeface.createFromAsset(mgr, "Beirut.ttc");
        //根据路径得到Typeface-思源黑体   NotoSansHans-Black
        notosanshans = Typeface.createFromAsset(mgr, "NotoSansHans-Black.otf");
        //根据路径得到Typeface-思源黑体粗体 SourceHanSansCN-Bold
        sourcehansanscnbold = Typeface.createFromAsset(mgr, "SourceHanSansCN-Bold.otf");
        //根据路径得到Typeface-SourceHanSansCN-Heavy
        sourcehansanscnheavy = Typeface.createFromAsset(mgr, "SourceHanSansCN-Heavy.otf");
        //根据路径得到Typeface-SourceHanSansCN-Normal
        sourcehansanscnnormal = Typeface.createFromAsset(mgr, "SourceHanSansCN-Normal.otf");



        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("simcaiyun.ttf")
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

    public static Typeface getBerirut() {
        return berirut;
    }

    public static void setBerirut(Typeface berirut) {
        MyApplication.berirut = berirut;
    }

    public static Typeface getNotosanshans() {
        return notosanshans;
    }

    public static void setNotosanshans(Typeface notosanshans) {
        MyApplication.notosanshans = notosanshans;
    }

    public static Typeface getSourcehansanscnbold() {
        return sourcehansanscnbold;
    }

    public static void setSourcehansanscnbold(Typeface sourcehansanscnbold) {
        MyApplication.sourcehansanscnbold = sourcehansanscnbold;
    }

    public static Typeface getSourcehansanscnheavy() {
        return sourcehansanscnheavy;
    }

    public static void setSourcehansanscnheavy(Typeface sourcehansanscnheavy) {
        MyApplication.sourcehansanscnheavy = sourcehansanscnheavy;
    }

    public static Typeface getSourcehansanscnnormal() {
        return sourcehansanscnnormal;
    }

    public static void setSourcehansanscnnormal(Typeface sourcehansanscnnormal) {
        MyApplication.sourcehansanscnnormal = sourcehansanscnnormal;
    }
}