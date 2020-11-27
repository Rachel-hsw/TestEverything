package com.example.pc.testeverything;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationActivity extends Activity {

    TextView welcomeTv;
    TextView pcrTv;
    ImageView blueImg;
    ImageView grayImg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        welcomeTv = (TextView) findViewById(R.id.welcome_tv);
        pcrTv = (TextView) findViewById(R.id.pcr_tv);
        blueImg = (ImageView) findViewById(R.id.blue_img);
        grayImg = (ImageView) findViewById(R.id.gray_img);
        bine();
    }


    /**
     * 组合动画
     */
    private void bine() {
        //welcomeTv
        PropertyValuesHolder welcomeTv_1 = PropertyValuesHolder.ofFloat("scaleX", 0.98f, 1f);
        PropertyValuesHolder welcomeTv_2 = PropertyValuesHolder.ofFloat("scaleY", 0.98f, 1f);
        ObjectAnimator welcomeAnimator1 = ObjectAnimator.ofPropertyValuesHolder(welcomeTv, welcomeTv_1, welcomeTv_2);
        welcomeAnimator1.setInterpolator(new EaseCubicInterpolator(0.33f, 0f, 0.67f, 1f));
        welcomeAnimator1.setDuration(1100);

        ValueAnimator welcomeAnimator2 = ObjectAnimator.ofFloat(this.welcomeTv, "alpha", 0f, 1f);
        welcomeAnimator2.setInterpolator(new EaseCubicInterpolator(0.63f, 0f, 0.76f, 1f));
        welcomeAnimator2.setDuration(1100);

        //pcrTv
        ValueAnimator pcrAnimator = ObjectAnimator.ofFloat(this.pcrTv, "alpha", 0f, 1f);
        pcrAnimator.setInterpolator(new EaseCubicInterpolator(0.76f, 0f, 0.8f, 1f));
        pcrAnimator.setDuration(500);
        pcrAnimator.setStartDelay(1100);
        //blueImg
        ValueAnimator blueAnimator = ObjectAnimator.ofFloat(this.blueImg, "alpha", 0f, 1f);
        blueAnimator.setInterpolator(new EaseCubicInterpolator(0.76f, 0f, 0.8f, 1f));
        blueAnimator.setDuration(500);
        blueAnimator.setStartDelay(1200);
        //grayImg

        ValueAnimator grayAnimator1 = ObjectAnimator.ofFloat(this.grayImg, "alpha", 0f, 1f);
        grayAnimator1.setInterpolator(new EaseCubicInterpolator(0.6f, 0f, 0.52f, 1f));
        grayAnimator1.setDuration(1100);
        grayAnimator1.setStartDelay(1000);

        ValueAnimator grayAnimator2 = ObjectAnimator.ofFloat(this.grayImg, "rotation", -200f, 0f);
        grayAnimator2.setInterpolator(new LinearInterpolator());
        grayAnimator2.setDuration(1100);
        grayAnimator2.setStartDelay(1000);


        AnimatorSet animSet = new AnimatorSet();
        animSet.play(welcomeAnimator1).with(welcomeAnimator2).with(pcrAnimator).with(blueAnimator).with(grayAnimator1).with(grayAnimator2);
        animSet.start();
    }
}
