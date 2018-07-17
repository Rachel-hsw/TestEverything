package com.example.pc.testeverything.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pc.testeverything.MqttManager;
import com.example.pc.testeverything.PopupQrCodeDialog;
import com.example.pc.testeverything.R;
import com.example.pc.testeverything.ZXingUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 生成二维码，弹出二维码
 */
public class QRcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

      /*  Resources res = QRcodeActivity.this.getResources();
        Bitmap bitmap= BitmapFactory.decodeResource(res, R.mipmap.QRcodeActivity);// 这里是获取图片Bitmap，也可以传入其他参数到Dialog中*/
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
