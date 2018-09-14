package com.example.pc.testeverything.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.pc.testeverything.R;

import java.io.IOException;
import java.util.Random;

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
    private String URL = "http://192.168.2.225/api/v1/order";
    private String orderid;
    private String num;
    private String sign = "64846ce37209c4d60fb69aa910f3d3d678731e96";
    private String time;
    private int headnum;
    private int middle;
    private String orderNumber;
    private TextView content;
    private TextView reponse;
    private EditText url, store;
    String responseMsg;
    String jsonString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_layout);
        button = (Button) findViewById(R.id.button2);
        final EditText editText1 = (EditText) findViewById(R.id.editText1);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        url = (EditText) findViewById(R.id.editText3);
        url.setText(URL);
        content = (TextView) findViewById(R.id.content);
        reponse = (TextView) findViewById(R.id.reponse);
        store = (EditText) findViewById(R.id.editText4);
        store.setText("1001");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OkHttpActivity.this, "正在发送中", Toast.LENGTH_SHORT).show();
                v.setClickable(false);
                time = String.valueOf(System.currentTimeMillis());
                orderid = editText1.getText().toString();
                num = editText2.getText().toString();
                if (num.equals("")) {
                    num = "1";
                }
                if (orderid.equals("")) {
                    orderid = "1";
                }
                sendRequestWithOkHttp();
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
/*** 点击空白位置 隐藏软键盘*/
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
    private void sendRequestWithOkHttp() {
        //生成一位[1,9]随机数
        headnum = getNum(1, 10);
        //生成隨機六位數
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            str.append(new Random().nextInt(10));
        }
        try {
            middle = Integer.parseInt(str.toString());
        } catch (Exception e) {
            middle = (int) ((Math.random() * 9 + 1) * 100000);
        }
        new Thread(new MyThread()).start();
    }

    /**
     * 生成一个startNum 到 endNum之间的随机数(不包含endNum的随机数)
     *
     * @param startNum
     * @param endNum
     * @return
     */
    public static int getNum(int startNum, int endNum) {
        if (endNum > startNum) {
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
            for (int i = 0; i < Integer.valueOf(num); i++) {
                if (TextUtils.equals(orderid, "0")) {
                    orderid = "1";
                }
                boolean isContinue = true;
                while (isContinue) {
                    try {
                        RequestBody body;
                        Request.Builder builder = new Request.Builder()
                                .url(url.getText().toString());
                        JSONObject request = new JSONObject();
                        String orderString = getOrderId(i, orderid);
                        orderNumber= headnum + middle + orderString;
                        request.put("number", headnum + middle + orderString);
                        request.put("store", store.getText().toString());
                        request.put("ctime", 1531574708);
                        request.put("ptime", 1531574708);
                        request.put("stype", 1);
                        request.put("seq", orderString);
                        request.put("notify_url", "http://192.168.2.225/api/v1/notify");
                        request.put("qos", 1);
                        request.put("appid", "123");
                        request.put("sign", sign);

                        SerializerFeature[] features;
                        features = new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat};
                        jsonString = JSON.toJSONString(request, features);

                        Log.e("asd", "需要发送的数据为" + jsonString);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content.setText(content.getText().toString() + jsonString + "\n");
                            }
                        });
                        body = RequestBody.create(MediaType.parse("application/json"), jsonString);
                        builder.post(body);
                        Response response = mOkHttpClient.newCall(builder.build()).execute();

                        if (!response.isSuccessful()) {
                            return;
                        }

                        ResponseBody responseBody = response.body();

                        responseMsg = responseBody.string();

                        Log.e("asd", "返回的数据为：" + responseMsg);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = JSON.parseObject(responseMsg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                reponse.setText(reponse.getText().toString() + responseMsg + "\n");
                            }
                        });
                        int code = jsonObject.getInteger("code");
                        if (code == 0) {
                            Log.e("asd", "成功");
                           /* runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OkHttpActivity.this,"成功"+ orderNumber,Toast.LENGTH_LONG);
                                }
                        });*/
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

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setClickable(true);
                }
            });
        }
    }

    private String getOrderId(int i, String orderId) {
        if (TextUtils.isEmpty(orderId)){
            if (i < 10) {
                return "000" + i;
            } else if (i < 100) {
                return "00" + i;
            } else {
                return String.valueOf(i);
            }
        } else {
            int orderInt = 0;
            try {
                orderInt = Integer.parseInt(orderId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            orderId = String.valueOf(orderInt + i);

            if (orderId.length() == 1) {
                return "000" + orderId;
            } else if (orderId.length() == 2) {
                return "00" + orderId;
            } else {
                return orderId;
            }
        }
    }
}
