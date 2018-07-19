package com.tiidian.threadmanager;

/**
 * Created by skyshi on 2017/1/18 0018.
 */

public abstract class ThreadListenerByCrash implements Listener {

    protected String[] dataString;
    protected Object[] dataObject;
    boolean isStart = false, isEnd = false;

    public ThreadListenerByCrash(String... data) {
        this.dataString = data;
    }

    public ThreadListenerByCrash(Object... data) {
        dataObject = data;
    }

    public String[] getStringData() {
        return dataString;
    }

    public Object[] getObjectData() {
        return dataObject;
    }

    protected boolean canContinue() {
        return !Thread.currentThread().isInterrupted();
    }
}
