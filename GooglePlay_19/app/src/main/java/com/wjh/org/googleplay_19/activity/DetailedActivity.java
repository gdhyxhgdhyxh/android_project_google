package com.wjh.org.googleplay_19.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.base.LoadingPager;
import com.wjh.org.googleplay_19.base.MyApplication;
import com.wjh.org.googleplay_19.bean.ItemBean;
import com.wjh.org.googleplay_19.holder.DetailedAppInfoHolder;
import com.wjh.org.googleplay_19.holder.DetailedDesHolder;
import com.wjh.org.googleplay_19.holder.DetailedDownLoadHolder;
import com.wjh.org.googleplay_19.holder.DetailedPicHolder;
import com.wjh.org.googleplay_19.holder.DetailedSafeHolder;
import com.wjh.org.googleplay_19.manager.DownLoadManager;
import com.wjh.org.googleplay_19.protocol.DetailedProtocol;
import com.wjh.org.googleplay_19.utils.UIUtils;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 条目点击的详情页面
 */
public class DetailedActivity extends AppCompatActivity {

    @Bind(R.id.detailed_fl_download)
    FrameLayout mDetailedFlDownload;
    @Bind(R.id.detailed_fl_content)
    FrameLayout mDetailedFlContent;
    @Bind(R.id.detailed_fl_safe)
    FrameLayout mDetailedFlSafe;
    @Bind(R.id.detailed_fl_pic)
    FrameLayout mDetailedFlPic;
    @Bind(R.id.detailed_fl_des)
    FrameLayout mDetailedFlDes;
    @Bind(R.id.detailed_fl_scrollview)
    ScrollView  mDetailedFlScrollview;
    private LoadingPager     mLoadingPager;
    private ItemBean         mItemBean;
    private DetailedProtocol mDetailedProtocol;
    private String           mPackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initView();
        initData();

        //在Destroy方法中调用LeakCanary检测捕捉泄漏的位置
        MyApplication.getRefWatcher().watch(this);

    }

    /**
     * 初始化接收Intent数据
     */
    private void init() {
        mPackageName = getIntent().getStringExtra("packageName");

    }

    /**
     * 1.初始化显示View视图; 2.这里把setContentView()方法放到这里来了;
     */
    private void initView() {

        mLoadingPager = new LoadingPager(UIUtils.getContext()) {

            /**
             * 返回成功的视图
             *
             * @return
             */
            @Override
            public View initSuccessView() {
                // 这里可以 使用同名方法,因为没有被占用
                return DetailedActivity.this.initSuccessView();
            }

            /**
             * 真正去子线程加载更多数据
             *
             * @return
             */
            @Override
            public LoadDataResultState initData() {

                // 返回不同页面的视图(经过data+view)
                return DetailedActivity.this.initLoadData();
            }
        };

		/*
         * 1.直接设置这个LoadingPager为显示的布局(经过data+view); 2.包含5种不同的视图 中的一种;
		 */
        setContentView(mLoadingPager);
    }

    /**
     * 触发加载数据
     */
    private void initData() {

        mLoadingPager.triggerLoadData();
    }

    /**
     * 网络请求,引入Protocol
     *
     * @return 返回请求的数据
     */
    private LoadingPager.LoadDataResultState initLoadData() {

        try {

            //请求网络
            mDetailedProtocol = new DetailedProtocol(mPackageName);
            mItemBean = mDetailedProtocol.loadData(0);

            //手动判断校验返回的数据
            if (mItemBean != null) {
                // 成功
                return LoadingPager.LoadDataResultState.SUCESS;
            }
            //空
            return LoadingPager.LoadDataResultState.EMPTY;

        } catch (IOException e) {
            e.printStackTrace();
            // 出错了
            return LoadingPager.LoadDataResultState.ERROR;
        }

    }

    /**
     * 1.返回不同的视图(经过data+view)
     * 2.引用Holder视图
     *
     * @return
     */
    private View initSuccessView() {

        View successView = View.inflate(UIUtils.getContext(),
                R.layout.activity_detailed, null);
        ButterKnife.bind(this, successView);

        // 详情页面应用的信息内容部分
        DetailedAppInfoHolder contentHolder = new DetailedAppInfoHolder();
        contentHolder.setDataAndRefreshHolderView(mItemBean);
        mDetailedFlContent.addView(contentHolder.mHolderView);

        // 详情页面应用的安全部分
        DetailedSafeHolder safeHolder = new DetailedSafeHolder();
        safeHolder.setDataAndRefreshHolderView(mItemBean);
        mDetailedFlSafe.addView(safeHolder.mHolderView);

        // 详情页面应用的截图部分
        DetailedPicHolder picHolder = new DetailedPicHolder();
        picHolder.setDataAndRefreshHolderView(mItemBean);
        mDetailedFlPic.addView(picHolder.mHolderView);

        // 详情页面应用的描述部分
        DetailedDesHolder desHolder = new DetailedDesHolder();
        desHolder.setDataAndRefreshHolderView(mItemBean);
        mDetailedFlDes.addView(desHolder.mHolderView);

        // 详情页面应用的下载按钮部分(观察者)
        DetailedDownLoadHolder downLoadHolder = new DetailedDownLoadHolder();
        downLoadHolder.setDataAndRefreshHolderView(mItemBean);
        mDetailedFlDownload.addView(downLoadHolder.mHolderView);

        //添加观察者到观察者集合
        DownLoadManager.getInstance().addOnDownLoadInfoListener(downLoadHolder);

        return successView;
    }
}
