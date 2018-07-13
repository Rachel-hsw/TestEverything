package com.example.pc.testeverything;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by PC on 2018/5/24.
 */

public class MqttCallbackBus implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        Log.i("test_constraintlayout",cause.getMessage().toString());

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Logger.d(topic + "====" + message.toString());
        EventBus.getDefault().post(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}

