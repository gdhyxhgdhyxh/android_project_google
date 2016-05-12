package com.wjh.org.googleplay_19.bean;

/*
 * @创建者     wjh
 * @创建时间   2016/3/1 12:47
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  分类里的数据用到的bean;
 *  1.这里的title是另外一个字段,他们不同一个对象,现在就改变策略,把他们放在一起
 */
public class CategoryBean {
    public String name1	;//休闲
    public String name2	;//棋牌
    public String name3	;//益智
    public String url1	;//image/category_game_0.jpg
    public String url2	;//image/category_game_1.jpg
    public String url3	;//image/category_game_2.jpg
    public String title	;//游戏
    public boolean isTitle; //默认是false
}
