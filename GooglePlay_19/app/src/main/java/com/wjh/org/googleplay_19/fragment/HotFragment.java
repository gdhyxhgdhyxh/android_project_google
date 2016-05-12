package com.wjh.org.googleplay_19.fragment;

/*
 * @创建者     wjh
 * @创建时间   2016/2/24 20:53
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wjh.org.googleplay_19.base.BaseFragment;
import com.wjh.org.googleplay_19.base.LoadingPager;
import com.wjh.org.googleplay_19.protocol.HotRotocol;
import com.wjh.org.googleplay_19.utils.UIUtils;
import com.wjh.org.googleplay_19.views.flyinout.FlowLayout;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 排行
 */
public class HotFragment extends BaseFragment {

    private HotRotocol   mHotRotocol;
    private List<String> mDatas;

    /**
     * @des 在子线程里面真正的加载数据
     * @called 外界需要加载数据的时候调用了triggerLoadData方法的时候
     */
    @Override
    public LoadingPager.LoadDataResultState initData() {

        try {
            mHotRotocol = new HotRotocol();
            mDatas = mHotRotocol.loadData(0);

            //检验返回的数据状态
            LoadingPager.LoadDataResultState state = checkResData(mDatas);
            return state;

        } catch (IOException e) {
            e.printStackTrace();
            //出错了
            return LoadingPager.LoadDataResultState.ERROR;
        }
    }

    /**
     * @return
     * @des 初始化成功视图, 完成成功视图的绑定操作
     * @called 外界触发加载了数据, 数据加载完成, 而且数据加载成功
     */
    @Override
    public View initSuccessView() {

        //分析得出结果,使用scrollView包裹FlowLayout流式布局
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());

        //遍历数据集,动态添加数据
        for (int i = 0; i < mDatas.size(); i++) {
            final String data = mDatas.get(i);

            //设置排行的数据
            TextView tv = new TextView(UIUtils.getContext());
            tv.setGravity(Gravity.CENTER);  //居中
            tv.setText(data);               //数据
            tv.setTextColor(Color.WHITE);  //颜色
            int padding = UIUtils.dp2px(3); //间距
            tv.setPadding(padding, padding, padding, padding);


            /*--------------- 普通状态下的Shape背景 ---------------*/
            //代码动态添加Shape 矩形
            GradientDrawable normalBg = new GradientDrawable();
            normalBg.setCornerRadius(UIUtils.dp2px(8)); //半径

            //添加随机背景颜色
            Random random = new Random();
            int blue = random.nextInt(180) + 30; // 颜色30-210
            int red = random.nextInt(180) + 30;
            int green = random.nextInt(180) + 30;
            normalBg.setColor(Color.argb(255, red, green, blue));


            /*--------------- 按压状态下的Shape背景 ---------------*/
            GradientDrawable pressedBg = new GradientDrawable();
            pressedBg.setCornerRadius(UIUtils.dp2px(8));//半径
            pressedBg.setColor(Color.DKGRAY);           //颜色


            /*--------------- selector选择器,这里"-"号是代表false,不写是true ---------------*/
            StateListDrawable selectorBg = new StateListDrawable();
            selectorBg.addState(new int[]{android.R.attr.state_pressed},pressedBg);//按压
            selectorBg.addState(new int[]{-android.R.attr.state_pressed},normalBg);//普通


            //和xml文件里的背景一样,设置选择器
            tv.setBackgroundDrawable(selectorBg);

            //添加到流式布局容器
            flowLayout.addView(tv);

            //设置点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), data, Toast.LENGTH_SHORT).show();
                }
            });
        }


        //添加到Scroll容器
        scrollView.addView(flowLayout);

        //返回这个布局
        return scrollView;
    }
}
