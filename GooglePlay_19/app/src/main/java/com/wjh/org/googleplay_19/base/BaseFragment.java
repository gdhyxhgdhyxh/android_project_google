package com.wjh.org.googleplay_19.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wjh.org.googleplay_19.utils.UIUtils;

import java.util.List;
import java.util.Map;

/*
 * @创建者     wjh
 * @创建时间   2016/2/26 12:27
 * @描述	     该项目使用的Fragment基类
 * @描述      1.提供视图
 * @描述      2.加载数据/接收数据
 * @描述      3.绑定视图和数据
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public abstract class BaseFragment extends Fragment{

    private LoadingPager mLoadingPager;

    /**
     *  暴露方法,获取LoadingPager对象
     * @return
     */
    public LoadingPager getLoadingPager() {
        return mLoadingPager;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //判断是否为空,如果是空才进行加载,避免重复构建
        if(mLoadingPager == null) {
            //返回4种视图类型中的一种(由于类是抽象类,所以使用匿名实现的子类,实现方法)
            mLoadingPager = new LoadingPager(UIUtils.getContext()) {

                @Override
                public View initSuccessView() {
                    //1. 使用BaseFragment同名的initSuccessView()方法
                    return BaseFragment.this.initSuccessView();
                }

                @Override
                public LoadDataResultState initData() {
                    //1. 使用BaseFragment同名的initData()方法
                    return BaseFragment.this.initData();
                }
            };
        }
        return mLoadingPager;
    }
    /**
     * @return
     * @des BaseFragment里面无法知道具体成功视图视图, 只能交给子类,而且发现是必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法交给子类具体实现
     */
    public abstract LoadingPager.LoadDataResultState initData() ;

    /**
     * @return
     * @des BaseFragment里面无法知道具体成功视图视图, 只能交给子类,而且发现是必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法交给子类具体实现
     */
    public abstract View initSuccessView() ;


    /**
     * @des 校验网络请求回来的bean/list/map是否为空
     * @des 返回加载数据的状态
     * @param resObj
     * @return 返回加载数据的状态
     */
    public LoadingPager.LoadDataResultState checkResData(Object resObj){

        //Obj对象
        if(resObj == null){
            //如果返回的对象为空,显示空视图
            return LoadingPager.LoadDataResultState.EMPTY;
        }

        //List集合
        if(resObj instanceof List){
            if(((List) resObj).size() == 0){
                //如果返回的List集合为空,显示空视图
                return LoadingPager.LoadDataResultState.EMPTY;
            }
        }

        //Map集合
        if(resObj instanceof Map){
            if(((Map) resObj).size() == 0){
                //如果返回的Map集合长度为空,显示空视图
                return LoadingPager.LoadDataResultState.EMPTY;
            }
        }

        //上面都不为空,就返回成功视图
        return LoadingPager.LoadDataResultState.SUCESS;
    }
}
    /*
    一,一个app视图展示方面有哪些特点

    1.一个app基本都是有4种视图类型组成
        1.加载中的视图-->初始化的时候
        2.成功视图-->真正想展示的视图
        失败
            3.空视图-->请求成功了.但是数据为空,这个时候应该显示的视图
            4.错误视图-->请求失败,请求异常-->用户可以点击重试

    2.一个Fragment/Activity同一时刻只可能显示4种视图信息中的1种
        coni 0
        coni 1
        coni 2
        coni 3
        coni 4
        coni 5
        curState==>决定了视图的显示的类型

    3.加载中的视图,空视图,错误视图基本属于静态视图,应用程序基本是固定的,
    但是其中的成功视图是经常变化

    4.最先应该展示什么视图给用户看?
        加载中的视图
    5.curState直接影响了显示视图的类型,而且我们的最终目的都是为了显示成功视图,那我们需要怎么做?
        如何状态才会发生改变-->需要进行数据的加载-->更新对应的状态--->刷新视图

   二,一个app数据加载的分析
       1.触发加载数据
         ①一进入页面就加载数据
         ②下拉刷新
         ③点击某一个按钮加载数据
       2.异步加载数据
       3.得到结果,处理结果,刷新ui
            成功-->成功视图
            失败
                空-->空视图
                错误-->错误视图
     */