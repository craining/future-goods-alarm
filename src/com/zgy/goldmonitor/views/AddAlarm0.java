package com.zgy.goldmonitor.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.bean.AlarmInfo;

public class AddAlarm0 extends LinearLayout {

	private View mManiContentView;
	private Context mContext;

	private Button mBtnRaise;
	private Button mBtnLow;
	private PopupAlarmView mPop;

	public AddAlarm0(Context context, PopupAlarmView pop) {
		super(context);
		this.mContext = context;
		this.mPop = pop;
		init();
	}

	private void init() {

		mManiContentView = LayoutInflater.from(mContext).inflate(R.layout.item_addalarm_0_raisorlow, null);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER;
		mManiContentView.setLayoutParams(lp);

		mBtnRaise = (Button) mManiContentView.findViewById(R.id.btn_addalarm_0_raise);
		mBtnLow = (Button) mManiContentView.findViewById(R.id.btn_addalarm_0_low);

		mBtnLow.setOnClickListener(mOnClickListener);
		mBtnRaise.setOnClickListener(mOnClickListener);

		switch (mPop.getmAlarmInfo().raseOrLow) {
		case -1:
			mBtnRaise.setBackgroundResource(R.drawable.bg_btn_selecte_p);
			mBtnLow.setBackgroundResource(R.drawable.bg_btn_selecte_n);
			mPop.getmAlarmInfo().raseOrLow = AlarmInfo.ALARM_RAISE;// 默认为涨提醒
			break;
		case AlarmInfo.ALARM_RAISE:
			mBtnRaise.setBackgroundResource(R.drawable.bg_btn_selecte_p);
			mBtnLow.setBackgroundResource(R.drawable.bg_btn_selecte_n);
			break;
		case AlarmInfo.ALARM_LOW:
			mBtnRaise.setBackgroundResource(R.drawable.bg_btn_selecte_n);
			mBtnLow.setBackgroundResource(R.drawable.bg_btn_selecte_p);
			break;
		default:
			break;
		}

		this.addView(mManiContentView);
	}

	private OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_addalarm_0_raise:
				mBtnRaise.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mBtnLow.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mPop.getmAlarmInfo().raseOrLow = AlarmInfo.ALARM_RAISE;
				if(mPop.getmViewPager()!=null) {
					mPop.getmViewPager().setCurrentItem(mPop.getmViewPager().getCurrentItem() + 1, true);
				}
				break;
			case R.id.btn_addalarm_0_low:
				mBtnRaise.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnLow.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mPop.getmAlarmInfo().raseOrLow = AlarmInfo.ALARM_LOW;
				if(mPop.getmViewPager()!=null) {
					mPop.getmViewPager().setCurrentItem(mPop.getmViewPager().getCurrentItem() + 1, true);
				}
				break;

			default:
				break;
			}

		}
	};

}
