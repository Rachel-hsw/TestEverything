package com.tiidian.log;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.glens.android.log.Logger;
import com.glens.android.log.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class LogManager {

    private static LogManager instance;
    // 文件路径
    private final String LogUploadFileName = "upload";
    private final String logChildName = "log.txt";
    private String LogChildPath;
    private HashMap<String, Logger> AllLogger;
    // 上传服务器地址
    private String endPoint = "log.tedia.info";
    private String accessKeyId = "LTAIBn0a4TUInWpn";
    private String accessKeySecret = "gxON2L1uqZVqWOZUZuzl6pD3wHS5uk";
    private String yourBucketName = "tedia-log-bucket";

    private LogManager() {

    }

    public static LogManager get() {
        if (instance == null) {
            instance = new LogManager();
        }

        return instance;
    }


    private void initPath(String logDetailPath) {
        LogChildPath = logDetailPath + File.separator + logChildName;
    }

    /**
     * 通过类对象 得到操作日志的对象
     *
     * @param clazz 类对象
     * @return
     */
    public Logger getLogger(Class<?> clazz) {
        if (AllLogger == null) {
            AllLogger = new HashMap<>();
        }

        if (AllLogger.containsKey(clazz.getName())) {
            return AllLogger.get(clazz.getName());
        } else {
            Logger log = LoggerFactory.getLogger(clazz);
            AllLogger.put(clazz.getName(), log);
            return log;
        }
    }

    public void init(String logDetailPath) {
        initPath(logDetailPath);

        fileDirectoryExistOrCreate(logDetailPath);

        LoggerFactory loggerFactory = LoggerFactory.getInstance();
        loggerFactory.setFileName(LogChildPath); // 设置日志文件名
        loggerFactory.setMaxFileSize(2 * 1024 * 1024); // 设置单个日志文件大小为2Mb
        loggerFactory.setMaxBackupSize(100); // 设置日志文件数量
        loggerFactory.setRootLevel(LoggerFactory.DEBUG); // 设置日志级别
        loggerFactory.configure();

        packageLog(logDetailPath);
    }

    private void packageLog(final String logPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 当log文件超过10个打包，并删除所有log文件
                File logFileFolder = new File(logPath);
                int logFileCount = logFileFolder.listFiles().length;
                if (logFileCount >= 100) {
                    File zipFile = new File(logFileFolder.getParent(),
                            "log_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date())
                                    + "_" + UUID.randomUUID().toString() + ".zip");

                    try {
                        // 在恢复文件之前先把IM_LOG文件夹下所有的文件都打成zip压缩包
                        ArrayList<File> files = new ArrayList<>();
                        files.add(logFileFolder);
                        ZipUtils.zipFiles(files, zipFile);

                        // 删除打包过的文件
                        isFileDeleteSuccess(logFileFolder);

                        // 复制该zip文件到log文件夹中
                        zipFile.renameTo(new File(logFileFolder, zipFile.getName()));
                    } catch (Exception e) {
                        LogManager.get().getLogger(LogManager.class).error("log文件过多，打包log文件失败", e);
                    }
                }
            }
        }).start();
    }

    private void fileDirectoryExistOrCreate(String logDetailPath) {
        File file = new File(logDetailPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 上传log文件
     *
     * @param mainFile 主目录
     * @param fileList 需要上传的文件列表（可以是文件夹）
     * @return 是否上传成功
     */
    public boolean uploadLogFile(Context context, File mainFile, ArrayList<File> fileList,
                                 String preString) {
        try {
            // 判断是否在主线程上
            if (Looper.myLooper() == Looper.getMainLooper()) {
                LogManager.get().getLogger(LogManager.class).error("非子线程无法调用上传日志方法");
                return false;
            }

            // 判断是否有上传的数据
            if (fileList.size() == 0) {
                LogManager.get().getLogger(LogManager.class).info("打包时没有文件需要打包");
                return true;
            }

            // 判断是否上传的数据是否存在
            ArrayList<File> data = new ArrayList<>();
            for (File item : fileList) {
                if (item.exists()) {
                    data.add(item);
                }
            }

            File logUploadFile = new File(mainFile, LogUploadFileName);

            // 打包zip文件失败
            if (!logUploadFile.exists() && !logUploadFile.mkdirs()) {
                LogManager.get().getLogger(LogManager.class).error("生成上传目录失败，无法上传");
                return false;
            }

            File file = new File(logUploadFile, preString + ".zip");
            try {
                // 打包文件
                ZipUtils.zipFiles(data, file);

                // 删除打包成功的文件
                for (File item : data) {
                    isFileDeleteSuccess(item);
                }
            } catch (IOException e) {
                LogManager.get().getLogger(LogManager.class).error("生成zip文件失败", e);
                return false;
            }

            if (logUploadFile.listFiles().length == 0) {
                LogManager.get().getLogger(LogManager.class).error("文件打包失败，无打包文件生成");
                return false;
            }

            // accessKey请登录https://ak-console.aliyun.com/#/查看
            // 创建OSSClient实例
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
            OSS oss = new OSSClient(context, endPoint, credentialProvider);
            boolean isUploadSuccess = true;
            for (File item : logUploadFile.listFiles()) {
                try {
                    PutObjectRequest put = new PutObjectRequest(yourBucketName, item.getName().replace("_", "/"), item.getAbsolutePath());
                    PutObjectResult result = oss.putObject(put);
                    // 上传成功，删除本地文件
                    if (result.getStatusCode() == 200) {
                        isFileDeleteSuccess(item);
                    }
                } catch (ServiceException e) {
                    LogManager.get().getLogger(LogManager.class).error(item.getAbsoluteFile() + "---上传zip文件失败", e);
                    isUploadSuccess = false;
                } catch (ClientException e) {
                    LogManager.get().getLogger(LogManager.class).error(item.getAbsoluteFile() + "---上传zip文件失败", e);
                    isUploadSuccess = false;
                } catch (Exception e) {
                    LogManager.get().getLogger(LogManager.class).error(item.getAbsoluteFile() + "---上传zip文件失败", e);
                    isUploadSuccess = false;
                }
            }

            return isUploadSuccess;
        } catch (Exception e) {
            getLogger(LogManager.class).error("上传打包数据错误", e);
            return false;
        }
    }

    private boolean isFileDeleteSuccess(File parent) {
        if (parent.isDirectory()) {
            if (parent.list().length == 0) {
                return parent.delete();
            } else {
                boolean isSuccess = true;
                for (File childParent : parent.listFiles()) {
                    if (!isFileDeleteSuccess(childParent)) {
                        isSuccess = false;
                    }
                }

                return isSuccess;
            }
        } else {
            return TextUtils.equals(parent.getName(), logChildName) || parent.delete();
        }
    }
}
