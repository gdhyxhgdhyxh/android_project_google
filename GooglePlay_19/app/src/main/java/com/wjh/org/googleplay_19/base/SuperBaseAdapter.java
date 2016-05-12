package com.wjh.org.googleplay_19.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wjh.org.googleplay_19.factory.ThreadPoolProxyFactory;
import com.wjh.org.googleplay_19.holder.BaseHolder;
import com.wjh.org.googleplay_19.holder.LoadMoreHolder;

import java.util.List;

/*
 * @创建者     wjh
 * @创建时间   2016/2/26 21:52
 * 描述	     主要针对的是BaseAdapter中的getView方法
 * 描述	     通过引入BaseHolder进来
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * 最终的Adapter的抽取
 *
 * @param <ITEMBEANTYPE>
 */
public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends MyBaseAdapter implements AdapterView.OnItemClickListener {
    private static final int VIEWTYPE_LOADMORE = 0; //加载更多类型
    private static final int VIEWTYPE_NORMAL   = 1;   //正常类型
    private static final int PAGESIZE          = 20;  //每次加载更多的页数
    private       AbsListView    mAbsListView;
    private       LoadMoreHolder mLoadMoreHolder;
    private       int            mState;
    private       LoadMoreTask   mLoadMoreTask;

    public SuperBaseAdapter(AbsListView absListView, List datas) {
        super(datas);
        //让外界调用者传递一个listView进来(由于这里有可能是ListView/GridView,所以直接用它们父类)
        mAbsListView = absListView;
        mAbsListView.setOnItemClickListener(this);
    }

    /**
     * 获得ViewType对应的ItemView对应的类型的总和
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {
        //默认是1(普通类型),+1是(普通类型+加载更多类型)
        return super.getViewTypeCount() + 1;
    }

    /**
     * 获取条目的总数
     *
     * @return
     */
    @Override
    public int getCount() {
        //因为条目的最后一个,是原数据,需要+1把加载更多类型显示出来.
        return super.getCount() + 1;
    }

    /**
     * 获得Item对应的ViewType类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if (position == getCount() - 1) {
            return VIEWTYPE_LOADMORE;   //加载更多
        } else {
            //通过一个方法返回Type类型
            return getHolderItemViewType(position);
        }
    }

    /**
     *  1.把它抽成方法,是因为子类可以重写这个方法,返回自己的type类型;
     *  2.子类复写,子类先调用
     * @param position
     * @return 1是默认普通类型
     */
    public int getHolderItemViewType(int position) {
         return VIEWTYPE_NORMAL;     //默认是普通类型;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            /*--------------- 决定根布局 ---------------*/
        BaseHolder baseHolder = null;
        int curItemViewType = getItemViewType(position); //获取条目的类型

        if (convertView == null) {
            //判断应该显示对应的type类型
            if (curItemViewType == VIEWTYPE_LOADMORE) {
                //0 .显示加载更多类型的条目
                baseHolder = getLoadMoreHolder();
            } else {

                //1. //普通条目的baseHolder子类
                baseHolder = getSpecialBaseHolder(position);
            }
            //2. 与baseHolder绑定在一起,免去了设置标记(已经在基类封装了)
            convertView = baseHolder.mHolderView;

        } else {
            //3.如果有复用,获取标记
            baseHolder = (BaseHolder) convertView.getTag();
        }

            /*--------------- 数据和视图的绑定 ---------------*/

        if (curItemViewType == VIEWTYPE_LOADMORE) {
            //显示加载更多类型的数据
            if (hasLoadMore()) {
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_LOADING);

                //触发加载更多数据
                triggerLoadMoreData();
            } else {
                //没有更多数据
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_NONE);
            }
        } else {
            //显示普通类型的数据
            baseHolder.setDataAndRefreshHolderView(mDatas.get(position));
        }


        return convertView;
    }

    /**
     * @return
     * @des 肯定是BaseHolder子类对象
     * @des SuperBaseAdapter中不能知道具体返回什么样的BaseHolder子类对象, 只能交给子类实现, 而且是必须实现
     * @param position
     */
    public abstract BaseHolder getSpecialBaseHolder(int position);

    /*--------------- 加载更多相关 ---------------*/

    /**
     * 1.触发加载更多数据;
     * 2.异步加载数据
     */
    public void triggerLoadMoreData() {
        if (mLoadMoreTask == null) {

            //在加载数据时,默认设置正在加载中
            int state = 0;
            mLoadMoreHolder.setDataAndRefreshHolderView(state);

            //给子线程对象判空操作,不允许在加载中,连续点击加载;
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolProxyFactory.createNormalThreadPoolProxy().execute(mLoadMoreTask);
        }
    }


    /**
     * 1.异步加载更多数据;
     * 2.在子线程中
     */
    class LoadMoreTask implements Runnable {

        @Override
        public void run() {

            /*--------------- 定义变量,赋予初始值 ---------------*/
            //真正的去加载更多数据(加载回来的数据不明确,所以用泛型集合接收)
            List<ITEMBEANTYPE> loadMoreList = null;
            //记录是否加载更多的状态值,根据返回的状态值进行赋值
            mState = 0;


            try {
            /*--------------- 加载数据,得到数据,处理数据 ---------------*/
                loadMoreList = initLoadMore();

                if (loadMoreList == null) {
                    //集合是空,没有更多数据
                    mState = LoadMoreHolder.LOADMORE_NONE;
                } else {
                    //集合不为空,判断长度是否与加载更多的数量一样
                    if (loadMoreList.size() == PAGESIZE) {
                        //有可能有加载更多
                        mState = LoadMoreHolder.LOADMORE_LOADING;
                    } else {
                        //小于的情况,没有更多数据
                        mState = LoadMoreHolder.LOADMORE_NONE;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //网络加载出错,加载失败,点击重试
                mState = LoadMoreHolder.LOADMORE_RETRY;
            }

            /*--------------- 创建临时变量,保存数据 ---------------*/
            final List<ITEMBEANTYPE> tempLoadMoreList = loadMoreList;


            /*--------------- 根据数据,刷新ui ---------------*/
            //需要去主线程更新Ui
            MyApplication.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    //添加子类传递回来的集合数据,然后更新Ui
                    if (tempLoadMoreList != null)  {
                        //listview--->mdatas--->mdatas.addAll(tempLoadMoreList)--->adapter.notifyDataSetChanged();
                        mDatas.addAll(tempLoadMoreList);
                        notifyDataSetChanged();
                    }

                    //更新Ui加载更多的状态  刷新ui--->loadmore--->state
                    mLoadMoreHolder.setDataAndRefreshHolderView(mState);
                }
            });
            //把线程对象置为空
            mLoadMoreTask = null;
        }
    }

    /***
     * 1.在子线程加载更多的数据;
     * 2.由于superBaseAdapter不知道具体的数据是什么,所以交给子类去选择性实现;
     * 3.去加载数据的时候有可能出现异常,而这里刚好也需要抛出异常做处理,所以在方法声明上抛出异常
     * @return
     */
    public List<ITEMBEANTYPE> initLoadMore() throws Exception {
        return null;
    }

    /**
     * 1.判断是否有加载更多数据;
     * 2.默认是没有加载更多数据
     * 3.让子类选择性实现该方法,不一定有更多数据
     *
     * @return true:有,false:没有
     */
    public boolean hasLoadMore() {
        return false;
    }

    /**
     * 1.获取加载更多的布局的对象(经过data+view绑定的布局)
     *
     * @return
     */
    private BaseHolder getLoadMoreHolder() {
        if (mLoadMoreHolder == null) {
            //保证SuperBaseAdapter里只初始化一次LoadMoreHolder对象
            mLoadMoreHolder = new LoadMoreHolder();
        }
        return mLoadMoreHolder;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //判断处理点击0条目的ViewPager头的时候,会出现点击的是1
        if(mAbsListView instanceof ListView){
            //如果当前点击的条目是ListView ,就要把头布局总数减去
            position = position -((ListView) mAbsListView).getHeaderViewsCount();
        }

        //获取点击Item的ViewType类型
        int curItemViewType = getItemViewType(position);

            //如果是加载更多类型
        if(curItemViewType == VIEWTYPE_LOADMORE){
                //如果状态是加载失败,点击重试的状态
            if(mState == LoadMoreHolder.LOADMORE_RETRY){
                //重新触发加载数据
                triggerLoadMoreData();
            }
        }else{
            //普通条目类型,
            OnNorMalItemClick(parent, view, position, id);
        }
    }

    /**
     * @des 自己定义一个条目点击的方法,然后交给子类去实现点击事件;
     * @des 由于SuperBaseAdapter不知道具体点击的是哪个条目,所以暴露给子类去复写
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void OnNorMalItemClick(AdapterView<?> parent, View view, int position, long id){

    }


}
