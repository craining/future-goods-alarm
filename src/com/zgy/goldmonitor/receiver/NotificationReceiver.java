package com.zgy.goldmonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.activity_fragment.AlertActivity;
import com.zgy.goldmonitor.util.AlarmUtil;

public class NotificationReceiver extends BroadcastReceiver {
	public static final String ACTION_NOTIFICATION = "com.zgy.goldmonitor.NOTIFICATION";

	@Override
	public void onReceive(Context context, Intent intent) {
		Debug.e("", "on NotificationReceiver");
		
		Bundle b = intent.getExtras();
		if(b!= null) {
			AlarmUtil.cancelNotification(context, b.getInt("groupid"), b.getInt("itemid"), b.getInt("raiselow"));
			Intent i = new Intent(context, AlertActivity.class);
			i.putExtra("groupid", b.getInt("groupid"));
			i.putExtra("itemid", b.getInt("itemid"));
			i.putExtra("nameen", b.getString("nameen"));
			i.putExtra("raiselow", b.getInt("raiselow"));
			i.putExtra("content", b.getString("content"));
			i.putExtra("title", b.getString("title"));
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} 
		
	}
}
