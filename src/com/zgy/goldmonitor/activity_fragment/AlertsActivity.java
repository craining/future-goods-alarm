package com.zgy.goldmonitor.activity_fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.bean.AlarmInfo;
import com.zgy.goldmonitor.dao.DbOpera;
import com.zgy.goldmonitor.logic.Controller;
import com.zgy.goldmonitor.util.ActivityManager;
import com.zgy.goldmonitor.views.ToastView;

@SuppressLint("NewApi")
public class AlertsActivity extends Activity implements OnClickListener {

	private static ArrayList<AlarmInfo> mAlarmsInfo;

	private Button mBtnClose;
	private ListView mListView;
	private AlertListViewAdapter mAdapter;

	public static void startAlertsDlg(Context context, ArrayList<AlarmInfo> alarms) {
		mAlarmsInfo = alarms;
		Intent i = new Intent(context, AlertsActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alerts);

		ActivityManager.push(this);

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			this.setFinishOnTouchOutside(false);
		}

		mBtnClose = (Button) findViewById(R.id.btn_close_alerts);
		mListView = (ListView) findViewById(R.id.list_alarms_alerts);

		mBtnClose.setOnClickListener(this);

		Debug.e("", "alerts on craete");
		show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.btn_alert_positiveButton:
		// // 进入详情
		// ActivityManager.popAll();
		// Intent i = new Intent(AlertsActivity.this, MainActivity.class);
		// i.putExtra("groupid", mGroupId);
		// i.putExtra("itemid", mItemId);
		// i.putExtra("raiselow", mRaiseLow);
		// startActivity(i);
		// finish();
		// break;
		// case R.id.btn_alert_negativeButton:
		// // 删除提醒
		// if (DbOpera.getInstance().deleteAlarm(mNameEn, mRaiseLow)) {
		// MainActivity.mNeedRefreshAlarmsViews = true;
		// Controller.getInstace().refreshAlarms(null);
		// ToastView.showToast(AlertsActivity.this, "提醒已删除！", ToastView.LENGTH_LONG);
		// finish();
		// } else {
		// ToastView.showToast(AlertsActivity.this, "提醒删除失败，请稍后再试", ToastView.LENGTH_LONG);
		// }
		// break;
		case R.id.btn_close_alerts:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.pop(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void show() {
		if (mAdapter == null) {
			mAdapter = new AlertListViewAdapter(AlertsActivity.this, mAlarmsInfo);
			mListView.setAdapter(mAdapter);
		} else {
			((AlertListViewAdapter) mListView.getAdapter()).notifyDataSetChanged(mAlarmsInfo);
		}
		((TextView) findViewById(R.id.text_title_alerts)).setText("期货提醒(" + mAlarmsInfo.size() + ")");
	}

	private void deleteAlarm(int position) {
		mAlarmsInfo.remove(position);
		if (mAlarmsInfo.size() == 0) {
			finish();
			return;
		}
		show();
	}

	private class AlertListViewAdapter extends BaseAdapter {

		private ArrayList<AlarmInfo> mAlarms;
		private LayoutInflater mInflater;
		private Context mContext;

		public AlertListViewAdapter(Context context, ArrayList<AlarmInfo> alarms) {
			this.mAlarms = alarms;
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}

		public void notifyDataSetChanged(ArrayList<AlarmInfo> alarms) {
			this.mAlarms = alarms;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mAlarms.size();
		}

		@Override
		public Object getItem(int position) {
			return mAlarms.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null || convertView.getTag() == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_alert_list, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img_alert_item_ic);
				holder.textTitle = (TextView) convertView.findViewById(R.id.text_alert_item_title);
				holder.textinfo = (TextView) convertView.findViewById(R.id.text_alert_item_content);
				holder.btnCheck = (Button) convertView.findViewById(R.id.btn_alert_item_check);
				holder.btnDelete = (Button) convertView.findViewById(R.id.btn_alert_item_delete);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			AlarmInfo info = mAlarms.get(position);

			String content =

			(Data.isInner(info.groupId) ? Data.Inner.getIdName(info.checkId) : Data.Outer.getIdName(info.checkId))

			+ "： " + info.nowValue

			+ "；\r\n相对于 "

			+ info.markValue

			+ (info.raseOrLow == AlarmInfo.ALARM_LOW ? " 跌破 " : " 涨超 ")

			+ Math.abs(info.markValue - info.nowValue)

			+ "，\r\n(预设范围是:" + info.changedValue + ")";

			holder.img.setImageResource(info.raseOrLow == AlarmInfo.ALARM_LOW ? R.drawable.ic_alarm_low : R.drawable.ic_alarm_raise);
			holder.textTitle.setText(info.nameCn + (info.raseOrLow == AlarmInfo.ALARM_LOW ? "跌提醒" : "涨提醒"));
			holder.textTitle.setTextColor(info.raseOrLow == AlarmInfo.ALARM_LOW ? getResources().getColor(R.color.green) : Color.RED);
			holder.textinfo.setText(content);

			holder.btnDelete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 删除提醒
					if (DbOpera.getInstance().deleteAlarm(mAlarms.get(position).nameEn, mAlarms.get(position).raseOrLow)) {
						MainActivity.mNeedRefreshAlarmsViews = true;
						Controller.getInstace().refreshAlarms(null);
						ToastView.showToast(AlertsActivity.this, "提醒已删除！", ToastView.LENGTH_LONG);
						deleteAlarm(position);
					} else {
						ToastView.showToast(AlertsActivity.this, "提醒删除失败，请稍后再试", ToastView.LENGTH_LONG);
					}
				}
			});
			holder.btnCheck.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(AlertsActivity.this, MainActivity.class);
					i.putExtra("groupid", mAlarms.get(position).groupId);
					i.putExtra("itemid", Data.getItemIdByNameEn(mAlarms.get(position).groupId, mAlarms.get(position).nameEn));
					i.putExtra("raiselow", mAlarms.get(position).raseOrLow);
					startActivity(i);
				}
			});
			return convertView;
		}

		private class ViewHolder {
			public ImageView img;
			public TextView textTitle;
			public TextView textinfo;
			public Button btnCheck;
			public Button btnDelete;
		}
	}

}
