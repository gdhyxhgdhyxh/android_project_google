package com.wjh.org.googleplay_19.base;

import android.widget.BaseAdapter;

import java.util.List;

/*
 * @创建者     wjh
 * @创建时间   2016/2/26 20:59
 * @描述	      抽取一个常规的BaseAdapter,实现三个方法,有一个getView()让子类选择实现
 * @描述	      自定义泛型标记ITEMBEANTYPE(因为具体传递的集合是什么类型不知道)
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public abstract class MyBaseAdapter<ITEMBEANTYPE> extends BaseAdapter {

    public List<ITEMBEANTYPE> mDatas;

    public MyBaseAdapter(List<ITEMBEANTYPE> datas) {
        this.mDatas = datas;
    }



    @Override
    public int getCount() {
        if(mDatas!=null){
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mDatas !=null){
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
