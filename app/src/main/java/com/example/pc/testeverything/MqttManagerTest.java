//package com.example.pc.testeverything;
//
//import org.eclipse.paho.android.service.MqttAndroidClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//
///**
// * Created by PC on 2018/5/24.
// */
//
//public class MqttManager{
//1.首先创建MqttAndroidClient和MqttConnectOptions，这两员大将一个是负责连接，一个是复杂属性设置的：
//    MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(MyApplication.getContext(), serverUri, clientId);
//
//    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
//    // 配置MQTT连接
//    mqttConnectOptions.setAutomaticReconnect(true);
//    mqttConnectOptions.setCleanSession(false);
//    mqttConnectOptions.setUserName(null);
//    mqttConnectOptions.setPassword(null);
//    mqttConnectOptions.setConnectionTimeout(30);  //超时时间
//    mqttConnectOptions.setKeepAliveInterval(60); //心跳时间,单位秒
//    mqttConnectOptions.setAutomaticReconnect(true);//自动重连
//
//   // 3.创建MQTT连接
//
//mqttAndroidClient.connect(mqttConnectOptions);
//    //4.设置监听
//
//mqttAndroidClient.setCallback(new MqttCallbackExtended() {
//        @Override
//        public void connectComplete(boolean reconnect, String serverURI) {
//            Log.e(TAG, "reconnect ---> " + reconnect + "       serverURI--->" + serverURI);
//        }
//
//        @Override
//        public void connectionLost(Throwable cause) {
//            Log.e(TAG, "cause ---> " + cause);
//        }
//
//        @Override
//        public void messageArrived(String topic, MqttMessage message) throws Exception {
//            Log.e(TAG, "topic ---> " + topic + "       message--->" + message);
//            startNotification(message);
//        }
//
//        @Override
//        public void deliveryComplete(IMqttDeliveryToken token) {
//            Log.e(TAG, "token ---> " + token);
//        }
//    });
//
//   // 5.订阅消息
//
//            我们在上面connectComplete方法里面去订阅消息
//
//    final String subscriptionTopic = "exampleAndroidTopic";
//
//    private void subscribeToTopic() {
//        try {
//            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    Log.e(TAG, "onFailure ---> " + asyncActionToken);
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Log.e(TAG, "onFailure ---> " + exception);
//                }
//            });
//        } catch (MqttException e) {
//            Log.e(TAG, "subscribeToTopic is error");
//            e.printStackTrace();
//        }
//    }
//
//}
