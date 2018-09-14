package com.example.pc.testeverything.manager;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by PC on 2018/8/27.
 */

public class Message {
    MqttEntity mqttEntity;
    String topic;
    MqttMessage message;

    public MqttEntity getMqttEntity() {
        return mqttEntity;
    }

    public void setMqttEntity(MqttEntity mqttEntity) {
        this.mqttEntity = mqttEntity;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqttMessage getMessage() {
        return message;
    }

    public void setMessage(MqttMessage message) {
        this.message = message;
    }
}
