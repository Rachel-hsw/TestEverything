package com.example.pc.testeverything.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.testeverything.MyApplication;
import com.example.pc.testeverything.R;


public class ToastUtils {
    private static ToastUtils toastUtils;
    private Toast toast;

    public static ToastUtils get() {
        if (toastUtils == null) toastUtils = new ToastUtils();
        return toastUtils;
    }

    public void showText(String msg) {
        showToast(msg, false, 0, Toast.LENGTH_SHORT);
    }

    public void showText(String msg, int imgLeft) {
        showToast(msg, true, imgLeft, Toast.LENGTH_SHORT);
    }

    private void showToast(String msg, boolean showImg, int imgId, int type) {
        showToast(MyApplication.getContext(), msg, showImg, imgId, type);
    }

    private void showToast(Context context, final String msg, final boolean showImg, final int imgId, final int type) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new BaseRunnable(context) {
                public void run(Object context) {
                    toast = Toast.makeText((Context) context, "", type);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setView(bindView((Context) context, msg, showImg, imgId));
                    toast.show();
                }
            });
        } else {
            toast = Toast.makeText(context, "", type);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(bindView(context, msg, showImg, imgId));
            toast.show();
        }

    }

    private View bindView(Context context, String text, boolean showImg, int imgId) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_text);
        ImageView img = (ImageView) view.findViewById(R.id.toast_img);
        textView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) textView.setText(text);
        if (showImg && imgId != 0) {
            img.setVisibility(View.VISIBLE);
            try {
                img.setImageResource(imgId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else img.setVisibility(View.GONE);
        return view;
    }
}
