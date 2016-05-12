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

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wjh.org.googleplay_19.base.BaseFragment;
import com.wjh.org.googleplay_19.base.LoadingPager;
import com.wjh.org.googleplay_19.base.SuperBaseAdapter;
import com.wjh.org.googleplay_19.bean.CategoryBean;
import com.wjh.org.googleplay_19.factory.ListViewFactory;
import com.wjh.org.googleplay_19.holder.BaseHolder;
import com.wjh.org.googleplay_19.holder.CategoryNormalHolder;
import com.wjh.org.googleplay_19.holder.CategoryTitleHolder;
import com.wjh.org.googleplay_19.protocol.CategoryProtocol;

import java.io.IOException;
import java.util.List;

/**
 * 分类
 */
public class CategoryFragment extends BaseFragment {

    private CategoryProtocol   mCategoryProtocol;
    private List<CategoryBean> mDatas;

    /**
     * @des 在子线程里面真正的加载数据
     * @called 外界需要加载数据的时候调用了triggerLoadData方法的时候
     */
    @Override
    public LoadingPager.LoadDataResultState initData() {

        try {
            mCategoryProtocol = new CategoryProtocol();
            mDatas = mCategoryProtocol.loadData(0);

            //检验返回的集合
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
        //创建ListView
        ListView listView = ListViewFactory.createListView();

        listView.setAdapter(new CategoryAdapter(listView, mDatas));

        return listView;
    }


    /**
     * 设置adapter;
     * 1.这里不需要返回更多数据,所以不复写父类方法
     */
    class CategoryAdapter extends SuperBaseAdapter<CategoryBean> {

        public CategoryAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }

        /**
         * 显示视图
         *
         * @param position
         * @return
         */
        @Override
        public BaseHolder getSpecialBaseHolder(int position) {
            CategoryBean bean = (CategoryBean) mDatas.get(position);

            //根据当前条目来判断是否是Title,显示两种不同的布局
            if (bean.isTitle) {
                return new CategoryTitleHolder();
            } else {
                return new CategoryNormalHolder();
            }
        }

        /**
         * 复写父类方法,添加一个Type类型
         */
        @Override
        public int getViewTypeCount() {
            //本来是有2中类型的,现在需要加多一个Tiltle类型,所以在 +1
            return super.getViewTypeCount() + 1; //2+1
        }

        /**复写父类方法,返回Title的Type类型,不复写,会出现Holder条目复用错乱情况*/
        @Override
        public int getHolderItemViewType(int position) {
            CategoryBean bean = (CategoryBean) mDatas.get(position);
            if (bean.isTitle) {
                //如果是Title部分,显示Title的Type类型
                return 2;
            } else {
                //否则就是普通Type类型
                return 1;
            }
        }
    }
}
