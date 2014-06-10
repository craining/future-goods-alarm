package com.zgy.goldmonitor.receiver;

import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.logic.CheckLogic;
import com.zgy.goldmonitor.util.AlarmUtil;
import com.zgy.goldmonitor.util.NetworkUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Debug.e("", "on recieve");
		if(NetworkUtil.isNetworkAvailable(MainApp.getInstance())) {
			CheckLogic.getInstance().check(context);
		} else {
			AlarmUtil.cancelAlarm();
		}
	}

	
}
