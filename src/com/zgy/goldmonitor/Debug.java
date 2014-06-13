package com.zgy.goldmonitor;

import android.util.Log;

/**
 * 日志管理类。主要负责5个级别的日志输出。
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 */
public class Debug {

	public static final boolean LOG = true;//Log开关，便于本地调试；（确保svn服务器上为false，以免发布时忘记关闭）
	
	// 用来防止msg为空时的异常
	private static final String NULL_STR = "msg is null!";

	public static void d(String tag, String msg) {
		if (LOG)
			Log.d(tag, msg != null ? msg : NULL_STR);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.d(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void e(String tag, String msg) {
		if (LOG)
			Log.e(tag, msg != null ? msg : NULL_STR);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.e(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void v(String tag, String msg) {
		if (LOG)
			Log.v(tag, msg != null ? msg : NULL_STR);

	}

	public static void v(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.v(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void i(String tag, String msg) {
		if (LOG)
			Log.i(tag, msg != null ? msg : NULL_STR);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.i(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void w(String tag, String msg) {
		if (LOG)
			Log.w(tag, msg != null ? msg : NULL_STR);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.w(tag, msg != null ? msg : NULL_STR, tr);
	}
}
