package com.example.pc.testeverything.otg;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.storage.StorageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.pc.testeverything.R;
import com.example.pc.testeverything.utils.ToastUtils;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.partition.Partition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OtgActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OtgActivity";
    //输入的内容
    private EditText u_disk_edt;
    //写入到U盘
    private Button u_disk_write;
    private Button install;
    //从U盘读取
    private Button u_disk_read;
    //显示读取的内容
    private TextView u_disk_show;
    //自定义U盘读写权限
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    //当前处接U盘列表
    private UsbMassStorageDevice[] storageDevices;
    //当前U盘所在文件目录
    private UsbFile cFolder;
    private final static String U_DISK_FILE_NAME = "u_disk.txt";
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION};
    List<String> path;
    /**
     * OTG广播，监听U盘的插入及拔出
     */
    private BroadcastReceiver mOtgReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            UsbInterface anInterface = device.getInterface(0);
            int interfaceClass = anInterface.getInterfaceClass();
            switch (action) {
                case ACTION_USB_PERMISSION://接受到自定义广播
                    add("接受到自定义广播interfaceClass" + interfaceClass);
                    //允许权限申请
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //用户已授权，可以进行读取操作
                            add("接受到自定义广播interfaceClass" + device);
//                            path = getUSBPaths(OtgActivity.this);
//                            readDevice(getUsbMass(device));
                        } else {
                            showToastMsg("没有插入U盘");
                        }
                    } else {
                        showToastMsg("未获取到U盘权限");
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_ATTACHED://接收到U盘设备插入广播
                    add("接收到U盘设备插入广播interfaceClass" + interfaceClass);
                    if (device != null) {
                        //接收到U盘插入广播，尝试读取U盘设备数据
//                        path = getUSBPaths(OtgActivity.this);
//                        redUDiskDevsList();
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED://接收到U盘设设备拔出广播
                    add("接收到U盘设设备拔出广播interfaceClass" + interfaceClass);
//                    path = getUSBPaths(OtgActivity.this);
                    showToastMsg("U盘已拔出");
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.u_disk_write:
                requestStorePermission();
                beginInstallLocl();
                break;
            case R.id.u_disk_read:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            readFromUDisk();
                        } catch (Exception e) {
                            e.printStackTrace();
                            add("-------------：" + e);
                        }
                    }
                });
                break;
            case R.id.install:
                beginInstall();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    showToastMsg("保存成功");
                    break;
                case 101:
                    if (msg.obj != null) {
                        String txt = msg.obj.toString();
                        if (!TextUtils.isEmpty(txt))
                            u_disk_show.setText("读取到的数据是：" + txt);
                    } else {
                        u_disk_show.setText("读取到的数据是：空");
                    }
                    break;
            }
        }
    };
    RecyclerView hsw;
    TextAdapter adapter;
    List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_otg);
        registerUDiskReceiver();
        initViews();
        dataList = new ArrayList<>();
        hsw = (RecyclerView) findViewById(R.id.hsw);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        hsw.setLayoutManager(layoutManage);
        adapter = new TextAdapter();
        adapter.setData(dataList);
        hsw.setAdapter(adapter);
        path = getUSBPaths(this);
        traverseFolder(path.get(0));
        add(packageCode(this) + "");
        add("getUSBPaths:" + JSON.toJSONString(path));
        List<String> pathList = getAllExternalSdcardPath();

        add("getAllExternalSdcardPath:" + JSON.toJSONString(pathList));
    }

    private void traverseFolder(String path) {
        File file = new File(path);
        if (!file.exists()) return;
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    traverseFolder(file1.getAbsolutePath());
                } else {
                    add("____________________________" + file1.getAbsolutePath());
                }
            }
        }
    }

    private void beginInstall() {
        if (path != null && path.size() != 0) {
            ToastUtils.get().showText("开始安装");
            String apkPath = path.get(0) + File.separator + "aa.apk";
            File file = new File(apkPath);
            String[] command = {"chmod", "777", file.getPath()};
            ProcessBuilder builder = new ProcessBuilder(command);
            try {
                builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
//                installApk(file);
                install(this, file);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.get().showText("开始安装,出现异常");
                add("+++++++++++出现异常" + e);
            }

//            install(this, apkPath);
        } else {
            ToastUtils.get().showText("路径为空，无法安装");
        }
    }

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    private void beginInstallLocl() {
        ToastUtils.get().showText("开始安装");
        String apkPath = SD_PATH + File.separator + "bb.apk";
        String p = SD_PATH + File.separator + "hh.txt";
        File pf = new File(p);
        try {
            pf.getParentFile().mkdirs();
            pf.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(apkPath);
//        String[] command = {"chmod", "777", file.getPath() }; ProcessBuilder builder = new ProcessBuilder(command); try { builder.start(); } catch (IOException e) { e.printStackTrace(); }

        boolean result = check(this, apkPath);
        add("=======================" + result);
        add("====================++===" + pf.exists());
        try {
            install(this, file);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.get().showText("开始安装,出现异常");
            add("+++++++++++出现异常" + e);
        }


    }

    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    private void add(String string) {
        dataList.add(string);
        adapter.setData(dataList);

    }

    public void install(Context context, String filePath) {
        add("+++++++++++install:" + filePath);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void install(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri uri = FileProvider.getUriForFile(context, "com.example.pc.testeverything.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    private void installApk(File file) {
        Intent intent = new Intent(android.content.Intent.ACTION_INSTALL_PACKAGE);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        startActivity(intent);
    }

    public static boolean check(Context context, String filePath) {
        PackageManager pm = context.getPackageManager();
        return pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES) != null;
    }

    private void initViews() {
        u_disk_edt = (EditText) findViewById(R.id.u_disk_edt);
        u_disk_write = (Button) findViewById(R.id.u_disk_write);
        install = (Button) findViewById(R.id.install);
        u_disk_read = (Button) findViewById(R.id.u_disk_read);
        u_disk_show = (TextView) findViewById(R.id.u_disk_show);
        u_disk_write.setOnClickListener(this);
        u_disk_read.setOnClickListener(this);
        install.setOnClickListener(this);
    }


    private void requestStorePermission() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            int l = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
            int m = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]);
            int n = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED || l != PackageManager.PERMISSION_GRANTED || m != PackageManager.PERMISSION_GRANTED ||
                    n != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                startRequestPermission();
            }
        }
    }

    /**
     * 开始提交请求权限
     */
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    /**
     * 用户权限 申请 的回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
                } else {
                    //获取权限成功提示，可以不要
                    Toast toast = Toast.makeText(this, "获取权限成功", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    final String content = u_disk_edt.getText().toString().trim();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            /*saveText2UDisk(content);*/
                        }
                    });
                }
            }
        }
    }

    private void readFromUDisk() {
        UsbFile[] usbFiles = new UsbFile[0];
        add("readFromUDisk0--- ");
        try {
            if (cFolder != null) {
                usbFiles = cFolder.listFiles();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        add("readFromUDisk1 ");
        if (null != usbFiles && usbFiles.length > 0) {
            add("readFromUDisk2 ");
            for (UsbFile usbFile : usbFiles) {
//                if (usbFile.getName().equals(U_DISK_FILE_NAME)) {
//                    readTxtFromUDisk(usbFile);
//                }else {
//                    if (usbFile.getName().equals("aa.apk")){
//                        ToastUtils.get().showText("存在apk文件");
//                        copy(usbFile,getFilesDir().toString());
//                    }
//                }
                try {
                    if (usbFile.getName().equals("aa.apk")) {
                        ToastUtils.get().showText("存在apk文件");
                        copyFile(usbFile.getParent(), getFilesDir().getAbsolutePath() + File.separator + "aa.apk");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    add("-----------存在apk文件：" + e);
                }
            }
        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 保存数据到U盘，目前是保存到根目录的
     *
     * @param content
     */
    private void saveText2UDisk(String content) {///storage/emulated/0/com.example.pc.testeverything/usb/u_disk.txt
        //项目中也把文件保存在了SD卡，其实可以直接把文本读取到U盘指定文件
        File file = FileUtil.getSaveFile(getPackageName()
                        + File.separator + FileUtil.DEFAULT_BIN_DIR,
                U_DISK_FILE_NAME);
        Toast.makeText(OtgActivity.this, "获取权限成功", Toast.LENGTH_LONG).show();
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != cFolder) {
            FileUtil.saveSDFile2OTG(file, cFolder);
            mHandler.sendEmptyMessage(100);
        }
    }

    /**
     * OTG广播注册
     */
    private void registerUDiskReceiver() {
        //监听otg插入 拔出
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mOtgReceiver, usbDeviceStateFilter);
        //注册监听自定义广播
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mOtgReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    public void unRegisterReceiver() {
        unregisterReceiver(mOtgReceiver);
    }

    /**
     * U盘设备读取
     */
    private void redUDiskDevsList() {
        add("U盘设备读取redUDiskDevsList1");
        //设备管理器
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        //获取U盘存储设备
        storageDevices = UsbMassStorageDevice.getMassStorageDevices(this);
        add("U盘设备读取redUDiskDevsList+storageDevices" + JSON.toJSONString(storageDevices));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        //一般手机只有1个OTG插口
        for (UsbMassStorageDevice device : storageDevices) {
            //读取设备是否有权限
            if (usbManager.hasPermission(device.getUsbDevice())) {
                add("U盘设备读取redUDiskDevsList2");
                readDevice(device);
            } else {
                //没有权限，进行申请
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
            }
        }
        if (storageDevices.length == 0) {
            showToastMsg("请插入可用的U盘");
        }
    }

    private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
        for (UsbMassStorageDevice device : storageDevices) {
            if (usbDevice.equals(device.getUsbDevice())) {
                return device;
            }
        }
        return null;
    }

    private void readDevice(UsbMassStorageDevice device) {
        add("U盘设备读取redUDiskDevsList4readDevice");
        try {
            device.init();//初始化
            //设备分区
            Partition partition = device.getPartitions().get(0);
            //文件系统
            FileSystem currentFs = partition.getFileSystem();
            currentFs.getVolumeLabel();//可以获取到设备的标识
            //通过FileSystem可以获取当前U盘的一些存储信息，包括剩余空间大小，容量等等
            add("Capacity: " + currentFs.getCapacity() + "");
            add("Occupied: " + currentFs.getOccupiedSpace() + "");
            add("Free: " + currentFs.getFreeSpace() + "");
            add("Capacity: " + currentFs.getChunkSize() + "");
            add("设置当前文件对象为根目录: " + currentFs.getRootDirectory());
            Log.e("Capacity: ", currentFs.getCapacity() + "");
            Log.e("Occupied Space: ", currentFs.getOccupiedSpace() + "");
            Log.e("Free Space: ", currentFs.getFreeSpace() + "");
            Log.e("Chunk size: ", currentFs.getChunkSize() + "");
            cFolder = currentFs.getRootDirectory();//设置当前文件对象为根目录
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 复制文件
     *
     * @param fromFile 要复制的文件目录
     * @param toFile   要粘贴的文件目录
     * @return 是否复制成功
     */
    private static int mIdent = 0;

    public boolean copyFile(UsbFile fromFile, String toFile) {
        CopySdcardFile(fromFile, toFile);
        return true;
    }

    public boolean copy(UsbFile fromFile, String toFile) {
        if (fromFile == null) return false;
        //要复制的文件目录
        UsbFile[] currentFiles = new UsbFile[0];
        //如果存在则获取当前目录下的全部文件 填充数组
        try {
            currentFiles = fromFile.listFiles();
            mIdent = currentFiles.length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (targetDir.exists()) {
            targetDir.delete();
        }
        targetDir.mkdirs();
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copy(fromFile, toFile + currentFiles[i].getName() + "/");
            } else//如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i], toFile + "/" + currentFiles[i].getName());
            }
        }
        return true;
    }

    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public boolean CopySdcardFile(UsbFile cFolder, String toFile) {
        ToastUtils.get().showText("正在拷贝文件");
        try {
            InputStream inusb = new UsbFileInputStream(cFolder);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024 * 10];
            int c;
            while ((c = inusb.read(bt)) != -1) {
                fosto.write(bt, 0, c);
            }
            mIdent--;
            Log.i(" songkunjian", " 完成拷贝==:" + mIdent);
            if (mIdent <= 0) {
                Log.i(" songkunjian", " 最终完成拷贝==:");
                ToastUtils.get().showText("拷贝完成");
            }
            fosto.flush();
            inusb.close();
            fosto.close();
            return true;
        } catch (Exception ex) {
            ToastUtils.get().showText("拷贝出现问题");
            add("拷贝出现问题" + ex);
            return false;
        }
    }

    //获取USB文件夹的大小 以便显示拷贝进度条
    private long prossgerzise = 0;

    public long getUsbFileLength(UsbFile fromFile) {
        if (fromFile == null) return 0;
        //要复制的文件目录
        UsbFile[] currentFiles = new UsbFile[0];
        //如果存在则获取当前目录下的全部文件 填充数组
        try {
            currentFiles = fromFile.listFiles();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                getUsbFileLength(currentFiles[i]);
            } else//如果当前项为文件则进行文件拷贝
            {
                prossgerzise += currentFiles[i].getLength();
            }
        }
        return prossgerzise;
    }


    private void readTxtFromUDisk(UsbFile usbFile) {
        UsbFile descFile = usbFile;

        //读取文件内容
        InputStream is = new UsbFileInputStream(descFile);
        //读取秘钥中的数据进行匹配
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        add("readFromUDisk3 ");
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                sb.append(read);
            }
            add("readFromUDisk5 " + sb);
            Message msg = mHandler.obtainMessage();
            msg.what = 101;
            msg.obj = sb;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getUSBPaths(Context con) {
        String[] paths = null;
        List<String> data = new ArrayList<String>();
        // include sd and usb devices
        StorageManager storageManager = (StorageManager) con
                .getSystemService(Context.STORAGE_SERVICE);
        try {
            paths = (String[]) StorageManager.class.getMethod("getVolumePaths", new Class[0]).invoke(
                    storageManager, new Object[]{});
            add("paths:" + JSON.toJSONString(paths));
            for (String path : paths) {
                String state = (String) StorageManager.class.getMethod("getVolumeState",
                        String.class).invoke(storageManager, path);
                if (state.equals(Environment.MEDIA_MOUNTED) && !path.contains("emulated")) {
                    data.add(path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<String> getAllExternalSdcardPath() {
        List<String> PathList = new ArrayList<String>();

        String firstPath = Environment.getExternalStorageDirectory().getPath();
        Log.d(TAG, "getAllExternalSdcardPath , firstPath = " + firstPath);

        try {
            // 运行mount命令，获取命令的输出，得到系统中挂载的所有目录
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                add("line:" + line);
                // 将常见的linux分区过滤掉
                if (line.contains("proc") || line.contains("tmpfs") || line.contains("media") || line.contains("asec") || line.contains("secure") || line.contains("system") || line.contains("cache")
                        || line.contains("sys") || line.contains("data") || line.contains("shell") || line.contains("root") || line.contains("acct") || line.contains("misc") || line.contains("obb")) {
                    continue;
                }
                // 下面这些分区是我们需要的
                if (line.contains("fat") || line.contains("fuse") || (line.contains("ntfs"))) {
                    // 将mount命令获取的列表分割，items[0]为设备名，items[1]为挂载路径
                    String items[] = line.split(" ");
                    if (items != null && items.length > 1) {
                        String path = items[1].toLowerCase(Locale.getDefault());
                        // 添加一些判断，确保是sd卡，如果是otg等挂载方式，可以具体分析并添加判断条件
                        PathList.add(items[1]);
//                        if (path != null && !PathList.contains(path) && path.contains("sd")) {
//                            PathList.add(items[1]);
//                        } else if (path != null && !PathList.contains(path) && path.contains("sd")) {
//                            PathList.add(items[1]);
//                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!PathList.contains(firstPath)) {
            PathList.add(firstPath);
        }

        return PathList;
    }

    List<File> mArrayList;
//
//    private void traverseFolder(String path) {
//        File file = new File(path);
//        if (file.exists()) {
//            File[] files = file.listFiles();
//            if (files.length == 0) {
//                Log.i(TAG, file.getAbsolutePath() + "\t" + "is null");
//                return;
//            } else {
//                for (File file2 : files) {
//                    if (file2.isDirectory()) {
//                        Log.i(TAG, "文件夹:" + file2.getAbsolutePath());
//                        traverseFolder(file2.getAbsolutePath());
//                    } else {
//                        mArrayList.add(file2);
//                    }
//                }
//            }
//        } else {
//            Log.i(TAG, "文件夹不存在");
//        }
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}