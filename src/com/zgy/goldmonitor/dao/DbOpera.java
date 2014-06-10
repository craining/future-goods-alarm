package com.zgy.goldmonitor.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.zgy.goldmonitor.bean.AlarmInfo;

/**
 * 数据库操作类
 * 
 * @Author zhuanggy
 * @Date:2014-1-3
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version
 * @since
 */
public class DbOpera extends DbHelper {

	private static final String TAG = "DbOpera";

	private DbOpera() {
		super();
	}

	private static String mSyn = "";

	private static DbOpera instances;

	public static DbOpera getInstance() {

		synchronized (mSyn) {
			if (instances == null) {
				instances = new DbOpera();
			}
			return instances;
		}
	}

	public boolean insertAlarm(AlarmInfo font) {

		Cursor cursor = null;
		try {

			ContentValues value = new ContentValues();
			value.put(Columns.Tb_Alarm.F_NAME, font.nameEn);
			value.put(Columns.Tb_Alarm.F_NAME_CN, font.nameCn);
			value.put(Columns.Tb_Alarm.F_CHANGE_VALUE, font.changedValue + "");
			value.put(Columns.Tb_Alarm.F_CHECK_ID, font.checkId);
			value.put(Columns.Tb_Alarm.F_MARK_VALUE, font.markValue + "");
			value.put(Columns.Tb_Alarm.F_RASE_LOW, font.raseOrLow);
			value.put(Columns.Tb_Alarm.F_GRUOP_ID, font.groupId);

			cursor = query(Columns.Tb_Alarm.TB_NAME, null, Columns.Tb_Alarm.F_NAME + "=? and " + Columns.Tb_Alarm.F_RASE_LOW + "=?", new String[] { font.nameEn, font.raseOrLow + "" });

			if (cursor != null && cursor.getCount() > 0) {
				update(Columns.Tb_Alarm.TB_NAME, value, Columns.Tb_Alarm.F_NAME + "=? and " + Columns.Tb_Alarm.F_RASE_LOW + " =?", new String[] { font.nameEn, font.raseOrLow + "" });
			} else {
				insertOrReplace(Columns.Tb_Alarm.TB_NAME, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return false;
	}

	public boolean deleteAlarm(String nameEn, int raiseOrLow) {
		try {
			delete(Columns.Tb_Alarm.TB_NAME, Columns.Tb_Alarm.F_NAME + "=? and " + Columns.Tb_Alarm.F_RASE_LOW + " =?", new String[] { nameEn, raiseOrLow + "" });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<AlarmInfo> getAllAlarms() {
		ArrayList<AlarmInfo> result = new ArrayList<AlarmInfo>();
		Cursor cursor = null;
		AlarmInfo a = null;
		try {
			cursor = query(Columns.Tb_Alarm.TB_NAME, null, null, null, null, null);

			if (cursor != null && cursor.getCount() > 0) {

				cursor.moveToFirst();
				do {
					a = new AlarmInfo();
					a.changedValue = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Columns.Tb_Alarm.F_CHANGE_VALUE)));
					a.checkId = cursor.getInt(cursor.getColumnIndex(Columns.Tb_Alarm.F_CHECK_ID));
					a.markValue = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Columns.Tb_Alarm.F_MARK_VALUE)));
					a.nameEn = cursor.getString(cursor.getColumnIndex(Columns.Tb_Alarm.F_NAME));
					a.nameCn = cursor.getString(cursor.getColumnIndex(Columns.Tb_Alarm.F_NAME_CN));
					a.groupId = cursor.getInt(cursor.getColumnIndex(Columns.Tb_Alarm.F_GRUOP_ID));
					a.raseOrLow = cursor.getInt(cursor.getColumnIndex(Columns.Tb_Alarm.F_RASE_LOW));
					result.add(a);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}
}
