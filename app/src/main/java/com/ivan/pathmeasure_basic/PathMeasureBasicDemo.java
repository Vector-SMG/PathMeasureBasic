package com.ivan.pathmeasure_basic;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/*
 * @author liuwei
 * @email 13040839537@163.com
 * create at 2018/8/9
 * description:PathMeasure例子
 */
public class PathMeasureBasicDemo extends View {

    private Paint mPaint;
    private Path path;
    private float mRadio;
    private PathMeasure mPathMeasure;

    private float pos[];
    private float tan[];
    private float mLength;


    public PathMeasureBasicDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        path=new Path();
        mPathMeasure=new PathMeasure();

        pos=new float[2];
        tan=new float[2];
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int centerX=getWidth()/2;
        int centerY=getHeight()/2;

        int radius=100;

        //从界面中心画圆
        path.addCircle(centerX,centerY,radius, Path.Direction.CW);
        canvas.drawPath(path,mPaint);

        mPathMeasure.setPath(path,false);
        mLength=mPathMeasure.getLength();

        //测量圆的pos,tan
        boolean hasPath= mPathMeasure.getPosTan(mRadio*mLength,pos,tan);
        if(hasPath){
            float degree= (float) (Math.atan2(tan[1],tan[0])*180.0f/ Math.PI);
            //以大圆的路径上的点为圆心画一个小圆
            canvas.drawCircle(pos[0],pos[1],10,mPaint);
            //平移到小圆的圆心
            canvas.translate(pos[0],pos[1]);
            //旋转切角
            canvas.rotate(degree);
            //沿着切角方向画一条线
            canvas.drawLine(0,0,200,0,mPaint);
        }
        canvas.restore();
    }

    //开始动画
    public  void startAnim(){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(0,1);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(8000l);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadio= (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.start();
    }
}
