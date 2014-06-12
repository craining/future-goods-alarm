package com.zgy.goldmonitor;

import android.content.Context;

import com.zgy.goldmonitor.util.PreferenceUtil;

public class Preference extends PreferenceUtil {

	// 文件名
	private static final String PREFERENCE_NAME = "futuregods_config";
	private static Preference instance;
	// 各个配置参数
	private static final String LAST_VIEW_ITEM_ID_0 = "last_view_innerid0";
	private static final String LAST_VIEW_ITEM_ID_1 = "last_view_innerid1";
	private static final String LAST_VIEW_ITEM_ID_2 = "last_view_innerid2";
	private static final String LAST_VIEW_ITEM_ID_3 = "last_view_innerid3";
	private static final String LAST_VIEW_ITEM_ID_4 = "last_view_innerid4";
	private static final String LAST_VIEW_ITEM_ID_5 = "last_view_innerid5";
	private static final String LAST_VIEW_GROUP_ID = "last_view_groupid";

	private static final String ALARM_AUDIO = "alarm_audio";
	private static final String ALARM_VIBRATE = "alarm_vibrate";
	private static final String ALARM_ALERT_WAY = "alarm_alertway";
	private static final String ALARM_REFRESH_RATE = "alarm_rate";

	private static final String FEEDBACK_STR = "feedback_str";
	private static final String FEEDBACK_ADDR = "feedback_addr";

	private static final String ALARM_3G = "3g_alarm";
	private static final String PIC_3G = "3g_pic";
	private static final String ALARM_SWITCH = "alarm_switch";

	private Preference() {
		super(PREFERENCE_NAME);
	}

	public static Preference getInstance() {
		if (instance == null) {
			instance = new Preference();
		}
		return instance;
	}

	@Override
	protected Context getContext() {
		return MainApp.getInstance();
	}

	public String getFeedbackStr() {
		return getString(FEEDBACK_STR, "");
	}

	public void setFeedbackStr(String str) {
		putString(FEEDBACK_STR, str);
	}

	public String getFeedbackAddr() {
		return getString(FEEDBACK_ADDR, "");
	}

	public void setFeedbackAddr(String str) {
		putString(FEEDBACK_ADDR, str);
	}

	public int getLastViewGroupId() {
		return getInt(LAST_VIEW_GROUP_ID, 0);
	}

	public void setLastViewGroupId(int id) {
		putInt(LAST_VIEW_GROUP_ID, id);
	}

	public int getLastViewItemId(int groupId) {
		switch (groupId) {
		case Data.GROUPID_INNER_0_ZHENGZHOU:
			return getInt(LAST_VIEW_ITEM_ID_0, 0);
		case Data.GROUPID_INNER_1_DALIAN:
			return getInt(LAST_VIEW_ITEM_ID_1, 0);
		case Data.GROUPID_INNER_2_SHANGHAI:
			return getInt(LAST_VIEW_ITEM_ID_2, 0);
		case Data.GROUPID_INNER_3_BOHAI:
			return getInt(LAST_VIEW_ITEM_ID_3, 0);
		case Data.GROUPID_INNER_4_FANYA:
			return getInt(LAST_VIEW_ITEM_ID_4, 0);
		case Data.GROUPID_OUTER_5_OTHER:
			return getInt(LAST_VIEW_ITEM_ID_5, 0);
		default:
			break;
		}

		return 0;
	}

	public void setLastViewItemId(int groupId, int id) {

		switch (groupId) {
		case Data.GROUPID_INNER_0_ZHENGZHOU:
			putInt(LAST_VIEW_ITEM_ID_0, id);
			break;
		case Data.GROUPID_INNER_1_DALIAN:
			putInt(LAST_VIEW_ITEM_ID_1, id);
			break;
		case Data.GROUPID_INNER_2_SHANGHAI:
			putInt(LAST_VIEW_ITEM_ID_2, id);
			break;
		case Data.GROUPID_INNER_3_BOHAI:
			putInt(LAST_VIEW_ITEM_ID_3, id);
			break;
		case Data.GROUPID_INNER_4_FANYA:
			putInt(LAST_VIEW_ITEM_ID_4, id);
			break;
		case Data.GROUPID_OUTER_5_OTHER:
			putInt(LAST_VIEW_ITEM_ID_5, id);
			break;
		default:
			break;
		}

	}

	public boolean isAlarmOff() {
		return getBool(ALARM_SWITCH, false);
	}

	public void setAlarmOff(boolean off) {
		putBool(ALARM_SWITCH, off);
	}

	public boolean is3GNoPicOn() {
		return getBool(PIC_3G, false);
	}

	public void set3GNoPicOn(boolean on) {
		putBool(PIC_3G, on);
	}

	public boolean is3GNoAlarmOn() {
		return getBool(ALARM_3G, false);
	}

	public void set3GNoAlarmOn(boolean on) {
		putBool(ALARM_3G, on);
	}

	public boolean isAlarmAudioOn() {
		return getBool(ALARM_AUDIO, true);
	}

	public void setAlarmAudioOn(boolean on) {
		putBool(ALARM_AUDIO, on);
	}

	public boolean isAlarmVibrateOn() {
		return getBool(ALARM_VIBRATE, true);
	}

	public void setAlarmVibrateOn(boolean on) {
		putBool(ALARM_VIBRATE, on);
	}

	public int getAlarmAlertWay() {
		return getInt(ALARM_ALERT_WAY, 0);
	}

	public void setAlarmAlertWay(int way) {
		putInt(ALARM_ALERT_WAY, way);
	}

	public int getRefreshRate() {
		return getInt(ALARM_REFRESH_RATE, 5);
	}

	public void setRefreshRate(int rate) {
		putInt(ALARM_REFRESH_RATE, rate);
	}
}
