package com.zgy.goldmonitor.util;

import java.util.Stack;

import android.app.Activity;
/**
 * activity 管理
 * @Author zhuanggy
 * @Date:2014-1-7
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version 
 * @since
 */
public class ActivityManager {
	
	private static Stack<Activity> stack = new Stack<Activity>();// Activity栈
	/**
	 * 移除所有activity
	 * @Description:
	 */
	public static void popAll() {
		while (!stack.isEmpty()) {
			pop();
		}
	}
	/**
	 * 移除位于栈顶的activity
	 * @Description:
	 */
	public static void pop() {
		Activity activity = stack.pop();
		if (activity != null && !activity.isFinishing()) {
			activity.finish();
		}
	}
	
	public static boolean isActivityRunning(String ActivityFullName) {
		for(Activity a : stack) {
			if(ActivityFullName.equals(a.getClass().getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isActivityRunning(Activity activity) {
		for(Activity a : stack) {
			if(activity != null && activity.getClass().getName().equals(a.getClass().getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 移除指定activity
	 * @Description:
	 * @param activity
	 */
	public static void pop(Activity activity) {
		if(!activity.isFinishing()) {
			activity.finish();
		}
		stack.remove(activity);
	}
	/**
	 * 移除并关闭指定某一类的activity
	 * @Description:
	 * @param cls
	 */
	public static void popClass(Class<? extends Activity> cls) {
		Stack<Activity> newStack = new Stack<Activity>();
		for(Activity a : stack) {
			if(a.getClass().equals(cls)) {
				if(!a.isFinishing()) {
					a.finish();
				}
			}else {
				newStack.push(a);
			}
		}
		stack = newStack;
	}
	/**
	 * 获取在栈顶的activity
	 * @Description:
	 * @return
	 */
	public static Activity current() {
		if(stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}
	/**
	 * 添加activity到栈中
	 * @Description:
	 * @param activity
	 */
	public static void push(Activity activity) {
		stack.push(activity);
	}
	/**
	 * 保留某一类的activity，其它的都关闭并移出栈
	 * @Description:
	 * @param cls
	 */
	public static void retain(Class<? extends Activity> cls) {
		Stack<Activity> newStack = new Stack<Activity>();
		for(Activity a : stack) {
			if(a.getClass().equals(cls)) {
				newStack.push(a);
			}else {
				if(!a.isFinishing()) {
					a.finish();
				}
			}
		}
		stack = newStack;
	}
	
	/**
	 * 应用当前最顶部的activity是否是activity
	 * @param @param cls
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-2-11
	 */
	public static boolean isAppTopActivity(Activity activity) {
		if(stack.size()>0) {
			return (stack.get(0).getClass()).equals(activity.getClass());
		}
		return false;
	}
}