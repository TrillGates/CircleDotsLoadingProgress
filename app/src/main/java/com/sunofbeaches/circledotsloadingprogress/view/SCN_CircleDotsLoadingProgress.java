package com.sunofbeaches.circledotsloadingprogress.view;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

/**
 * Copyright(C) 2014-2016 sunofbeaches.com All Rights Reserved.
 * 作者： TrillGates
 * 描述： 这个Drawable呢，实现一下小动画，进度圈圈的动画。
 * 时间： 2016-7-15 16：18
 * 版本号  作者  日期            简介
 * 1.0  TrillGates  $data$ $time$  Create
 */
public class SCN_CircleDotsLoadingProgress extends Drawable {


    private static final float POINT_WIDTH = 5;
    private static final String TAG = "SCN_CircleDotsLoadingProgress";
    private final Paint mPaint;
    private final Activity mActivity;

    private int mPointCount;
    private final float mDelayAngle;
    private float baseAnble = 0;
    private long mDuration = 100;


    /************************************************
     * 构造方法
     * 在这里的话，一般初始化一些数据一些工具
     ************************************************/
    public SCN_CircleDotsLoadingProgress(Activity activity, @NonNull int pointCount) {

        this.mActivity = activity;
        this.mPointCount = pointCount;

        if (mPointCount < 2) {
            throw new IllegalArgumentException("最少也要给3个吧！");
        }

        //创建画笔
        mPaint = new Paint();

        //设置画笔的相关属性
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
        mPaint.setColor(Color.parseColor("#ffffff"));

        //求出偏移角度
        mDelayAngle = 360 * 1.0f / pointCount;

        //开启动画
        AnimationsRunnable aniamaRunnable = new AnimationsRunnable();
        new Thread(aniamaRunnable).start();


    }


    @Override
    public void draw(Canvas canvas) {

        //获取到外框
        Rect rect = getBounds();

        //获取到圆心
        int a = rect.centerX();
        int b = rect.centerY();

        if (a == 0 || b == 0) {
            throw new IllegalArgumentException("请设置一下大小嘛！");
        }

        //这里的圆心是相对于外框的。比如说，我们用ImageView来承载
        //ImageView的大小为100dp*100dp，那么不管这个ImageView在个位置
        //圆心还是为(50dp,50dp)


        //这里循环把所有的点画出来
        for (int i = 0; i < mPointCount; i++) {


            //算出点的偏转角度
            float sweepAngle = i * mDelayAngle + baseAnble;
            //根据角度算出弧度
            double pointAngle = (Math.PI / 180 * (sweepAngle));


            //计算Cos角度值和Sin角度值
            double sinNum = Math.sin(pointAngle);
            double cosNum = Math.cos(pointAngle);

            //这里是因为呀，这个计算机的计算精确度，有时候呢，会过小，有时候会比实际的要大一点占。
            if (sinNum < -1.0) {
                sinNum = -1.0;
            } else if (sinNum > 1.0) {
                sinNum = 1.0;
            } else if (cosNum < -1.0) {
                cosNum = -1.0;
            } else if (cosNum > 1.0) {
                cosNum = 1.0;
            }


            //根据角度来算出每个点的位置
            //计算圆圈的位置
            //计算出坐标
            float x = (float) (a + cosNum * (a - POINT_WIDTH));
            float y = (float) (b + sinNum * (b - POINT_WIDTH));


            //根据象限来作一些偏移，否则都画到边上去了
            if (sinNum > 0) {
                y -= POINT_WIDTH;
            } else if (sinNum < 0) {
                y += POINT_WIDTH;
            }
            //
            if (cosNum > 0) {
                x -= POINT_WIDTH;
            } else if (cosNum < 0) {
                x += POINT_WIDTH;
            }

            //画出所有的圆点，此处应有掌声
            canvas.drawCircle(x, y, POINT_WIDTH * 3.0f / 4.0f + (int) (i * 1.01), mPaint);

        }

        //叠加角度
        baseAnble += mDelayAngle;

        //归零
        if (baseAnble >= 360) {
            baseAnble = 0;
        }

    }


    private class AnimationsRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {

                //睡一下
                SystemClock.sleep(mDuration);

                //跑在主线程上
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //画一次
                        invalidateSelf();
                    }
                });

            }
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
