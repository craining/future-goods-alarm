package com.zgy.goldmonitor.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.Preference;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.activity_fragment.AlertActivity;
import com.zgy.goldmonitor.bean.AlarmInfo;
import com.zgy.goldmonitor.receiver.AlarmReceiver;
import com.zgy.goldmonitor.receiver.NotificationReceiver;

public class AlarmUtil {

	/**
	 * 设置闹钟，定期验证字体是否到期
	 * 
	 * @param
	 * @author zhuanggy
	 * @date 2014-1-8
	 */
	public static void setAlarm() {
		long dely = Preference.getInstance().getRefreshRate() * 60 * 1000;//
		Debug.v("", "setAlarm  dely=" + dely);
		// // 启动新闹钟
		Intent intentSend = new Intent();
		intentSend.setClass(MainApp.getInstance(), AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(MainApp.getInstance(), 0, intentSend, 0);
		AlarmManager am = (AlarmManager) MainApp.getInstance().getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC, System.currentTimeMillis() + dely, sender);
		// am.setRepeating(AlarmManager.RTC, 0, FontCoolConstant.CHECK_NOOP, sender);// 每次到时，都会重新set，可以直接用set()，但保证准确性，设置重复闹钟
	}

	public static void cancelAlarm() {
		Debug.v("", "cancelAlarm");
		// // 启动新闹钟
		Intent intentSend = new Intent();
		intentSend.setClass(MainApp.getInstance(), AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(MainApp.getInstance(), 0, intentSend, 0);
		AlarmManager am = (AlarmManager) MainApp.getInstance().getSystemService(Activity.ALARM_SERVICE);
		am.cancel(sender);
	}

	public static void doAlert(Context context, AlarmInfo alarmInfo, float nowValue) {

		String title = alarmInfo.nameCn + (alarmInfo.raseOrLow == AlarmInfo.ALARM_LOW ? "跌提醒" : "涨提醒");
		String content =

		alarmInfo.nameCn + "的"

		+ (Data.isInner(alarmInfo.groupId) ? Data.Inner.getIdName(alarmInfo.checkId) : Data.Outer.getIdName(alarmInfo.checkId))

		+ "目前是： " + nowValue

		+ "；\r\n相对于 "

		+ alarmInfo.markValue

		+ (alarmInfo.raseOrLow == AlarmInfo.ALARM_LOW ? " 跌破了 " : " 涨超了 ")

		+ Math.abs(alarmInfo.markValue - nowValue)

		+ "，\r\n此变化范围已经达到了预设的变化提醒范围。(预设范围是:" + alarmInfo.changedValue + ")";

		if (Preference.getInstance().getAlarmAlertWay() == MainApp.ALARM_WAY_DLG) {
			Intent i = new Intent(context, AlertActivity.class);
			i.putExtra("groupid", alarmInfo.groupId);
			i.putExtra("itemid", Data.getItemIdByNameEn(alarmInfo.groupId, alarmInfo.nameEn));
			i.putExtra("nameen", alarmInfo.nameEn);
			i.putExtra("raiselow", alarmInfo.raseOrLow);
			i.putExtra("content", content);
			i.putExtra("title", title);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		} else {

			String id=(alarmInfo.raseOrLow + 1) + "" + alarmInfo.groupId + "" + Data.getItemIdByNameEn(alarmInfo.groupId, alarmInfo.nameEn);
			int id_notification = Integer.parseInt(id);
			Debug.e("", "notification id =" + id + "-->" + id_notification);

			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(alarmInfo.raseOrLow == AlarmInfo.ALARM_LOW ? R.drawable.ic_alarm_low : R.drawable.ic_alarm_raise, alarmInfo.nameCn
					+ (alarmInfo.raseOrLow == AlarmInfo.ALARM_LOW ? "跌提醒" : "涨提醒"), System.currentTimeMillis());
			notification.icon = alarmInfo.raseOrLow == AlarmInfo.ALARM_LOW ? R.drawable.ic_alarm_low : R.drawable.ic_alarm_raise;
			// notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			// notification.flags |= Notification.FLAG_NO_CLEAR;
			// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // 设置通知的事件消息

			CharSequence contentText = (Data.isInner(alarmInfo.groupId) ? Data.Inner.getIdName(alarmInfo.checkId) : Data.Outer.getIdName(alarmInfo.checkId))

			+ ":" + nowValue + "；比" + alarmInfo.markValue

			+ (alarmInfo.raseOrLow == AlarmInfo.ALARM_LOW ? "跌破了 " : "涨超了 ")

			+ Math.abs(alarmInfo.markValue - nowValue);
			CharSequence contentTitle = alarmInfo.nameCn + (alarmInfo.raseOrLow == AlarmInfo.ALARM_LOW ? "跌提醒" : "涨提醒");

			Intent notificationIntent = new Intent(NotificationReceiver.ACTION_NOTIFICATION);

			notificationIntent.putExtra("groupid", alarmInfo.groupId);
			notificationIntent.putExtra("itemid", Data.getItemIdByNameEn(alarmInfo.groupId, alarmInfo.nameEn));
			notificationIntent.putExtra("nameen", alarmInfo.nameEn);
			notificationIntent.putExtra("raiselow", alarmInfo.raseOrLow);
			notificationIntent.putExtra("content", content);
			notificationIntent.putExtra("title", title);

			PendingIntent contentItent = PendingIntent.getBroadcast(context, id_notification, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(id_notification, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同

		}

		try {
			if (Preference.getInstance().isAlarmAudioOn()) {
				Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				Ringtone r = RingtoneManager.getRingtone(MainApp.getInstance(), notification);
				r.play();
			}

			if (Preference.getInstance().isAlarmVibrateOn()) {
				PhoneUtil.doVibraterNormal((Vibrator) MainApp.getInstance().getSystemService(Service.VIBRATOR_SERVICE));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void cancelNotification(Context context, int groupId, int itemId, int raiseLow) {
		String id=(raiseLow + 1) + "" +  groupId + "" + itemId;
		int id_notification = Integer.parseInt(id);
		Debug.e("", "notification id =" + id_notification);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id_notification);// 注意ID号，不能与此程序中的其他通知栏图标相同

	}
}
