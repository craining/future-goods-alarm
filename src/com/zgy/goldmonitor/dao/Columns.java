package com.zgy.goldmonitor.dao;

/**
 * 数据库字段
 * 
 * @Author zhuanggy
 * @Date:2014-1-3
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version
 * @since
 */
public class Columns {

	public static class Tb_Alarm {
		public static final String TB_NAME = "tb_alarm";
		public static String F_NAME = "name_en";
		public static String F_NAME_CN = "name_cn";
		public static String F_GRUOP_ID = "group_id";
		public static String F_CHECK_ID = "check_id";// 提醒字段
		public static String F_RASE_LOW = "raise_low";// 涨/跌
		public static String F_MARK_VALUE = "mark_value";// 基准值
		public static String F_CHANGE_VALUE = "change_value";// 波动范围
	}

}
