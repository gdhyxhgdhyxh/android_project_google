package com.wjh.org.googleplay_19.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wjh.org.googleplay_19.base.BaseProtocol;
import com.wjh.org.googleplay_19.bean.ItemBean;

import java.util.List;

/*
 * @创建者     wjh
 * @创建时间   2016/2/29 20:18
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * App的网络协议请求
 */
public class AppProtocol extends BaseProtocol<List<ItemBean>> {

    /**
     *  返回协议关键字
     * @return
     */
    @Override
    public String getInterfaceKey() {
        return "app";
    }

    /**
     *  初始化解析请求回来的数据
     * @param resultJsonString
     * @return
     */
    @Override
    public List<ItemBean> processJsonString(String resultJsonString) {

        Gson gson = new Gson();
        //使用TypeToKen,泛型解析,解析成List,返回这个List对象
        return gson.fromJson(resultJsonString,new TypeToken<List<ItemBean>>(){}.getType());
    }
}
