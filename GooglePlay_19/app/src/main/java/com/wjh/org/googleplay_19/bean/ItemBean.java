package com.wjh.org.googleplay_19.bean;

/*
 * @创建者     wjh
 * @创建时间   2016/2/27 18:35
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

import java.util.List;

/**
 *  Home的数据bean里的ItemBean
 */
public class ItemBean {


    public long   id;
    public String des;
    public String packageName;
    public float  stars;
    public String name;
    public String iconUrl;
    public String downloadUrl;
    public long   size;

    /*--------------- 下面是详情页面额外添加的字段 ---------------*/
    public String             author;
    public String             date;
    public String             downloadNum;
    public String             version;
    public List<ItemSafeBean> safe;
    public List<String>       screen;

    public class ItemSafeBean {
        public String safeDes	    ;
        public int safeDesColor	;
        public String safeDesUrl	;
        public String safeUrl	    ;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "id=" + id +
                ", des='" + des + '\'' +
                ", packageName='" + packageName + '\'' +
                ", stars=" + stars +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", size=" + size +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", downloadNum='" + downloadNum + '\'' +
                ", version='" + version + '\'' +
                ", safe=" + safe +
                ", screen=" + screen +
                '}';
    }
}
