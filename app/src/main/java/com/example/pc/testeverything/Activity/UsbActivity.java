package com.example.pc.testeverything.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.pc.testeverything.R;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileStreamFactory;
import com.github.mjdev.libaums.partition.Partition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;


/**
 * 写了一个简单测试
 *
 * @author Rachel
 */
public class UsbActivity extends AppCompatActivity {
    private static final String TAG = "Rachel_test";
    private static final String ACTION_USB_PERMISSION = "com.demo.otgusb.USB_PERMISSION";
    private UsbMassStorageDevice[] storageDevices;
    private TextView logTv;
    /**
     * 监听USB设备的广播
     */
    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            switch (intentAction) {
                case ACTION_USB_PERMISSION:
                    //自定义Action
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        logShow("用户同意USB设备访问权限");
                        readDevice(getUsbMass(device));
                    } else {
                        logShow("用户拒绝USB设备访问权限");
                    }
                    break;
                case ACTION_USB_DEVICE_ATTACHED:
                    logShow("U盘设备插入");
                    if (device != null) {
                        redUDiskDeviceList();
                    }
                    break;
                case ACTION_USB_DEVICE_DETACHED:
                    logShow("U盘设备移除");
                    break;
                default:
                    Log.i(TAG, "----------------------------------");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u);
        logTv = (TextView) findViewById(R.id.log_tv);
        init();
    }

    /**
     * 读写权限
     */
    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        }
        registerUsbReceiver();
        redUDiskDeviceList();
    }

    /**
     * 权限申请回调
     *
     * @param requestCode 标识码
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            registerUsbReceiver();
            redUDiskDeviceList();
        }
    }

    /**
     * 动态注册监听USB设备的广播。
     * 由于耗电等原因，8.0不能对大部分的广播进行静态注册
     */
    private void registerUsbReceiver() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction("android.hardware.usb.action.USB_STATE");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        registerReceiver(mUsbReceiver, filter);
    }

    /**
     * 获取存储设备
     *
     * @param usbDevice 与android设备连接在一起的USB设备
     * @return Usb设备的一个包装类
     */
    private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
        for (UsbMassStorageDevice device : storageDevices) {
            if (usbDevice.equals(device.getUsbDevice())) {
                return device;
            }
        }
        return null;
    }

    /**
     * 初始化USB设备
     *
     * @param device USB设备
     */
    private void readDevice(UsbMassStorageDevice device) {
        try {
            device.init();//初始化
            //设备分区
            Partition partition = device.getPartitions().get(0);
            //文件系统
            FileSystem currentFs = partition.getFileSystem();
            //可以获取到设备的标识
            currentFs.getVolumeLabel();
            //通过FileSystem可以获取当前U盘的一些存储信息，包括剩余空间大小，容量等等
            Log.e("Volume Label: ", currentFs.getVolumeLabel());
            Log.e("Capacity: ", fSize(currentFs.getCapacity()));
            Log.e("Occupied Space: ", fSize(currentFs.getOccupiedSpace()));
            Log.e("Free Space: ", fSize(currentFs.getFreeSpace()));
            Log.e("Chunk size: ", fSize(currentFs.getChunkSize()));
            readAndWriteTest(currentFs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * USB设备读取
     */
    private void redUDiskDeviceList() {
        Context context = this;
        //设备管理器
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        //获取U盘存储设备
        storageDevices = UsbMassStorageDevice.getMassStorageDevices(context);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        //一般手机只有1个OTG插口
        if (storageDevices.length <= 0) {
            return;
        }
        UsbMassStorageDevice device = storageDevices[0];
        //读取设备是否有权限
        if (usbManager.hasPermission(device.getUsbDevice())) {
            readDevice(device);
        } else {
            //没有权限，进行申请
            usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
        }
    }

    private void logShow(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTv.setText(logTv.getText() + "\n" + s);
            }
        });
    }


    /**
     * 读写文件尝试
     *
     * @param currentFs
     */
    private void readAndWriteTest(FileSystem currentFs) {
        try {
            //设置当前文件对象为根目录
            UsbFile usbFile = currentFs.getRootDirectory();
            UsbFile[] files = usbFile.listFiles();
            for (UsbFile file : files) {
                logShow("文件: " + file.getName());
            }
            // 新建文件

            UsbFile newFile = usbFile.createFile("hello_" + System.currentTimeMillis() + ".txt");
            logShow("新建文件: " + newFile.getName());

            // 写文件
            OutputStream os = UsbFileStreamFactory.createBufferedOutputStream(newFile, currentFs);
            os.write(("hi_" + System.currentTimeMillis()).getBytes());
            os.close();
            logShow("写文件: " + newFile.getName());
            // 读文件
            // InputStream is = new UsbFileInputStream(newFile);
            InputStream is = UsbFileStreamFactory.createBufferedInputStream(newFile, currentFs);
            byte[] buffer = new byte[currentFs.getChunkSize()];
            int len;
            File sdFile = new File("/sdcard/111");
            sdFile.mkdirs();
            FileOutputStream sdOut = new FileOutputStream(sdFile.getAbsolutePath() + "/" + newFile.getName());
            while ((len = is.read(buffer)) != -1) {
                sdOut.write(buffer, 0, len);
            }
            is.close();
            sdOut.close();
            logShow("读文件: " + newFile.getName() + " ->复制到/sdcard/111/");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String fSize(long sizeInByte) {
        if (sizeInByte < 1024) {
            return String.format("%s", sizeInByte);
        } else if (sizeInByte < 1024 * 1024) {
            return String.format(Locale.CANADA, "%.2fKB", sizeInByte / 1024.);
        } else if (sizeInByte < 1024 * 1024 * 1024) {
            return String.format(Locale.CANADA, "%.2fMB", sizeInByte / 1024. / 1024);
        } else {
            return String.format(Locale.CANADA, "%.2fGB", sizeInByte / 1024. / 1024 / 1024);
        }
    }
}
