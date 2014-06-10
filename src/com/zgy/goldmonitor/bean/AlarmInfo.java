package com.zgy.goldmonitor.bean;

public class AlarmInfo {
	
	
	public static final int ALARM_RAISE = 0;
	public static final int ALARM_LOW = 1;
	
	public String nameEn;
	public String nameCn;
	public int groupId;
	public int checkId = -1;//提醒字段
	public int raseOrLow = -1;//涨/跌
	public float markValue;//基准值
	public float changedValue;//波动范围
	
}
