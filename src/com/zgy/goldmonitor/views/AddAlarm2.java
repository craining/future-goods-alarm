package com.zgy.goldmonitor.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.activity_fragment.MainActivity;

public class AddAlarm2 extends LinearLayout {

	private View mManiContentView;
	private Context mContext;

	private TextView mTextShow;

	private Button mBtnC;
	private Button mBtnDot;
	private Button mBtn0;
	private Button mBtn1;
	private Button mBtn2;
	private Button mBtn3;
	private Button mBtn4;
	private Button mBtn5;
	private Button mBtn6;
	private Button mBtn7;
	private Button mBtn8;
	private Button mBtn9;

	private PopupAlarmView mPop;
	private boolean mChanged;

	public AddAlarm2(Context context, PopupAlarmView pop) {
		super(context);
		this.mPop = pop;
		this.mContext = context;
		init();
	}

	private void init() {
		mManiContentView = LayoutInflater.from(mContext).inflate(R.layout.item_addalarm_2_changevalue, null);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER;
		mManiContentView.setLayoutParams(lp);

		mTextShow = (TextView) mManiContentView.findViewById(R.id.text_addalarm_2_show);
		mBtnC = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_c);
		mBtnDot = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_dot);
		mBtn0 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_0);
		mBtn1 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_1);
		mBtn2 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_2);
		mBtn3 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_3);
		mBtn4 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_4);
		mBtn5 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_5);
		mBtn6 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_6);
		mBtn7 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_7);
		mBtn8 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_8);
		mBtn9 = (Button) mManiContentView.findViewById(R.id.btn_addalarm_2_9);

		mBtnC.setOnClickListener(mOnClickListener);
		mBtnDot.setOnClickListener(mOnClickListener);
		mBtn0.setOnClickListener(mOnClickListener);
		mBtn1.setOnClickListener(mOnClickListener);
		mBtn2.setOnClickListener(mOnClickListener);
		mBtn3.setOnClickListener(mOnClickListener);
		mBtn4.setOnClickListener(mOnClickListener);
		mBtn5.setOnClickListener(mOnClickListener);
		mBtn6.setOnClickListener(mOnClickListener);
		mBtn7.setOnClickListener(mOnClickListener);
		mBtn8.setOnClickListener(mOnClickListener);
		mBtn9.setOnClickListener(mOnClickListener);

		if (mPop.getmAlarmInfo().markValue - 0 > 0) {
			mTextShow.setText(mPop.getmAlarmInfo().markValue + "");
		}
//		} else {
//			if (MainActivity.mInfoShow != null) {
//				mTextShow.setText(MainActivity.mInfoShow.values[mPop.getmAlarmInfo().checkId]);
//				mPop.getmAlarmInfo().markValue = Float.parseFloat(mTextShow.getText().toString() + "");
//			}
//		}

		this.addView(mManiContentView);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		Debug.e("", "onLayout");

		if (mPop != null && mPop.getmAlarmInfo() != null && mPop.ismIsAddNew() &&  !mChanged && MainActivity.mInfoShow != null) {
			mTextShow.setText(MainActivity.mInfoShow.values[mPop.getmAlarmInfo().checkId]);
			mPop.getmAlarmInfo().markValue = Float.parseFloat(mTextShow.getText().toString() + "");
		}
//		if (mPop != null && mPop.getmAlarmInfo() != null && mPop.getmAlarmInfo().markValue - 0 > 0) {
//			mTextShow.setText(mPop.getmAlarmInfo().markValue + "");
//		} else {
//			if (MainActivity.mInfoShow != null && !mChanged) {
//				mTextShow.setText(MainActivity.mInfoShow.values[mPop.getmAlarmInfo().checkId]);
//				mPop.getmAlarmInfo().markValue = Float.parseFloat(mTextShow.getText().toString() + "");
//			}
//		}
	}

	private OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			mChanged = true;
			
			if (v.getId() == R.id.btn_addalarm_2_c) {
				mTextShow.setText("");
				mPop.getmAlarmInfo().markValue = 0;
				return;
			}

			String preStr = mTextShow.getText().toString();

			if (!TextUtils.isEmpty(preStr)) {
				if (preStr.contains(".")) {
					int id = preStr.indexOf(".");
					if (preStr.length() - id > 5) {

						return;
					}
				}
			}
			
			switch (v.getId()) {
			case R.id.btn_addalarm_2_dot:
				if (TextUtils.isEmpty(preStr)) {
					mTextShow.setText("0.");
				} else if (preStr.contains(".")) {

				} else {
					mTextShow.setText(preStr + ".");
				}

				break;
			case R.id.btn_addalarm_2_0:
				mTextShow.setText(preStr + "0");
				break;
			case R.id.btn_addalarm_2_1:
				mTextShow.setText(preStr + "1");
				break;
			case R.id.btn_addalarm_2_2:
				mTextShow.setText(preStr + "2");
				break;
			case R.id.btn_addalarm_2_3:
				mTextShow.setText(preStr + "3");
				break;
			case R.id.btn_addalarm_2_4:
				mTextShow.setText(preStr + "4");
				break;
			case R.id.btn_addalarm_2_5:
				mTextShow.setText(preStr + "5");
				break;
			case R.id.btn_addalarm_2_6:
				mTextShow.setText(preStr + "6");
				break;
			case R.id.btn_addalarm_2_7:
				mTextShow.setText(preStr + "7");
				break;
			case R.id.btn_addalarm_2_8:
				mTextShow.setText(preStr + "8");
				break;
			case R.id.btn_addalarm_2_9:
				mTextShow.setText(preStr + "9");
				break;

			default:
				break;
			}

			mPop.getmAlarmInfo().markValue = Float.parseFloat(mTextShow.getText().toString() + "");
		}
	};

}
