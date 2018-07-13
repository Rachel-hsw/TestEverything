package com.example.pc.testeverything;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class qrcode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

      /*  Resources res = qrcode.this.getResources();
        Bitmap bitmap= BitmapFactory.decodeResource(res, R.mipmap.qrcode);// 这里是获取图片Bitmap，也可以传入其他参数到Dialog中*/
        Bitmap bitmap = ZXingUtils.createQRImage("{\"type\":\"predeploy\",\"hardworkKey\":\"xxx\"}",800,800 );
        PopupQrCodeDialog.Builder dialogBuild = new PopupQrCodeDialog.Builder(this);
        dialogBuild.setImage(bitmap);
        PopupQrCodeDialog dialog = dialogBuild.create();
        dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
        dialog.show();
        MqttManager.getInstance().creatConnect("","lens_NFOphbu5QmbEND5OcNpIeWQTmFy");




    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MqttMessage event) {
        Log.i("test_constraintlayout", "message is ");
        // 更新界面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
