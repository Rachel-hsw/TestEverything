package com.example.pc.testeverything.DesignPatterns.Observer;

import android.text.TextUtils;
import android.util.Log;

import com.tiidian.log.LogManager;
import com.tiidian.threadmanager.ThreadListener;
import com.tiidian.threadmanager.ThreadManger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rachel on 2018/11/29.
 */

public class NetManager {
    private LinkedBlockingQueue<Date> networkQueue;
    private static NetManager instance = new NetManager();
    private final static int NORMAL_PING = 1;
    private final static int TEST_PING = 2;
    public final static int CHECK_NETWORK_BY_USER = 1;
    public final static int CHECK_NETWORK_AUTO = 2;
    private final static String TAG = "NetManager";

    private NetObserver mNetObserver = new NetObserver() {
        @Override
        public void notify(Object data) {
            addCheck();
        }
    };

    public static NetManager getInstance() {
        if (instance == null) {
            instance = new NetManager();
        }
        return instance;
    }

    public void addCheck() {
        try {
            networkQueue.add(new Date());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private NetManager() {
        networkQueue = new LinkedBlockingQueue<>(100);
        MonitorManager.get().addObserver(mNetObserver);
        ThreadManger.get().add(new ThreadListener() {
            @Override
            public void doAction() throws Exception {
                while (true) {
                    try {
                        while (networkQueue.size() > 1) {
                            networkQueue.take();
                        }
                        networkQueue.take();
                        checkNetWork(CHECK_NETWORK_AUTO);
                    } catch (Exception e) {
                        LogManager.get().getLogger(NetManager.class).warn("检查网络状态逻辑出现异常", e);
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            checkNetWork(CHECK_NETWORK_AUTO);
                        } catch (Exception bindE) {
                            LogManager.get().getLogger(NetManager.class).warn("检查网络状态逻辑出现异常", bindE);
                        }
                    }
                }
            }
        });
    }

    /**
     * 检查网络
     *
     * @param type 1用户手动点击 2自动
     */
    public synchronized void checkNetWork(int type) {
        try {
            PingEntity delayDNS;
            PingEntity delayPingBaiDu = null;
            delayDNS = pingNet("114.114.114.114", NORMAL_PING);
            if (delayDNS.isSuccess()) {
                delayPingBaiDu = pingNet("www.baidu.com", NORMAL_PING);
            }
            if (!delayDNS.isSuccess()) {
                Log.i(TAG, "网络断开");
            } else if (delayPingBaiDu != null && !delayPingBaiDu.isSuccess()) {
                Log.i(TAG, "网络断开");
            } else if ((delayDNS != null && delayDNS.isSuccess()) && (delayPingBaiDu != null && delayPingBaiDu.isSuccess())) {
                Log.i(TAG, "网络状态良好");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.get().getLogger(NetManager.class).warn("检查网络捕获到异常", e);
        }
    }

    private PingEntity pingNet(String ip, int type) {
        PingEntity pingEntity = new PingEntity();

        if (TextUtils.isEmpty(ip)) {
            pingEntity.setSuccess(false);
            return pingEntity;
        }

        LogManager.get().getLogger(TAG.getClass()).info("开始Ping:" + ip);
        try {
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 10 -W 30 " + ip);

            InputStream streamErr = p.getErrorStream();
            long avgDelay = 0L;
            pingEntity.setSuccess(false);
            if (!TextUtils.isEmpty(getInputStream2String(streamErr))) {
                avgDelay = 9999L;
                LogManager.get().getLogger(getClass()).info("Ping:" + ip + ",ping不通");
            } else {
                String str = getInputStream2String(p.getInputStream());
                LogManager.get().getLogger(getClass()).info("Ping:" + ip + "的数据为：" + str);

                if (str.contains("avg")) {
                    String delay = str.split("avg")[1].split("=")[1].trim().split("/")[1];
                    String[] list = delay.split("\\.");
                    if (list.length > 1) {
                        avgDelay = Long.valueOf(list[0]);
                        pingEntity.setSuccess(true);
                    } else {
                        avgDelay = 9999L;
                    }
                }
            }
            pingEntity.setAvgDelay(avgDelay);
            if (type == TEST_PING) {
                pingEntity.setSuccess(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pingEntity;
    }

    /**
     * 根据服务器地址，获取PING的平均数据
     *
     * @param ip 服务器地址
     * @return PING的平均数据
     */
    private String getPingTimeResult(String ip) {
        String delay = "9999";

        LogManager.get().getLogger(getClass()).info("开始Ping:" + ip);
        try {
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 10 -W 30 " + ip);

            InputStream streamErr = p.getErrorStream();
            if (!TextUtils.isEmpty(getInputStream2String(streamErr))) {
                LogManager.get().getLogger(getClass()).info("Ping:" + ip + ",ping不通");
            } else {
                String str = getInputStream2String(p.getInputStream());
                LogManager.get().getLogger(getClass()).info("Ping:" + ip + "的数据为：" + str);

                if (str.contains("avg")) {
                    delay = str.split("avg")[1].split("=")[1].trim().split("/")[1];
                    String[] list = delay.split("\\.");
                    if (list.length > 1) {
                        delay = list[0];
                    } else {
                        return "9999";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return delay;
    }

    private String getInputStream2String(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            String str;
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            while ((str = buf.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return sb.toString();
    }
}
