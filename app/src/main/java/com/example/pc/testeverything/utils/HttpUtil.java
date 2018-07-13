package com.example.pc.testeverything.utils;



        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;

/**
 * Created by xzh on 2017/2/6.
 *  网络请求工具类
 *
 */

public class HttpUtil {

    /**
     *  请求服务器
     * @param address   请求地址
     * @param requestBody   请求主体
     * @param callback  响应
     */
    public static void sendOkHttpRequest(String address, RequestBody requestBody,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}