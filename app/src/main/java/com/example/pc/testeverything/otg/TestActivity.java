package com.example.pc.testeverything.otg;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pc.testeverything.utils.ToastUtils;

public class TestActivity extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "REQUEST.USB_PERMISSION";//USB外设权限
    private static final int ACTION_OPEN_CONNECTION = 1;
    private UsbManager usbManager;
    private static final String TAG = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerUsbReceiver();
    }

    /**
     * 注册监听USB设备的广播
     */
    private void registerUsbReceiver() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction("android.hardware.usb.action.USB_STATE");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        registerReceiver(mUsbReceiver, filter);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(intentAction)) {// 获取权限
                ToastUtils.get().showText("获取USB权限回调");
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    ToastUtils.get().showText("用户同意USB设备访问权限");
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    int action = intent.getIntExtra("action", ACTION_OPEN_CONNECTION);
                    if (action == ACTION_OPEN_CONNECTION) {

                    }
                } else {
                    ToastUtils.get().showText("用户拒绝USB设备访问权限");
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
                ToastUtils.get().showText("U盘设备插入");
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    /*requestUsbPermission(device);*/
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(intent.getAction())) {
                ToastUtils.get().showText("U盘设备移除");

            } else {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.i(TAG, "----------------------------------");
            }
        }
    };

    private void requestUsbPermission(UsbDevice device) {
        ToastUtils.get().showText("发起USB设备访问权限申请");
        Intent requestUSBPermission = new Intent(ACTION_USB_PERMISSION);
        requestUSBPermission.putExtra("action", ACTION_OPEN_CONNECTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, requestUSBPermission, PendingIntent.FLAG_CANCEL_CURRENT);
        usbManager.requestPermission(device, pendingIntent);
    }

}
