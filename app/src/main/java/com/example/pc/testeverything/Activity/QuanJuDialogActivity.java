package com.example.pc.testeverything.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.example.pc.testeverything.MyApplication;
import com.example.pc.testeverything.R;

public class QuanJuDialogActivity extends Activity {
    private Handler mHandler = null;
    private Dialog netWorkDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanju_mian_layout);
        mHandler = new Handler();
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //第二种方式
                        WindowUtils.showPopupWindow(QuanJuDialogActivity.this);
                    }
                }, 1000 * 3);
            }
        });
    }

    /**
     * 第一种方式
     *
     * @param msg
     */
    private void showNetWorkPrintConnectLost(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getContext());
        builder.setTitle("请确认下列打印机已正常连接到局域网")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        netWorkDialog.dismiss();
                    }
                });
        builder.create();
        netWorkDialog = builder.create();
        netWorkDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        netWorkDialog.show();
        netWorkDialog.setCanceledOnTouchOutside(false);
    }
    //第三种方式，直接启动一个activity
}
