package com.wjh.org.googleplay_19.protocol;

import com.google.gson.Gson;
import com.wjh.org.googleplay_19.base.BaseProtocol;
import com.wjh.org.googleplay_19.bean.ItemBean;

import java.util.HashMap;
import java.util.Map;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 22:50
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  详情页面的网络请求协议
 */
public class DetailedProtocol extends BaseProtocol<ItemBean>{

    private String mPackageName;

    /**让外界通过构造方法,把包名传递过来*/
    public DetailedProtocol(String packageName) {
        mPackageName = packageName;
    }

    /**
     *  协议关键字
     * @return
     */
    @Override
    public String getInterfaceKey() {
        return "detail";
    }

    /**
     *  请求网络加载解析数据
     * @param resultJsonString
     * @return
     */
    @Override
    public ItemBean processJsonString(String resultJsonString) {

        Gson gson = new Gson();
        ItemBean itemBean = gson.fromJson(resultJsonString, ItemBean.class);
        return itemBean;
    }

    /**
     *  复写方法, 返回需要的url参数
     * @param index
     * @return
     */
    @Override
    public Map<String, Object> getRequestParamsMap(int index) {

        Map<String,Object> params = new HashMap<>();
        params.put("packageName",mPackageName);

        return params;
    }
}
