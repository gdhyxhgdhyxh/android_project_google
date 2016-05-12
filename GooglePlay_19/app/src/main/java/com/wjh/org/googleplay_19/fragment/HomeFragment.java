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
import com.wjh.org.googleplay_19.bean.HomeBean;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.factory.ListViewFactory;
import com.wjh.org.googleplay_19.holder.HomePictureHolder;
import com.wjh.org.googleplay_19.protocol.HomeProtocol;

import java.io.IOException;
import java.util.List;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    private List<ItemBean> mDatas;
    private List<String>   mPicture;
    HomeProtocol mHomeProtocol;

    /**
     * @des 在子线程里面真正的加载数据
     * @called 外界需要加载数据的时候调用了triggerLoadData方法的时候
     */
    @Override
    public LoadingPager.LoadDataResultState initData() {

        /*
        public static final int STATE_EMPTY   = 1; // 空的状态
        public static final int STATE_ERROR   = 2; // 错误的状态
        public static final int STATE_SUCCESS = 3; // 成功的状态
        */

        try {

            /*--------------- 协议简单封装后 ---------------*/

            //使用协议封装的类去加载网络数据
            mHomeProtocol = new HomeProtocol();
            HomeBean homeBean = mHomeProtocol.loadData(0);

            //校验对象,返回加载的状态
            LoadingPager.LoadDataResultState state = checkResData(homeBean);
            if(state != LoadingPager.LoadDataResultState.SUCESS){
                //如果返回的状态不是成功视图,就说明HomeBean为null了,出错
                return state;
            }

            //校验对象中的集合
            state = checkResData(homeBean.list);
            if(state != LoadingPager.LoadDataResultState.SUCESS){
                //如果校验的集合返回的不是成功,也说明出问题了
                return state;
            }

            //走到这里,说明返回的数据是有效的,获取数据,解析对象的数据
            mDatas = homeBean.list;
            mPicture = homeBean.picture;

        } catch (IOException e) {
            e.printStackTrace();
            //出异常了
            return LoadingPager.LoadDataResultState.ERROR;
        }
        return LoadingPager.LoadDataResultState.SUCESS; //3种状态,空,错误,成功
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

        //构建轮播图实现类
        HomePictureHolder pictureHolder = new HomePictureHolder();
        //设置需要展示的图片集合数据
        pictureHolder.setDataAndRefreshHolderView(mPicture);
        //添加一个ListView的头布局(经过data+view)
        listView.addHeaderView(pictureHolder.mHolderView);

        //设置Adapter
        listView.setAdapter(new HomeAdapter(listView,mDatas));

        return listView;
    }

    /**
     * 三版本: 构建adapter,继承抽取的MyBaseAdapter,抽取成一个superBaseAdapter;
     *
     * @des 抽取代码, 封装
     * 1.对getView()进行抽取,然后把MyBaseAdapter里抽取的3个方法进行关联
     */
    class HomeAdapter extends ItemAdapter {


        public HomeAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }

        /**
         *  在子线程中真正加载数据
         * @return
         * @throws Exception
         */
        @Override
        public List<ItemBean> initLoadMore() throws Exception {

            SystemClock.sleep(2000); //休眠,然后再去加载

            HomeBean homeBean = mHomeProtocol.loadData(mDatas.size());
            if (homeBean != null) {
                return homeBean.list;
            }
            return null;
        }
    }
    /**
     *  二版本: 构建adapter,继承抽取的MyBaseAdapter;
     *  1.把getView()里的代码和ViewHolder进行抽取
     */
/*    class HomeAdapter extends MyBaseAdapter<String>{

        public HomeAdapter(List<String> datas) {
            super(datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            *//*--------------- 决定根布局 ---------------*//*
            ItemHolder homeHolder = null;
            if(convertView == null){
                //1. 构建抽取的HomeHolder类
                homeHolder = new ItemHolder();
                //2. 与HomeHolder绑定在一起,免去了设置标记(已经在基类封装了)
                convertView = homeHolder.mHolderView;
            }else{
                //3.如果有复用,获取标记
                homeHolder = (ItemHolder) convertView.getTag();
            }

            *//*--------------- 数据和视图的绑定 ---------------*//*
            homeHolder.setDataAndRefreshHolderView(mDatas.get(position));

            return convertView;
        }
    }*/

    /**
     *  一版本: 构建adapter,继承抽取的MyBaseAdapter
     *  1.对BaseAdapter里的getItem(),getItemId(),getCount()进行抽取
     */
/*    class HomeAdapter  extends MyBaseAdapter {

        public HomeAdapter(List<String> datas) {
            super(datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){

                //1.构建ViewHolder对象(避免重复构建布局的条目)
                holder = new ViewHolder();
                //2.加载布局
                convertView = View.inflate(UIUtils.getContext(), R.layout.home_listview_item,null);
                //3.查找布局控件
                holder.mTmp_tv_1 = (TextView) convertView.findViewById(R.id.tmp_tv_1);
                holder.mTmp_tv_2 = (TextView) convertView.findViewById(R.id.tmp_tv_2);

                //4.设置标记(减轻findViewById的工作)
                convertView.setTag(holder);
            }else{
                //5. 复用标记
                holder = (ViewHolder) convertView.getTag();
            }

            //6.获取条目对应的数据
            String data = mDatas.get(position);
            //7.控件赋值
            holder.mTmp_tv_1.setText("我是头 = "+data);
            holder.mTmp_tv_2.setText("我是尾 = "+data);

            return convertView;
        }

        class ViewHolder{
            TextView mTmp_tv_1;
            TextView mTmp_tv_2;
        }
    }*/

}
