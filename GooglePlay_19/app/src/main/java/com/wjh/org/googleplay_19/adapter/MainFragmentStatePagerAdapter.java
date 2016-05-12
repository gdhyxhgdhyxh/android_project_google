package com.wjh.org.googleplay_19.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wjh.org.googleplay_19.factory.FragmentFactory;
import com.wjh.org.googleplay_19.utils.LogUtils;

/*
 * @创建者     wjh
 * @创建时间   2016/2/24 20:18
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  内容页面的页签对应的ViewPager的Adapter
 *  MainFragmentStatePagerAdapter:说明没有缓存Fragment,会缓存FragmentState
 */
public class MainFragmentStatePagerAdapter extends FragmentPagerAdapter {
    //传递过来的数据
    String[] mTitles;
    public MainFragmentStatePagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        mTitles = titles;
    }


    /**
     *  返回ViewPager的页面样子
     * @param position Fragment对应的position页面
     * @return fragment
     */
    @Override
    public Fragment getItem(int position) {
        LogUtils.sf("初始化  = "+FragmentFactory.createFragment(position));
        //把position传递到工厂类,构建对应的position页面
        Fragment fragment = FragmentFactory.createFragment(position);

        return fragment;
    }

    /**返回ViewPager页面的数量*/
    @Override
    public int getCount() {
        if(mTitles!=null){
            return mTitles.length;
        }
        return 0;
    }

    /**
     *  由于与PagerSlidingTabStrip绑定在一起,所以要复写这个方法,否则会包null异常
     * @param position
     * @return 页签对应的页面
     */
    @Override
    public CharSequence getPageTitle(int position) {

        return mTitles[position];
    }
}
