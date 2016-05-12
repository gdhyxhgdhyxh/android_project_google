package com.wjh.org.googleplay_19.utils;

import android.content.Context;
import android.content.res.Resources;

import com.wjh.org.googleplay_19.base.MyApplication;

/*
 * @创建者     wjh
 * @创建时间   2016/2/24 12:43
 * @描述	      Ui操作的工具类
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class UIUtils {

    /**
     * 获取上下文
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 获取Resources对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取应用程序的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 获取String.xml的字符
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取String.xml的字符数组
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取color.xml的颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }


    /**
     *  px像素值转dp值
     * @param px
     * @return
     */
    public static int px2dp(int px) {
        //1. px*dp = density
        //2. px/(ppi/160) = dp
        /*
         320x480    density = 1     ppi:160   1px=1dp
         480x800    density = 1.5   ppi:240   1.5px = 1dp
         720x1280   density=2       ppi:320   2px = 1dp
         */

        float density = getResources().getDisplayMetrics().density;
        return (int) (px / density + .5f);

    }

    /**
     *  dp值转px像素值
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        //dp--->px
        //获取ppi值,
        float density = getResources().getDisplayMetrics().density;
        //使用公式
        return (int) (dp * density + .5f);
    }

}
