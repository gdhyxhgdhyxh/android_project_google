package com.wjh.org.googleplay_19.holder;

import android.view.View;
import android.widget.TextView;

import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * @创建者     wjh
 * @创建时间   2016/2/26 21:34
 * 描述	      1.提供视图
 * 描述	      2.接收数据
 * 描述	      3.数据和视图的绑定
 * 描述	      4.把HomeFragment里面的Adapter里面的getView方法内容和ViewHolder相关的内容全部移到HomeHolder里面来
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * HomeHolder的类,设置显示的视图,充当ViewHolder,只需要在调用的时候把数据传递进来
 */
public class HomeHolderCopy extends BaseHolder<String> {

    @Bind(R.id.tmp_tv_1)
    TextView mTmpTv1;
    @Bind(R.id.tmp_tv_2)
    TextView mTmpTv2;


    /**
     * 持有的视图的是啥
     *
     * @return
     */
    @Override
    public View initHolderView() {
        //1.加载布局
        View rootView = View.inflate(UIUtils.getContext(), R.layout.home_listview_item, null);
        //2.找出控件对象,并且转成成员变量
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 数据和视图如何绑定
     *
     * @param datas
     */
    @Override
    public void refreshHolderView(String datas) {

        /*mTmpTv1.setText("HomeHolder的头 :　"+datas);
        mTmpTv2.setText("HomeHolder的尾 :　"+datas);*/
        mTmpTv1.setText("superBaseAdapter的头 :　" + datas);
        mTmpTv2.setText("superBaseAdapter的尾 :　" + datas);
    }
}