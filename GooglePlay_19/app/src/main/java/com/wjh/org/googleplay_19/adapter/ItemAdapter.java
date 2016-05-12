package com.wjh.org.googleplay_19.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.wjh.org.googleplay_19.activity.DetailedActivity;
import com.wjh.org.googleplay_19.base.SuperBaseAdapter;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.holder.BaseHolder;
import com.wjh.org.googleplay_19.holder.ItemHolder;
import com.wjh.org.googleplay_19.utils.UIUtils;

import java.util.List;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 20:42
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  抽取App/Home/Game相同的Adapter为一个基类;
 *  1.在这里实现一次点击事件监听,3个都共用
 */
public class ItemAdapter extends SuperBaseAdapter<ItemBean> {
    public ItemAdapter(AbsListView absListView, List datas) {
        super(absListView, datas);
    }

    /**
     * @return
     * @des 肯定是BaseHolder子类对象
     * @param position
     */
    @Override
    public BaseHolder getSpecialBaseHolder(int position) {
        return new ItemHolder();
    }

    /**
     * 复写方法,返回true,代表有更多数据
     *
     * @return
     */
    @Override
    public boolean hasLoadMore() {
        return true;
    }

    @Override
    public void OnNorMalItemClick(AdapterView<?> parent, View view, int position, long id) {

        ItemBean bean = (ItemBean) mDatas.get(position);

        //点击跳转详情页面
        Intent intent = new Intent(UIUtils.getContext(),DetailedActivity.class);
        //把包名传递过去
        intent.putExtra("packageName",bean.packageName);
        //在不是activity的页面跳转,需要添加这个标记,因为这里没有任务栈
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        UIUtils.getContext().startActivity(intent);

        Toast.makeText(UIUtils.getContext(), bean.name, Toast.LENGTH_SHORT).show();
    }
}
