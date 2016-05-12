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

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 详情页面的 应用安全部分
 */
public class DetailedSafeHolder extends BaseHolder<ItemBean> implements View.OnClickListener {
    @Bind(R.id.app_detail_safe_iv_arrow)
    ImageView    mAppDetailSafeIvArrow;
    @Bind(R.id.app_detail_safe_pic_container)
    LinearLayout mAppDetailSafePicContainer;
    @Bind(R.id.app_detail_safe_des_container)
    LinearLayout mAppDetailSafeDesContainer;
    private String mSafeDes;
    private String mSafeUrl;
    private String mSafeDesUrl;
    private int    mSafeDesColor;

    private boolean isOpen = true; //判断是否折叠或打开

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View rootView = View.inflate(UIUtils.getContext(), R.layout.detailed_safe_holder_item, null);
        ButterKnife.bind(this, rootView);
        rootView.setOnClickListener(this);
        return rootView;
    }

    /**
     * data+view 的绑定
     *
     * @param datas
     */
    @Override
    public void refreshHolderView(ItemBean datas) {

        List<ItemBean.ItemSafeBean> safe = datas.safe;
        //遍历取出数据,保存到成员变量
        for (ItemBean.ItemSafeBean safeBean : safe) {
            mSafeDes = safeBean.safeDes;
            mSafeDesColor = safeBean.safeDesColor;
            mSafeDesUrl = safeBean.safeDesUrl;
            mSafeUrl = safeBean.safeUrl;

            //安全图片
            ImageView safeUrlImg = new ImageView(UIUtils.getContext());
            ImageLoader.getInstance().displayImage(Constants.URL.IMAGEURL + mSafeUrl, safeUrlImg);
            mAppDetailSafePicContainer.addView(safeUrlImg);

            //安全描述/图片  (使用线性布局包裹起来)
            LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());

            ImageView safeDesImg = new ImageView(UIUtils.getContext());
            TextView safeDesTv = new TextView(UIUtils.getContext());
            ImageLoader.getInstance().displayImage(Constants.URL.IMAGEURL + mSafeDesUrl, safeDesImg);
            safeDesTv.setText(mSafeDes);
            int padding = UIUtils.dp2px(5);
            linearLayout.setPadding(padding, padding, padding, padding);

            if (mSafeDesColor == 0) {
                //安全色
                safeDesTv.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));
            } else {
                //警告色
                safeDesTv.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
            }
            //添加到线性布局容器
            linearLayout.addView(safeDesImg);
            linearLayout.addView(safeDesTv);

            //把线性布局容器添加到这个容器
            mAppDetailSafeDesContainer.addView(linearLayout);
        }

        //一开始默认就让容器折叠起来
        changeAnimationHeight(false);

    }

    /**
     * 容器点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        changeAnimationHeight(true);
    }

    /**
     *  抽取成方法,点击事件需要给容器添加动画折叠效果
     * @param isAnimation 判断是否需要动画
     */
    private void changeAnimationHeight(boolean isAnimation) {
        //控制容器是否折叠或打开
        if (isOpen) {
            //如果是true,就折叠   mAppDetailSafeDesContainer --->应有的高度--->0的位置

            //先测量一下容器的高度
            mAppDetailSafeDesContainer.measure(0, 0);

            int start = mAppDetailSafeDesContainer.getMeasuredHeight();
            int end = 0;

            if(isAnimation){
                //需要动画
            doAnimation(start, end);
            }else{
                //不需要动画,重新设置高度
                ViewGroup.LayoutParams params = mAppDetailSafeDesContainer.getLayoutParams();
                params.height = end;

                //重新设置params
                mAppDetailSafeDesContainer.setLayoutParams(params);
            }

        } else {
            //需要打开
            int start = 0; //这里先放在前面,需要测绘一下

            //先测量一下容器的高度
            mAppDetailSafeDesContainer.measure(0, 0);
            int end = mAppDetailSafeDesContainer.getMeasuredHeight();

            if(isAnimation){
                //需要动画
                doAnimation(start, end);
            }else{
                //不需要动画,重新设置高度
                ViewGroup.LayoutParams params = mAppDetailSafeDesContainer.getLayoutParams();
                params.height = end;

                //重新设置params
                mAppDetailSafeDesContainer.setLayoutParams(params);
            }

        }
        //需要把状态取反操作
        isOpen = !isOpen;
    }

    /**
     * 动画事件
     *
     * @param start
     * @param end
     */
    private void doAnimation(int start, int end) {
        //辅助生成渐变值,还需要监听动画,才能有移动的效果
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取动画从开始到结束之间的值
                int height = (int) valueAnimator.getAnimatedValue();

                //获取容器,动态设置容器的高度
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = height;

                //重新设置容器的layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        });


        if (isOpen) {
            //箭头需要跟着改变
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 180, 0).start();

        } else {

            //箭头需要跟着改变
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 0, 180).start();
        }
        valueAnimator.start();
    }
}
