package com.wjh.org.googleplay_19.holder;

import android.view.View;

/*
 * @创建者     wjh
 * @创建时间   2016/2/26 21:26
 * 描述	      1.提供视图
 * 描述	      2.接收数据
 * 描述	      3.数据和视图的绑定
 * 描述        针对所有BaseAdapter里面的getView方法和ViewHolder类里面相关的代码
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * ItemHolder 的抽取基类，把接收的数据和视图在基类中实现
 * @param <ITEMBEANTYPE>
 */
public abstract class BaseHolder<ITEMBEANTYPE> {

    public View mHolderView;   //视图
    public ITEMBEANTYPE mDatas;//泛型数据

    public BaseHolder() {
        //1.初始化根部局
        mHolderView = initHolderView();
        //mHolderView去找一个对象(可以充当ViewHolder),然后进行绑定
        mHolderView.setTag(this);
    }

    /**
     * @return
     * @des 初始化持有的视图
     * @des BaseHolder其实不知道如何具体初始化视图信息, 所以只有交给子类, 而且子类必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     */
    public abstract View initHolderView() ;

    /**
     * @param datas
     * @des 接收数据, 数据和视图的绑定操作
     */
    public void setDataAndRefreshHolderView(ITEMBEANTYPE datas){
        //保存数据到成员变量
        this.mDatas = datas;
        refreshHolderView(datas);
    }

    /**
     * @param datas
     * @des 数据和视图的绑定操作
     * @des 在BaseHolder中, 其实不知道如何具体进行数据和视图的绑定, 而且子类是必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     */
    public abstract void refreshHolderView(ITEMBEANTYPE datas);
}
