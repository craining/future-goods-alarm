package com.zgy.goldmonitor.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.zgy.goldmonitor.MainApp;

public class AppUtil {

	public static String getChannelId() {
		String channel = "\r\nChannel Id = ";
		try {
			ApplicationInfo appInfo = MainApp.getInstance().getPackageManager().getApplicationInfo(MainApp.getInstance().getPackageName(), PackageManager.GET_META_DATA);
			channel = channel + appInfo.metaData.getString("UMENG_CHANNEL");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return channel;
	}
}
