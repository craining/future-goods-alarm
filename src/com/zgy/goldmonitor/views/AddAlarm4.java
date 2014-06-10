package com.zgy.goldmonitor.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Preference;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.activity_fragment.MainActivity;
import com.zgy.goldmonitor.bean.AlarmInfo;
import com.zgy.goldmonitor.dao.DbOpera;
import com.zgy.goldmonitor.logic.CheckLogic;

public class AddAlarm4 extends LinearLayout {

	private View mManiContentView;
	private Context mContext;
	private TextView mTextShow;
	private TextView mTextTip;

	private MainActivity mMainAct;
	private PopupAlarmView mPop;

	public AddAlarm4(Context context, PopupAlarmView pop) {
		super(context);
		this.mContext = context;
		this.mPop = pop;

		init();
	}

	private void init() {
		mManiContentView = LayoutInflater.from(mContext).inflate(R.layout.item_addalarm_3_last, null);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER;
		mManiContentView.setLayoutParams(lp);
		mTextTip = (TextView) mManiContentView.findViewById(R.id.text_addalarm_3_tip);
		mTextShow = (TextView) mManiContentView.findViewById(R.id.text_addalarm_3_info);

		Button btnOk = (Button) mManiContentView.findViewById(R.id.btn_addalarm_3_finish);
		final Button btnDel = (Button) mManiContentView.findViewById(R.id.btn_addalarm_3_delete);
		
		
		if(Preference.getInstance().is3GNoAlarmOn()) {
			mTextTip.setText("您已设置为2G/3G网络下不提醒，请注意。");
		} else {
			mTextTip.setText("提醒功能需要消耗少量网络流量，用于实时更新当前期货行情；\r\n您若不想消耗2g/3g网络流量，请在设置页面里设置");
		}
		
		
		if (mPop.ismIsAddNew()) {
			btnDel.setVisibility(View.GONE);
			btnOk.setText("完成添加");
		} else {
			refreshInfo();
			btnOk.setText("完成修改");
			btnDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new DialogNormal.Builder(mContext).setTitle("提示").setMessage1("您确定要删除此提醒吗？", Gravity.CENTER).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 删除
							dialog.dismiss();
							if (mPop.getmPopWin() != null && mPop.getmPopWin().isShowing()) {
								mPop.dismiss();
							}
							if (DbOpera.getInstance().deleteAlarm(mPop.getmAlarmInfo().nameEn, mPop.getmAlarmInfo().raseOrLow)) {
								((MainActivity) mContext).getAlarmsandRefresh();
								CheckLogic.getInstance().check(mContext);
								ToastView.showToast(mContext, "提醒已删除！", ToastView.LENGTH_LONG);
							} else {
								ToastView.showToast(mContext, "提醒删除失败，请稍后再试", ToastView.LENGTH_LONG);
							}

						}
					}).setCancelable(false).create().show();
				}
			});
		}

		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mPop.getmAlarmInfo().markValue - 0 <= 0) {
					ToastView.showToast(mContext, "基准值设置不能为0", ToastView.LENGTH_LONG);
					mPop.getmViewPager().setCurrentItem(mPop.ismNoSelectRaiseLow() ? 2 : 1, true);
					return;
				}

				if (mPop.getmAlarmInfo().changedValue - 0 <= 0) {
					ToastView.showToast(mContext, "变化范围设置不能为0", ToastView.LENGTH_LONG);
					mPop.getmViewPager().setCurrentItem(mPop.ismNoSelectRaiseLow() ? 3 : 2, true);
					return;
				}

				if (mPop.getmPopWin() != null && mPop.getmPopWin().isShowing()) {
					mPop.dismiss();
				}
				if (DbOpera.getInstance().insertAlarm(mPop.getmAlarmInfo())) {
					((MainActivity) mContext).getAlarmsandRefresh();
					CheckLogic.getInstance().check(mContext);
					ToastView.showToast(mContext, btnDel.getVisibility() == View.VISIBLE ? "提醒已更新！" : "提醒已添加！", ToastView.LENGTH_LONG);
				} else {
					ToastView.showToast(mContext, btnDel.getVisibility() == View.VISIBLE ? "提醒更新失败，请稍后再试" : "提醒添加失败，请稍后再试", ToastView.LENGTH_LONG);
				}

			}
		});

		this.addView(mManiContentView);

	}

	public void refreshInfo() {
		String checkname = (Data.isInner(mPop.getmAlarmInfo().groupId) ? Data.Inner.getIdName(mPop.getmAlarmInfo().checkId) : Data.Outer.getIdName(mPop.getmAlarmInfo().checkId));
		mTextShow.setText("【" + mPop.getmAlarmInfo().nameCn + "】" + checkname + (mPop.getmAlarmInfo().raseOrLow == AlarmInfo.ALARM_RAISE ? "涨" : "跌") + "提醒：\r\n\r\n当" + checkname + "相对于 "
				+ mPop.getmAlarmInfo().markValue + (mPop.getmAlarmInfo().raseOrLow == AlarmInfo.ALARM_RAISE ? " 涨超 " : " 跌破 ") + mPop.getmAlarmInfo().changedValue + " 时，会自动提醒");
	}
}
