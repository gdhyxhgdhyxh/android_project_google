package com.wjh.org.googleplay_19.holder;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 23:03
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import android.graphics.Color;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 详情页面的 应用描述部分
 */
public class DetailedDesHolder extends BaseHolder<ItemBean> implements View.OnClickListener {
    @Bind(R.id.app_detail_des_tv_des)
    TextView  mAppDetailDesTvDes;
    @Bind(R.id.app_detail_des_tv_author)
    TextView  mAppDetailDesTvAuthor;
    @Bind(R.id.app_detail_des_iv_arrow)
    ImageView mAppDetailDesIvArrow;
    private ItemBean mDesDatas;

    private boolean isOpen = true; // 默认是打开
    private int mDesMeasuredHeight;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View rootView = View.inflate(UIUtils.getContext(), R.layout.detailed_des_holder_item, null);
        ButterKnife.bind(this, rootView);
        rootView.setOnClickListener(this);
        return rootView;
    }

    /**
     * data+view的绑定
     *
     * @param datas
     */
    @Override
    public void refreshHolderView(ItemBean datas) {
        mDesDatas = datas;


        mAppDetailDesTvAuthor.setText(mDesDatas.author); //作者名
        mAppDetailDesTvAuthor.setTextColor(Color.BLACK);
        mAppDetailDesTvDes.setText(mDesDatas.des);       //内容


        //测量宽高
        mAppDetailDesTvDes.measure(0, 0);
        //监听view完成
        mAppDetailDesTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //监听布局完成的时候,获得测量后应有的高度
                mDesMeasuredHeight = mAppDetailDesTvDes.getMeasuredHeight();

                //View的初始化完成,就进行折叠,不需要动画
                changeDesHeight(false);

                //移除监听
                mAppDetailDesTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    @Override
    public void onClick(View v) {

        changeDesHeight(true);
    }

    /**
     * 使用动画修改高度
     */
    private void changeDesHeight(boolean isAnimation) {
        if (isOpen) {
            //折叠
            //mAppDetailDesTvDes的高度 --->应有的高度---->7行的高度

            //Toast.makeText(UIUtils.getContext(), "" + mDesMeasuredHeight, Toast.LENGTH_SHORT).show();

            int start = mDesMeasuredHeight;             //测量后应该有的高度
            int end = getShortHeight(3, mDesDatas.des); //定义一个方法,用来测量几行的高度

            if (isAnimation) {
                //如果需要动画
                doAnimation(start, end);
            } else {
                //不需要动画,手动设置
                mAppDetailDesTvDes.setHeight(end);
            }

        } else {
            //打开
            //mAppDetailDesTvDes的高度 --->7行的高度--->应有的高度

            int end = mDesMeasuredHeight;             //测量后应该有的高度
            int start = getShortHeight(3, mDesDatas.des); //定义一个方法,用来测量几行的高度

            if (isAnimation) {
                //如果需要动画
                doAnimation(start, end);
            } else {
                //不需要动画,手动设置
                mAppDetailDesTvDes.setHeight(end);
            }

        }

        //把状态取反操作
        isOpen = !isOpen;
    }

    /**
     * 折叠打开动画
     *
     * @param start
     * @param end
     */
    private void doAnimation(int start, int end) {
        //内容折叠动画
        ObjectAnimator objectAnimator =  ObjectAnimator.ofInt(mAppDetailDesTvDes, "Height", start, end);
        objectAnimator.start();


        if (isOpen) {
            //箭头旋转动画
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "Rotation", 180, 0).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "Rotation", 0, 180).start();
        }

        //监听动画的完成
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {       //开始

            }

            @Override
            public void onAnimationEnd(Animator animator) {         //结束

                //结束动画的时候,递归拿到父容器scrollView,然后对它进行设置滚动

                //1.首先拿到上一级父亲
                ViewParent parent = mAppDetailDesTvDes.getParent();

                while(true){
                    //2.递归循环继续获取上一级 父亲--->父亲
                    parent = parent.getParent();

                    if(parent instanceof ScrollView){
                        //3.如果遍历到的是ScrollView,那么就设置它的滚动,并且返回
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                        return;
                    }

                    if(parent == null){
                        //4.如果遍历到最后,没有父亲了,就跳出循环
                        break;
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {      //取消

            }

            @Override
            public void onAnimationRepeat(Animator animator) {      //重复

            }
        });
    }

    /**
     * 测量外界传递过来的行数,然后内容高度
     *
     * @param line    几行
     * @param content 内容
     * @return 测量后的几行中的内容高度
     */
    private int getShortHeight(int line, String content) {
        TextView tv = new TextView(UIUtils.getContext());
        tv.setLines(line);
        tv.setText(content);

        //测量
        tv.measure(0, 0);

        //获取测量后的高
        return tv.getMeasuredHeight();
    }
}
