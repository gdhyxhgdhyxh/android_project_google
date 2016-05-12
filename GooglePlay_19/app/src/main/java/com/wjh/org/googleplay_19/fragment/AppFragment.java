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

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wjh.org.googleplay_19.adapter.ItemAdapter;
import com.wjh.org.googleplay_19.base.BaseFragment;
import com.wjh.org.googleplay_19.base.LoadingPager;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.factory.ListViewFactory;
import com.wjh.org.googleplay_19.protocol.AppProtocol;

import java.io.IOException;
import java.util.List;

/**
 * 应用
 */
public class AppFragment extends BaseFragment {

    private List<ItemBean> mDatas;
    private AppProtocol    mAppProtocol;

    /**
     * @des 在子线程里面真正的加载数据
     * @called 外界需要加载数据的时候调用了triggerLoadData方法的时候
     */
    @Override
    public LoadingPager.LoadDataResultState initData() {

        try {
            mAppProtocol = new AppProtocol();
            //请求网络数据
            mDatas = mAppProtocol.loadData(0);

            //校验请求协议返回的状态
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

        //设置Adapter
        listView.setAdapter(new AppAdapter(listView, mDatas));

        return listView;
    }

    /**
     * App的ListView适配器
     */
    class AppAdapter extends ItemAdapter {

        public AppAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }

        /**
         *  重复父类方法,把集合数据,传递过去加载更多数据
         * @return
         * @throws Exception
         */
        @Override
        public List<ItemBean> initLoadMore() throws Exception {

            SystemClock.sleep(2000); //休眠,然后再去加载

            //返回一个集合
            List<ItemBean> itemBeans = mAppProtocol.loadData(mDatas.size());
            return itemBeans;
        }
    }
}
