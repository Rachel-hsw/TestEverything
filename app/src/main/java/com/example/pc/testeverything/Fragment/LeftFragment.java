package com.example.pc.testeverything.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.testeverything.R;

/**
 * Created by PC on 2018/6/13.
 * https://blog.csdn.net/itachi85/article/details/77826112
 * Fragment测试内存泄漏
 */

public class LeftFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("hsw", "------------");
        //测试内存泄漏
//        LeakThread leakThread = new LeakThread();
//        leakThread.start();
    }
//    测试内存泄漏
//    class LeakThread extends Thread {
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(6 * 60 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //测试内存泄漏
//        MyApplication.getRefWatcher().watch(this);
    }
}