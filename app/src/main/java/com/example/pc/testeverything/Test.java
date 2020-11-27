package com.example.pc.testeverything;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Test extends View {
    public Test(Context context) {
        super(context);
    }

    public Test(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public Test(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 需禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.drawCircle(20f, 20f, 10f, paint);

        canvas.translate(35f, 0f);
        paint.setMaskFilter(new BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(20f, 20f, 10f, paint);

        canvas.translate(-35f, 35f);
        paint.setMaskFilter(new BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL));
        canvas.drawCircle(20f, 20f, 10f, paint);

        canvas.translate(35f, 0f);
        paint.setMaskFilter(new BlurMaskFilter(10f, BlurMaskFilter.Blur.OUTER));

        canvas.drawCircle(20f, 20f, 10f, paint);

//        canvas.translate(-350f, 350f);
//        paint.setMaskFilter(new BlurMaskFilter(100f, BlurMaskFilter.Blur.SOLID)) ;
//        canvas.drawBitmap(bmp, 200f,200f, paint);
    }
}
