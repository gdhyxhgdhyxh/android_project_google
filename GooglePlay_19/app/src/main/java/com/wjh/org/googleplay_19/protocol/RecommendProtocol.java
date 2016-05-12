package com.wjh.org.googleplay_19.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wjh.org.googleplay_19.base.BaseProtocol;

import java.util.List;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 19:29
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class RecommendProtocol extends BaseProtocol<List<String>> {
    @Override
    public String getInterfaceKey() {
        return "recommend";
    }

    @Override
    public List<String> processJsonString(String resultJsonString) {

        Gson gson = new Gson();
        //返回Strng集合数据
        return gson.fromJson(resultJsonString,new TypeToken<List<String>>(){}.getType());
    }
}
