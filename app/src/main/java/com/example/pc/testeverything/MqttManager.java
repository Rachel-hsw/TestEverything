package com.example.pc.testeverything;

import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 * Created by PC on 2018/5/24.
 */

public class MqttManager {
    // 单例
    private static MqttManager mInstance = null;
    // 回调
    private MqttCallback mCallback;
    // Private instance variables
    private MqttAndroidClient client;
    private MqttConnectOptions conOpt;
    private boolean clean = true;
    private MqttManager() {
        mCallback = new MqttCallbackBus();
    }
    public static MqttManager getInstance() {
        if (null == mInstance) {
            mInstance = new MqttManager();
        }
        return mInstance;
    }
    /**     * 释放单例, 及其所引用的资源     */
    public static void release() {
        try {
            if (mInstance != null) {
                mInstance.disConnect();
                mInstance = null;
            }
        } catch (Exception e) {
        }
    }
    /**
     * 创建Mqtt 连接
     *
     * @param host Mqtt服务器地址(tcp://xxxx:1863)
     * @param clientId  clientId
     * @return
     */
    public boolean creatConnect(String host, String clientId) {
        boolean flag = false;
        //1、 首先创建MqttAndroidClient,负责连接,host为主机名 Mqtt服务器地址(tcp://xxxx:1863)，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttAndroidClient(MyApplication.getContext(), host, clientId);
        // 2、创建MqttConnectOptions，是复杂属性设置的，配置MQTT连接
        conOpt = new MqttConnectOptions();
        conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        conOpt.setCleanSession(clean);
        conOpt.setPassword(null);
        conOpt.setUserName(null);

        // 3.创建MQTT连接
        flag = doConnect();
        //4.设置监听
        client.setCallback(mCallback);

        return flag;
    }
    /**
     * 建立连接
     *
     * @return
     */
    public boolean doConnect() {
        boolean flag = false;
        if (client != null) {
            try {
                // 3.创建MQTT连接
                client.connect(conOpt);
                Logger.d("Connected to " + client.getServerURI() + " with client ID " + client.getClientId());
                flag = true;
            } catch (Exception e) {
            }
        }
        return flag;
    }
    /**
     * Publish / send a message to an MQTT server 6、发布消息
     *
     * @param topicName the name of the topic to publish to
     * @param qos       the quality of service to delivery the message at (0,1,2)
     * @param payload   the set of bytes to send to the MQTT server
     * @return boolean
     */
    public boolean publish(String topicName, int qos, byte[] payload) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            Logger.d("Publishing to topic \"" + topicName + "\" qos " + qos);
            // Create and configure a message
            MqttMessage message = new MqttMessage(payload);            message.setQos(qos);
            // Send the message to the server, control is not returned until
            // it has been delivered to the server meeting the specified
            // quality of service.
            try {
                client.publish(topicName, message);
                flag = true;
            } catch (MqttException e) {
            }
        }
        return flag;
    }

    /**5.订阅消息
     *  我们在监听的connectComplete方法里面去订阅消息
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     *
     * @param topicName to subscribe to (can be wild carded)
     * @param qos       the maximum quality of service to receive messages at for this subscription
     * @return boolean
     */
    public boolean subscribe(String topicName, int qos) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            // Subscribe to the requested topic
            // The QoS specified is the maximum level that messages will be sent to the client at.
            // For instance if QoS 1 is specified, any messages originally published at QoS 2 will
            // be downgraded to 1 when delivering to the client but messages published at 1 and 0
            // will be received at the same level they were published at.
            Logger.d("Subscribing to topic \"" + topicName + "\" qos " + qos);
            try {
                client.subscribe(topicName, qos);
                flag = true;
            } catch (MqttException e) {
            }
        }
        return flag;
    }
    /**
     * 取消连接
     *
     * @throws MqttException
     */
    public void disConnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }
}
