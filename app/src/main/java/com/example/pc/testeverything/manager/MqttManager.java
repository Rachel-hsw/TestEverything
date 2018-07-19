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
import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by skyshi on 2017/2/20 0020.
 */
public class MqttManager {

    public static MqttManager instance;

    private MqttAndroidClient businessMqttAndroidClient;
    private MqttAndroidClient internetMqttAndroidClient;
    private MqttConnectOptions businessOptions;
    private static final int MQTT_CONNECT_FIVE = 10;
    private static final int MQTT_CONNECT_THOUSAND = 60;
    private static final int MQTT_CONNECT_COUNTLESS = 60 * 4;
    private static final int MQTT_SUBSCRIBE_FIVE = 10;
    private static final int MQTT_SUBSCRIBE_THOUSAND = 60;
    private static final int MQTT_SUBSCRIBE_COUNTLESS = 60 * 4;
    // mqtt是否正在运行
    private int  businessReconnectTime = 0, businessResubscribeTime = 0;
    private String businessSubscribeThreadUuid;
    private AtomicBoolean isBusinessSubscribe = new AtomicBoolean(false);
    private boolean isFirstConnect =false;
    private Queue<Boolean> connectQueue = new LinkedList<Boolean>();


    private MqttManager() {
        EventBus.getDefault().register(this);
    }

    public static MqttManager get() {
        if (instance == null) {
            instance = new MqttManager();
        }
        return instance;
    }

    private void mqtt_business_connect_fail(final MqttEnity mqttEnity){
        sendMqttDisconnect(mqttEnity);
        ThreadManger.get().add(new ThreadListener() {
            @Override
            public void doAction() throws Exception {
                if (businessReconnectTime <= 5) {
                    TimeUnit.SECONDS.sleep(MQTT_CONNECT_FIVE);
                } else if (businessReconnectTime > 5 && businessReconnectTime < 1000) {
                    TimeUnit.SECONDS.sleep(MQTT_CONNECT_THOUSAND);
                } else if (businessReconnectTime >= 1000) {
                    TimeUnit.SECONDS.sleep(MQTT_CONNECT_COUNTLESS);
                }
                businessReconnectTime++;
                LogManager.get().getLogger(this.getClass()).warn("business mqtt已经有" + businessReconnectTime + "次重连");
                if (businessMqttAndroidClient == null) {
                    initBusinessMqtt(mqttEnity);
                    return;
                }

                if (!businessMqttAndroidClient.isConnected()) {
                    businessMqttConnect(mqttEnity);
                }
            }
        });
    }
    private void mqtt_business_subscribe_fail(final MqttEnity mqttEnity){
        sendMqttDisconnect(mqttEnity);
        if (isBusinessSubscribe.get()) {
            return;
        } else {
            if (!TextUtils.isEmpty(businessSubscribeThreadUuid)) {
                ThreadManger.get().cancel(businessSubscribeThreadUuid);
            }
        }
        businessSubscribeThreadUuid = ThreadManger.get().add(new ThreadListener() {
            @Override
            public void doAction() throws Exception {
                isBusinessSubscribe.getAndSet(true);
                if (businessResubscribeTime <= 5) {
                    TimeUnit.SECONDS.sleep(MQTT_SUBSCRIBE_FIVE);
                } else if (businessResubscribeTime > 5 && businessResubscribeTime < 1000) {
                    TimeUnit.SECONDS.sleep(MQTT_SUBSCRIBE_THOUSAND);
                } else if (businessResubscribeTime >= 1000) {
                    TimeUnit.SECONDS.sleep(MQTT_SUBSCRIBE_COUNTLESS);
                }
                businessResubscribeTime++;
                LogManager.get().getLogger(this.getClass()).warn("business mqtt的监听已经有" + businessResubscribeTime + "次重连");
                LogManager.get().getLogger(MqttManager.class).info("Business--订阅失败，重新订阅resubscribeTime");
                subscribeBusinessMqtt(mqttEnity);
                isBusinessSubscribe.getAndSet(false);
            }
        });
    }

