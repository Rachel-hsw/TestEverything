package com.example.pc.testeverything.DesignPatterns.Observer;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Rachel on 2018/11/30.
 * https://www.jianshu.com/p/cd3557b1a474
 */

public abstract class NetObserver implements Observer {
    public abstract void notify(Object data);

    @Override
    public void update(Observable observable, Object data) {
        this.notify(data);
    }
}
