package com.example.pc.testeverything.DesignPatterns.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rachel on 2018/11/30.
 */

public class MonitorManager {
    private static MonitorManager instance = new MonitorManager();
    private List<NetObserver> netObserverList;

    private MonitorManager() {
        netObserverList = new ArrayList<>();
    }

    public static MonitorManager get() {
        if (instance == null) {
            instance = new MonitorManager();
        }
        return instance;
    }

    public void addObserver(NetObserver mNetObserver) {
        netObserverList.add(mNetObserver);
    }

    public List<NetObserver> getNetObserverList() {
        return netObserverList;
    }

    public void setNetObserverList(List<NetObserver> netObserverList) {
        this.netObserverList = netObserverList;
    }
}
