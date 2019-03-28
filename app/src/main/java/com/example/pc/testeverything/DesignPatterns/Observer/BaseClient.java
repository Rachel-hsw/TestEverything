package com.example.pc.testeverything.DesignPatterns.Observer;

import com.tiidian.log.LogManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class BaseClient extends Observable {

    public static final String Client_NAME = "BaseClient";

    private static final OkHttpClient mOkHttpClient;

    public BaseClient() {
        bind();
    }

    private void bind() {
        List<NetObserver> netObserverList = MonitorManager.get().getNetObserverList();
        if (netObserverList != null && netObserverList.size() != 0) {
            for (NetObserver observer : netObserverList) {
                this.addObserver(observer);
            }
        }
    }

    static {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS).build();
    }

    /**
     * 执行远程方法
     *
     * @return 返回结果
     */
    public void invoke() {
        try {
        } catch (Exception e) {
            // 检查网络状态
            if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                LogManager.get().getLogger(BaseClient.class).info("网络，无数据返回");
                checkNetwork();
            }
        }
    }

    private void checkNetwork() {
        setChanged();
        notifyObservers();
    }
}