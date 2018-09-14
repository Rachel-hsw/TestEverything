package com.example.pc.testeverything.manager;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by PC on 2018/7/19.
 */

public interface MqttConnectCallback {
    /**
     * 连接状态
     *
     * @param isSuccess 连接是否成功
     * @param msg
     */
    void connectCallback(boolean isSuccess, String msg);


    void onMessageArrivedCallback(String topic, MqttMessage message);


    void onClientExist(MqttEntity mqttEntity);
}
