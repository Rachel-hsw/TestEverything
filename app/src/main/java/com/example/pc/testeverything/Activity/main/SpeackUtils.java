//package com.example.pc.testeverything.Activity.main;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.media.SoundPool;
//import android.os.Build;
//import android.os.LocaleList;
//import android.text.TextUtils;
//
//import com.example.pc.testeverything.MyApplication;
//import com.tiidian.threadmanager.ThreadListener;
//import com.tiidian.threadmanager.ThreadManger;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.concurrent.BlockingDeque;
//import java.util.concurrent.LinkedBlockingDeque;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by Nothing
// * Time 2018/6/9
// * Info :語音播报订单
// */
//
//public class SpeackUtils {
//
//    private static final char END_CONFIG = '%';//结束标志
//    private static final char END_ITEM = '&';//结束标志
//    private static final int END_ITEM_NUMBER = -1;
//    private SoundPool mediaPlayer;
//    private static SpeackUtils speackUtils;
//    private Context context;
//    private Map<Character, Integer> srcMap = new HashMap<>();
//    private Map<Character, Integer> srcMapEn = new HashMap<>();
//    private AudioManager am;
//    private float audioMaxVolumn;
//    private float volumnCurrent;
//    private float volumnRatio;
//    private long delay = 800;
//    private long itemEndDelay = 1500;
//    private long allEndDelay = 2000;
//    private static String SPEAKERUUID;
//
//    /**
//     * 设置播放间隔（过滤队列间隔）
//     *
//     * @param delay
//     */
//    public void setDelay(long delay) {
//        this.delay = delay;
//    }
//
//    /**
//     * 设置播放结束间隔（
//     *
//     * @param itemEndDelay
//     */
//    public void setItemEndDelay(long itemEndDelay) {
//        this.itemEndDelay = itemEndDelay;
//    }
//
//    /**
//     * 播放
//     *
//     * @param number
//     */
//    public void speakNumber(final String number) {
//        ShowManager.getInstance().getLog().i(getClass(), "SpeakUtils speakNumber", "正在设置播放数据：" + number);
//        ScheduledThreadManager.get().doDelayOnce(new Runnable() {
//            @Override
//            public void run() {
//                List<Integer> numberList = getSpeakList(number);
//                for (int i = 0; i < numberList.size(); i++) {
//                    try {
//                        queue.put(numberList.get(i));
//                    } catch (InterruptedException e) {
//                        ShowManager.getInstance().getLog().e(getClass(), "speakNumber", "添加语音失败", e);
//                        continue;
//                    }
//                }
//            }
//        }, 0, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 获取单例
//     *
//     * @return
//     */
//
//    public static SpeackUtils getInstance() {
//        if (speackUtils == null) {
//
//            speackUtils = new SpeackUtils();
//            speackUtils.init();
//        }
//        return speackUtils;
//    }
//
//    long sleepTime = 0;
//    private ThreadListener threadListener = new ThreadListener() {
//        @Override
//        public void doAction() throws Exception {
//            while (true) {
//                try {
//                    final int number = queue.take();
//                    ShowManager.getInstance().doInUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (number == END_ITEM_NUMBER) return;
//                            if (mediaPlayer == null) return;
//                            ShowManager.getInstance().getLog().i(getClass(), "SpeakUtils run", "开始播放" + number);
//                            mediaPlayer.play(number, 1f, 1f, 1, 0, 1);
//                        }
//                    }, sleepTime);
//                    if (number == srcMapEn.get(END_CONFIG)) {
//                        try {
//                            Thread.sleep(sleepTime + itemEndDelay);
//                            sleepTime = 0;
//                        } catch (InterruptedException e) {
//                            ShowManager.getInstance().getLog().e(getClass(), "SpeakUtils run", "语音停顿异常" + number, e);
//                        }
//                        continue;
//                    } else if (number == srcMap.get(END_CONFIG)) {
//                        sleepTime += itemEndDelay;
//                    } else {
//                        sleepTime += delay;
//                    }
//                } catch (Exception e) {
//                    continue;
//                }
//            }
//        }
//    };
//
//    private void init() {
//        context = MyApplication.getContext();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            SoundPool.Builder builder = new SoundPool.Builder();
//            mediaPlayer = builder.setMaxStreams(11).build();
//        }
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
//            mediaPlayer = new SoundPool(11, AudioManager.STREAM_MUSIC, 100);
//        }
//        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
//        volumnCurrent = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
//        volumnRatio = volumnCurrent / audioMaxVolumn;
//        Locale locale;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            locale = LocaleList.getDefault().get(0);
//        } else locale = Locale.getDefault();
//        String language = locale.getLanguage() + "-" + locale.getCountry();
////        if (language.equals("en-US")) {
//        initSrcEn();
////        } else {
//        initSrcCh();
////        }
//        if (!TextUtils.isEmpty(SPEAKERUUID)) {
//            ThreadManger.get().cancel(SPEAKERUUID);
//        }
//        if (threadListener != null) {
//            SPEAKERUUID = ThreadManger.get().add(threadListener);
//
//        }
//    }
//
//
//}
