package com.wjh.org.googleplay_19.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
 * @创建者     wjh
 * @创建时间   2016/2/29 23:10
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  1.自定义ViewPager;
 *  2.为了兼容某些2.3 , 4.1 等手机sdk版本问题,ViewPager嵌套会被拦截触摸事件问题
 *  3.所以这里设置请求父容器不要拦截
 */
public class ChildViewPager extends ViewPager {

    private float mDownX;
    private float mDownY;

    public ChildViewPager(Context context) {
        this(context, null);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *  1.事件分发
     * @param ev
     * @return true:分发事件,InterceptTouchEvent收不到事件通知; false:不分发事件,同样收不到
     * 注意:一般不对这个方法的返回值做处理;
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 2. 事件拦截
     * @param ev
     * @return true:拦截事件, false:放行
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 3. 消费事件
     * getParent().requestDisallowInterceptTouchEvent(false)
     * true:请求父容器不要拦截touch事件; false:请求父容器拦截事件(默认)
     * @param ev
     * @return  true:自己消费; false:不消费
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:   //按下
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:   //滑动

                float moveX = ev.getX();
                float moveY = ev.getY();

                //获取X/Y轴从按下到滑动的距离
                int diffX = (int) (moveX - mDownX+.5f);
                int diffY = (int) (moveY - mDownY+.5f);

                //水平滑动大于垂直滑动
                if(Math.abs(diffX) > Math.abs(diffY)){
                    //请求父容器不要拦截滑动事件,孩子自己处理
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else{
                    //垂直滑动的时候,让父容器自己拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:     //松开
            case MotionEvent.ACTION_CANCEL: //结束

                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
