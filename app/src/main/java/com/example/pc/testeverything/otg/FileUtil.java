package com.example.pc.testeverything.otg;

import android.Manifest;
import android.os.Environment;
import android.util.Log;

import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.os.Environment.getExternalStorageDirectory;

public class FileUtil {
    public static final String DEFAULT_BIN_DIR = "usb";

    /**
     * 检测SD卡是否存在
     */
    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 从指定文件夹获取文件
     *
     * @param folderPath com.example.pc.testeverything/usb
     * @param fileNmae   u_disk.txt
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileNmae);
        try {
            //创建文件之前，要先把该文件的上层文件夹全部创建
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("hsw", "IOException" + e);
        }
        return file;
    }

    /**
     * 获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getExternalStorageDirectory()
                .getAbsoluteFile()
                + File.separator
                + folderName
                + File.separator);
        file.mkdirs();
        return file;
    }

    /**
     * 关闭流
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void redFileStream(OutputStream os, InputStream is) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[1024 * 8];
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
        os.close();
        is.close();
    }

    /**
     * 把本地文件写入到U盘中
     *
     * @param f
     * @param usbFile
     */
    public static void saveSDFile2OTG(final File f, final UsbFile usbFile) {
        UsbFile uFile = null;
        FileInputStream fis = null;
        try {//开始写入
            fis = new FileInputStream(f);//读取选择的文件的
            if (usbFile.isDirectory()) {//如果选择是个文件夹
                UsbFile[] usbFiles = usbFile.listFiles();
                if (usbFiles != null && usbFiles.length > 0) {
                    for (UsbFile file : usbFiles) {
                        if (file.getName().equals(f.getName())) {
                            file.delete();
                        }
                    }
                }
                uFile = usbFile.createFile(f.getName());
                UsbFileOutputStream uos = new UsbFileOutputStream(uFile);
                try {
                    redFileStream(uos, fis);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}

