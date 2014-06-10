package com.zgy.goldmonitor.activity_fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.bean.AlarmInfo;
import com.zgy.goldmonitor.dao.DbOpera;
import com.zgy.goldmonitor.logic.Controller;
import com.zgy.goldmonitor.logic.Listener;
import com.zgy.goldmonitor.util.ActivityManager;
import com.zgy.goldmonitor.views.DialogNormal;
import com.zgy.goldmonitor.views.ToastView;

public class AlarmListActivity extends Activity implements OnClickListener {

	private ImageView mImgBack;
	private ListView mListView;
	private AlarmListViewAdapter mAdapter;
	private TextView mTextNull;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarmlist);
		ActivityManager.push(this);

		mImgBack = (ImageView) findViewById(R.id.img_alarmlist_back);
		mListView = (ListView) findViewById(R.id.list_alarmlist);
		mTextNull = (TextView) findViewById(R.id.text_alarmlist_null);

		mImgBack.setOnClickListener(this);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				final AlarmInfo alarm = (AlarmInfo) ((AlarmListViewAdapter) mListView.getAdapter()).getItem(position);

				String checkname = (Data.isInner(alarm.groupId) ? Data.Inner.getIdName(alarm.checkId) : Data.Outer.getIdName(alarm.checkId));
				String content = "当" + checkname + "相对于 " + alarm.markValue + (alarm.raseOrLow == AlarmInfo.ALARM_RAISE ? " 涨超 " : " 跌破 ") + alarm.changedValue + " 时，会自动提醒";

				new DialogNormal.Builder(AlarmListActivity.this).setTitle(alarm.nameCn + (alarm.raseOrLow == AlarmInfo.ALARM_LOW ? "跌提醒" : "涨提醒")).setMessage1(content, Gravity.CENTER)
						.setPositiveButton("查看", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								ActivityManager.popAll();
								Intent i = new Intent(AlarmListActivity.this, MainActivity.class);
								i.putExtra("groupid", alarm.groupId);
								i.putExtra("itemid", Data.getItemIdByNameEn(alarm.groupId, alarm.nameEn));
								i.putExtra("raiselow", alarm.raseOrLow);
								startActivity(i);
							}
						}).setNegativeButton("删除", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								showDeleteDlg(alarm);
							}
						}).setNeutralButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).setCancelable(true).create().show();
			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshList();
	}

	private void refreshAlarms() {
		Controller.getInstace().refreshAlarms(mListener);
	}

	private Listener mListener = new Listener() {

		@Override
		public void updateAlarmsFinished(boolean result) {
			super.updateAlarmsFinished(result);
			if (result) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						refreshList();
					}
				});
			}
		}

	};

	private void showDeleteDlg(final AlarmInfo alarm) {
		new DialogNormal.Builder(AlarmListActivity.this).setTitle("提示").setMessage1("您确定要删除此提醒吗？", Gravity.CENTER).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 删除
				dialog.dismiss();

				if (DbOpera.getInstance().deleteAlarm(alarm.nameEn, alarm.raseOrLow)) {
					refreshAlarms();
					// CheckLogic.getInstance().check(mContext);
					MainActivity.mNeedRefreshAlarmsViews = true;
					ToastView.showToast(AlarmListActivity.this, "提醒已删除！", ToastView.LENGTH_LONG);
				} else {
					ToastView.showToast(AlarmListActivity.this, "提醒删除失败，请稍后再试", ToastView.LENGTH_LONG);
				}

			}
		}).setCancelable(false).create().show();
	}

	private void refreshList() {
		if (MainApp.mAlarms == null || MainApp.mAlarms.size() <= 0) {
			mTextNull.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			return;
		}
		mTextNull.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		if (mAdapter == null) {
			mAdapter = new AlarmListViewAdapter(AlarmListActivity.this, MainApp.mAlarms);
			mListView.setAdapter(mAdapter);
		} else {
			((AlarmListViewAdapter) mListView.getAdapter()).notifyDataSetChanged(MainApp.mAlarms);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_alarmlist_back:
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

	private class AlarmListViewAdapter extends BaseAdapter {

		private ArrayList<AlarmInfo> mAlarms;
		private LayoutInflater mInflater;
		private Context mContext;

		public AlarmListViewAdapter(Context context, ArrayList<AlarmInfo> alarms) {
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
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null || convertView.getTag() == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_alarmlist, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img_item_alarm_logo);
				holder.textinfo = (TextView) convertView.findViewById(R.id.text_item_alarm_info);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

//			holder.textinfo.setText(mAlarms.get(position).nameCn + "     相对于 " + mAlarms.get(position).markValue + (mAlarms.get(position).raseOrLow == AlarmInfo.ALARM_LOW ? " 跌破 " : " 涨超 ") + mAlarms.get(position).changedValue + " 提醒");
			holder.textinfo.setText(mAlarms.get(position).nameCn + "  " + (Data.isInner(mAlarms.get(position).groupId) ? Data.Inner.getIdName(mAlarms.get(position).checkId) : Data.Outer.getIdName(mAlarms.get(position).checkId)) + "  " + (mAlarms.get(position).raseOrLow == AlarmInfo.ALARM_LOW ? " 跌提醒 " : " 涨提醒 "));
			holder.img.setImageResource(mAlarms.get(position).raseOrLow == AlarmInfo.ALARM_LOW ? R.drawable.ic_alarm_low : R.drawable.ic_alarm_raise);

			return convertView;
		}

		private class ViewHolder {
			public ImageView img;
			public TextView textinfo;
		}
	}

}
