package com.wjh.org.googleplay_19.manager;

/*
 * @创建者     wjh
 * @创建时间   2016/3/3 13:53
 * @描述	      下载管理器,负责去真正下载数据
 * @描述	      1.需要`时刻记录`当前的状态
 * @描述	      2.在DetailedDownLoadHolder中根据当前数据,返回一个一个对应的DownLoadInfo
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.factory.ThreadPoolProxyFactory;
import com.wjh.org.googleplay_19.utils.CommonUtils;
import com.wjh.org.googleplay_19.utils.FileUtils;
import com.wjh.org.googleplay_19.utils.HttpUtils;
import com.wjh.org.googleplay_19.utils.IOUtils;
import com.wjh.org.googleplay_19.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1.下载管理器; 2.使用单例,保证该对象只初始化一次
 */
public class DownLoadManager {

    public static final int STATE_UNDOWNLOAD      = 0;// 未下载
    public static final int STATE_DOWNLOADING     = 1;// 下载中
    public static final int STATE_PAUSEDOWNLOAD   = 2;// 暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;// 等待下载
    public static final int STATE_DOWNLOADFAILED  = 4;// 下载失败
    public static final int STATE_DOWNLOADED      = 5;// 下载完成
    public static final int STATE_INSTALLED       = 6;// 已安装

    // 用来保存点击下载的时候,产生不同的downLoadInfo
    private Map<String, DownLoadInfo> cacheDownLoadInfo = new HashMap<>();

    private static DownLoadManager instance; // 第一步私有化静态变量

    private DownLoadManager() {// 第二步私有化构造方法

    }

