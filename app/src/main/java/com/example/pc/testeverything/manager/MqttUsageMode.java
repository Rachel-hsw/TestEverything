package com.example.pc.testeverything.manager;

import android.content.Context;
import android.text.TextUtils;
import com.tiidian.log.LogManager;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;


/**
 * Created by PC on 2018/8/24.
 */

public class MqttUsageMode implements MqttConnectCallback {
    public static MqttUsageMode instance;
    private MqttUsageMode() {

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
    private void addNewMqttManager(String serverURI, String clientId, Context context, List<String> pathList) {
        MqttEntity mqttEntity = new MqttEntity();
        mqttEntity.setContext(context);
        mqttEntity.setClientId(clientId);
        mqttEntity.setServerURI(serverURI);
    /*        mqttEntity.setCleanSession(DeviceManager.get().getCleanSession());
            mqttEntity.setUserName(DeviceManager.get().getUserName());
            mqttEntity.setPassword(DeviceManager.get().getPassword());*/
        mqttEntity.setConnectCallback(instance);
        mqttEntity.setPathList(pathList);
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始连接");
        MqttManager.get().addNewMqttClient(mqttEntity);

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
    public void onMessageArrivedCallback(String topic, MqttMessage message) {
        try {
            if (topic.endsWith("/")) {
                topic = topic.substring(0, topic.length() - 1);
            }
            String data = new String(message.getPayload());
            if (TextUtils.equals(topic, getCmdPath())) {

            } else if (TextUtils.equals(topic, getBasicConfigPath())) {

            } else if (TextUtils.equals(topic, getDataPath())) {

            } else if (TextUtils.equals(topic, getMenuBasicPath())) {

            } else if (TextUtils.equals(topic, getMenusizePath())) {

            } else if (TextUtils.equals(topic, getMenusoldPath())) {

            }
            LogManager.get().getLogger(MqttManager.class).info("接收mqtt消息：" + message.getPayload() + "。监听地址为：" + topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClientExist(MqttEntity mqttEntity) {
        LogManager.get().getLogger(MqttManager.class).info("服务器地址为：" + mqttEntity.getServerURI() + ",ClientID为" + mqttEntity.getClientId() + "的Mqtt已经存在，无需重复创建");

    }

    private String getMenusizePath() {
        return "";
    }

    private String getMenuBasicPath() {
        return "";
    }

    private String getDataPath() {
        return "";
    }

    private String getMenusoldPath() {
        return "";
    }

    private String getBasicConfigPath() {
        return "";
    }

    private String getCmdPath() {
        return "";
    }

}
