package com.example.pc.testeverything.manager;

import android.content.Context;

import com.tiidian.log.LogManager;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PC on 2018/8/24.
 */

public class MqttUsageMode implements MqttConnectCallback {
    List<String> clientIdList;
    List<MqttAndroidClient> MqttAndroidClientList;
    boolean isIdExit;
    public static MqttUsageMode instance;

    private MqttUsageMode() {
        clientIdList = new ArrayList<>();
        MqttAndroidClientList = new ArrayList<>();
    }

    public static MqttUsageMode get() {
        if (instance == null) {
            instance = new MqttUsageMode();
        }
        return instance;
    }

    /**
     * 开启一个新的Mqtt订阅
     *
     * @param serverURI
     * @param clientId
     * @param context
     */
    private void startNewMqttManager(String serverURI, String clientId, Context context) {
        isIdExit = false;
        for (String id : clientIdList) {
            if (id.equals(clientId)) {
                isIdExit = true;
            }
        }
        if (!isIdExit) {
            clientIdList.add(clientId);
            MqttEnity mqttEnity = new MqttEnity();
            mqttEnity.setContext(context);
            mqttEnity.setClientId(clientId);
            mqttEnity.setServerURI(serverURI);
            mqttEnity.setCleanSession(DeviceManager.get().getCleanSession());
            mqttEnity.setUserName(DeviceManager.get().getUserName());
            mqttEnity.setPassword(DeviceManager.get().getPassword());
            mqttEnity.setConnectCallback(instance);
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始连接");
            MqttManager.get().initMqtt(mqttEnity);
        }

    }


    @Override
    public void connectCallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            //停止轮询
            // 上传设备状态
        } else {
            //开启轮询
        }

    }

    @Override
    public void onCmdMessageArrivedCallback(String message) {

    }

    @Override
    public void onBasicConfigMessageArrivedCallback(String message) {

    }

    @Override
    public void onDataMessageArrivedCallback(String message) {

    }

    @Override
    public void onMenuBasicMessageArrivedCallback(String message) {

    }

    @Override
    public void onMenusizeMessageArrivedCallback(String message) {

    }

    @Override
    public void onMenusoldMessageArrivedCallback(String message) {

    }
}
