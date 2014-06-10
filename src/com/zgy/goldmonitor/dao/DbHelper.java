package com.zgy.goldmonitor.dao;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.util.SQLiteHelper;

/**
 * 数据库帮助类
 * @Author zhuanggy
 * @Date:2014-1-3
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version 
 * @since
 */
public class DbHelper extends SQLiteHelper {

	private static final String TAG = "DbHelper";
	
	private static final String DB_NAME = "goods.db";
	private static final int DB_VERSION = 2;

	private static String CREATE_TB_ALARM;
	private static String DROP_TB_ALARM = "DROP TABLE IF EXISTS "
			+ Columns.Tb_Alarm.TB_NAME;



	public DbHelper() {
		super(MainApp.getInstance(), getDbName(), null, DB_VERSION);
	}

	private static String getDbName() {
			return DB_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Debug.v(TAG, "onCreate");
		initCreateSql();
		db.execSQL(CREATE_TB_ALARM);
		Debug.v(TAG, "onCreate finish");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TB_ALARM);
		onCreate(db);
	}

	private void initCreateSql() {
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Columns.Tb_Alarm.TB_NAME).append(" (")

		.append(Columns.Tb_Alarm.F_NAME).append(" TEXT, ")
		
		.append(Columns.Tb_Alarm.F_NAME_CN).append(" TEXT, ")
		
		.append(Columns.Tb_Alarm.F_GRUOP_ID).append(" integer, ")

		.append(Columns.Tb_Alarm.F_CHECK_ID).append(" integer, ")

		.append(Columns.Tb_Alarm.F_RASE_LOW).append(" integer, ")

		.append(Columns.Tb_Alarm.F_MARK_VALUE).append(" TEXT, ")

		.append(Columns.Tb_Alarm.F_CHANGE_VALUE).append(" TEXT);");

		CREATE_TB_ALARM = sb.toString();
 
	}

}
