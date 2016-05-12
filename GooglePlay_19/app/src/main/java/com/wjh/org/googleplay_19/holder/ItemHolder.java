package com.wjh.org.googleplay_19.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.utils.StringUtils;
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
public class ItemHolder extends BaseHolder<ItemBean> {

    @Bind(R.id.item_appinfo_iv_icon)
    ImageView mItemAppinfoIvIcon;
    @Bind(R.id.item_appinfo_tv_title)
    TextView  mItemAppinfoTvTitle;
    @Bind(R.id.item_appinfo_rb_stars)
    RatingBar mItemAppinfoRbStars;
    @Bind(R.id.item_appinfo_tv_size)
    TextView  mItemAppinfoTvSize;
    @Bind(R.id.item_appinfo_tv_des)
    TextView  mItemAppinfoTvDes;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View rootView = View.inflate(UIUtils.getContext(), R.layout.home_holder_item, null);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    /**
     * 接收数据/加载数据
     * data+view
     * @param datas
     */
    @Override
    public void refreshHolderView(ItemBean datas) {

        mItemAppinfoTvTitle.setText(datas.name);//标题
        mItemAppinfoTvSize.setText((StringUtils.formatFileSize(datas.size)));//应用大小
        mItemAppinfoTvDes.setText(datas.des); //描述

        /**
         *  评分功能RatingBar,类似淘宝的评分星星
         */
        mItemAppinfoRbStars.setRating(datas.stars);

        //图片加载需要的配置(使用ImagLoad开源框架)
        DisplayImageOptions option = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        //图片uRL
        String url = Constants.URL.IMAGEURL+datas.iconUrl;

        //获取实例对象,显示加载到控件
        ImageLoader.getInstance().displayImage(url, mItemAppinfoIvIcon, option);
    }
}