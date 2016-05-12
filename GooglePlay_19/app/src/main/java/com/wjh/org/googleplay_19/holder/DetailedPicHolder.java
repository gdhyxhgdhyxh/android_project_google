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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.utils.UIUtils;
import com.wjh.org.googleplay_19.views.RatioLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 详情页面的 应用截图部分
 */
public class DetailedPicHolder extends BaseHolder<ItemBean> {
    @Bind(R.id.app_detail_pic_iv_container)
    LinearLayout mAppDetailPicIvContainer;

    @Override
    public View initHolderView() {
        View rootView = View.inflate(UIUtils.getContext(), R.layout.detailed_pic_holder_item, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void refreshHolderView(ItemBean datas) {

        List<String> picScreen = datas.screen;
        for (int i = 0; i < picScreen.size(); i++) {
            String picUrl = picScreen.get(i);

            //使用这个拿来测量的自定义控件来包裹ImagView
            RatioLayout ratioLayout = new RatioLayout(UIUtils.getContext());

            //设置属性
            ratioLayout.setCurState(RatioLayout.RELATIVE_WIDTH); //已知宽度,动态计算高度
            ratioLayout.setPicRatio((float) 150/250); //计算图片宽高比

            //加载图片
            ImageView imageView = new ImageView(UIUtils.getContext());
            ImageLoader.getInstance().displayImage(Constants.URL.IMAGEURL + picUrl, imageView);

            //获取屏幕的宽度,还要减去设置的padding值
            int screenPixels = UIUtils.getResources().getDisplayMetrics().widthPixels;
            screenPixels = screenPixels -UIUtils.dp2px(16);

            //把图片拆分成3份显示在屏幕上
            int width = screenPixels /3;
            int height= LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);

            if(i != 0){
                //设置左边间距
                params.leftMargin = UIUtils.dp2px(3);
            }

            ratioLayout.addView(imageView); //添加容器
            mAppDetailPicIvContainer.addView(ratioLayout,params);
        }
    }
}
