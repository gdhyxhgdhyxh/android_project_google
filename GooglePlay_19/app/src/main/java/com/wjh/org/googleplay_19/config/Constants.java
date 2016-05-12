package com.wjh.org.googleplay_19.config;

import com.wjh.org.googleplay_19.utils.LogUtils;

/*
 * @创建者     wjh
 * @创建时间   2016/2/24 12:55
 * @描述	      Constants配置类;用于添加一些常量
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
/**Constants配置类;用于添加一些常量*/
public class Constants {

    /**
     * LEVEL_OFF : 关闭log日志
     * LEVEL_ALL : 打开Log日志
     */
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;

    public static long PROTOCOLTIMEOUT = 5*60*1000; //5分钟


    public static class URL {

        public static final String BASE_URL = "http://10.0.3.2:8080/GooglePlayServer/";
        //真机测试地址
        //public static final String BASE_URL = "http://127.0.0.1:8090/GooglePlayServer/";
        public static final String IMAGEURL = BASE_URL + "/image?name="; //图片url
        public static final String DOWNLOADBASEURL = BASE_URL + "download"; //下载url

    }
}
