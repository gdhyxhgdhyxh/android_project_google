package com.wjh.org.googleplay_19.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.factory.ThreadPoolProxyFactory;
import com.wjh.org.googleplay_19.utils.UIUtils;

/*
 * @创建者     wjh
 * @创建时间   2016/2/26 12:34
 * @描述      1.提供视图(经过绑定数据的,而且是4种类型中的一种)
 * @描述      2.加载数据/接收数据
 * @描述      3.绑定视图和数据
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public abstract class LoadingPager extends FrameLayout {

    public View mLoadingView; // 加载视图
    public View mEmptyView; // 空视图
    public View mErrorView;// 错误视图

    public View mSuccessView; // 成功视图

    public static final int STATE_LOADING = 0; // 加载的状态
    public static final int STATE_EMPTY   = 1; // 空的状态
    public static final int STATE_ERROR   = 2; // 错误的状态
    public static final int STATE_SUCCESS = 3; // 成功的状态
    public              int mCurState     = STATE_LOADING; // 记录状态(默认)
    private LoadDtaTask mLoadDtaTask;

    public LoadingPager(Context context) {
        super(context);

        initCommonView();
    }

    /**
     * @des 初始化3个常规视图(静态视图)
     * @called LoadingPager初始化的时候
     */
    private void initCommonView() {

        // 初始化Loading视图,添加到当前的类容器
        mLoadingView = View.inflate(UIUtils.getContext(),
                R.layout.pager_loading, null);
        addView(mLoadingView);

        // 初始化Empty视图,添加到当前的类容器
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty,
                null);
        addView(mEmptyView);

        // 初始化Error视图,添加到当前的类容器
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error,
                null);
        addView(mErrorView);
        //绑定View
        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //失败的点击事件,重新触发加载数据
                triggerLoadData();
            }
        });

        // 根据状态更新Ui
        refreshViewByState();
    }

    /**
     * @des 根据状态刷新显示对应的UI
     */
    private void refreshViewByState() {

        // 加载视图的状态显示
        if (mCurState == STATE_LOADING) {
            mLoadingView.setVisibility(View.VISIBLE);
        } else {
            mLoadingView.setVisibility(View.GONE);
        }

        // 空视图的状态显示
        if (mCurState == STATE_EMPTY) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }

        // 错误视图的状态显示
        if (mCurState == STATE_ERROR) {
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            mErrorView.setVisibility(View.GONE);
        }

        // 数据加载完成,假如数据有加载回来,并且没有初始化过成功的视图,添加到容器
        if (mCurState == STATE_SUCCESS && mSuccessView == null) {
            mSuccessView = initSuccessView();
            addView(mSuccessView);
        }

        // 控制成功的视图状态显示
        if (mSuccessView != null) {
            if (mCurState == STATE_SUCCESS) {
                mSuccessView.setVisibility(View.VISIBLE);
            } else {
                mSuccessView.setVisibility(View.GONE);
            }
        }

    }

    /**
     * @return
     * @des 初始化成功视图, 完成成功视图的绑定操作
     * @des LoadingPager里面无法知道具体成功视图视图, 只能交给子类,而且发现是必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法交给子类具体实现
     * @called 外界触发加载了数据, 数据加载完成, 而且数据加载成功
     */
    public abstract View initSuccessView();

    /**
     * @des 触发加载数据
     * @called 外界需要加载数据的时候调用该方法
     */
    public void triggerLoadData() {
        //判断状态不是成功的视图,并且任务对象是空,才去显示和加载数据(避免加载中,再次重复加载)
        if (mCurState != STATE_SUCCESS && mLoadDtaTask == null) {
           // LogUtils.sf("加载数据mLoadDtaTask");
            //显示加载中的视图,并且更新UI
            mCurState = STATE_LOADING;
            refreshViewByState();

            //异步加载数据
            mLoadDtaTask = new LoadDtaTask();
           // new Thread(mLoadDtaTask).start();
            // 使用线程池工厂代理类去执行线程
            ThreadPoolProxyFactory.createNormalThreadPoolProxy().execute(mLoadDtaTask);
        }
    }

    /**
     * @des 开启线程异步加载数据
     */
    class LoadDtaTask implements Runnable {
        @Override
        public void run() {
            // 1. 去加载数据(获取返回的枚举常量值)
            LoadDataResultState loadDataResultState = initData();
            int state = loadDataResultState.getState();

            // 2. 得到数据,处理更新状态
            mCurState = state;

            // 3. 更新需要在主线程执行
            MyApplication.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    //刷新Ui(显示3种类型中的一种)-->
                    refreshViewByState();
                }
            });

            //当数据异步加载完毕,重置对象为空
            mLoadDtaTask = null;
        }
    }

    /**
     * @des 在子线程里面真正的加载数据
     * @des LoadingPager无法知道现在应该具体如何加载数据, 只能交给子类,而且发现是必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法交给子类具体实现
     * @called 外界需要加载数据的时候调用了triggerLoadData方法的时候
     */
    public abstract LoadDataResultState initData();

    /**
     * @des 通过枚举定义三个常量, 用来返回显示页面状态的标记
     * @des 使用枚举的好处:可以限定子类调用返回的状态是枚举里的常量,避免返回错误值
     */
    public enum LoadDataResultState {
        SUCESS(STATE_SUCCESS),
        ERROR(STATE_ERROR),
        EMPTY(STATE_EMPTY);

        int state; //初始化

        //构造方法
        LoadDataResultState(int state) {
            this.state = state;
        }

        //get方法
        public int getState() {
            return state;
        }
    }

}
