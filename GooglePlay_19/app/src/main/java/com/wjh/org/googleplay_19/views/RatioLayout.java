package com.wjh.org.googleplay_19.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wjh.org.googleplay_19.R;

/*
 * @创建者     wjh
 * @创建时间   2016/3/2 21:08
 * @描述	      已知宽度,动态计算高度
 * @描述	      已知高度,动态计算宽度
 * @描述	      现在只要图片不变形
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  用来设置图片的宽高比问题
 */
public class RatioLayout extends FrameLayout {

    private float mPicRatio = 1.0f;  //图片宽高比

    public static final int RELATIVE_WIDTH = 0;//已知宽度,动态计算高度
    public static final int RELATIVE_HEIGHT = 1;//已知高度,动态计算宽度
    public int mCurState = RELATIVE_WIDTH; // 默认的状态就是已知宽度

    /**
     *  外界可以通过设置状态值,来确定是否计算宽或者高
     * @param curState
     */
    public void setCurState(int curState) {
        mCurState = curState;
    }

    /**
     *  外界可以设置宽高比的值
     * @param picRatio
     */
    public void setPicRatio(float picRatio) {
        mPicRatio = picRatio;
    }

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        //1. 取出自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);

        //2. 把xml文件里设置的自定义属性取出来赋值给这里定义的状态变量(这样就可以让xml设置的属性到了onMeasure生效了)
        mCurState = typedArray.getInt(R.styleable.RatioLayout_rlrelative,RELATIVE_WIDTH);
        // 取出xml里设置的宽高比属性值  (默认是1.0f)
        mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_rlpicRatio,1.0f);

        //3. 比较消耗资源,进行回收
        typedArray.recycle();
    }

    /**
     * 动态计算宽度和高度
     * 1.ratioLayout自身宽度 / ratioLayout高度 = 图片的宽高比
     * 2.如果是wrap_content /match_parent 都可以计算得出,因为宽高都判断了
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //什么时候自己测绘
        //可以确定宽度是已知的时候
        /*
        UNSPECIFIED 不确定的 wrap_content
        AT_MOST 至多
        EXACTLY 精确的 match_parent 100dp
         */

        //1.得到外界传递过来的宽 / 高的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //明确宽度已知---->宽度可以得到 (需要判断是否明确,并且是已知宽度)
        if (widthMode == MeasureSpec.EXACTLY &&mCurState == RELATIVE_WIDTH) {
            //计算已知宽度的模式,让高度拉伸不变形

            //得到自身的宽度
            int slefWidth = MeasureSpec.getSize(widthMeasureSpec);

            //根据公式计算自身应有的高度 ratioLayout自身宽度 / ratioLayout高度 = 图片的宽高比
            int slefHeight = (int) (slefWidth / mPicRatio + .5f);

            //2.自己测量孩子的宽高值
            int childWidth = slefWidth - getPaddingLeft() - getPaddingRight(); //得到孩子应有的宽
            int childHeight = slefHeight - getPaddingTop() - getPaddingBottom();//得到孩子应有的高

            //测绘孩子的宽高值 (精确的)
            int childrenWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childrenHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childrenWidthMeasureSpec, childrenHeightMeasureSpec);

            //3.保存测绘的结果
            setMeasuredDimension(slefWidth, slefHeight);

        } else if(heightMode == MeasureSpec.EXACTLY &&mCurState == RELATIVE_HEIGHT){
            //计算已知高度的模式,让宽度拉伸不变形


            //得到自身的高度
            int slefHeight = MeasureSpec.getSize(heightMeasureSpec);

            //根据公式计算自身应有的高度 ratioLayout自身宽度 * ratioLayout高度 = 图片的宽高比
            int slefWidth = (int) (slefHeight * mPicRatio + .5f);

            //2.自己测量孩子的宽高值
            int childWidth = slefWidth - getPaddingLeft() - getPaddingRight(); //得到孩子应有的宽
            int childHeight = slefHeight - getPaddingTop() - getPaddingBottom();//得到孩子应有的高

            //测绘孩子的宽高值 (精确的)
            int childrenWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childrenHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childrenWidthMeasureSpec, childrenHeightMeasureSpec);

            //3.保存测绘的结果
            setMeasuredDimension(slefWidth, slefHeight);

        }else
         {
            //走父亲的测绘
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
