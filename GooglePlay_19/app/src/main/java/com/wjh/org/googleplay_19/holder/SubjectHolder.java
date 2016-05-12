package com.wjh.org.googleplay_19.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.bean.SubjectBean;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * @创建者     wjh
 * @创建时间   2016/2/29 23:41
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class SubjectHolder extends BaseHolder<SubjectBean> {

    @Bind(R.id.item_subject_iv_icon)
    ImageView mItemSubjectIvIcon;
    @Bind(R.id.item_subject_tv_title)
    TextView  mItemSubjectTvTitle;
    private SubjectBean mSubjectDatas;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View rootView = View.inflate(UIUtils.getContext(), R.layout.subject_holder_item, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * data+view的绑定
     *
     * @param datas
     */
    @Override
    public void refreshHolderView(SubjectBean datas) {
        //保存局部变量到成员变量
        mSubjectDatas = datas;

        //设置描述
        mItemSubjectTvTitle.setText(mSubjectDatas.des);
        mItemSubjectTvTitle.setTextColor(Color.GRAY);
        //获取图片url,进行拼接
        String subjectUrl = mSubjectDatas.url;
        subjectUrl = Constants.URL.IMAGEURL + subjectUrl;

        //网络加载图片 ,这里的图片是用一个自定义的控件容器包裹起来了,可以不让图片拉伸变形
        ImageLoader.getInstance().displayImage(subjectUrl,mItemSubjectIvIcon);

    }
}
