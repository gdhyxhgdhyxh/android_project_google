package com.wjh.org.googleplay_19.holder;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 23:03
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import android.view.View;
import android.widget.Button;

import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.base.MyApplication;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.manager.DownLoadInfo;
import com.wjh.org.googleplay_19.manager.DownLoadManager;
import com.wjh.org.googleplay_19.utils.CommonUtils;
import com.wjh.org.googleplay_19.utils.LogUtils;
import com.wjh.org.googleplay_19.utils.PrintDownLoadInfo;
import com.wjh.org.googleplay_19.utils.UIUtils;
import com.wjh.org.googleplay_19.views.ProgressButtonView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 详情页面的 应用下载按钮部分
 */
public class DetailedDownLoadHolder extends BaseHolder<ItemBean>
        implements
        DownLoadManager.OnDownLoadInfoListener {
    @Bind(R.id.app_detail_download_btn_favo)
    Button             mAppDetailDownloadBtnFavo;
    @Bind(R.id.app_detail_download_btn_share)
    Button             mAppDetailDownloadBtnShare;
    @Bind(R.id.app_detail_download_btn_download)
    ProgressButtonView mAppDetailDownloadBtnDownload;
    private ItemBean mDownLoadDatas;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View rootView = View.inflate(UIUtils.getContext(),
                R.layout.detailed_download_holder_item, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * data+view
     *
     * @param datas
     */
    @Override
    public void refreshHolderView(ItemBean datas) {
        // 保存成成员变量
        mDownLoadDatas = datas;

		/*--------------- 2.根据不同的状态给用户提示 (修改按钮的ui)---------------*/
        // 得到状态--->downLoadInfo--->根据当前的ItemBean的数据获得一个指定的downLoadInfo
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance()
                .getDownLoadInfo(mDownLoadDatas);
        refreshDownLoadBtnByUI(downLoadInfo);

    }

    /**
     * 1.刷新下载按钮的ui状态
     *
     * @param downLoadInfo
     */
    private void refreshDownLoadBtnByUI(DownLoadInfo downLoadInfo) {

		/*
         * 状态(编程记录) | 给用户的提示(ui展现) ----------------|----------------------- 未下载
		 * |下载 下载中 |显示进度条 暂停下载 |继续下载 等待下载 |等待中... 下载失败 |重试 下载完成 |安装 已安装 |打开
		 */
        /*
         * public static final int STATE_UNDOWNLOAD = 0;//未下载 public static
		 * final int STATE_DOWNLOADING = 1;//下载中 public static final int
		 * STATE_PAUSEDOWNLOAD = 2;//暂停下载 public static final int
		 * STATE_WAITINGDOWNLOAD = 3;//等待下载 public static final int
		 * STATE_DOWNLOADFAILED = 4;//下载失败 public static final int
		 * STATE_DOWNLOADED = 5;//下载完成 public static final int STATE_INSTALLED =
		 * 6;//已安装
		 */

        // 一开始设置默认的背景
        mAppDetailDownloadBtnDownload
                .setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);

        switch (downLoadInfo.curState) {
            case DownLoadManager.STATE_UNDOWNLOAD:// 未下载
                mAppDetailDownloadBtnDownload.setText("下载");
                break;
            case DownLoadManager.STATE_DOWNLOADING:// 下载中

                // 更换背景
                mAppDetailDownloadBtnDownload
                        .setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
                mAppDetailDownloadBtnDownload.setIsProgressEnable(true); // 需要绘制

                // 获取进度条的值
                long max = downLoadInfo.max;
                long progress = downLoadInfo.progress;
                int index = (int) (progress * 100.0f / max + .5f);

                // 设置进度条状态的值
                mAppDetailDownloadBtnDownload.setMax((int) max);
                mAppDetailDownloadBtnDownload.setProgress((int) progress);
                mAppDetailDownloadBtnDownload.setText(index + "%");

                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                mAppDetailDownloadBtnDownload.setText("继续下载");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                mAppDetailDownloadBtnDownload.setText("等待中...");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
                mAppDetailDownloadBtnDownload.setText("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED:// 下载完成

                mAppDetailDownloadBtnDownload.setIsProgressEnable(false); // 下载完成不需要绘制
                mAppDetailDownloadBtnDownload.setText("安装");
                break;
            case DownLoadManager.STATE_INSTALLED:// 已安装
                mAppDetailDownloadBtnDownload.setText("打开");
                break;

            default:
                break;
        }
    }

    /**
     * 下载按钮点击事件
     *
     * @param v
     */
    @OnClick(R.id.app_detail_download_btn_download)
    public void ClickDownLoad(View v) {

        /*public static final int STATE_UNDOWNLOAD      = 0;// 未下载
        public static final int STATE_DOWNLOADING     = 1;// 下载中
        public static final int STATE_PAUSEDOWNLOAD   = 2;// 暂停下载
        public static final int STATE_WAITINGDOWNLOAD = 3;// 等待下载
        public static final int STATE_DOWNLOADFAILED  = 4;// 下载失败
        public static final int STATE_DOWNLOADED      = 5;// 下载完成
        public static final int STATE_INSTALLED       = 6;// 已安装*/

		/*--------------- 3.根据不同的状态触发不同的操作 ---------------*/
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance()
                .getDownLoadInfo(mDownLoadDatas);
        switch (downLoadInfo.curState) {
            case DownLoadManager.STATE_UNDOWNLOAD:// 去下载
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADING:// 暂停下载
                LogUtils.d("Test","暂停下载被点击了");
                pauseDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:// 断点继续下载
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:// 取消下载
                canceDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:// 重试下载
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADED:// 安装应用
                installApp(downLoadInfo);
                break;
            case DownLoadManager.STATE_INSTALLED:// 打开应用
                openApp(downLoadInfo);
                break;

            default:
                break;
        }

    }

    /**
     * 暂停下载
     *
     * @param downLoadInfo
     */
    private void pauseDownLoad(DownLoadInfo downLoadInfo) {
        LogUtils.d("Test", "暂停下载被点击了");
        DownLoadManager.getInstance().pausesDownLoad(downLoadInfo);
    }

    /**
     * 取消下载
     *
     * @param downLoadInfo
     */
    private void canceDownLoad(DownLoadInfo downLoadInfo) {

    }

    /**
     * 安装应用
     *
     * @param downLoadInfo
     */
    private void installApp(DownLoadInfo downLoadInfo) {
        File safeFile = new File(downLoadInfo.safePath);
        CommonUtils.installApp(UIUtils.getContext(), safeFile);
    }

    /**
     * 打开应用
     *
     * @param downLoadInfo
     */
    private void openApp(DownLoadInfo downLoadInfo) {
        CommonUtils.openApp(UIUtils.getContext(), downLoadInfo.packageName);
    }

    /**
     * 去下载,继续下载,重试下载
     *
     * @param downLoadInfo
     */
    private void doDownLoad(DownLoadInfo downLoadInfo) {
        // 下载--->异步加载--->真正去下载数据

        // 传递的对象,给变量赋值参数
        /*
		 * DownLoadInfo downLoadInfo = new DownLoadInfo();
		 * 
		 * //文件保存在外置SD卡里 --->mnt/sdcard/data/包名.apk
		 * 
		 * File safePath = new File(FileUtils.getDir("apk"),
		 * mDownLoadDatas.packageName + ".apk"); downLoadInfo.safePath =
		 * safePath.getAbsolutePath();
		 * 
		 * downLoadInfo.downloadUrl = mDownLoadDatas.downloadUrl;
		 */

        // 1.获取下载管理器的实例,触发下载
        DownLoadManager.getInstance().triggerDownLoad(downLoadInfo);

    }

    /**
     * 1.接收到DownLoadInfo数据的改变消息,更新按钮ui状态
     *
     * @param downLoadInfo
     */
    @Override
    public void OnDownLoadInfoChanged(final DownLoadInfo downLoadInfo) {

        //对观察者进行过滤--->通过包名过滤----->因为在点击一个下载的时候,在去点击另外一个下载会出现两个观察者都收到消息,所以过滤
        if (!mDownLoadDatas.packageName.equals(downLoadInfo.packageName)) {
            return;
        }

        PrintDownLoadInfo.printDownLoadInfo(downLoadInfo); // 打印下载日志

        // 被观察者,是在子线程中发送的通知,所以这里接收的时候需要在主线程中更新ui
        MyApplication.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                refreshDownLoadBtnByUI(downLoadInfo);

            }
        });
    }
}
