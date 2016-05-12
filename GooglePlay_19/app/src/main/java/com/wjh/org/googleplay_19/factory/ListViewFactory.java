package com.wjh.org.googleplay_19.factory;

/*
 * @创建者     wjh
 * @创建时间   2016/2/29 20:46
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.wjh.org.googleplay_19.utils.UIUtils;

/**
 * ListView工厂类
 */
public class ListViewFactory {

    /**
     *  1.使用工厂类创建ListView;
     *  2.好处是:只需要设置一次属性就可以了
     * @return
     */
    public static ListView createListView(){

        ListView listView = new ListView(UIUtils.getContext());
        listView.setCacheColorHint(Color.TRANSPARENT); //复用的缓存
        listView.setDividerHeight(0); //条目下划线的高度
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));//点击的背景颜色
        listView.setFastScrollEnabled(true);//右边滚动条

        return listView;
    }
}
