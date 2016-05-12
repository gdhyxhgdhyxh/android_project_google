package com.wjh.org.googleplay_19.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * @创建者     wjh
 * @创建时间   2016/2/25 13:33
 * @描述	     Fragment的常规抽取
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public abstract class BaseFragmentCommon extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		initListener();
		super.onActivityCreated(savedInstanceState);
	}

    /**
     * @des 初始化Fragment调用
     * @des 子类选择性实现
     */
	public void init() {
	}

    /**
     * @des 初始化视图
     * @des 不知道视图是什么,具体实现让子类必须实现,定义为抽象类
     * @return
     */
    public abstract View initView();

    /**
     * @des 初始化Fragment的数据时调用
     * @des 子类选择性实现
     */
    public void initData() {

	}
    /**
     * @des 初始化Fragment的设置监听时调用
     * @des 子类选择性实现
     */
    public void initListener() {
	}
}
