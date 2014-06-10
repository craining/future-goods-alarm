package com.zgy.goldmonitor;

import java.io.File;
import java.util.ArrayList;

import android.app.Application;
import android.os.Environment;

import com.zgy.goldmonitor.bean.AlarmInfo;
import com.zgy.goldmonitor.util.ImageLoaderUtil;

public class MainApp extends Application {

	// 提醒id、提醒字段、涨/跌、基准值，波动范围
	//
	//
	//
	//
	//
	//
	// 声音
	// 振动
	// 通知栏/弹窗
	// 间隔频率(3、5、10、20、30、60、)

//	public static final String FILE_SELECTED_ITEM_0 = "/data/data/com.zgy.goldmonitor/selected_items_0.cfg";
//	public static final String FILE_SELECTED_ITEM_1 = "/data/data/com.zgy.goldmonitor/selected_items_1.cfg";
//	public static final String FILE_SELECTED_ITEM_2 = "/data/data/com.zgy.goldmonitor/selected_items_2.cfg";
//	public static final String FILE_SELECTED_ITEM_3 = "/data/data/com.zgy.goldmonitor/selected_items_3.cfg";
//	public static final String FILE_SELECTED_ITEM_4 = "/data/data/com.zgy.goldmonitor/selected_items_4.cfg";
//	public static final String FILE_SELECTED_ITEM_5 = "/data/data/com.zgy.goldmonitor/selected_items_5.cfg";

	
	public static final String FILE_SELECTED_ITEM_Inner = "/data/data/com.zgy.goldmonitor/selected_items_i.cfg";
	public static final String FILE_SELECTED_ITEM_Outer = "/data/data/com.zgy.goldmonitor/selected_items_o.cfg";
	
	
	public static final int ALARM_WAY_NOTIFY = 0;
	public static final int ALARM_WAY_DLG = 1;		
	
	
	private static MainApp instance;

	
	public static ArrayList<AlarmInfo> mAlarms;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		ImageLoaderUtil.getInstance().initConfig(this);
	}

	public static MainApp getInstance() {
		return instance;
	}

	public static File getSelectedItemsFile(int groupId) {
//		switch (groupId) {
//		case Data.GROUPID_INNER_0_ZHENGZHOU:
//			return new File(FILE_SELECTED_ITEM_0);
//		case Data.GROUPID_INNER_1_DALIAN:
//			return new File(FILE_SELECTED_ITEM_1);
//		case Data.GROUPID_INNER_2_SHANGHAI:
//			return new File(FILE_SELECTED_ITEM_2);
//		case Data.GROUPID_INNER_3_BOHAI:
//			return new File(FILE_SELECTED_ITEM_3);
//		case Data.GROUPID_INNER_4_FANYA:
//			return new File(FILE_SELECTED_ITEM_4);
//		case Data.GROUPID_OUTER_5_OTHER:
//			return new File(FILE_SELECTED_ITEM_5);
//		default:
//			break;
//		}

		if(Data.isInner(groupId)) {
			return new File(FILE_SELECTED_ITEM_Inner);
		} else{
			return new File(FILE_SELECTED_ITEM_Outer);
		}
		
	}
}
