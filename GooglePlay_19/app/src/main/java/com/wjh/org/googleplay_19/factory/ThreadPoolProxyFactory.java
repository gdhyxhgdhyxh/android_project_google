package com.wjh.org.googleplay_19.factory;

import com.wjh.org.googleplay_19.proxy.ThreadPoolProxy;

/*
 * @创建者     wjh
 * @创建时间   2016/2/27 12:58
 * @描述	      产生不同ThreadPoolProxy(线程池代理)
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  ThreadPoolProxyFactory线程池代理工厂;
 *  两个代理,产生不同ThreadPoolProxy(线程池代理)
 */
public class ThreadPoolProxyFactory {

    static ThreadPoolProxy mNormalThreadPoolProxy;  //正常的普通线程池代理
    static ThreadPoolProxy mDownLoadThreadPoolProxy;//下载的线程池代理

    /**
     *  普通的线程池代理
     * @return
     */
    public static ThreadPoolProxy createNormalThreadPoolProxy(){

        //使用双重检查加锁机制,保证工厂代理类只静态加载一次
        if(mNormalThreadPoolProxy == null){

            synchronized (ThreadPoolProxyFactory.class){

                if(mNormalThreadPoolProxy == null){

                    mNormalThreadPoolProxy = new ThreadPoolProxy(5,5);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }

    /**
     *  下载的线程代理
     * @return
     */
    public static ThreadPoolProxy createDownLoadThreadPoolProxy(){
        //使用双重检查加锁机制,保证工厂代理类只静态加载一次
        if(mDownLoadThreadPoolProxy == null){

            synchronized (ThreadPoolProxyFactory.class){

                if(mDownLoadThreadPoolProxy == null){

                    mDownLoadThreadPoolProxy = new ThreadPoolProxy(3,3);
                }
            }
        }

        return mDownLoadThreadPoolProxy;
    }
}
