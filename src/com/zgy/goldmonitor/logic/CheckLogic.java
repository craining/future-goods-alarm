package com.zgy.goldmonitor.logic;

import java.util.ArrayList;

import android.content.Context;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.Preference;
import com.zgy.goldmonitor.bean.AlarmInfo;
import com.zgy.goldmonitor.bean.Info;
import com.zgy.goldmonitor.bean.InfoList;
import com.zgy.goldmonitor.dao.DbOpera;
import com.zgy.goldmonitor.util.AlarmUtil;
import com.zgy.goldmonitor.util.NetworkUtil;

public class CheckLogic {

	private static CheckLogic instance;

	private CheckLogic() {
	}

	public static CheckLogic getInstance() {
		if (instance == null) {
			instance = new CheckLogic();
		}
		return instance;
	}

	public void check(final Context context) {
		AlarmUtil.cancelAlarm();
		
		if(!NetworkUtil.isNetworkAvailable(MainApp.getInstance())) {
			return;
		}
		
		if(Preference.getInstance().is3GNoAlarmOn()) {
			if(!NetworkUtil.isWifiEnabled(MainApp.getInstance())) {
				
				Debug.e("", "非WIFI，不提醒");
				return;
			}
		}
		
		AlarmUtil.setAlarm();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ArrayList<AlarmInfo> alarms = DbOpera.getInstance().getAllAlarms();

					if (alarms != null && alarms.size() > 0) {

						ArrayList<AlarmInfo> alarms_inner = new ArrayList<AlarmInfo>();
						ArrayList<AlarmInfo> alarms_outer = new ArrayList<AlarmInfo>();

						ArrayList<String> innerList = new ArrayList<String>();
						ArrayList<String> outerList = new ArrayList<String>();

						for (AlarmInfo a : alarms) {
							if (Data.isInner(a.groupId)) {
								alarms_inner.add(a);
								if (!innerList.contains(a.nameEn)) {
									innerList.add(a.nameEn);
								}
							} else {
								alarms_outer.add(a);
								if (!outerList.contains(a.nameEn)) {
									outerList.add(a.nameEn);
								}
							}
						}

						if (innerList.size() > 0) {
							Controller.getInstace().getDataNoThread(true, (String[]) innerList.toArray(new String[innerList.size()]), new TestListener(alarms_inner, context));
						}
						if (outerList.size() > 0) {
							Controller.getInstace().getDataNoThread(false, (String[]) outerList.toArray(new String[innerList.size()]), new TestListener(alarms_outer, context));
						}

					} else {
						AlarmUtil.cancelAlarm();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private class TestListener extends Listener {
		ArrayList<AlarmInfo> mAlarm;
		Context mContext;

		public TestListener(ArrayList<AlarmInfo> alarm, Context context) {
			this.mAlarm = alarm;
			mContext = context;
		}

		@Override
		public void getDataFinished(boolean result, InfoList data) {
			super.getDataFinished(result, data);
			if (result && data.infos != null) {
				for (AlarmInfo a : mAlarm) {
					if (data.infos.containsKey(a.nameCn)) {
						Info info = data.infos.get(a.nameCn);
						if (a.raseOrLow == AlarmInfo.ALARM_LOW) {
							if (a.markValue - a.changedValue - Float.parseFloat(info.values[a.checkId]) > 0) {
								// 跌破
								Debug.e("", a.nameCn + ":" + info.values[a.checkId] + "相对于 " + a.markValue + " 跌破 " + a.changedValue);
								AlarmUtil.doAlert(mContext, a, Float.parseFloat(info.values[a.checkId]));
							} else {
								Debug.v("", a.nameCn + ":" + info.values[a.checkId] + "相对于 " + a.markValue + " 未跌破 " + a.changedValue);
							}
						} else {
							if (Float.parseFloat(info.values[a.checkId]) - a.markValue - a.changedValue > 0) {
								// 涨超
								AlarmUtil.doAlert(mContext, a, Float.parseFloat(info.values[a.checkId]));
								Debug.e("", a.nameCn + ":" + info.values[a.checkId] + "相对于 " + a.markValue + " 涨超 " + a.changedValue);
							} else {
								Debug.v("", a.nameCn + ":" + info.values[a.checkId] + "相对于 " + a.markValue + " 未涨超 " + a.changedValue);
							}
						}
					}
				}
			}

		}

	}

}
