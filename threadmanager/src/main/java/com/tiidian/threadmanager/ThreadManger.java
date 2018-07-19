package com.tiidian.threadmanager;

import android.text.TextUtils;
import android.util.Log;

import com.tiidian.threadmanager.eb.EBThreadPoolEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by skyshi on 2017/1/11 0011.
 */
public class ThreadManger {

    private static final String EVENT_BUS_THREADPOOL_DELETE_EVENT = "deleteevent";
    private static ThreadManger instance;
    private ExecutorService service;
    private HashMap<String, ThreadListenerByCrash> listeners;
    private HashMap<String, Future> cancelListener;

    private ThreadManger() {
        service = Executors.newCachedThreadPool();
        listeners = new HashMap<>();
        cancelListener = new HashMap<>();

        EventBus.getDefault().register(this);
    }

    public static ThreadManger get() {
        if (instance == null) {
            instance = new ThreadManger();
        }

        return instance;
    }

    public void close() {
        service.shutdownNow();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void eventBusCall(EBThreadPoolEntity entity) {
        switch (entity.getType()) {
            case EVENT_BUS_THREADPOOL_DELETE_EVENT:
                if (!TextUtils.isEmpty(entity.getUuid())) {
                    removeRecord(entity.getUuid());
                }
                break;
        }
    }

    /**
     * 根据线程编号取消线程
     *
     * @param uuid 线程编号
     */
    public void cancel(String uuid) {
        Future future = cancelListener.get(uuid);
        if (future != null) {
            future.cancel(true);

            // 清空线程数据
            EBThreadPoolEntity ebThreadPoolEntity = new EBThreadPoolEntity(EVENT_BUS_THREADPOOL_DELETE_EVENT);
            ebThreadPoolEntity.setUuid(uuid);
            EventBus.getDefault().post(ebThreadPoolEntity);
        }
    }

    /**
     * 增加需要在子线程完成为任务
     *
     * @param listener 需要在子线程上的工作（回调）
     * @return 唯一识别码
     */
    public String add(ThreadListenerByCrash listener) {
        if (listener == null) {
            return null;
        }

        String uuid = getUUID();
        listeners.put(uuid, listener);
        startThread(uuid);

        return uuid;
    }

    private void startThread(final String uuid) {
        Future future = service.submit(new Runnable() {
            @Override
            public void run() {
                ThreadListenerByCrash listener = listeners.get(uuid);
                try {
                    if (!listener.isStart) {
                        listener.isStart = true;
                        if (!listener.canContinue()) {
                            Log.e("ThreadManger", "当前线程中断");
                            return;
                        }

                        listener.doAction();
                        if (!listener.canContinue()) {
                            Log.e("ThreadManger", "当前线程中断");
                            return;
                        }

                        listener.isEnd = true;
                        EBThreadPoolEntity ebThreadPoolEntity = new EBThreadPoolEntity(EVENT_BUS_THREADPOOL_DELETE_EVENT);
                        ebThreadPoolEntity.setUuid(uuid);
                        EventBus.getDefault().post(ebThreadPoolEntity);
                    }
                } catch (Exception e) {
                    Log.e("ThreadManger", "当前线程中断");
                    e.printStackTrace();
                    try {
                        listener.exceptionCallBack();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        cancelListener.put(uuid, future);
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private void removeRecord(String uuid) {
        listeners.remove(uuid);
        cancelListener.remove(uuid);
    }
}
