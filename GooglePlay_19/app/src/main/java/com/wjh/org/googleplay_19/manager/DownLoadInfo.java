package com.wjh.org.googleplay_19.manager;

/*
 * @创建者     wjh
 * @创建时间   2016/3/3 14:02
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 *  用来存储下载需要的参数---->javabean
 */
public class DownLoadInfo {

    public String safePath;    //sd卡文件保存路径
    public String downloadUrl;  //下载路径
    public int    curState;        //记录下载过程的状态
    public String packageName;  //包名
    public long   max;          //进度条最大值
    public long   progress;     //进度条当前值
}