    private void publish(final String path, final String msg, final boolean isBusiness) {
        ThreadManger.get().add(new ThreadListener() {
            @Override
            public void doAction() {
                try {
                    LogManager.get().getLogger(MqttManager.class).info("发送异步消息；path为" +
                            path + "，发送消息为：" + msg);
                    MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
                    mqttMessage.setQos(2);
                    if (isBusiness) {
                        if (businessMqttAndroidClient != null) {
                            businessMqttAndroidClient.publish(path, mqttMessage);
                        }
                    } else {
                        if (internetMqttAndroidClient != null) {
                            internetMqttAndroidClient.publish(path, mqttMessage);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化Business Mqtt，并连接
     */
    private synchronized void initBusinessMqtt(final MqttEnity mqttEnity) {
        try {
            LogManager.get().getLogger(MqttManager.class).info("BusinessMqtt开始创建Client连接对象");
            LogManager.get().getLogger(MqttManager.class).info(businessMqttAndroidClient == null ? "BusinessMqtt的Client连接对象为空" : "BusinessMqtt的Client连接对象不为空，连接ClientId为" +
                    businessMqttAndroidClient.getClientId() + "，ServerURI为" + businessMqttAndroidClient.getServerURI());
            LogManager.get().getLogger(MqttManager.class).info("最新BusinessMqtt的ClientId为" + mqttEnity.getClientId() + "，ServerURI为" + mqttEnity.getServerURI());
            if (businessMqttAndroidClient == null ||
                    !TextUtils.equals(businessMqttAndroidClient.getClientId(),mqttEnity.getClientId()) ||
                    !TextUtils.equals(businessMqttAndroidClient.getServerURI(), mqttEnity.getServerURI())) {
                if (businessMqttAndroidClient != null) {
                    LogManager.get().getLogger(MqttManager.class).info("BusinessMqtt的Client连接对象不为空，并还连接着，关闭之后再连接");
                    if (businessMqttAndroidClient.isConnected()) {
                        LogManager.get().getLogger(MqttManager.class).info("BusinessMqtt的Client连接着，关闭之后再连接");
                        businessMqttAndroidClient.disconnect();
                    }
                } else {
                    businessMqttAndroidClient = new MqttAndroidClient(mqttEnity.getContext(),
                            mqttEnity.getServerURI(),mqttEnity.getClientId());
                }
            } else {
                if (businessMqttAndroidClient.isConnected()) {
                    LogManager.get().getLogger(MqttManager.class).warn("BusinessMqtt已连接");
                    return;
                }
            }
            LogManager.get().getLogger(MqttManager.class).info("BusinessMqtt开始创建Option配置对象");
            LogManager.get().getLogger(MqttManager.class).info(businessOptions == null ? "BusinessMqtt的Option配置对象为空" : "BusinessMqtt的Option配置对象不为空");
            LogManager.get().getLogger(MqttManager.class).info("BusinessMqtt的Option配置对象中连接UserName为" + mqttEnity.getUserName() + "，Password为" + mqttEnity.getPassword());

            if (businessOptions == null || !TextUtils.equals(businessOptions.getUserName(), mqttEnity.getUserName()) ||
                    !TextUtils.equals(String.valueOf(businessOptions.getPassword()), mqttEnity.getPassword())) {

                businessOptions = getMqttConnectOptions(mqttEnity.isCleanSession(), mqttEnity.getUserName(), mqttEnity.getPassword().toCharArray());
            }
            LogManager.get().getLogger(MqttManager.class).info("BusinessMqtt开始设置CallBack对象");
            businessMqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    if (reconnect) {
                        LogManager.get().getLogger(MqttManager.class).info("business--Reconnected to : " + serverURI);
                        sendMqttConnect(mqttEnity);
                    } else {
                        LogManager.get().getLogger(MqttManager.class).info("business--Connected to : " + serverURI);
                        sendMqttConnect(mqttEnity);
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    LogManager.get().getLogger(MqttManager.class).warn("business--The Connection was lost.");
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
            LogManager.get().getLogger(MqttManager.class).info("BusinessMqtt开始连接");
            businessMqttConnect( mqttEnity);

        } catch (Exception e) {
            LogManager.get().getLogger(MqttManager.class).error("Business Mqtt创建和连接失败（Crash），失败原因：", e);
            mqtt_business_connect_fail(mqttEnity);
        }
    }

    private void businessMqttConnect(final MqttEnity mqttEnity) {

        try {

            businessMqttAndroidClient.connect(businessOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogManager.get().getLogger(MqttManager.class).info("BusinessMqtt连接成功");
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    businessMqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeBusinessMqtt(mqttEnity);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogManager.get().getLogger(MqttManager.class).error("Failed to connect to businessMqtt: " +
                            businessMqttAndroidClient.getServerURI() + "。错误原因为:", exception);
                    mqtt_business_connect_fail(mqttEnity);
                }
            });

        } catch (MqttException e) {
            LogManager.get().getLogger(this.getClass()).error("Business连接异常", e);

            mqtt_business_connect_fail(mqttEnity);
        }
    }

    private MqttConnectOptions getMqttConnectOptions(boolean isCleanSession, String userName, char[] password) {
        MqttConnectOptions businessOptions = new MqttConnectOptions();
        businessOptions.setUserName(userName);
        businessOptions.setPassword(password);
        businessOptions.setCleanSession(isCleanSession);
        businessOptions.setAutomaticReconnect(true);
        businessOptions.setConnectionTimeout(10);
        businessOptions.setKeepAliveInterval(5);
        businessOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        return businessOptions;
    }
    /**
     * 初始化所有business的监听接口
     */
    private void subscribeBusinessMqtt(final MqttEnity mqttEnity) {

        try {
            if (businessMqttAndroidClient == null) {
                LogManager.get().getLogger(this.getClass()).error("mqtt连接对象为空");
                mqtt_business_connect_fail(mqttEnity);
                return;
            }

            if (!businessMqttAndroidClient.isConnected()) {
                LogManager.get().getLogger(this.getClass()).error("mqtt未连接");
                mqtt_business_connect_fail(mqttEnity);
                return;
            }

            if (mqttEnity.getPathList()!= null) {
                for (final String path : mqttEnity.getPathList()) {
                    businessMqttAndroidClient.subscribe(path, 2, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            LogManager.get().getLogger(MqttManager.class).info("getOrderPath---" + path + "Subscribed!");
                        }
                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            LogManager.get().getLogger(MqttManager.class).info("getOrderPath---" + path + "Failed to subscribe!");
                            mqtt_business_subscribe_fail(mqttEnity);
                        }
                    });

                }
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public int getBusinessUnPublishMessageCount() {
        if (!businessMqttAndroidClient.isConnected()) {
            return businessMqttAndroidClient.getBufferedMessageCount();
        } else {
            return 0;
        }
    }






    /**
     * 发送mqtt断开的消息
     */
    private void sendMqttDisconnect(MqttEnity mqttEnity) {
        connectQueue.offer(false);
        isFirstConnect =false;
        mqttEnity.getConnectCallback().connectCallback(false,"mqtt断开");
        // 上传设备状态
      //  EventBus.getDefault().post(new EBDeviceEntity(EVENT_BUS_DEVICE_REPORT_ERROR_STATUS));
    }

    /**
     * 发送mqtt已连接的消息
     */
    private void sendMqttConnect(final MqttEnity mqttEnity) {
        //第一次连接
       if (!isFirstConnect){
           isFirstConnect =true;
       }
        connectQueue.offer(true);
        //如果第一次连接时间
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mqttEnity.getConnectCallback() != null) {
                        mqttEnity.getConnectCallback().connectCallback(false,"mqtt连接成功");
                        connectQueue.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },3000);//延时1s执行


    }

    /**
     * 关闭所有Mqtt连接
     */
    public void close() {
        if (businessMqttAndroidClient != null) {
            businessMqttAndroidClient.unregisterResources();
            businessMqttAndroidClient.close();
            businessMqttAndroidClient = null;
        }

        if (internetMqttAndroidClient != null) {
            internetMqttAndroidClient.unregisterResources();
            internetMqttAndroidClient.close();
            internetMqttAndroidClient = null;
        }
    }

    private void forwardServerMessage(MqttEnity mqttEnity,String path, MqttMessage message) {
        try {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            String result = new String(message.getPayload());
            if ( mqttEnity.getConnectCallback() != null) {
                mqttEnity.getConnectCallback().onMessageArrivedCallback(result);
            }
            LogManager.get().getLogger(MqttManager.class).info("接收mqtt消息：" + result + "。监听地址为：" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}