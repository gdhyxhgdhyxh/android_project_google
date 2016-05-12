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
import com.wjh.org.googleplay_19.protocol.GameProtocol;

import java.io.IOException;
import java.util.List;

/**
 * 游戏
 */
public class GameFragment extends BaseFragment {

    private List<ItemBean> mDatas;
    private GameProtocol   mGameProtocol;

    /**
     * @des 在子线程里面真正的加载数据
     * @called 外界需要加载数据的时候调用了triggerLoadData方法的时候
     */
    @Override
    public LoadingPager.LoadDataResultState initData() {

        try {
            //加载网络请求
            mGameProtocol = new GameProtocol();
            mDatas = mGameProtocol.loadData(0);

            //校验返回的状态
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

        listView.setAdapter(new GameAdapter(listView,mDatas));

        return listView;
    }

    class GameAdapter extends ItemAdapter {

        public GameAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }

        /**
         *  加载更多数据
         * @return
         * @throws Exception
         */
        @Override
        public List<ItemBean> initLoadMore() throws Exception {

            SystemClock.sleep(2000); //休眠时间

            List<ItemBean> itemBeans = mGameProtocol.loadData(mDatas.size());
            //返回集合数据
            return itemBeans;
        }
    }
}
