package com.example.pc.testeverything.Activity;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.testeverything.MyApplication;
import com.example.pc.testeverything.MyTextView;
import com.example.pc.testeverything.R;

import java.util.Calendar;
import java.util.TimeZone;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.pc.testeverything.DateUtil.getUnixTimeByCalendar;

public class PropertyAnimationActivity extends ActionBarActivity {

    private TextView textview;
    private Button scaleX;
    private Button scaleY;
    private Button alpha;
    private Button rotate;
    private Button translationX;
    private Button translationY;
    private Button EraseAnimation;
    private Button CutAnimation;
    private ValueAnimator va;
    private ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_property_animation);
        this.textview = (TextView) findViewById(R.id.textview);
        textview.setText("欢\n迎");
        MyTextView tv1 = (MyTextView) findViewById(R.id.welcome_tv);
        MyTextView tv2 = (MyTextView) findViewById(R.id.pcr_tv);
        MyTextView tv3 = (MyTextView) findViewById(R.id.tv3);
        getUnixTimeByCalendar();
        //获取整天的毫秒数-本时区偏移的毫秒数
        long currentZreo = System.currentTimeMillis() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        //获取此时的时间-距离零点便宜的时间
        long currentZre = System.currentTimeMillis() - (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
        Calendar canlder = Calendar.getInstance();
        canlder.set(Calendar.HOUR_OF_DAY, 0);
        canlder.set(Calendar.MINUTE, 0);
        canlder.set(Calendar.SECOND, 0);
        canlder.set(Calendar.MILLISECOND, 0);
        canlder.getTimeInMillis();
        int i = TimeZone.getDefault().getRawOffset() / 1000 / 60 / 60;

       /* //实例化TextView
        TextView sans = (TextView) findViewById(R.id.sans);
        //设置字体
        sans.setTypeface(MyApplication.getTypefaceKai());
        //实例化TextView
        TextView serif = (TextView) findViewById(R.id.serif);
        //设置字体
        serif.setTypeface(MyApplication.getTypefaceHei());*/
        //实例化TextView
        TextView monospace = (TextView) findViewById(R.id.monospace);
        //设置字体
        monospace.setTypeface(MyApplication.getTypefaceSiYuan());
        //实例化TextView
        TextView monospacec = (TextView) findViewById(R.id.monospacec);
        //设置字体
        monospacec.setTypeface(MyApplication.getTypefaceCaiYun());


        //实例化TextView
        TextView berirut = (TextView) findViewById(R.id.berirut);
        //设置字体
        berirut.setTypeface(MyApplication.getBerirut());
        //实例化TextView
        TextView sourcehansanscnheavy = (TextView) findViewById(R.id.sourcehansanscnheavy);
        //设置字体
        sourcehansanscnheavy.setTypeface(MyApplication.getSourcehansanscnheavy());
        //实例化TextView
        TextView notosanshansblack = (TextView) findViewById(R.id.notosanshansblack);
        //设置字体
        notosanshansblack.setTypeface(MyApplication.getNotosanshans());
        //实例化TextView
        TextView sourcehansanscnbold = (TextView) findViewById(R.id.sourcehansanscnbold);
        //设置字体
        sourcehansanscnbold.setTypeface(MyApplication.getSourcehansanscnbold());
        //实例化TextView
        TextView sourcehansanscnnormal = (TextView) findViewById(R.id.sourcehansanscnnormal);
        //设置字体
//        sourcehansanscnnormal.setTypeface(MyApplication.getSourcehansanscnnormal());
        Typeface simcaiyun = Typeface.createFromAsset(getAssets(), "simcaiyun.ttf");
        sourcehansanscnnormal.setTypeface(simcaiyun);

        // 设置滚动的速度
        tv1.setScrollMode(MyTextView.SCROLL_SLOW);
        tv2.setScrollMode(MyTextView.SCROLL_NORM);
        tv3.setScrollMode(MyTextView.SCROLL_FAST);
        this.view = (ImageView) findViewById(R.id.view);
        this.alpha = (Button) findViewById(R.id.alpha);
        this.rotate = (Button) findViewById(R.id.rotate);
        this.translationX = (Button) findViewById(R.id.translationX);
        this.translationY = (Button) findViewById(R.id.translationY);
        this.EraseAnimation = (Button) findViewById(R.id.EraseAnimation);
        this.CutAnimation = (Button) findViewById(R.id.CutAnimation);
        this.scaleX = (Button) findViewById(R.id.scaleX);
        this.scaleY = (Button) findViewById(R.id.scaleY);
        this.scaleX.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scaleX();
            }

        });

        this.scaleY.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scaleY();
            }
        });
        this.translationY.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                translation(2);
            }
        });
        this.translationX.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                translation(1);
            }
        });
        this.alpha.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alpha();
            }
        });

        this.rotate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rotate();
            }
        });
        this.EraseAnimation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EraseAnimation();
            }
        });
        this.CutAnimation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CutAnimation();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /*
     * 旋转动画
     */
    private void rotate() {
        this.va = ObjectAnimator.ofFloat(textview, "rotation", 0f, 360f);   //从0度旋转到360度
        this.va.setDuration(1000 * 60);   //设置从0度到360度的旋转时间
        //this.va.setRepeatCount(5);   重复5次
        //this.va.setRepeatMode(ValueAnimator.REVERSE);  播放完毕直接翻转播放
        //this.va.setRepeatMode(ValueAnimator.RESTART);  播放完毕直接从头播放
        this.va.start();
    }

    /*
     *左右移动 so  up and down  is translationY
     *flag==1  zuoyou
     *flag==2 shangxia
     */
    private void translation(int flag) {
        if (flag == 1)
            /*   this.va = ObjectAnimator.ofFloat(textview, "translationX", 0f,50f,5f);   //left and right*/
            this.va = ObjectAnimator.ofFloat(textview, "translationX", -textview.getWidth(), 0);
        else
            this.va = ObjectAnimator.ofFloat(textview, "translationY", -textview.getHeight(), 0);
        /*this.va = ObjectAnimator.ofFloat(textview, "translationY", 0f,50f,5f);   //top and bottom*/
        this.va.setDuration(1500);   //设置从0度到360度的旋转时间
        this.va.setRepeatCount(5);  // 重复5次
        //this.va.setRepeatMode(ValueAnimator.REVERSE);  播放完毕直接翻转播放
        //this.va.setRepeatMode(ValueAnimator.RESTART);  播放完毕直接从头播放
        this.va.start();
    }


    /*
     *上下移动移动
     */
    private void translationY() {
        /*   this.va = ObjectAnimator.ofFloat(textview, "translationY", 0f,50f,5f);   //left and right*/
        this.va = ObjectAnimator.ofFloat(textview, "translationX", -textview.getWidth(), 0);
        this.va.setDuration(1500);   //设置从0度到360度的旋转时间
        this.va.setRepeatCount(5);  // 重复5次
        //this.va.setRepeatMode(ValueAnimator.REVERSE);  播放完毕直接翻转播放
        //this.va.setRepeatMode(ValueAnimator.RESTART);  播放完毕直接从头播放
        this.va.start();
    }

    /**
     * 擦除动画
     */
    private void EraseAnimation() {
        this.va = ValueAnimator.ofInt(0, 10000);
 /*       final ClipDrawable clipDrawable = new ClipDrawable(view.getDrawable(), Gravity.RIGHT, ClipDrawable.HORIZONTAL);从右到左开始出现
        final ClipDrawable clipDrawable = new ClipDrawable(view.getDrawable(), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        final ClipDrawable clipDrawable = new ClipDrawable(view.getDrawable(), Gravity.BOTTOM, ClipDrawable.VERTICAL);*/
        final ClipDrawable clipDrawable = new ClipDrawable(view.getDrawable(), Gravity.TOP, ClipDrawable.VERTICAL);
        view.setImageDrawable(clipDrawable);
        this.va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int level = (int) animation.getAnimatedValue();
                clipDrawable.setLevel(level);
            }
        });

        this.va.setDuration(5000);   //设置变化间隔
        this.va.start();
    }

    /**
     * 切割动画
     */
    private void CutAnimation() {
        this.va = ValueAnimator.ofInt(0, 10000);
//        final ClipDrawable clipDrawable = new ClipDrawable(view.getDrawable(), Gravity.CENTER_HORIZONTAL, ClipDrawable.HORIZONTAL);//从中间到两边（左右）
        final ClipDrawable clipDrawable = new ClipDrawable(view.getDrawable(), Gravity.CENTER_VERTICAL, ClipDrawable.VERTICAL);//从中间到两边（上下）
        view.setImageDrawable(clipDrawable);
        this.va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int level = (int) animation.getAnimatedValue();
                clipDrawable.setLevel(level);
            }
        });

        this.va.setDuration(5000);   //设置变化间隔
        this.va.start();
    }

    /*
     * 改变透明度（渐渐消失）
     */
    private void alpha() {
        this.va = ObjectAnimator.ofFloat(this.textview, "alpha", 1f, 0f, 1f); //透明度从1变成0然后变成1，以此类推。。
        this.va.setDuration(5000);   //设置变化间隔
        //this.va.setRepeatCount(5);   重复5次
        //this.va.setRepeatMode(ValueAnimator.REVERSE);  播放完毕直接翻转播放
        //this.va.setRepeatMode(ValueAnimator.RESTART);  播放完毕直接从头播放
        this.va.start();
    }

    /*
     * 左右拉伸
     */
    private void scaleX() {
        this.va = ObjectAnimator.ofFloat(this.textview, "scaleX", 1f, 5f, 1f);
        this.va.setDuration(5000);
        //this.va.setRepeatCount(5);   重复5次
        //this.va.setRepeatMode(ValueAnimator.REVERSE);  播放完毕直接翻转播放
        //this.va.setRepeatMode(ValueAnimator.RESTART);  播放完毕直接从头播放
        this.va.start();

    }

    /*
     * 组合动画
     */
    private void bine() {
        ValueAnimator va1 = ObjectAnimator.ofFloat(this.textview, "scaleX", 1f, 5f, 1f);


        //this.va.setRepeatCount(5);   重复5次
        //this.va.setRepeatMode(ValueAnimator.REVERSE);  播放完毕直接翻转播放
        //this.va.setRepeatMode(ValueAnimator.RESTART);  播放完毕直接从头播放
        // this.va.start();
        ValueAnimator va2 = ObjectAnimator.ofFloat(this.textview, "scaleY", 1f, 5f, 1f); //透明度从1变成0然后变成1，以此类推。。

        //this.va.start();
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(va1).after(va2);
        animSet.setDuration(2000);
        animSet.start();

    }

    /*
     * 上下拉伸
     */
    private void scaleY() {
        this.va = ObjectAnimator.ofFloat(this.textview, "scaleY", 1f, 5f, 1f);
        this.va.setDuration(5000);
        //this.va.setRepeatCount(5);   重复5次
        //this.va.setRepeatMode(ValueAnimator.REVERSE);  播放完毕直接翻转播放
        //this.va.setRepeatMode(ValueAnimator.RESTART);  播放完毕直接从头播放
        this.va.start();

    }
}

