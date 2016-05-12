package com.wjh.org.googleplay_19.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.base.MyApplication;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.utils.UIUtils;
import com.wjh.org.googleplay_19.views.ChildViewPager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * @创建者     wjh
 * @创建时间   2016/2/29 21:19
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * Home的轮播图实现
 */
public class HomePictureHolder extends BaseHolder<List<String>> {
    @Bind(R.id.item_home_picture_pager)
    ChildViewPager mItemHomePicturePager;
    @Bind(R.id.item_home_picture_container_indicator)
    LinearLayout   mItemHomePictureContainerIndicator;
    private List<String>   mPictureUrl;
    private AutoScrollTask mAutoScrollTask;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {

        View rootView = View.inflate(UIUtils.getContext(), R.layout.home_header_page, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * data+view的绑定
     *
     * @param pictureUrl
     */
    @Override
    public void refreshHolderView(List<String> pictureUrl) {
        //保存数据到成员变量
        mPictureUrl = pictureUrl;

        //动态添加indicator到轮播图上
        for (int i = 0; i < mPictureUrl.size(); i++) {

            ImageView iv = new ImageView(UIUtils.getContext());
            if (i == 0) {
                //默认第一个选中
                iv.setImageResource(R.drawable.indicator_selected);
            } else {

                //其它情况,全部都是普通状态的圆点
                iv.setImageResource(R.drawable.indicator_normal);

            }
            /*--------------- 容器的宽高,间距 ---------------*/
            int width = UIUtils.dp2px(6);
            int height = UIUtils.dp2px(6);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            //设置margin值
            params.leftMargin = UIUtils.dp2px(6);
            params.bottomMargin = UIUtils.dp2px(6);

            //添加到容器
            mItemHomePictureContainerIndicator.addView(iv, params);
        }

        //设置Pager的adapter
        mItemHomePicturePager.setAdapter(new PicturePagerAdapter());

        //监听ViewPager
        initListener();

        //设置自动轮播
        mAutoScrollTask = new AutoScrollTask();
        mAutoScrollTask.start();

        //ViewPager的触摸监听
        initTouchListener();


    }

    /**
     * 1.ViewPager的触摸监听;
     * 2.用来控制自动轮播是否要滑动;
     * 3.按下的时候停止,松开的时候开始轮播
     */
    private void initTouchListener() {
        mItemHomePicturePager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: //按下
                        //停止自动轮播
                        mAutoScrollTask.stop();
                        break;

                    case MotionEvent.ACTION_MOVE: //滑动

                        break;

                    case MotionEvent.ACTION_UP:   //松开
                    case MotionEvent.ACTION_CANCEL://结束
                        //松开的时候,自动轮播
                        mAutoScrollTask.start();
                        break;
                }

                return false;
            }
        });
    }

    /**
     * 1.自动轮播页面;
     * 2. 使用handler周期性执行任务
     */
    class AutoScrollTask implements Runnable {

        /**
         * 开始自动轮播
         */
        public void start() {
            //延迟发送
            MyApplication.getMainHandler().postDelayed(this, 2000);
        }

        /**
         * 停止自动轮播
         */
        public void stop() {
            MyApplication.getMainHandler().removeCallbacks(this);
        }

        @Override
        public void run() {
            //每次执行任务都轮播到下一个页面
            int currentItem = mItemHomePicturePager.getCurrentItem();
            currentItem++;

            //重新设置轮播的页面
            mItemHomePicturePager.setCurrentItem(currentItem);

            //递归调用方法,实现周期性执行任务
            start();

        }
    }

    /**
     * 设置ViewPager的监听
     */
    private void initListener() {
        //(4)无限轮播,默认选中条目的中间
        int index = Integer.MAX_VALUE / 2;
        int diff = index % mPictureUrl.size();
        index = index - diff;
        mItemHomePicturePager.setCurrentItem(index);

        //ViewPager的滑动监听
        mItemHomePicturePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //页面滑动时
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //页面选中时
            @Override
            public void onPageSelected(int position) {

                //(3)设置无限滑动轮播,只要有position就要%
                position = position % mPictureUrl.size();

                for (int i = 0; i < mPictureUrl.size(); i++) {
                    //遍历集合,获取对应的indicator
                    ImageView iv = (ImageView) mItemHomePictureContainerIndicator.getChildAt(i);

                    //判断,动态设置
                    if (position == i) {
                        //如果当前滑动的页面是集合遍历中的i,代表是选中
                        iv.setImageResource(R.drawable.indicator_selected);
                    } else {
                        //普通状态,没有选中
                        iv.setImageResource(R.drawable.indicator_normal);
                    }
                }
            }

            //页面滑动状态发生改变时
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class PicturePagerAdapter extends PagerAdapter {

        /**
         * 返回页面的数量
         *
         * @return
         */
        @Override
        public int getCount() {
            if (mPictureUrl != null) {
                //(1)设置无限滑动轮播,需要返回int的最大值
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        /**
         * 判断初始化返回的标记
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * ViewPager页面初始化
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //(2)设置无限滑动轮播,只要有position就要%
            position = position % mPictureUrl.size();

            //用来展示图片的控件
            ImageView imageView = new ImageView(UIUtils.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            //获取每个页面的图片url
            String picUrl = mPictureUrl.get(position);
            picUrl = Constants.URL.IMAGEURL + picUrl; //拼接url

            //使用框架,网络加载图片
            ImageLoader.getInstance().displayImage(picUrl, imageView);

            //添加到容器
            container.addView(imageView);

            return imageView;
        }

        /**
         * ViewPager页面销毁的时候
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
