package com.zgy.goldmonitor.activity_fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.Preference;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.logic.CheckLogic;
import com.zgy.goldmonitor.util.ActivityManager;
import com.zgy.goldmonitor.util.AlarmUtil;
import com.zgy.goldmonitor.util.NetworkUtil;
import com.zgy.goldmonitor.views.DialogFeedback;
import com.zgy.goldmonitor.views.DialogInput;
import com.zgy.goldmonitor.views.DialogList;
import com.zgy.goldmonitor.views.DialogList.Item;
import com.zgy.goldmonitor.views.DialogNormal;
import com.zgy.goldmonitor.views.ToastView;

public class SettingsActivity extends Activity implements OnClickListener {

	private ImageView mImgBack;
	private CheckBox mCheckAudio;
	private CheckBox mCheckVibrate;
	private CheckBox mCheckSwitchAlarm;
	private CheckBox mCheck3GPic;
	private CheckBox mCheck3GAlarm;

	private RelativeLayout mLayoutAlarmList;
	private RelativeLayout mLayoutAlarmWay;
	private RelativeLayout mLayoutRefreshRate;
	private RelativeLayout mLayoutFeedback;
	private RelativeLayout mLayoutDeclaration;
	private RelativeLayout mLayoutAbout;

	private TextView mTextAlarmWay;
	private TextView mTextAlarmCount;
	private TextView mTextRefreshRate;

	private TextView mTextAlarmSetAvoid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		ActivityManager.push(this);

		mImgBack = (ImageView) findViewById(R.id.img_setting_back);

		mCheckAudio = (CheckBox) findViewById(R.id.check_settings_audio);
		mCheckVibrate = (CheckBox) findViewById(R.id.check_settings_vibrate);
		mCheckSwitchAlarm = (CheckBox) findViewById(R.id.check_settings_switch_alarm);
		mCheck3GPic = (CheckBox) findViewById(R.id.check_settings_3g_pic);
		mCheck3GAlarm = (CheckBox) findViewById(R.id.check_settings_3g_alarm);
		mLayoutAlarmList = (RelativeLayout) findViewById(R.id.layout_settings_alarmlist);
		mLayoutAlarmWay = (RelativeLayout) findViewById(R.id.layout_settings_alertway);
		mLayoutRefreshRate = (RelativeLayout) findViewById(R.id.layout_settings_rate);
		mLayoutFeedback = (RelativeLayout) findViewById(R.id.layout_settings_feedback);
		mLayoutAbout = (RelativeLayout) findViewById(R.id.layout_settings_about);
		mLayoutDeclaration = (RelativeLayout) findViewById(R.id.layout_settings_declaration);
		mTextAlarmSetAvoid = (TextView) findViewById(R.id.text_alarm_avoid);
		mTextAlarmWay = (TextView) findViewById(R.id.text_settings_alertway);
		mTextRefreshRate = (TextView) findViewById(R.id.text_settings_rate);
		mTextAlarmCount = (TextView) findViewById(R.id.text_settings_alarm_count);

		mImgBack.setOnClickListener(this);
		mCheckAudio.setOnClickListener(this);
		mCheckVibrate.setOnClickListener(this);
		mCheckSwitchAlarm.setOnClickListener(this);
		mCheck3GPic.setOnClickListener(this);
		mCheck3GAlarm.setOnClickListener(this);
		mLayoutAlarmList.setOnClickListener(this);
		mLayoutAlarmWay.setOnClickListener(this);
		mLayoutRefreshRate.setOnClickListener(this);
		mLayoutFeedback.setOnClickListener(this);
		mLayoutDeclaration.setOnClickListener(this);
		mLayoutAbout.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		mCheckAudio.setChecked(Preference.getInstance().isAlarmAudioOn());
		mCheckVibrate.setChecked(Preference.getInstance().isAlarmVibrateOn());

		mCheck3GAlarm.setChecked(Preference.getInstance().is3GNoAlarmOn());
		mCheck3GPic.setChecked(Preference.getInstance().is3GNoPicOn());
		mCheckSwitchAlarm.setChecked(Preference.getInstance().isAlarmOff());

