package com.zgy.goldmonitor.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	private static String TIME_DATE_TIME_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";// String 时间格式
	private static String TIME_DATE_STRING_FORMAT = "yyyy-MM-dd";// String 时间格式

	/**
	 * 获得系统当前时间
	 * 
	 * @Description:
	 * @return long型时间
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public static long getCurrentTimeMillis() {
		return (System.currentTimeMillis() / 1000) * 1000;// 精确到秒
	}

	
	/**
	 * 获得当前日期
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-5-16
	 */
	public static String getCurrentDateString() {
		return longToDateString(System.currentTimeMillis());
	}
	
	/**
	 * 获得当前日期时间
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-5-16
	 */
	public static String getCurrentDateTimeString() {
		return longToDateTimeString(System.currentTimeMillis());
	}
	
	/**
	 * 日期+时间的string 型转为long型
	 * 
	 * @Description:
	 * @param dateTime
	 *            (型如 ： 2013-03-08 13:51:00)
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public static long dateTimeStringToLong(String dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_DATE_TIME_STRING_FORMAT);
		Date dt2;
		try {
			dt2 = sdf.parse(dateTime);
			return dt2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * long型时间转为日期+时间的String型
	 * 
	 * @Description:
	 * @param dateTimeMillis
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public static String longToDateTimeString(long dateTimeMillis) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_DATE_TIME_STRING_FORMAT);
		Date dt = new Date(dateTimeMillis);
		return sdf.format(dt);
	}
	
	/**
	 * str型的long时间转为日期+时间的String型
	 * 
	 * @Description:
	 * @param dateTimeMillis
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-5-14
	 */
	public static String longToDateTimeString(String dateTimeMillis) {
		
		String result = "";
		try {
			result = longToDateTimeString(Long.parseLong(dateTimeMillis));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return result;
	}

	/**
	 * 日期+时间的string 型转为long型
	 * 
	 * @Description:
	 * @param date
	 *            (型如 ： 2013-03-08)
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public static long dateStringToLong(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_DATE_STRING_FORMAT);
		Date dt2;
		try {
			dt2 = sdf.parse(date);
			return dt2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * long型时间转为日期String型
	 * 
	 * @Description:
	 * @param dateTimeMillis
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public static String longToDateString(long dateTimeMillis) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_DATE_STRING_FORMAT);
		Date dt = new Date(dateTimeMillis);
		return sdf.format(dt);
	}
	
	/**
	 * str型的long时间转为日期String型
	 * 
	 * @Description:
	 * @param dateTimeMillis
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-5-14
	 */
	public static String longToDateString(String dateTimeMillis) {
		String result = "";
		try {
			result = longToDateString(Long.parseLong(dateTimeMillis));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return result;
	}
}
