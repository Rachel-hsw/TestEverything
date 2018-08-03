package com.example.pc.testeverything.Recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by PC on 2018/8/3.
 * 外部拦截法，解决滑动冲突  https://blog.csdn.net/a992036795/article/details/51735501
 * 重写父控件的onInterceptTouchEvent方法，然后根据具体的需求，来决定父控件是否拦截事件。如果拦截返回返回true，不拦截返回false。
 */

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
