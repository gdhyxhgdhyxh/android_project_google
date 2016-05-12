package com.wjh.org.googleplay_19.protocol;

/*
 * @创建者     wjh
 * @创建时间   2016/2/29 20:58
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wjh.org.googleplay_19.base.BaseProtocol;
import com.wjh.org.googleplay_19.bean.ItemBean;

import java.util.List;

/**
 *  游戏协议请求类
 */
public class GameProtocol extends BaseProtocol<List<ItemBean>>{

    /**
     *
     * @return 网络协议关键字
     */
    @Override
    public String getInterfaceKey() {
        return "game";
    }

    /**
     *  解析请求回来的数据
     * @param resultJsonString
     * @return
     */
    @Override
    public List<ItemBean> processJsonString(String resultJsonString) {
        Gson gson = new Gson();
        //返回泛型解析之后的集合
        return gson.fromJson(resultJsonString,new TypeToken<List<ItemBean>>(){}.getType());
    }
}
