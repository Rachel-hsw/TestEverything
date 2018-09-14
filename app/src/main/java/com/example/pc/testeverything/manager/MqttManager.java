package com.example.pc.testeverything.manager;

import android.text.TextUtils;

import com.tiidian.log.LogManager;
import com.tiidian.threadmanager.ThreadListener;
import com.tiidian.threadmanager.ThreadManger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MqttManager {

    public static MqttManager instance;
    private MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mqttOptions;
    private static final int MQTT_CONNECT_FIVE = 10;
    private static final int MQTT_CONNECT_THOUSAND = 60;
    private static final int MQTT_CONNECT_COUNTLESS = 60 * 4;
    private static final int MQTT_SUBSCRIBE_FIVE = 10;
    private static final int MQTT_SUBSCRIBE_THOUSAND = 60;
    private static final int MQTT_SUBSCRIBE_COUNTLESS = 60 * 4;
    // mqtt是否正在运行
    private int reconnectTime = 0, resubscribeTime = 0;
    private String subscribeThreadUuid;
    private AtomicBoolean isSubscribe = new AtomicBoolean(false);
    private Queue<Boolean> connectQueue = new LinkedList<>();
    List<MqttAndroidClient> clientList;
    public ConcurrentLinkedQueue<Message> messageQueue;
    boolean isIdExist;
    private MqttManager() {
        clientList = new ArrayList<>();
        messageQueue = new ConcurrentLinkedQueue<>();
        ThreadManger.get().add(new ThreadListener() {
            @Override
            public void doAction() throws Exception {
                while (true) {
                    try {
                        if (messageQueue.isEmpty()) {
                            TimeUnit.MILLISECONDS.sleep(1000);
                            continue;
                        }
                        Message message = messageQueue.poll();
                        forwardServerMessage(message.getMqttEntity(), message.getTopic(), message.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public static MqttManager get() {
        if (instance == null) {
            instance = new MqttManager();
        }
        return instance;
    }

    private int getMessageSize() {
        return messageQueue.size();
    }


    private void mqttConnectFail(MqttEntity mqttEntity) {
        sendMqttDisconnect(mqttEntity);
        ThreadManger.get().add(new ThreadListener(mqttEntity) {
            @Override
            public void doAction() throws Exception {
                MqttEntity mqttEnity = (MqttEntity) getObjectData()[0];
                if (reconnectTime <= 5) {
                    TimeUnit.SECONDS.sleep(MQTT_CONNECT_FIVE);
                } else if (reconnectTime > 5 && reconnectTime < 1000) {
                    TimeUnit.SECONDS.sleep(MQTT_CONNECT_THOUSAND);
                } else if (reconnectTime >= 1000) {
                    TimeUnit.SECONDS.sleep(MQTT_CONNECT_COUNTLESS);
                }
                reconnectTime++;
                LogManager.get().getLogger(this.getClass()).warn("mqtt已经有" + reconnectTime + "次重连");
                if (mqttAndroidClient == null) {
                    initMqtt(mqttEnity);
                    return;
                }

                if (!mqttAndroidClient.isConnected()) {
                    mqttConnect(mqttEnity);
                }
            }
        });
    }

    private void mqttSubscribeFail(MqttEntity mqttEntity) {
        sendMqttDisconnect(mqttEntity);
        if (isSubscribe.get()) {
            return;
        } else {
            if (!TextUtils.isEmpty(subscribeThreadUuid)) {
                ThreadManger.get().cancel(subscribeThreadUuid);
            }
        }
        subscribeThreadUuid = ThreadManger.get().add(new ThreadListener(mqttEntity) {
            @Override
            public void doAction() throws Exception {
                MqttEntity mqttEnity = (MqttEntity) getObjectData()[0];
                isSubscribe.getAndSet(true);
                if (resubscribeTime <= 5) {
                    TimeUnit.SECONDS.sleep(MQTT_SUBSCRIBE_FIVE);
                } else if (resubscribeTime > 5 && resubscribeTime < 1000) {
                    TimeUnit.SECONDS.sleep(MQTT_SUBSCRIBE_THOUSAND);
                } else if (resubscribeTime >= 1000) {
                    TimeUnit.SECONDS.sleep(MQTT_SUBSCRIBE_COUNTLESS);
                }
                resubscribeTime++;
                LogManager.get().getLogger(this.getClass()).warn("mqtt的监听已经有" + resubscribeTime + "次重连");
                LogManager.get().getLogger(MqttManager.class).info("订阅失败，重新订阅resubscribeTime");
                subscribeMqtt(mqttEnity);
                isSubscribe.getAndSet(false);
            }
        });
    }

    private void publish(String path, String msg) {
        ThreadManger.get().add(new ThreadListener(path, msg) {
            @Override
            public void doAction() {
                try {
                    String path = getStringData()[0];
                    String msg = getStringData()[1];
                    LogManager.get().getLogger(MqttManager.class).info("发送异步消息；path为" +
                            path + "，发送消息为：" + msg);
                    MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
                    mqttMessage.setQos(2);
                    if (mqttAndroidClient != null) {
                        mqttAndroidClient.publish(path, mqttMessage);
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //如果已经创建的clientList列表里不存在，则去创建
    public void addNewMqttClient(MqttEntity mqttEntity) {
        isIdExist = false;
        if (mqttEntity.getClientId() != null) {
            for (MqttAndroidClient client : clientList) {
                if (client.getClientId().equals(mqttEntity.getClientId()) && client.getServerURI().equals(mqttEntity.getServerURI())) {
                    isIdExist = true;
                }
            }
        }
        if (!isIdExist) {
            MqttAndroidClient mqttAndroidClient = initMqtt(mqttEntity);
            clientList.add(mqttAndroidClient);
        } else {
            if (mqttEntity.getConnectCallback() != null) {
                mqttEntity.getConnectCallback().onClientExist(mqttEntity);
            }
        }
    }

    /**
     * 初始化Mqtt，并连接
     */
    public synchronized MqttAndroidClient initMqtt(final MqttEntity mqttEntity) {
        try {
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始创建Client连接对象");
            LogManager.get().getLogger(MqttManager.class).info(mqttAndroidClient == null ? "Mqtt的Client连接对象为空" : "Mqtt的Client连接对象不为空，连接ClientId为" +
                    mqttAndroidClient.getClientId() + "，ServerURI为" + mqttAndroidClient.getServerURI());
            LogManager.get().getLogger(MqttManager.class).info("最新Mqtt的ClientId为" + mqttEntity.getClientId() + "，ServerURI为" + mqttEntity.getServerURI());
            if (mqttAndroidClient == null ||
                    !TextUtils.equals(mqttAndroidClient.getClientId(), mqttEntity.getClientId()) ||
                    !TextUtils.equals(mqttAndroidClient.getServerURI(), mqttEntity.getServerURI())) {
                if (mqttAndroidClient != null) {
                    LogManager.get().getLogger(MqttManager.class).info("Mqtt的Client连接对象不为空，并还连接着，关闭之后再连接");
                    if (mqttAndroidClient.isConnected()) {
                        LogManager.get().getLogger(MqttManager.class).info("Mqtt的Client连接着，关闭之后再连接");
                        mqttAndroidClient.disconnect();
                    }
                } else {
                    mqttAndroidClient = new MqttAndroidClient(mqttEntity.getContext(),
                            mqttEntity.getServerURI(), mqttEntity.getClientId());
                }
            } else {
                if (mqttAndroidClient.isConnected()) {
                    LogManager.get().getLogger(MqttManager.class).warn("Mqtt已连接");
                    return mqttAndroidClient;
                }
            }
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始创建Option配置对象");
            LogManager.get().getLogger(MqttManager.class).info(mqttOptions == null ? "Mqtt的Option配置对象为空" : "Mqtt的Option配置对象不为空");
            LogManager.get().getLogger(MqttManager.class).info("Mqtt的Option配置对象中连接UserName为" + mqttEntity.getUserName() + "，Password为" + mqttEntity.getPassword());

            if (mqttOptions == null || !TextUtils.equals(mqttOptions.getUserName(), mqttEntity.getUserName()) ||
                    !TextUtils.equals(String.valueOf(mqttOptions.getPassword()), mqttEntity.getPassword())) {

                mqttOptions = getMqttConnectOptions(mqttEntity.isCleanSession(), mqttEntity.getUserName(), mqttEntity.getPassword().toCharArray());
            }
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始设置CallBack对象");
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    if (reconnect) {
                        LogManager.get().getLogger(MqttManager.class).info("--Reconnected to : " + serverURI);
                        sendMqttConnect(mqttEntity);
                    } else {
                        LogManager.get().getLogger(MqttManager.class).info("--Connected to : " + serverURI);
                        sendMqttConnect(mqttEntity);
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    LogManager.get().getLogger(MqttManager.class).warn("--The Connection was lost.");
                    sendMqttDisconnect(mqttEntity);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //订阅的消息都会发送到这里
                    Message messageArrived = new Message();
                    messageArrived.setTopic(topic);
                    messageArrived.setMqttEntity(mqttEntity);
                    messageArrived.setMessage(message);
                    messageQueue.offer(messageArrived);

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    LogManager.get().getLogger(MqttManager.class).warn("有消息推送成功");
                }
            });
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始连接");
            mqttConnect(mqttEntity);

        } catch (Exception e) {
            LogManager.get().getLogger(MqttManager.class).error("Mqtt创建和连接失败（Crash），失败原因：", e);
            mqttConnectFail(mqttEntity);
        }
        return mqttAndroidClient;
    }

    private void mqttConnect(final MqttEntity mqttEntity) {

        try {

            mqttAndroidClient.connect(mqttOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogManager.get().getLogger(MqttManager.class).info("Mqtt连接成功");
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeMqtt(mqttEntity);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogManager.get().getLogger(MqttManager.class).error("Failed to connect to Mqtt: " +
                            mqttAndroidClient.getServerURI() + "。错误原因为:", exception);
                    mqttConnectFail(mqttEntity);
                }
            });

        } catch (MqttException e) {
            LogManager.get().getLogger(this.getClass()).error("连接异常", e);

            mqttConnectFail(mqttEntity);
        }
    }

    private MqttConnectOptions getMqttConnectOptions(boolean isCleanSession, String userName, char[] password) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(password);
        options.setCleanSession(isCleanSession);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(5);
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        return options;
    }
    /**
     * 初始化所有mqtt的监听接口
     */
    private void subscribeMqtt(final MqttEntity mqttEntity) {

        try {
            if (mqttAndroidClient == null) {
                LogManager.get().getLogger(this.getClass()).error("mqtt连接对象为空");
                mqttConnectFail(mqttEntity);
                return;
            }

            if (!mqttAndroidClient.isConnected()) {
                LogManager.get().getLogger(this.getClass()).error("mqtt未连接");
                mqttConnectFail(mqttEntity);
                return;
            }

            if (mqttEntity.getPathList() != null) {
                for (final String path : mqttEntity.getPathList()) {
                    mqttAndroidClient.subscribe(path, 2, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            LogManager.get().getLogger(MqttManager.class).info("getOrderPath---" + path + "Subscribed!");
                        }
                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            LogManager.get().getLogger(MqttManager.class).info("getOrderPath---" + path + "Failed to subscribe!");
                            mqttSubscribeFail(mqttEntity);
                        }
                    });

                }
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送mqtt断开的消息
     */
    private void sendMqttDisconnect(MqttEntity mqttEntity) {
        connectQueue.offer(false);
        mqttEntity.getConnectCallback().connectCallback(false, "mqtt断开");


    }

    /**
     * 发送mqtt已连接的消息
     */
    private synchronized void sendMqttConnect(final MqttEntity mqttEnity) {
        //第一次连接
        ThreadManger.get().add(new ThreadListener() {
            @Override
            public void doAction() {
                try {
                    connectQueue.clear();
                    connectQueue.offer(true);
                    Thread.sleep(3000);
                   if (connectQueue.size()==1&&connectQueue.poll()==true){
                       mqttEnity.getConnectCallback().connectCallback(true,"mqtt连接成功");
                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 关闭当前Mqtt连接
     */
    public void close(MqttAndroidClient mqttAndroidClient) {
        if (mqttAndroidClient != null) {
            mqttAndroidClient.unregisterResources();
            mqttAndroidClient.close();
            mqttAndroidClient = null;
        }

    }

    /**
     * 关闭所有Mqtt连接
     */
    public void close() {
        for (MqttAndroidClient mqttAndroidClient : clientList) {
            MqttManager.get().close(mqttAndroidClient);
        }

    }

    private void forwardServerMessage(MqttEntity mqttEntity, String topic, MqttMessage message) {
        try {
            if (mqttEntity.getConnectCallback() != null) {
                mqttEntity.getConnectCallback().onMessageArrivedCallback(topic, message);
            }
            LogManager.get().getLogger(MqttManager.class).info("接收mqtt消息：" + message.getPayload() + "。监听地址为：" + topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}