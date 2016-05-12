package com.wjh.org.googleplay_19.protocol;

import com.wjh.org.googleplay_19.base.BaseProtocol;
import com.wjh.org.googleplay_19.bean.CategoryBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 12:46
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryBean>>{

    /**
     *  网络协议关键字
     * @return
     */
    @Override
    public String getInterfaceKey() {
        return "category";
    }

    /**
     *  解析数据
     * @param resultJsonString
     * @return
     */
    @Override
    public List<CategoryBean> processJsonString(String resultJsonString) {

        //初始化集合对象
        List<CategoryBean> result = new ArrayList<>();

        //由于gson不能解析不同对象字段在一起的数据
        /*--------------- 使用json节点解析 ---------------*/
        try {
            //解析json的集合数据
            JSONArray jsonArray = new JSONArray(resultJsonString);

            //遍历jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                //获取集合中的对象
                JSONObject itemJsonObject = jsonArray.getJSONObject(i);

                /*--------------- 解析title ---------------*/
                //获取解析的字段
                String title = itemJsonObject.getString("title");

                //把解析的数据用bean封装
                CategoryBean categoryTitleBean = new CategoryBean();
                categoryTitleBean.title = title;
                categoryTitleBean.isTitle = true;

                //添加到集合
                result.add(categoryTitleBean);

                /*--------------- 解析infos ---------------*/
                JSONArray infosJsonArray = itemJsonObject.getJSONArray("infos");

                //遍历infos的集合
                for (int j = 0; j < infosJsonArray.length(); j++) {
                    //获取集合对象
                    JSONObject infosJsonObj = infosJsonArray.getJSONObject(j);

                    //获取集合对应的字段
                    String name1 = infosJsonObj.getString("name1");
                    String name2 = infosJsonObj.getString("name2");
                    String name3 = infosJsonObj.getString("name3");

                    String url1 = infosJsonObj.getString("url1");
                    String url2 = infosJsonObj.getString("url2");
                    String url3 = infosJsonObj.getString("url3");

                   /* LogUtils.d("Test", url1);
                    LogUtils.d("Test", url2);
                    LogUtils.d("Test", url3);*/

                    //把获取的字段封装到bean
                    CategoryBean categoryInfosBean = new CategoryBean();
                    categoryInfosBean.name1 = name1;
                    categoryInfosBean.name2 = name2;
                    categoryInfosBean.name3 = name3;
                    categoryInfosBean.url1 = url1;
                    categoryInfosBean.url2 = url2;
                    categoryInfosBean.url3 = url3;

                    //添加到集合
                    result.add(categoryInfosBean);

                }
            }

            //循环解析完,返回这个集合
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
