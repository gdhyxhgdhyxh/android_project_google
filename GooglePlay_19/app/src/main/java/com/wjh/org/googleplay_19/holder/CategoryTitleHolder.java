package com.wjh.org.googleplay_19.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.wjh.org.googleplay_19.bean.CategoryBean;
import com.wjh.org.googleplay_19.utils.UIUtils;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 13:24
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  分类中的标题
 */
public class CategoryTitleHolder extends BaseHolder<CategoryBean> {

    private TextView mTextView;

    @Override
    public View initHolderView() {
        mTextView = new TextView(UIUtils.getContext());
        int padding = UIUtils.dp2px(6);
        mTextView.setPadding(padding, padding, padding, padding);
        mTextView.setTextColor(Color.BLACK);
        return mTextView;
    }

    @Override
    public void refreshHolderView(CategoryBean datas) {

        mTextView.setText(datas.title);
    }
}
