package com.wjh.org.googleplay_19.factory;

import android.support.v4.app.Fragment;

import com.wjh.org.googleplay_19.base.BaseFragment;
import com.wjh.org.googleplay_19.fragment.AppFragment;
import com.wjh.org.googleplay_19.fragment.CategoryFragment;
import com.wjh.org.googleplay_19.fragment.GameFragment;
import com.wjh.org.googleplay_19.fragment.HomeFragment;
import com.wjh.org.googleplay_19.fragment.RecommendFragment;
import com.wjh.org.googleplay_19.fragment.HotFragment;
import com.wjh.org.googleplay_19.fragment.SubjectFragment;

import java.util.HashMap;
import java.util.Map;

/*
 * @创建者     wjh
 * @创建时间   2016/2/24 20:37
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * 用来构建Fragment的工厂类
 */
public class FragmentFactory {

    /*<item>首页</item>
    <item>应用</item>
    <item>游戏</item>
    <item>专题</item>
    <item>推荐</item>
    <item>分类</item>
    <item>排行</item>*/

    private static final int                        FRAGMENT_HOME      = 0;
    private static final int                        FRAGMENT_APP       = 1;
    private static final int                        FRAGMENT_GEAM      = 2;
    private static final int                        FRAGMENT_SUBJECT   = 3;
    private static final int                        FRAGMENT_RECOMMEND = 4;
    private static final int                        FRAGMENT_ASSORT    = 5;
    private static final int                        FRAGMENT_ROW       = 6;
    public static        Map<Integer, BaseFragment> mFragmentMap       = new HashMap<>();


    /**
     * 根据传递进来的ViewPager的position构建对应的Fragment
     *
     * @param position
     * @return fragment
     */
    public static Fragment createFragment(int position) {

        //初始化对象
        BaseFragment fragment = null;

        //判断集合中是否包含,如果包含才去获取
        if (mFragmentMap.containsKey(position)) {
            fragment = mFragmentMap.get(position);
            return fragment;
        }
        switch (position) {
            case FRAGMENT_HOME:
                fragment = new HomeFragment();  //首页
                break;
            case FRAGMENT_APP:
                fragment = new AppFragment();  //应用
                break;
            case FRAGMENT_GEAM:
                fragment = new GameFragment(); //游戏
                break;
            case FRAGMENT_SUBJECT:
                fragment = new SubjectFragment();  //专题
                break;
            case FRAGMENT_RECOMMEND:
                fragment = new RecommendFragment(); //推荐
                break;
            case FRAGMENT_ASSORT:
                fragment = new CategoryFragment(); //分类
                break;
            case FRAGMENT_ROW:
                fragment = new HotFragment();//排行
                break;

            default:
                break;
        }
        //保存fragment到集合
        mFragmentMap.put(position,fragment);

        return fragment;
    }
}
