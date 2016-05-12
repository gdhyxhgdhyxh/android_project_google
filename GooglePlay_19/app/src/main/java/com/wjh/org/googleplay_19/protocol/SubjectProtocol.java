package com.wjh.org.googleplay_19.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wjh.org.googleplay_19.base.BaseProtocol;
import com.wjh.org.googleplay_19.bean.SubjectBean;

import java.util.List;

/*
 * @创建者     wjh
 * @创建时间   2016/2/29 23:34
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class SubjectProtocol extends BaseProtocol<List<SubjectBean>> {

    /**
     *  网络协议关键字
     * @return
     */
    @Override
    public String getInterfaceKey() {
        return "subject";
    }

    /**
     *  解析数据
     * @param resultJsonString
     * @return
     */
    @Override
    public List<SubjectBean> processJsonString(String resultJsonString) {

        Gson gson = new Gson();
        //返回一个集合数据
        return gson.fromJson(resultJsonString,new TypeToken<List<SubjectBean>>(){}.getType());
    }
}
