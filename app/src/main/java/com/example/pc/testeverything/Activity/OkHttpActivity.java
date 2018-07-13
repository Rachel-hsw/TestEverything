package com.example.pc.testeverything.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.pc.testeverything.R;
import com.example.pc.testeverything.model.OkhttpResponse;
import com.example.pc.testeverything.utils.HttpUtil;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by PC on 2018/7/13.
 */

public class OkHttpActivity extends AppCompatActivity {
    private static final OkHttpClient mOkHttpClient;
    static {
        mOkHttpClient = new OkHttpClient();
    }
    private Button button;
    private String URL="http://192.168.3.20:2222/api/v1/order";
    private String orderid;
    private  String num;
    private  String sign="64846ce37209c4d60fb69aa910f3d3d678731e96";
    private  String time;
    private int headnum;
    private int middle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_layout);
        button= (Button) findViewById(R.id.button2);
        final EditText editText1= (EditText) findViewById(R.id.editText1);
        final EditText editText2= (EditText) findViewById(R.id.editText2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time= String.valueOf(System.currentTimeMillis());
                orderid=editText1.getText().toString();
                num=editText2.getText().toString();
                if (num.equals("")){
                    num="1";
                }
                if (orderid.equals("")){
                    orderid="1";
                }
                sendRequestWithOkHttp();
            }
        });
    }

    private void sendRequestWithOkHttp() {
        headnum=getNum(1,10);
        //生成隨機六位數
        middle=(int) ((Math.random() * 9 + 1) * 100000);
        new Thread(new MyThread()).start();
    }
    /**
     * 生成一个startNum 到 endNum之间的随机数(不包含endNum的随机数)
     * @param startNum
     * @param endNum
     * @return
     */
    public static int getNum(int startNum,int endNum){
        if(endNum > startNum){
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }
    /**
     * 创建网络请求线程
     */
    public class MyThread implements Runnable {
        @Override
        public void run() {
            Log.i("sign2：",sign);
            for (int i=0;i<Integer.valueOf(num);i++){
                orderid=String.valueOf(Integer.valueOf(orderid)+1);
                boolean isContinue = true;
                while (isContinue) {
                    try {
                        RequestBody body;
                        Request.Builder builder = new Request.Builder()
                                .url(URL);
                        JSONObject request = new JSONObject();
                        request.put("number",  headnum+middle+ orderid);
                        request.put("store", "1001");
                        request.put("ctime", 1531479552);
                        request.put("ptime", 1531479552);
                        request.put("stype", 1);
                        request.put("seq", orderid);
                        request.put("notify_url", "http://192.168.2.225/api/v1/notify");
                        request.put("qos", 1);
                        request.put("appid", "123");
                        request.put("sign", sign);
                        String jsonString;
                        SerializerFeature[] features;
                        features = new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat};
                        jsonString = JSON.toJSONString(request, features);
                        body = RequestBody.create(MediaType.parse("application/json"), jsonString);
                        builder.post(body);
                        Response response = mOkHttpClient.newCall(builder.build()).execute();

                        if (!response.isSuccessful()) {
                            return;
                        }

                        ResponseBody responseBody = response.body();

                        String responseMsg = responseBody.string();

                        Log.e("asd", responseMsg);

                        JSONObject jsonObject = JSON.parseObject(responseMsg);

                        int code = jsonObject.getInteger("code");
                        if (code == 0) {
                            Log.e("asd", "成功");

                        } else {
                            if (code == 2000) {
                                String msg = jsonObject.getString("msg");
                                sign = msg.split("。")[1];
                                continue;
                            }
                        }
                        isContinue = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
