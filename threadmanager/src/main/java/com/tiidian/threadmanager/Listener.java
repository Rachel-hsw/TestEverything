package com.tiidian.threadmanager;

/**
 * Created by skyshi on 2017/1/11 0011.
 */
public interface Listener {

    void doAction() throws Exception;

    void exceptionCallBack() throws Exception;
}
