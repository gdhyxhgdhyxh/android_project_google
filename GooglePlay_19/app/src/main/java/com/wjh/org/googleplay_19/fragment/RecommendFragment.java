package com.wjh.org.googleplay_19.fragment;

/*
 * @创建者     wjh
 * @创建时间   2016/2/24 20:53
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wjh.org.googleplay_19.base.BaseFragment;
import com.wjh.org.googleplay_19.base.LoadingPager;
import com.wjh.org.googleplay_19.protocol.RecommendProtocol;
import com.wjh.org.googleplay_19.utils.UIUtils;
import com.wjh.org.googleplay_19.views.flyinout.ShakeListener;
import com.wjh.org.googleplay_19.views.flyinout.StellarMap;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 推荐
 */
public class RecommendFragment extends BaseFragment {

    private RecommendProtocol mRecommendProtocol;
    private List<String>      mDatas;
    private RecommendAdapter  mAdapter;
    private ShakeListener mShakeListener;

    /**
     * @des 在子线程里面真正的加载数据
     * @called 外界需要加载数据的时候调用了triggerLoadData方法的时候
     */
    @Override
    public LoadingPager.LoadDataResultState initData() {

        try {
            mRecommendProtocol = new RecommendProtocol();
            mDatas = mRecommendProtocol.loadData(0);

            //检验返回的数据状态
            LoadingPager.LoadDataResultState state = checkResData(mDatas);
            return state;

        } catch (IOException e) {
            e.printStackTrace();
            //出错了
            return LoadingPager.LoadDataResultState.ERROR;
        }
    }

    /**
     * @return
     * @des 初始化成功视图, 完成成功视图的绑定操作
     * @called 外界触发加载了数据, 数据加载完成, 而且数据加载成功
     */
    @Override
    public View initSuccessView() {

        //1.创建飞入飞出的类对象
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        //1.1 设置适配器数据
        mAdapter = new RecommendAdapter();
        stellarMap.setAdapter(mAdapter);

        //2.默认选中首页加载飞入飞出效果
        stellarMap.setGroup(0, true);//参数有动画

        //3.拆分屏幕为300份小格子
        stellarMap.setRegularity(15, 20);

        //4. 添加摇一摇功能,切换下一页
        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                //得到当前组的页面
                int currentGroup = stellarMap.getCurrentGroup();
                if (currentGroup != mAdapter.getGroupCount() - 1) {
                    //不是最后一组就切换到下一页
                    currentGroup++;
                } else {
                    //如果到最后一组就设置回0
                    currentGroup = 0;
                }
                //设置回切换的页面
                stellarMap.setGroup(currentGroup, true);
            }
        });


        //返回就可以了,已经封装好了
        return stellarMap;
    }

    /**
     *  1.复写生命周期方法,可见的时候;
     *  2.由于摇一摇的功能是调用传感器服务,所以需要注册服务
     */
    @Override
    public void onResume() {
        if(mShakeListener !=null){
            mShakeListener.resume();
        }
        super.onResume();
    }

    /**
     *  1.不可见的时候;
     *  2.取消摇一摇的服务注册
     */
    @Override
    public void onPause() {
        if(mShakeListener !=null){
            mShakeListener.pause();
        }
        super.onPause();
    }

    class RecommendAdapter implements StellarMap.Adapter {

        //定义好一组页面展示多少个
        public static final int PAGESIZI = 15;

        /**
         * 返回一共有多少组,
         *
         * @return
         */
        @Override
        public int getGroupCount() {
            //mDatas  PAGESIZE
            if (mDatas.size() % PAGESIZI != 0) {
                //如果有余数,就需要多加一组页面
                return mDatas.size() / PAGESIZI + 1;//比如32 /15 = (15+15)+2 所以会多出两个
            }
            //其它情况整除,就返回刚好的页面
            return mDatas.size() / PAGESIZI;
        }

        /**
         * 返回一个组里有多少个
         *
         * @param group
         * @return
         */
        @Override
        public int getCount(int group) {
            //如果不等0,说明有余数
            if (mDatas.size() % PAGESIZI != 0) {
                if (group == getGroupCount() - 1) {
                    //如果最后一组有余数,特殊处理
                    return mDatas.size() % PAGESIZI;
                }
            }
            //其它情况
            return PAGESIZI;
        }

        /**
         * 展示的样子
         *
         * @param group
         * @param position
         * @param convertView
         * @return
         */
        @Override
        public View getView(int group, int position, View convertView) {

            //通过 组 * 页面 +当前条目获取页面索引有多少组页面
            int index = group * PAGESIZI + position;
            //根据索引获取数据
            final String data = mDatas.get(index);

            TextView textView = new TextView(UIUtils.getContext());
            textView.setText(data);

            //添加随机颜色随机大小字体
            Random random = new Random();

            int alpha = 255;                    //没有透明度
            int red = random.nextInt(180) + 30; //颜色最大值30-210
            int green = random.nextInt(180) + 30;//颜色最大值30-210
            int blue = random.nextInt(180) + 30;//颜色最大值30-210
            //随机字体大小 和随机颜色
            textView.setTextSize(random.nextInt(4) + 14); //14-18 取两者最小
            textView.setTextColor(Color.argb(alpha, red, green, blue));

            //设置点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), data, Toast.LENGTH_SHORT).show();
                }
            });
            return textView;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }
}
