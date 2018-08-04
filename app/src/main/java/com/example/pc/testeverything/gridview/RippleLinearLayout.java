package com.example.pc.testeverything.gridview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by moon.zhong on 2015/4/27.
 */
public class RippleLinearLayout extends LinearLayout {

    private final int DURATION = 150;
    private final int FREQUENCY = 10;
    private final Rect mRect = new Rect();
    /*起始点*/
    private int mInitX;
    private int mInitY;
    private float mCurrentX;
    private float mCurrentY;
    /*高度和宽度*/
    private int mWidth;
    private int mHeight;
    /*绘制的半径*/
    private float mRadius;
    private float mStepRadius;
    private float mStepOriginX;
    private float mStepOriginY;
    private float mDrawRadius;
    private boolean mDrawFinish;
    private float mCycle;
    private boolean mPressUp = false;


    private Paint mRevealPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public RippleLinearLayout(Context context) {
        super(context);
        initView(context);
    }

    public RippleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RippleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mRevealPaint.setColor(0x25000000);
        mCycle = DURATION / FREQUENCY;
        final float density = getResources().getDisplayMetrics().density;
        mCycle = (density * mCycle);
        mDrawFinish = true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawFinish) {
            super.onDraw(canvas);
            return;
        }
        canvas.drawColor(0x15000000);
        super.onDraw(canvas);
        if (mStepRadius == 0) {
            return;
        }
        mDrawRadius = mDrawRadius + mStepRadius;
        mCurrentX = mCurrentX + mStepOriginX;
        mCurrentY = mCurrentY + mStepOriginY;
        if (mDrawRadius > mRadius) {
            mDrawRadius = 0;
            canvas.drawCircle(mRect.width() / 2, mRect.height() / 2, mRadius, mRevealPaint);
            mDrawFinish = true;
            if (mPressUp)
                invalidate();
            return;
        }

        canvas.drawCircle(mCurrentX, mCurrentY, mDrawRadius, mRevealPaint);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    private void updateDrawData() {
//        int radiusLeftTop = (int) Math.sqrt((mRect.left - mInitX) * (mRect.left - mInitX) +
//                (mRect.top - mInitY) * (mRect.top - mInitY));
//        int radiusRightTop = (int) Math.sqrt((mRect.right - mInitX) * (mRect.right - mInitX) +
//                (mRect.top - mInitY) * (mRect.top - mInitY));
//        int radiusLeftBottom = (int) Math.sqrt((mRect.left - mInitX) * (mRect.left - mInitX) +
//                (mRect.bottom - mInitY) * (mRect.bottom - mInitY));
//        int radiusRightBottom = (int) Math.sqrt((mRect.right - mInitX) * (mRect.right - mInitX) +
//                (mRect.bottom - mInitY) * (mRect.bottom - mInitY));
//        mRadius = getMax(radiusLeftTop, radiusRightTop, radiusLeftBottom, radiusRightBottom);
        /*最大半径*/
        mRadius = (float) Math.sqrt(mRect.width() / 2 * mRect.width() / 2 + mRect.height() / 2 * mRect.height() / 2);
        ;
        /*半径的偏移量*/
        mStepRadius = mRadius / mCycle;
        /*圆心X的偏移量*/
        mStepOriginX = (mRect.width() / 2 - mInitX) / mCycle;
        /*圆心Y的偏移量*/
        mStepOriginY = (mRect.height() / 2 - mInitY) / mCycle;

        mCurrentX = mInitX;
        mCurrentY = mInitY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        Log.i("hswtest", "-----------" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mPressUp = false;
                mDrawFinish = false;
                int index = MotionEventCompat.getActionIndex(event);
                int eventId = MotionEventCompat.getPointerId(event, index);
                if (eventId != -1) {
                    mInitX = (int) MotionEventCompat.getX(event, index);
                    mInitY = (int) MotionEventCompat.getY(event, index);
                    updateDrawData();
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mStepRadius = (int) (5 * mStepRadius);
                mStepOriginX = (int) (5 * mStepOriginX);
                mStepOriginY = (int) (5 * mStepOriginY);
                mPressUp = true;
                invalidate();
                break;
        }
        Log.i("hswtest", "-----------" + super.onTouchEvent(event));
        super.onTouchEvent(event);
        return true;
    }

    private int getMax(int... radius) {
        if (radius.length == 0) {
            return 0;
        }
        int max = radius[0];
        for (int m : radius) {
            if (m > max) {
                max = m;
            }
        }
        return max;
    }

    @Override
    public boolean performClick() {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                RippleLinearLayout.super.performClick();
            }
        }, 150);
        return true;

    }
}