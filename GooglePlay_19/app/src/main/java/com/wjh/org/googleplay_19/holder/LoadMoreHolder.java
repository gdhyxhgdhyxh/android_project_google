package com.wjh.org.googleplay_19.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * @创建者     wjh
 * @创建时间   2016/2/29 12:45
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * 加载更多的条目;
 * 泛型:Integer(由于是有3种不同的状态显示)
 */
public class LoadMoreHolder extends BaseHolder<Integer> {

    @Bind(R.id.item_loadmore_tv_retry)
    TextView mItemLoadmoreTvRetry;              //加载失败的控件

    @Bind(R.id.item_loadmore_container_loading)
    LinearLayout mItemLoadmoreContainerLoading;  //加载更多的容器

    @Bind(R.id.item_loadmore_container_retry)
    LinearLayout mItemLoadmoreContainerRetry;  //加载失败的容器

    public static final int LOADMORE_LOADING = 0; //可能有加载更多
    public static final int LOADMORE_RETRY   = 1;   //加载更多失败,点击重试
    public static final int LOADMORE_NONE    = 2;    //没有更多


    /**
     *  初始化持有的视图
     * @return
     */
    @Override
    public View initHolderView() {
        View rootView = View.inflate(UIUtils.getContext(), R.layout.load_more_holder_item, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     *  视图和数据的绑定
     * @param datas
     */
    @Override
    public void refreshHolderView(Integer datas) {

        //一开始隐藏所有视图
        mItemLoadmoreContainerLoading.setVisibility(View.GONE);
        mItemLoadmoreContainerRetry.setVisibility(View.GONE);

        switch (datas) {
            case LOADMORE_LOADING:  //正在加载更多
                mItemLoadmoreContainerLoading.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_RETRY:    //加载更多失败
                mItemLoadmoreContainerRetry.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_NONE:     //没有加载更多

                break;

            default:
                break;
        }


    }
}
