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

import java.util.LinkedList;
import java.util.Queue;
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


    private MqttManager() {
    }

    public static MqttManager get() {
        if (instance == null) {
            instance = new MqttManager();
        }
        return instance;
    }

    private void mqttConnectFail(MqttEnity mqttEnity) {
        sendMqttDisconnect(mqttEnity);
        ThreadManger.get().add(new ThreadListener(mqttEnity) {
            @Override
            public void doAction() throws Exception {
                MqttEnity mqttEnity = (MqttEnity) getObjectData()[0];
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

    private void mqttSubscribeFail(MqttEnity mqttEnity) {
        sendMqttDisconnect(mqttEnity);
        if (isSubscribe.get()) {
            return;
        } else {
            if (!TextUtils.isEmpty(subscribeThreadUuid)) {
                ThreadManger.get().cancel(subscribeThreadUuid);
            }
        }
        subscribeThreadUuid = ThreadManger.get().add(new ThreadListener(mqttEnity) {
            @Override
            public void doAction() throws Exception {
                MqttEnity mqttEnity = (MqttEnity) getObjectData()[0];
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

    /**
     * 初始化Mqtt，并连接
     */
    public synchronized MqttAndroidClient initMqtt(final MqttEnity mqttEnity) {
        try {
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始创建Client连接对象");
            LogManager.get().getLogger(MqttManager.class).info(mqttAndroidClient == null ? "Mqtt的Client连接对象为空" : "Mqtt的Client连接对象不为空，连接ClientId为" +
                    mqttAndroidClient.getClientId() + "，ServerURI为" + mqttAndroidClient.getServerURI());
            LogManager.get().getLogger(MqttManager.class).info("最新Mqtt的ClientId为" + mqttEnity.getClientId() + "，ServerURI为" + mqttEnity.getServerURI());
            if (mqttAndroidClient == null ||
                    !TextUtils.equals(mqttAndroidClient.getClientId(), mqttEnity.getClientId()) ||
                    !TextUtils.equals(mqttAndroidClient.getServerURI(), mqttEnity.getServerURI())) {
                if (mqttAndroidClient != null) {
                    LogManager.get().getLogger(MqttManager.class).info("Mqtt的Client连接对象不为空，并还连接着，关闭之后再连接");
                    if (mqttAndroidClient.isConnected()) {
                        LogManager.get().getLogger(MqttManager.class).info("Mqtt的Client连接着，关闭之后再连接");
                        mqttAndroidClient.disconnect();
                    }
                } else {
                    mqttAndroidClient = new MqttAndroidClient(mqttEnity.getContext(),
                            mqttEnity.getServerURI(),mqttEnity.getClientId());
                }
            } else {
                if (mqttAndroidClient.isConnected()) {
                    LogManager.get().getLogger(MqttManager.class).warn("Mqtt已连接");
                    return mqttAndroidClient;
                }
            }
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始创建Option配置对象");
            LogManager.get().getLogger(MqttManager.class).info(mqttOptions == null ? "Mqtt的Option配置对象为空" : "Mqtt的Option配置对象不为空");
            LogManager.get().getLogger(MqttManager.class).info("Mqtt的Option配置对象中连接UserName为" + mqttEnity.getUserName() + "，Password为" + mqttEnity.getPassword());

            if (mqttOptions == null || !TextUtils.equals(mqttOptions.getUserName(), mqttEnity.getUserName()) ||
                    !TextUtils.equals(String.valueOf(mqttOptions.getPassword()), mqttEnity.getPassword())) {

                mqttOptions = getMqttConnectOptions(mqttEnity.isCleanSession(), mqttEnity.getUserName(), mqttEnity.getPassword().toCharArray());
            }
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始设置CallBack对象");
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    if (reconnect) {
                        LogManager.get().getLogger(MqttManager.class).info("--Reconnected to : " + serverURI);
                        sendMqttConnect(mqttEnity);
                    } else {
                        LogManager.get().getLogger(MqttManager.class).info("--Connected to : " + serverURI);
                        sendMqttConnect(mqttEnity);
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    LogManager.get().getLogger(MqttManager.class).warn("--The Connection was lost.");
                    sendMqttDisconnect(mqttEnity);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //订阅的消息都会发送到这里
                    forwardServerMessage(mqttEnity,topic, message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    LogManager.get().getLogger(MqttManager.class).warn("有消息推送成功");
                }
            });
            LogManager.get().getLogger(MqttManager.class).info("Mqtt开始连接");
            mqttConnect(mqttEnity);

        } catch (Exception e) {
            LogManager.get().getLogger(MqttManager.class).error("Mqtt创建和连接失败（Crash），失败原因：", e);
            mqttConnectFail(mqttEnity);
        }
        return mqttAndroidClient;
    }

    private void mqttConnect(final MqttEnity mqttEnity) {

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
                    subscribeMqtt(mqttEnity);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogManager.get().getLogger(MqttManager.class).error("Failed to connect to Mqtt: " +
                            mqttAndroidClient.getServerURI() + "。错误原因为:", exception);
                    mqttConnectFail(mqttEnity);
                }
            });

        } catch (MqttException e) {
            LogManager.get().getLogger(this.getClass()).error("连接异常", e);

            mqttConnectFail(mqttEnity);
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
    private void subscribeMqtt(final MqttEnity mqttEnity) {

        try {
            if (mqttAndroidClient == null) {
                LogManager.get().getLogger(this.getClass()).error("mqtt连接对象为空");
                mqttConnectFail(mqttEnity);
                return;
            }

            if (!mqttAndroidClient.isConnected()) {
                LogManager.get().getLogger(this.getClass()).error("mqtt未连接");
                mqttConnectFail(mqttEnity);
                return;
            }

            if (mqttEnity.getPathList()!= null) {
                for (final String path : mqttEnity.getPathList()) {
                    mqttAndroidClient.subscribe(path, 2, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            LogManager.get().getLogger(MqttManager.class).info("getOrderPath---" + path + "Subscribed!");
                        }
                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            LogManager.get().getLogger(MqttManager.class).info("getOrderPath---" + path + "Failed to subscribe!");
                            mqttSubscribeFail(mqttEnity);
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
    private void sendMqttDisconnect(MqttEnity mqttEnity) {
        connectQueue.offer(false);
        mqttEnity.getConnectCallback().connectCallback(false,"mqtt断开");


    }

    /**
     * 发送mqtt已连接的消息
     */
    private synchronized void sendMqttConnect(final MqttEnity mqttEnity) {
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
     * 关闭Mqtt连接
     */
    public void close(MqttAndroidClient mqttAndroidClient) {
        if (mqttAndroidClient != null) {
            mqttAndroidClient.unregisterResources();
            mqttAndroidClient.close();
            mqttAndroidClient = null;
        }

    }

    private void forwardServerMessage(MqttEnity mqttEnity, String topic, MqttMessage message) {
        try {
            if (topic.endsWith("/")) {
                topic = topic.substring(0, topic.length() - 1);
            }

            if ( mqttEnity.getConnectCallback() != null) {
                try {
                    String data = new String(message.getPayload());

                    if (TextUtils.equals(topic, getCmdPath())) {
                        mqttEnity.getConnectCallback().onCmdMessageArrivedCallback(data);
                      /*  try {
                            JSONObject jsonObject = JSON.parseObject(data);
                            String type = jsonObject.getString("type");
                            if (!TextUtils.isEmpty(type)) {
                                switch (type.toLowerCase()) {
                                    case "cmd":
                                        mqttEnity.getConnectCallback().onCmdMessageArrivedCallback(data);
                                        break;
                                    case "basicconfig":
                                        mqttEnity.getConnectCallback().onBasicConfigMessageArrivedCallback(data);
                                        break;
                                    case "data":
                                        mqttEnity.getConnectCallback().onDataMessageArrivedCallback(data);
                                        break;
                                    case "menubasic":
                                        mqttEnity.getConnectCallback().onMenuBasicMessageArrivedCallback(data);
                                        break;
                                    case "menusize":
                                        mqttEnity.getConnectCallback().onMenusizeMessageArrivedCallback(data);
                                        break;
                                    case "menusold":
                                        mqttEnity.getConnectCallback().onMenusoldMessageArrivedCallback(data);
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    } else if (TextUtils.equals(topic, getBasicConfigPath())) {
                        mqttEnity.getConnectCallback().onBasicConfigMessageArrivedCallback(data);
                    } else if (TextUtils.equals(topic, getDataPath())) {
                        mqttEnity.getConnectCallback().onDataMessageArrivedCallback(data);
                    } else if (TextUtils.equals(topic, getMenuBasicPath())) {
                        mqttEnity.getConnectCallback().onMenuBasicMessageArrivedCallback(data);
                    } else if (TextUtils.equals(topic, getMenusizePath())) {
                        mqttEnity.getConnectCallback().onMenusizeMessageArrivedCallback(data);

                    } else if (TextUtils.equals(topic, getMenusoldPath())) {
                        mqttEnity.getConnectCallback().onMenusoldMessageArrivedCallback(data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LogManager.get().getLogger(MqttManager.class).info("接收mqtt消息：" + message.getPayload() + "。监听地址为：" + topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CharSequence getMenusizePath() {
        return "";
    }

    private CharSequence getMenuBasicPath() {
        return "";
    }

    private CharSequence getDataPath() {
        return "";
    }

    private CharSequence getMenusoldPath() {
        return "";
    }

    private CharSequence getBasicConfigPath() {
        return "";
    }

    private String getCmdPath() {
        return "";
    }


}