package com.wjh.org.googleplay_19.proxy;

/*
 * @创建者     wjh
 * @创建时间   2016/2/27 9:25
 * @描述	      线程池代理类,替某个线程做一些事情
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolProxy 线程池代理类做的事情;
 * 1.执行任务;
 * 2.提交任务;
 * 3.移除任务;
 * 4.使用单例模式的双重检查加锁机制,保证只初始化一次实例对象
 */
public class ThreadPoolProxy {

    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;      //线程池的大小
    private int mMaximumPoolSize;   //线程池最大线程数量

    /**
     * 通过构造方法,让外界自己设定这两个参数
     * @param corePoolSize  线程池的大小
     * @param maximumPoolSize  线程池最大线程数量
     */
    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
    }

    /**
     * 初始化线程池对象
     * @return 返回线程池
     */
    public void initThreadPoolExecutor() {
        //通过双重检查加锁--->只有第一次实例化对象的时候才会启用同步机制,提高了性能
        if (mExecutor == null|| mExecutor.isShutdown() || mExecutor.isTerminated()) {
            //如果前面有线程等待,后面的线程就进不来了
            synchronized (ThreadPoolProxy.class) {
                //是空,停止,执行完毕就进来
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {

                    long keepAliveTime = 5000; //保持时间
                    TimeUnit unit = TimeUnit.MILLISECONDS; //单位(毫秒)
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();//任务队列(无界队列)
                    ThreadFactory threadFactory = Executors.defaultThreadFactory(); //线程工厂
                    RejectedExecutionHandler handler; //异常捕获

                    //创建线程
                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit,
                            workQueue, threadFactory);
                }
            }
        }
    }

     /*
    执行任务和提交任务的区别?
     1.是否有返回值
        execute-->没有返回值
        submit-->有返回值
     2.submit返回回来的Future,干嘛的,有啥用?
           Future-->接收任务执行完成之后的结果
            1.可以使用其中的get方法接收结果
            2.其中get方法还是一个阻塞方法
            3.得到任务执行过程中抛出的异常
     */

    /**
     * 执行任务
     *
     * @param task
     */
    public void execute(Runnable task) {
        //初始化线程池对象
        initThreadPoolExecutor();

        mExecutor.execute(task);
    }

    /**
     * 提交任务
     * @param task
     * @return
     */
    public Future<?> submit(Runnable task) {
        //初始化线程池对象
        initThreadPoolExecutor();
        Future<?> submit = mExecutor.submit(task);
        return submit;
    }

    /**
     * 移除任务
     *
     * @param task
     */
    public void remove(Runnable task) {
        //初始化线程池对象
        initThreadPoolExecutor();

        mExecutor.remove(task);
    }
}
