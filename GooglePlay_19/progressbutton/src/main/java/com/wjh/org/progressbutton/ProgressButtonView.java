package com.wjh.org.progressbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/*
 * @创建者     wjh
 * @创建时间   2016/3/3 12:59
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * 1.自定义的Progress控件,在原有的Button上扩展功能;
 * 2.重新绘制
 */
public class ProgressButtonView extends Button {

    private int mMax = 100; //最大值默认100
    private int mProgress; //当前进度值
    private boolean isProgressEnable = true; //默认是有进度条的

    /**
     * 设置进度条最大值
     */
    public void setMax(int max) {
        mMax = max;
    }

    /**
     * 设置当前进度条的值
     */
    public void setProgress(int progress) {
        mProgress = progress;

        //动态计算,就需要视图的重绘了,当外界调用这个方法的时候,就会重新调用OnDraw()方法
        invalidate();
    }

    /**
     * 是否有进度条
     */
    public void setIsProgressEnable(boolean isProgressEnable) {
        this.isProgressEnable = isProgressEnable;
    }

    public ProgressButtonView(Context context) {
        this(context, null);
    }

    public ProgressButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写onDraw方法,进行按钮的绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {


        /**
         * 通过查看ProgressBar的源码里的OnDraw()方法,它是通过drawTrack()方法,把Drawable自身画到了canvas上;
         * 所以我们也可以通过Drawable来画到自身的画笔上;
         *
         */
        if (isProgressEnable) {
            //如果是有进度条,才进行绘制

            Drawable drawable = new ColorDrawable(Color.BLUE);
            int left = 0;                   //左边也是紧贴自身
            int top = 0;                    //头部紧贴自身

            //右边  需要动态计算  百分数除以最大值 乘上宽度
            int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + .5f);
            int boottom = getBottom();      //底部

            //设置需要画在那个坐标上
            drawable.setBounds(left, top, right, boottom);

            drawable.draw(canvas);
        }
        super.onDraw(canvas); //这个super方法要放置在后面,否则会被Drawable覆盖上去
    }
}
