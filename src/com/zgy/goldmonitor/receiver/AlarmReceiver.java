package com.zgy.goldmonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.logic.CheckLogic;
import com.zgy.goldmonitor.util.AlarmUtil;
import com.zgy.goldmonitor.util.NetworkUtil;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Debug.e("", "on alarm receive");
		if (NetworkUtil.isNetworkAvailable(MainApp.getInstance())) {
			CheckLogic.getInstance().check(context);
		} else {
			AlarmUtil.cancelAlarm();
		}
	}
}
