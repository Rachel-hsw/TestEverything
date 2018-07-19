package com.tiidian.threadmanager;

/**
 * Created by skyshi on 2017/1/18 0018.
 */

public abstract class ThreadListener extends ThreadListenerByCrash {

    public ThreadListener(String... data) {
        super(data);
    }

    public ThreadListener(Object... data) {
        super(data);
    }

    @Override
    public void exceptionCallBack() throws Exception {
    }
}
