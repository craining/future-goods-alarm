package com.zgy.goldmonitor.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.R;

public class AddAlarm1 extends LinearLayout {

	private View mManiContentView;
	private Context mContext;

	private Button mBtnNew;
	private Button mBtnIn;
	private Button mBtnOut;
//	private Button mBtnRate;
	private PopupAlarmView mPop;

	public AddAlarm1(Context context, PopupAlarmView pop) {
		super(context);
		this.mPop = pop;
		this.mContext = context;
		init();
	}

	private void init() {
		mManiContentView = LayoutInflater.from(mContext).inflate(R.layout.item_addalarm_1_checkid, null);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER;
		mManiContentView.setLayoutParams(lp);

		mBtnNew = (Button) mManiContentView.findViewById(R.id.btn_addalarm_1_new);
		mBtnIn = (Button) mManiContentView.findViewById(R.id.btn_addalarm_1_in);
		mBtnOut = (Button) mManiContentView.findViewById(R.id.btn_addalarm_1_out);
//		mBtnRate = (Button) mManiContentView.findViewById(R.id.btn_addalarm_1_rate);

		mBtnOut.setOnClickListener(mOnClickListener);
//		mBtnRate.setOnClickListener(mOnClickListener);
		mBtnNew.setOnClickListener(mOnClickListener);
		mBtnIn.setOnClickListener(mOnClickListener);

		if (Data.isInner(mPop.getmAlarmInfo().groupId)) {
//			mBtnRate.setVisibility(View.GONE);

			switch (mPop.getmAlarmInfo().checkId) {
			case -1:
				mPop.getmAlarmInfo().checkId = Data.Inner.CHECK_ID_NEW;
				break;
			case Data.Inner.CHECK_ID_IN:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				break;
			case Data.Inner.CHECK_ID_OUT:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				break;
			case Data.Inner.CHECK_ID_NEW:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				break;
			default:
				break;
			}

		} else {
//			mBtnRate.setVisibility(View.VISIBLE);
			switch (mPop.getmAlarmInfo().checkId) {
			case -1:
				mPop.getmAlarmInfo().checkId = Data.Outer.CHECK_ID_NEW;
				break;
			case Data.Outer.CHECK_ID_IN:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
//				mBtnRate.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				break;
			case Data.Outer.CHECK_ID_OUT:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_p);
//				mBtnRate.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				break;
			case Data.Outer.CHECK_ID_NEW:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
//				mBtnRate.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				break;
			case Data.Outer.CHECK_ID_RATE:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
//				mBtnRate.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				break;
			default:
				break;
			}
		}

		this.addView(mManiContentView);

	}

	private OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_addalarm_1_new:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
//				mBtnRate.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mPop.getmAlarmInfo().checkId = Data.isInner(mPop.getmAlarmInfo().groupId) ? Data.Inner.CHECK_ID_NEW : Data.Outer.CHECK_ID_NEW;
				if (mPop.getmViewPager() != null) {
					mPop.getmViewPager().setCurrentItem(mPop.getmViewPager().getCurrentItem() + 1, true);
				}
				break;
			case R.id.btn_addalarm_1_in:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_p);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
//				mBtnRate.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mPop.getmAlarmInfo().checkId = Data.isInner(mPop.getmAlarmInfo().groupId) ? Data.Inner.CHECK_ID_IN : Data.Outer.CHECK_ID_IN;
				if (mPop.getmViewPager() != null) {
					mPop.getmViewPager().setCurrentItem(mPop.getmViewPager().getCurrentItem() + 1, true);
				}
				break;
			case R.id.btn_addalarm_1_out:
				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_p);
//				mBtnRate.setBackgroundResource(R.drawable.bg_btn_selecte_n);
				mPop.getmAlarmInfo().checkId = Data.isInner(mPop.getmAlarmInfo().groupId) ? Data.Inner.CHECK_ID_OUT : Data.Outer.CHECK_ID_OUT;
				if (mPop.getmViewPager() != null) {
					mPop.getmViewPager().setCurrentItem(mPop.getmViewPager().getCurrentItem() + 1, true);
				}
				break;
//			case R.id.btn_addalarm_1_rate:
//				mBtnNew.setBackgroundResource(R.drawable.bg_btn_selecte_n);
//				mBtnIn.setBackgroundResource(R.drawable.bg_btn_selecte_n);
//				mBtnOut.setBackgroundResource(R.drawable.bg_btn_selecte_n);
//				mBtnRate.setBackgroundResource(R.drawable.bg_btn_selecte_p);
//				mPop.getmAlarmInfo().checkId = Data.Outer.CHECK_ID_RATE;
//				if (mPop.getmViewPager() != null) {
//					mPop.getmViewPager().setCurrentItem(mPop.getmViewPager().getCurrentItem() + 1, true);
//				}
//				break;

			default:
				break;
			}

		}
	};

}
