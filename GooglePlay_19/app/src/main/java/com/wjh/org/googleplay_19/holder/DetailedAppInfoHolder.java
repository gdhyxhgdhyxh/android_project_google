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
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.utils.StringUtils;
import com.wjh.org.googleplay_19.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 详情页面的 应用信息内容部分
 */
public class DetailedAppInfoHolder extends BaseHolder<ItemBean> {
    @Bind(R.id.app_detail_info_iv_icon)
    ImageView mAppDetailInfoIvIcon;
    @Bind(R.id.app_detail_info_tv_name)
    TextView  mAppDetailInfoTvName;
    @Bind(R.id.app_detail_info_rb_star)
    RatingBar mAppDetailInfoRbStar;
    @Bind(R.id.app_detail_info_tv_downloadnum)
    TextView  mAppDetailInfoTvDownloadnum;
    @Bind(R.id.app_detail_info_tv_version)
    TextView  mAppDetailInfoTvVersion;
    @Bind(R.id.app_detail_info_tv_time)
    TextView  mAppDetailInfoTvTime;
    @Bind(R.id.app_detail_info_tv_size)
    TextView  mAppDetailInfoTvSize;
    private ItemBean mdatas;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {

        View rootView = View.inflate(UIUtils.getContext(), R.layout.detailed_appinfo_holder_item, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     *  data+view的绑定
     * @param datas
     */
    @Override
    public void refreshHolderView(ItemBean datas) {
        //保存成成员变量
        mdatas = datas;


        mAppDetailInfoRbStar.setRating(mdatas.stars);    //评分的状态
        mAppDetailInfoTvName.setText(mdatas.name);      //应用程序名

        mAppDetailInfoTvDownloadnum.setText(UIUtils.getResources().getString(R.string.app_detail_info_downloadnum,
                mdatas.downloadNum));
        mAppDetailInfoTvTime.setText(UIUtils.getResources().getString(R.string.app_detail_info_time, mdatas.date));
        mAppDetailInfoTvVersion.setText(UIUtils.getResources().getString(R.string.app_detail_info_version, mdatas
                .version));
        mAppDetailInfoTvSize.setText(UIUtils.getResources().getString(R.string.app_detail_info_size, StringUtils
                .formatFileSize(mdatas.size)));

        //加载图片
        ImageLoader.getInstance().displayImage(Constants.URL.IMAGEURL + mDatas.iconUrl, mAppDetailInfoIvIcon);

    }
}
