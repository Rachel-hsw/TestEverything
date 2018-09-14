package com.example.pc.testeverything.manager;

import android.content.Context;
import java.util.List;

/**
 * Created by PC on 2018/7/19.
 */

public class MqttEntity {
    //host为主机名 Mqtt服务器地址(tcp://xxxx:1863)
    private String serverURI;
    //连接MQTT的客户端ID，一般以唯一标识符表示
    private String clientId;
    //
    private String userName;
    //
    private String password;
    //
    private boolean isCleanSession;

    private Context context;
    //监听的数据 cmd data 门店配置basicconfig 菜品基础数据menubasic 尺寸menusize 售罄数据menusold
    private List<String> pathList;

    private MqttConnectCallback connectCallback;

    public MqttConnectCallback getConnectCallback() {
        return connectCallback;
    }

    public void setConnectCallback(MqttConnectCallback connectCallback) {
        this.connectCallback = connectCallback;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getServerURI() {
        return serverURI;
    }

    public void setServerURI(String serverURI) {
        this.serverURI = serverURI;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCleanSession() {
        return isCleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        isCleanSession = cleanSession;
    }
}
