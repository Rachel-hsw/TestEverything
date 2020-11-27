package com.example.pc.testeverything;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class BreathActivity1 extends AppCompatActivity {
    private View test1;
    private View test2;
    private ImageView test3;
    private int value;
    private Animator animator;
    private ValueAnimator anInt;
    private int[] a = {R.mipmap.circle_blue, R.mipmap.circle_grey, R.mipmap.ic_launcher, R.mipmap.qrcode
            , R.mipmap.back, R.mipmap.circle_blue, R.mipmap.circle_grey, R.mipmap.ic_launcher};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);
        test2 = findViewById(R.id.test2);
        test3 = (ImageView) findViewById(R.id.test3);
        animation();
//        anInt = ValueAnimator.ofInt(7, 0);
//        anInt.setInterpolator(new LinearInterpolator());
//        anInt.setDuration(8000);
//        anInt.setRepeatCount(ValueAnimator.INFINITE);
//        anInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int animatedValue = (int) animation.getAnimatedValue();
//                if (value != animatedValue) {
//                    value = animatedValue;
//                    Log.i("Rachel_test", animatedValue + "");
//                    test3.setImageResource(a[animatedValue]);
//                }
//            }
//        });
//        anInt.start();
    }

    /**
     * 常亮、闪烁、呼吸和红绿交替这几种
     */
    private void animation() {
        //呼吸
        ValueAnimator pcrAnimator = ObjectAnimator.ofFloat(this.test1, "alpha", 0.2f, 1f, 0.2f);
        pcrAnimator.setDuration(3000);
        pcrAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pcrAnimator.start();
        //闪烁
        ValueAnimator animator2 = ObjectAnimator.ofFloat(this.test2, "alpha", 0f, 1f);
        animator2.setDuration(500);
        animator2.setRepeatCount(ValueAnimator.INFINITE);
        animator2.start();

        ValueAnimator anInt = ValueAnimator.ofInt(0, 2);
        anInt.setDuration(500);
        anInt.setRepeatCount(ValueAnimator.INFINITE);
        anInt.setInterpolator(new LinearInterpolator());
        anInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //默认间隔10ms https://blog.csdn.net/yy471101598/article/details/50344917
                int animatedValue = (int) animation.getAnimatedValue();
                if (value != animatedValue) {
                    value = animatedValue;
                    Log.i("Rachel_test", animatedValue + "");
                    Drawable drawable = getDrawable(R.drawable.ic_lens_black_24dp);
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(BreathActivity1.this, getColorByValue(animatedValue)));
                    test3.setImageDrawable(drawable);
                }
            }
        });
        anInt.start();
    }

    private int getColorByValue(int animatedValue) {
        int result = R.color.colorAccent;
        switch (animatedValue) {
            case 0:
                result = R.color.colorAccent;
                break;
            case 1:
                result = R.color.colorGreen;
                break;
            default:
        }
        return result;
    }

    public void imageSize(View view) {
        ImageView image_scale = (ImageView) findViewById(R.id.image_scale);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.size);
        image_scale.startAnimation(animation);
    }

}
