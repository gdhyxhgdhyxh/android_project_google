package com.wjh.org.googleplay_19.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjh.org.googleplay_19.R;
import com.wjh.org.googleplay_19.bean.CategoryBean;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 13:16
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class CategoryNormalHolder extends BaseHolder<CategoryBean> {

    @Bind(R.id.item_category_icon_1)
    ImageView    mItemCategoryIcon1;
    @Bind(R.id.item_category_name_1)
    TextView     mItemCategoryName1;
    @Bind(R.id.item_category_item_1)
    LinearLayout mItemCategoryItem1;
    @Bind(R.id.item_category_icon_2)
    ImageView    mItemCategoryIcon2;
    @Bind(R.id.item_category_name_2)
    TextView     mItemCategoryName2;
    @Bind(R.id.item_category_item_2)
    LinearLayout mItemCategoryItem2;
    @Bind(R.id.item_category_icon_3)
    ImageView    mItemCategoryIcon3;
    @Bind(R.id.item_category_name_3)
    TextView     mItemCategoryName3;
    @Bind(R.id.item_category_item_3)
    LinearLayout mItemCategoryItem3;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View rootView = View.inflate(UIUtils.getContext(), R.layout.category_holer_item, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * data+view的绑定
     *
     * @param datas
     */
    @Override
    public void refreshHolderView(CategoryBean datas) {

        setData(datas.name1, Constants.URL.IMAGEURL + datas.url1, mItemCategoryName1, mItemCategoryIcon1);
        setData(datas.name2, Constants.URL.IMAGEURL + datas.url2, mItemCategoryName2, mItemCategoryIcon2);
        setData(datas.name3, Constants.URL.IMAGEURL + datas.url3, mItemCategoryName3, mItemCategoryIcon3);


    }

    /**
     *  设置请求回来的数据
     * @param name
     * @param url
     * @param tv
     * @param iv
     */
    public void setData(final String name,String url, TextView tv, ImageView iv){

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(url)){
            //如果name 和 url 不是空的状态下,才去设置赋值
            tv.setText(name);

            //加载网络图片
            ImageLoader.getInstance().displayImage(url, iv);

            //设置显示图标
            ViewGroup parent = (ViewGroup) tv.getParent();
            parent.setVisibility(View.VISIBLE);

            //设置点击事件
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        }else {

            //数据为空,就隐藏图标
            ViewGroup parent = (ViewGroup) tv.getParent();
            parent.setVisibility(View.INVISIBLE);
        }
    }
}
