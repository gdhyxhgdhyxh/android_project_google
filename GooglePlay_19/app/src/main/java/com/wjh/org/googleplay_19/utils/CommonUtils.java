package com.wjh.org.googleplay_19.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import java.io.File;

/**
 * @类名: CommonUtils
 * @创建时间: 2015-5-9 下午6:00:40
 * @描述: TODO
 * 
 * @svn版本: $Rev: 50 $
 * @更新人: $Author: xq $
 * @更新时间: $Date: 2015-05-10 10:24:11 +0800 (Sun, 10 May 2015) $
 * @更新描述: TODO
 */
public class CommonUtils
{

	/**
	 * 判断包是否安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstalled(Context context, String packageName)
	{
		PackageManager manager = context.getPackageManager();
		try
		{
			manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

			return true;
		}
		catch (NameNotFoundException e)
		{
			return false;
		}
	}

	/**
	 * 安装应用程序
	 * 
	 * @param context
	 * @param apkFile
	 */
	public static void installApp(Context context, File apkFile)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 打开应用程序
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void openApp(Context context, String packageName)
	{
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}
}
