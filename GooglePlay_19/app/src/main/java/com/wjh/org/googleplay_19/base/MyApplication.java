package com.wjh.org.googleplay_19.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;
import java.util.Map;

/*
 * @创建者     wjh
 * @创建时间   2016/2/24 12:25
 * @描述	     全局的类,可以用来初始化变量/方法,该类就是一个单例(只初始化一次)
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static int     mMainThreadId;
    private static Handler mMainHandler;
    Map<String, String> mCacheMap = new HashMap<>(); //用来存储网络协议的缓存集合
    private RefWatcher mRefWatcher;

    /**暴露方法给所有人调用*/
    public Map<String, String> getCacheMap() {
        return mCacheMap;
    }

    /**获取Context上下文*/
    public static Context getContext() {
        return mContext;
    }

    /**获取主线程id*/
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**获取主线程Handler*/
    public static Handler getMainHandler() {
        return mMainHandler;
    }

    /**获取LeakCanary的抓取泄露的方法*/
    public static RefWatcher getRefWatcher() {
        MyApplication application = (MyApplication) mContext.getApplicationContext();
        return application.mRefWatcher;
    }

    /**在全局的类中初始化一些方法和变量,会在主线程中执行*/
    @Override
    public void onCreate() {
        super.onCreate();

        //配置LeakCanary,进行检测
        mRefWatcher = LeakCanary.install(this);


        //上下文
        mContext = getApplicationContext();

        //主线程Id
        mMainThreadId = android.os.Process.myTid();

        //主线程的handlder
        mMainHandler = new Handler();


        initImageLoader(mContext);

    }

    /**
     *  初始化ImageLoader图片加载的框架;
     *  1.设置一些配置,也有默认配置
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)//
                .threadPriority(Thread.NORM_PRIORITY - 2)//
                .denyCacheImageMultipleSizesInMemory()//
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .memoryCache(new LruMemoryCache(4 * 1024 * 1024)).tasksProcessingOrder(QueueProcessingType.LIFO)//
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
