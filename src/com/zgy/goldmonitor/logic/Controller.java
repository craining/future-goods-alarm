package com.zgy.goldmonitor.logic;

import java.util.Date;

import android.text.TextUtils;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.bean.AlarmInfo;
import com.zgy.goldmonitor.bean.GoodAlarmInfo;
import com.zgy.goldmonitor.bean.HttpRequestResult;
import com.zgy.goldmonitor.dao.DbOpera;
import com.zgy.goldmonitor.util.HttpUtil;
import com.zgy.goldmonitor.util.PraseUtil;

public class Controller {

	private static final String URL = "http://hq.sinajs.cn/";

	private static Controller instance;

//	private Set<Listener> mListeners = new CopyOnWriteArraySet<Listener>();
//
//	public void addCallback(Listener callback) {
//		mListeners.add(callback);
//	}
//
//	public void removeCallback(Listener callback) {
//		mListeners.remove(callback);
//	}

	private Controller() {

	}

	public static Controller getInstace() {
		if (instance == null) {
			instance = new Controller();

		}

		return instance;
	}

	public void getData(final boolean inner_outer, final String[] datas, final Listener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					StringBuilder url = new StringBuilder(URL);
					
					
					String rn = (new Date()).getTime() + (Math.random() + "").toString().replace("0.", "");
					url.append("rn=").append(rn);
					url.append("&list=");
					
					for (String s : datas) {
						if (!TextUtils.isEmpty(s)) {
							url.append(s).append(",");
						}
					}

					HttpRequestResult result = HttpUtil.getInstence().sendRequest(url.toString().substring(0, url.length() - 1), null, false);
					if (result == null || !result.isConnectionOk() || TextUtils.isEmpty(result.result)) {
						listener.getDataFinished(false, null);
						return;
					}

					Debug.i("", "resonse=" + result.result);
					if (TextUtils.isEmpty(result.result) || !result.result.contains(";") || !result.result.contains("=") || !result.result.contains(",")) {
						Debug.e("", "没有数据");
						listener.getDataFinished(false, null);
						return;
					}
					if(listener!=null) {
						listener.getDataFinished(true, PraseUtil.prase(inner_outer, result.result));
					}

//					if(mListeners!=null) {
//						for(Listener l : mListeners) {
//							l.getDataFinished(true, PraseUtil.prase(inner_outer, result.result));
//						}
//					}

				} catch (Exception e) {
					if(listener!=null) {
						listener.getDataFinished(false, null);
					}
					e.printStackTrace();
				}

			}
		}).start();

	}

	public void getDataNoThread(final boolean inner_outer, final String[] datas, final Listener listener) {

		try {
			StringBuilder url = new StringBuilder(URL);
			
			String rn = (new Date()).getTime() + (Math.random() + "").toString().replace("0.", "");
			url.append("rn=").append(rn);
			url.append("&list=");
			
			
			for (String s : datas) {
				if (!TextUtils.isEmpty(s)) {
					url.append(s).append(",");
				}
			}

			HttpRequestResult result = HttpUtil.getInstence().sendRequest(url.toString().substring(0, url.length() - 1), null, false);
			if (result == null || !result.isConnectionOk() || TextUtils.isEmpty(result.result)) {
				listener.getDataFinished(false, null);
				return;
			}

			Debug.i("", "resonse=" + result.result);
			if (TextUtils.isEmpty(result.result) || !result.result.contains(";") || !result.result.contains("=") || !result.result.contains(",")) {
				Debug.e("", "没有数据");
				listener.getDataFinished(false, null);
				return;
			}

			if(listener!=null) {
				listener.getDataFinished(true, PraseUtil.prase(inner_outer, result.result));
			}

//			if(mListeners!=null) {
//				for(Listener l : mListeners) {
//					l.getDataFinished(true, PraseUtil.prase(inner_outer, result.result));
//				}
//			}

		} catch (Exception e) {
			if(listener!=null) {
				listener.getDataFinished(false, null);
			}
			e.printStackTrace();
		}

	}

	public void refreshAlarms(final Listener listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					MainApp.mAlarms = DbOpera.getInstance().getAllAlarms();
				} catch (Exception e) {
					e.printStackTrace();
					if(listener != null) {
						listener.updateAlarmsFinished(false);
					}
				}
				if(listener != null) {
					listener.updateAlarmsFinished(true);
				}
			}
		}).start();
	}

	/**
	 * 获得某个item的提醒设置
	 * 
	 * @param @param groupId
	 * @param @param itemId
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-5-30
	 */
	public static GoodAlarmInfo getGoodAlarmInfo(int groupId, int itemId) {
		GoodAlarmInfo info = new GoodAlarmInfo();
		if (MainApp.mAlarms != null && MainApp.mAlarms.size() > 0) {
			String name = Data.getItemNameEn(groupId, itemId);
			for (AlarmInfo i : MainApp.mAlarms) {
				if (i.nameEn.equals(name)) {
					if (i.raseOrLow == AlarmInfo.ALARM_RAISE) {
						info.alarmRaise = i;
					} else {
						info.alarmLow = i;
					}
				}
			}
		}

		return info;
	}

}