    /**
     * 获取下载管理器的实例对象
     *
     * @return
     */
    public static DownLoadManager getInstance() { // 第三步构建实例
        // 使用单例的双重检查加锁机制
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    /**
     * @param downLoadInfo 每一个下载,都是一个downLoadInfo,因为是单例,自己调用自己的
     * @des 触发下载
     */
    public void triggerDownLoad(DownLoadInfo downLoadInfo) {

        //保存当前下载的downLoadInfo
        cacheDownLoadInfo.put(downLoadInfo.packageName, downLoadInfo);

		/* ############### 当前状态---->未下载 ############### */
        downLoadInfo.curState = STATE_UNDOWNLOAD;

        // 通知观察者,downLoadInfo已经更新
        notifyOnDownLoadInfoListener(downLoadInfo);
        /* ####################################### */

		/* ############### 当前状态---->等待下载 ############### */
        downLoadInfo.curState = STATE_WAITINGDOWNLOAD;

        // 通知观察者,downLoadInfo已经更新
        notifyOnDownLoadInfoListener(downLoadInfo);
		/* ####################################### */

        /**
         * 1.使用线程池构建对象,去异步下载(立马执行,等待执行) 我预先设置为等待中 1.立马执行-->会把预先设置的等待,更改为下载中;
         * 2.等待执行-->预先设置的等待,会继续保持,不会发生改变
         */
        ThreadPoolProxyFactory.createDownLoadThreadPoolProxy().execute(
                new DownLoadTask(downLoadInfo));
    }

    /**
     * 1.在DetailedDownLoadHolder中根据当前数据,返回每一个对应的DownLoadInfo
     *
     * @param downLoadDatas 传递javabean数据
     * @return
     */
    public DownLoadInfo getDownLoadInfo(ItemBean downLoadDatas) {

        DownLoadInfo downLoadInfo = new DownLoadInfo();

        // 文件保存在外置SD卡里 --->mnt/sdcard/data/包名.apk
        File safePath = new File(FileUtils.getDir("apk"),
                downLoadDatas.packageName + ".apk");
        // 常规赋值
        downLoadInfo.safePath = safePath.getAbsolutePath(); // 路径
        downLoadInfo.downloadUrl = downLoadDatas.downloadUrl; // 下载url
        downLoadInfo.packageName = downLoadDatas.packageName; // 包名
        downLoadInfo.max = downLoadDatas.size; // 进度条最大值就是文件大小
        downLoadInfo.progress = 0; // 初始化值为0

		/*--------------- 判断不同的状态,返回对应的DownLoadInfo ---------------*/

        // 已安装 使用工具类判断是否安装
        if (CommonUtils.isInstalled(UIUtils.getContext(),
                downLoadDatas.packageName)) {
            downLoadInfo.curState = STATE_INSTALLED;
            return downLoadInfo;
        }

        // 下载完成 下载apk存在,文件大小==应有大小
        if (safePath.exists() && safePath.length() == downLoadDatas.size) {
            downLoadInfo.curState = STATE_DOWNLOADED;
            return downLoadInfo;
        }

		/*
		 * 下载中 暂停下载 等待下载
		 * 下载失败-->一个应用点击下载的时候,会产生不同的DownLoadInfo,这个DownLoadInfo就是4种状态中的一种
		 */
        if (cacheDownLoadInfo.containsKey(downLoadDatas.packageName)) {
            // 如果包含直接返回,不用更新状态,因为状态在下载过程中就时刻在记录了,所以返回对象
            downLoadInfo = cacheDownLoadInfo.get(downLoadDatas.packageName);
            return downLoadInfo;
        }

		/* 未下载,当上面条件都不满足的时候 */
        downLoadInfo.curState = STATE_UNDOWNLOAD;

        // 返回不同的downLoadInfo
        return downLoadInfo;
    }


    class DownLoadTask implements Runnable {

        private final DownLoadInfo downLoadInfo;

        // 通过构造方法,传递一个javabean对象
        public DownLoadTask(DownLoadInfo downLoadInfo) {
            this.downLoadInfo = downLoadInfo;
        }

        @Override
        public void run() {
            // 真正的去下载数据
            // 使用okHttp框架去异步请求数据

			/* ############### 当前状态---->下载中 ############### */
            downLoadInfo.curState = STATE_DOWNLOADING;

            // 通知观察者,downLoadInfo已经更新
            notifyOnDownLoadInfoListener(downLoadInfo);
			/* ####################################### */

            InputStream is = null;
            FileOutputStream fos = null;
            try {
                // 1.构建okHttpclient对象
                OkHttpClient okHttpClient = new OkHttpClient();

				/* ############### url参数拼接 ############### */
                // http://localhost:8080/GooglePlayServer/download?
                // name=app/com.itheima.www/com.itheima.www.apk&range=0

                String url = Constants.URL.DOWNLOADBASEURL + "?";
                File safeFile = new File(downLoadInfo.safePath);

                // 使用集合拼接url参数,返回拼接之后的url
                Map<String, Object> params = new HashMap<>();
                params.put("range", "0"); // 断点用到的
                params.put("name", downLoadInfo.downloadUrl); // app具体路径
                String urlParamsByMap = HttpUtils.getUrlParamsByMap(params);

                // 把拼接的url和下载请求的url拼接在一起
                url = url + urlParamsByMap;
				/* ####################################### */

                // 2.添加请求对象
                Request reques = new Request.Builder().url(url).get().build();

                // 3.发起请求
                Response response = okHttpClient.newCall(reques).execute();


                if (response.isSuccessful()) { // 有响应


                    // 3.1 获取请求回来的字节流数据
                    is = response.body().byteStream();
                    // 3.1.1 使用输出流,把数据写入sd卡文件
                    fos = new FileOutputStream(safeFile);
                    byte[] bys = new byte[1024];
                    int len = -1;

                    boolean isPause = false; //用来判断用户是否点击暂停
                    while ((len = is.read(bys)) != -1) {

                        if (downLoadInfo.curState == STATE_PAUSEDOWNLOAD) {
                            isPause = true; //用户点击了暂停下载,然后跳出,不需要继续下载
                            break;
                        }

                        // 读流的时候,就对进度条赋值
                        downLoadInfo.progress += len;

                        fos.write(bys, 0, len);

						/* ############### 当前状态---->下载中 ############### */
                        downLoadInfo.curState = STATE_DOWNLOADING;

                        // 通知观察者,downLoadInfo已经更新
                        notifyOnDownLoadInfoListener(downLoadInfo);
						/* ####################################### */

                    }

                    if (isPause) {
                        //用户点击了暂停下载,不需要做什么,因为上面已经跳出了
                    } else {
                        //不是暂停下载,下载完成

					/* ############### 当前状态---->下载完成 ############### */
                        downLoadInfo.curState = STATE_DOWNLOADED;

                        // 通知观察者,downLoadInfo已经更新
                        notifyOnDownLoadInfoListener(downLoadInfo);
					/* ####################################### */
                    }

                } else { // 没有响应
					/* ############### 当前状态---->下载失败 ############### */
                    downLoadInfo.curState = STATE_DOWNLOADFAILED;

                    // 通知观察者,downLoadInfo已经更新
                    notifyOnDownLoadInfoListener(downLoadInfo);
					/* ####################################### */
                }

            } catch (IOException e) {
                e.printStackTrace();
				/* ############### 当前状态---->下载失败 ############### */
                downLoadInfo.curState = STATE_DOWNLOADFAILED;

                // 通知观察者,downLoadInfo已经更新
                notifyOnDownLoadInfoListener(downLoadInfo);
				/* ####################################### */
            } finally {
                IOUtils.close(fos);
                IOUtils.close(is);
            }

        }
    }

	/*--------------- 自己实现观察者模式的监听  (这里是被观察者,发布消息)---------------*/

    // 1.定义接口和接口方法
    public interface OnDownLoadInfoListener {
        /**
         * 当DownLoadInfo状态发生改变的时候
         */
        void OnDownLoadInfoChanged(DownLoadInfo message);
    }

    // 2.定义观察者集合对象,保存观察者
    private List<OnDownLoadInfoListener> mOnDownLoadInfoListeners = new ArrayList<>();

    // 3.添加监听到观察者集合中
    public void addOnDownLoadInfoListener(OnDownLoadInfoListener listener) {

        if (listener == null) {
            // 如果当前调用的观察者为空,就抛出异常
            throw new NullPointerException();
        }

        if (!mOnDownLoadInfoListeners.contains(listener)) {
            // 如果观察者集合中,不包含当前调用的观察者,就添加到集合
            mOnDownLoadInfoListeners.add(listener);
        }
    }

    // 4. 移除观察者集合中的监听
    public void removeOnDownLoadInfoListener(OnDownLoadInfoListener listener) {

        mOnDownLoadInfoListeners.remove(listener);
    }

    // 5.暴露监听的方法,通知所有观察者
    public void notifyOnDownLoadInfoListener(DownLoadInfo message) {

        for (OnDownLoadInfoListener onDownLoadInfoListener : mOnDownLoadInfoListeners) {
            // 遍历观察者集合,一个一个通知观察者
            onDownLoadInfoListener.OnDownLoadInfoChanged(message);
        }
    }

    /**
     * 1.暂停下载
     * 2.在下载过程中,用户点击下载的时候调用
     *
     * @param downLoadInfo
     */
    public void pausesDownLoad(DownLoadInfo downLoadInfo) {


        /*############### 当前状态: 暂停下载 ###############*/
        downLoadInfo.curState = STATE_PAUSEDOWNLOAD;

        //通知观察者,已经发生改变
        notifyOnDownLoadInfoListener(downLoadInfo);

        /*#######################################*/
    }
}
