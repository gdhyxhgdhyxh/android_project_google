package com.wjh.org.googleplay_19;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.astuetz.PagerSlidingTabStripExtends;
import com.wjh.org.googleplay_19.adapter.MainFragmentStatePagerAdapter;
import com.wjh.org.googleplay_19.base.BaseFragment;
import com.wjh.org.googleplay_19.base.LoadingPager;
import com.wjh.org.googleplay_19.base.MyApplication;
import com.wjh.org.googleplay_19.factory.FragmentFactory;
import com.wjh.org.googleplay_19.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_tabs)
    PagerSlidingTabStripExtends mMainTabs;
    @Bind(R.id.main_viewpager)
    ViewPager                   mMainViewpager;
    @Bind(R.id.main_drawerlayout)
    DrawerLayout                mMainDrawerlayout;

    private ActionBarDrawerToggle         mToggle;
    // private MainFragmentPagerAdapter mAdapter;
    private MainFragmentStatePagerAdapter mAdapter;
    private MyOnPageChangeListener        mMyOnPageChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        initActionBarTitle();
        initActionBarDrawerToggle();
        initListener();

        //在Destroy方法中调用LeakCanary检测捕捉泄漏的位置
        MyApplication.getRefWatcher().watch(this);
    }

    /**
     * 初始化View
     */
    private void initData() {

        // 获取string.xml里定义的数组字符
        String[] titles = UIUtils.getStrings(R.array.main_titles);

        // 1.设置adapter
        /*
         * mAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),
		 * titles);
		 */
        mAdapter = new MainFragmentStatePagerAdapter(
                getSupportFragmentManager(), titles);
        mMainViewpager.setAdapter(mAdapter);

        // 2.把viewPager绑定在一起
        mMainTabs.setViewPager(mMainViewpager);
    }

    /**
     * 初始化状态栏显示
     */
    private void initActionBarTitle() {

        // 1. 获取ActionBar实例对象
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("GooglePlay"); // 设置标题
        // actionBar.setSubtitle("副标题"); //副标题

        actionBar.setIcon(R.mipmap.ic_launcher); // 设置标题图标
        // actionBar.setLogo(R.mipmap.ic_action_done);//LOGO图标

        // true:(默认Icon)显示图标; false:不显示图标
        actionBar.setDisplayShowHomeEnabled(false);

        // true:显示菜单按钮; false:(默认不显示)
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 可以更换图标显示的优先级; true:(显示LOGO);false(默认显示Icon)
        actionBar.setDisplayUseLogoEnabled(false);

        // (默认)ture:显示标题; false:不显示
        actionBar.setDisplayShowTitleEnabled(true);
    }

    /**
     * 初始化开关
     */
    private void initActionBarDrawerToggle() {

        // 1. 初始化ActionBarDrawerToggle
        mToggle = new ActionBarDrawerToggle(this, mMainDrawerlayout,
                R.string.open, R.string.close);

        // 2. 同步状态
        mToggle.syncState();

        // 3. 设置mToggle监听
        mMainDrawerlayout.setDrawerListener(mToggle);

    }

    /**
     * 设置状态栏条目选择
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 根据id选择点击的事件
        switch (item.getItemId()) {
            case android.R.id.home:
                // 系统默认帮我们定义好了id,把点击的item传递进去
                mToggle.onOptionsItemSelected(item);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化设置监听
     */
    private void initListener() {
        mMyOnPageChangeListener = new MyOnPageChangeListener();

        // 对viewPager页面进行监听
        mMainTabs.setOnPageChangeListener(mMyOnPageChangeListener);

        // 对View加载完毕进行监听
        mMainViewpager.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // 在View渲染初始化完毕的时候,就让页面默认选中0
                        mMyOnPageChangeListener.onPageSelected(0);

                        // 移除监听
                        mMainViewpager.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
    }

    /**
     * 内部类实现接口监听
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // 1.根据key找value(对应的Fragment)
            BaseFragment baseFragment = FragmentFactory.mFragmentMap
                    .get(position);
            // 1.1 通过baseFragment对象获取LoadingPager对象
            LoadingPager loadingPager = baseFragment.getLoadingPager();
            // 1.2 触发加载数据
            loadingPager.triggerLoadData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
