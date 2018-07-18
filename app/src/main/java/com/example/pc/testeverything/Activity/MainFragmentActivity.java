package com.example.pc.testeverything.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pc.testeverything.Fragment.AnotherRightFragment;
import com.example.pc.testeverything.Fragment.RightFragment;
import com.example.pc.testeverything.R;

/**
 * Created by PC on 2018/6/13.
 */

public class MainFragmentActivity extends Activity implements View.OnClickListener {
     private  int  i=0;
     private AnotherRightFragment anotherRightFragment;
     private RightFragment rightFragment;


    /**
     *  每个活动中我们都重写了这个方法，它会在活动第一次被创建的时候调用。你应该在这个方法中完成活动的初始化操作，比如说加载布局、绑定事件等。
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        /*button.setEnabled(false);*/  //变灰色
        button.setClickable(false);
        anotherRightFragment = new AnotherRightFragment();
        rightFragment = new RightFragment();
        LinearLayout image= (LinearLayout) findViewById(R.id.image);
        image.setBackgroundResource(R.drawable.round_half_green);
        image.setBackgroundResource(R.drawable.round_half_red);
        image.setBackground(null);

    }

    /**
     *这个方法在活动由不可见变为可见的时候调用。
     */
    @Override
    protected void onStart() {
        super.onStart();
    }
    /**
     *这个方法在活动准备好和用户进行交互的时候调用。此时的活动一定位于返回栈的栈顶，并且处于运行状态。
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
    /**
     *这个方法在系统准备去启动或者恢复另一个活动的时候调用。我们通常会在这个方法中将一些消耗CPU 的资源释放掉，
     * 以及保存一些关键数据，但这个方法的执行速度一定要快，不然会影响到新的栈顶活动的使用。
     */
    @Override
    protected void onPause() {
        super.onPause();
    }
    /**
     * 这个方法在活动完全不可见的时候调用。它和onPause()方法的主要区别在于，如果启动的新活动是一个对话框式的活动，那么onPause()方法会得到执行，而onStop()方法并不会执行。
     */
    @Override
    protected void onStop() {
        super.onStop();
    }
    /**
     *这个方法在活动被销毁之前调用，之后活动的状态将变为销毁状态。
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     *这个方法在活动由停止状态变为运行状态之前调用，也就是活动被重新启动了。
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                try {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager. beginTransaction();
                    /*transaction.add(R.id.right_layout,anotherRightFragment);
                    transaction.add(R.id.right_layout,rightFragment);*/
                    if (i == 0) {
                      /*  transaction.hide(rightFragment).show(anotherRightFragment);*/
                    transaction.replace(R.id.right_layout, anotherRightFragment);

                        i = 1;
                    } else {
                          transaction.replace(R.id.right_layout, rightFragment);
                       /* transaction.hide(anotherRightFragment).show(rightFragment);*/
                        i = 0;
                    }
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;
            default:
                break;
        }
    }

   /* 这个方法你已经看到过很多次了，每个活动中我们都重写了这个方法，它会在活动第一次被创建的时候调用。你应该在这个方法中完成活动的初始化操作，比如说加载布局、绑定事件等。

            2. onStart()
    这个方法在活动由不可见变为可见的时候调用。

            3. onResume()
    这个方法在活动准备好和用户进行交互的时候调用。此时的活动一定位于返回栈的栈顶，并且处于运行状态。

            4. onPause()
    这个方法在系统准备去启动或者恢复另一个活动的时候调用。我们通常会在这个方法中将一些消耗CPU 的资源释放掉，以及保存一些关键数据，但这个方法的执行速度一定要快，不然会影响到新的栈顶活动的使用。

            5. onStop()
    这个方法在活动完全不可见的时候调用。它和onPause()方法的主要区别在于，如果启动的新活动是一个对话框式的活动，那么onPause()方法会得到执行，而onStop()方法并不会执行。

            6. onDestroy()
    这个方法在活动被销毁之前调用，之后活动的状态将变为销毁状态。

            7. onRestart()
*/
}