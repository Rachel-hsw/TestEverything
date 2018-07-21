package com.example.pc.testeverything.Activity.main;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;

import com.example.pc.testeverything.MyApplication;
import com.example.pc.testeverything.R;
import com.tiidian.threadmanager.ThreadListener;
import com.tiidian.threadmanager.ThreadManger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nothing
 * Time 2018/6/9
 * Info :語音播报订单
 */

public class SpeackUtils {
    private SoundPool mediaPlayer;
    private static SpeackUtils speackUtils;
    private Context context;
    private int soundID_1;

    /**
     * 获取单例
     *
     * @return
     */

    public static SpeackUtils getInstance() {
        if (speackUtils == null) {
            speackUtils = new SpeackUtils();
        }
        return speackUtils;
    }


    /**
     * 1、新建一个SoundPool实例。SoundPool(int maxStreams, int streamType, int srcQuality)在声音池中允许同时存在的声音数量,声音流的类型
     * 2、加载资源方法：soundID=public int load(Context context, int resId, int priority)context,想要加载的音效资源ID,音效播放时的优先级
     * 3、播放 public final int play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)soundID,左声道音量大小,右声道音量大小,优先级，值越大优先级越高,是否需要循环播放,
     * rate这个参数有点意思，可以理解为播放速率（就是快进、快退啥的），取值0.5f - 2.0f，其中0.5表示播放速度为正常的0.5倍。1表示正常速率播放。
     */
    public void init() {
        context = MyApplication.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            mediaPlayer = builder.setMaxStreams(11).build();
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer = new SoundPool(11, AudioManager.STREAM_MUSIC, 100);
        }
         soundID_1=mediaPlayer.load(context, R.raw.dingdong, 1);

    }
    public void play(){
        mediaPlayer.play(soundID_1, 0.8f, 0.8f,1, 1, 1.0f);

    }


}