		switch (Preference.getInstance().getAlarmAlertWay()) {
		case MainApp.ALARM_WAY_NOTIFY:
			mTextAlarmWay.setText("通知栏消息");
			break;
		case MainApp.ALARM_WAY_DLG:
			mTextAlarmWay.setText("弹窗提示");
			break;
		default:
			break;
		}

		mTextRefreshRate.setText(Preference.getInstance().getRefreshRate() + "分钟");

		refreshPannel();

	}

	private void refreshPannel() {

		if (MainApp.mAlarms != null && MainApp.mAlarms.size() > 0) {
			mTextAlarmSetAvoid.setVisibility(View.GONE);
			mTextAlarmCount.setText("" + MainApp.mAlarms.size());
		} else {
			mTextAlarmSetAvoid.setText("您尚未增加任何提醒\r\n无需进行设置");
			mTextAlarmSetAvoid.setVisibility(View.VISIBLE);
			mTextAlarmCount.setText("0");
		}

		if (Preference.getInstance().isAlarmOff()) {
			mTextAlarmSetAvoid.setText("您已关闭提醒功能\r\n无需进行设置");
			mTextAlarmSetAvoid.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.pop(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_setting_back:
			finish();
			break;

		case R.id.check_settings_audio:
			Preference.getInstance().setAlarmAudioOn(mCheckAudio.isChecked());
			break;
		case R.id.check_settings_vibrate:
			Preference.getInstance().setAlarmVibrateOn(mCheckVibrate.isChecked());
			break;

		case R.id.check_settings_switch_alarm:
			Preference.getInstance().setAlarmOff(mCheckSwitchAlarm.isChecked());
			if (mCheckSwitchAlarm.isChecked()) {
				AlarmUtil.cancelAlarm();
			} else {
				CheckLogic.getInstance().check(SettingsActivity.this);
			}
			
			refreshPannel();
			break;
		case R.id.check_settings_3g_pic:
			Preference.getInstance().set3GNoPicOn(mCheck3GPic.isChecked());
			break;
		case R.id.check_settings_3g_alarm:
			Preference.getInstance().set3GNoAlarmOn(mCheck3GAlarm.isChecked());
			break;
		case R.id.layout_settings_alertway:
			showAlertWayDlg();
			break;
		case R.id.layout_settings_rate:
			showRefreshRateDlg();
			break;
		case R.id.layout_settings_feedback:
			if (!NetworkUtil.isNetworkAvailable(SettingsActivity.this)) {
				ToastView.showToast(SettingsActivity.this, "当前无网络，请稍后再试", Toast.LENGTH_LONG);
				return;
			}

			showFeedbackDlg();
			break;
		case R.id.layout_settings_about:
			showAboutDlg();
			break;
		case R.id.layout_settings_declaration:
			showDelarationDlg();
			break;
		case R.id.layout_settings_alarmlist:
			startActivity(new Intent(SettingsActivity.this, AlarmListActivity.class));
			break;
		default:
			break;
		}
	}

	private void showAlertWayDlg() {
		Item[] items = new Item[2];
		Item item1 = new Item();
		item1.itemText = "通知栏消息提醒";
		item1.tag = 0;
		Item item2 = new Item();
		item2.itemText = "弹窗提醒";
		item2.tag = 1;
		items[0] = item1;
		items[1] = item2;
		items[Preference.getInstance().getAlarmAlertWay()].isSelected = true;
		new DialogList.Builder(SettingsActivity.this).setTitle("选择提醒方式").setItems(items).setOnListItemSelectedListener(new DialogList.OnListItemSelectedListener() {

			@Override
			public void onListItemSelected(Item item) {
				mTextAlarmWay.setText(item.itemText);
				Preference.getInstance().setAlarmAlertWay(item.tag);
			}
		}).setCancelable(true).create().show();
	}

	private void showRefreshRateDlg() {
		Item[] items = new Item[7];
		Item item1 = new Item();
		item1.itemText = "3分钟";
		item1.tag = 3;
		Item item2 = new Item();
		item2.itemText = "5分钟";
		item2.tag = 5;

		Item item3 = new Item();
		item3.itemText = "10分钟";
		item3.tag = 10;

		Item item4 = new Item();
		item4.itemText = "15分钟";
		item4.tag = 15;

		Item item5 = new Item();
		item5.itemText = "30分钟";
		item5.tag = 30;

		Item item6 = new Item();
		item6.itemText = "60分钟";
		item6.tag = 60;

		Item item7 = new Item();
		item7.itemText = "自定义";
		item7.tag = -1;
		items[0] = item1;
		items[1] = item2;
		items[2] = item3;
		items[3] = item4;
		items[4] = item5;
		items[5] = item6;
		items[6] = item7;

		int time = Preference.getInstance().getRefreshRate();
		boolean hasSelected = false;
		for (Item i : items) {
			if (time == i.tag) {
				i.isSelected = true;
				hasSelected = true;
			}
		}
		if (!hasSelected) {
			items[6].itemText = "自定义(" + time + "分钟)";
			items[6].isSelected = true;
		}

		new DialogList.Builder(SettingsActivity.this).setTitle("选择后台刷新频率").setItems(items).setOnListItemSelectedListener(new DialogList.OnListItemSelectedListener() {

			@Override
			public void onListItemSelected(Item item) {
				if (item.tag != -1) {
					Preference.getInstance().setRefreshRate(item.tag);
					mTextRefreshRate.setText(item.tag + "分钟");
					CheckLogic.getInstance().check(SettingsActivity.this);
				} else {
					showRefreshRateInputDlg();
				}

			}
		}).setCancelable(true).create().show();
	}

	private void showRefreshRateInputDlg() {
		final DialogInput.Builder dlg = new DialogInput.Builder(SettingsActivity.this);

		dlg.setTitle("自定义更新频率").setMessageHint("以分钟为单位\r\n太过频繁会消耗更多的电量").setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!TextUtils.isEmpty(dlg.mInputStr)) {
					try {
						int time = Integer.parseInt(dlg.mInputStr);
						if (time > 0) {
							Preference.getInstance().setRefreshRate(time);
							mTextRefreshRate.setText(time + "分钟");
							CheckLogic.getInstance().check(SettingsActivity.this);
							dialog.dismiss();
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				ToastView.showToast(SettingsActivity.this, "请输入正确的刷新间隔", Toast.LENGTH_LONG);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setCancelable(false).create().show();
	}

	private void showFeedbackDlg() {
		final DialogFeedback.Builder dlg = new DialogFeedback.Builder(SettingsActivity.this);
		dlg.setTitle("意见反馈");
		dlg.setPositiveButton("发送", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (dlg.sendFinished) {
					dialog.dismiss();
				} else {
					showFeedbackDlg();
				}
			}
		});
		dlg.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dlg.setCancelable(false).create().show();
	}

	private void showDelarationDlg() {

		new DialogNormal.Builder(SettingsActivity.this).setTitle("声明").setMessage1("本软件数据来源于网络，对于时效性、准确性等问题给用户造成的精神、经济等所有损失，本软件不负任何责任。", Gravity.LEFT)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).setCancelable(false).create().show();

	}

	private void showAboutDlg() {

		new DialogNormal.Builder(SettingsActivity.this).setTitle("关于" + getString(R.string.app_name))
				.setMessage1("期货提醒是一款在移动设备上实时查看期货行情的软件，涵盖国内外多种热门期货；\r\n您可以针对某种期货的最新价、买入价、卖出价添加涨跌提醒，当期货价格达到提醒条件时，手机会及时发出通知。", Gravity.LEFT)
				.setMessage2("作者：ZGY\r\n\r\nv" + getString(R.string.version_str), Gravity.CENTER).setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).setCancelable(false).create().show();

	}
}
