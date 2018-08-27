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


    //监听的数据 cmd data 门店配置basicconfig 菜品基础数据menubasic 尺寸menusize 售罄数据menusold
    void onCmdMessageArrivedCallback(String message);

    void onBasicConfigMessageArrivedCallback(String message);

    void onDataMessageArrivedCallback(String message);

    void onMenuBasicMessageArrivedCallback(String message);

    void onMenusizeMessageArrivedCallback(String message);

    void onMenusoldMessageArrivedCallback(String message);

}
