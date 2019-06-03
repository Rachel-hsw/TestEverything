package com.example.pc.testeverything.utils;

public class BaseRunnable implements Runnable {
    private Object object;

    public BaseRunnable(Object object) {
        this.object = object;
    }

    public BaseRunnable() {
    }

    @Override
    public void run() {
        run(object);
    }

    public void run(Object object) {

    }
}
