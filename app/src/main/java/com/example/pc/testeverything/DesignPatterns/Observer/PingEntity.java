package com.example.pc.testeverything.DesignPatterns.Observer;

/**
 * Created by Rachel on 2018/12/12.
 */

public class PingEntity {

    private boolean isSuccess;
    private long avgDelay;

    public long getAvgDelay() {
        return avgDelay;
    }

    public void setAvgDelay(long avgDelay) {
        this.avgDelay = avgDelay;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
