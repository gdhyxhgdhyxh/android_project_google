package com.wjh.org.googleplay_19.protocol;

import com.google.gson.Gson;
import com.wjh.org.googleplay_19.base.BaseProtocol;
import com.wjh.org.googleplay_19.bean.HomeBean;

/*
 * @创建者     wjh
 * @创建时间   2016/2/27 20:35
 * @描述	      Home的网络协议封装
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class HomeProtocol extends BaseProtocol<HomeBean> {

    /**
     * @return 网络协议的关键字
     */
    @Override
    public String getInterfaceKey() {
        return "home";
    }

    /**
     *  解析json数据,转化成bean对象
     * @param resultJsonString 网络请求回来的json数据
     * @return 返回解析后的javabean
     */
    @Override
    public HomeBean processJsonString(String resultJsonString) {
        //1.解析json数据
        Gson gson = new Gson();
        HomeBean homeBean = gson.fromJson(resultJsonString, HomeBean.class);
        //LogUtils.d("Test", "协议封装返回homebean.....");
        //解析成bean,然后返回
        return homeBean;
    }
}
