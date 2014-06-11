package com.zgy.goldmonitor.activity_fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.dao.DbOpera;
import com.zgy.goldmonitor.logic.Controller;
import com.zgy.goldmonitor.util.ActivityManager;
import com.zgy.goldmonitor.views.ToastView;

@SuppressLint("NewApi")
public class AlertActivity extends Activity implements OnClickListener {

	private int mGroupId = 0;
	private int mItemId = 0;

	private String mNameEn;
	private int mRaiseLow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alert);

		ActivityManager.push(this);

		if(android.os.Build.VERSION.SDK_INT>=11) {
			this.setFinishOnTouchOutside(false);
		}
		 
		Button btnIn = (Button) findViewById(R.id.btn_alert_positiveButton);
		Button btnDelete = (Button) findViewById(R.id.btn_alert_negativeButton);
		Button btnIgnore = (Button) findViewById(R.id.btn_alert_ignore);
		TextView text = (TextView) findViewById(R.id.text_alert_message);
		TextView textTitle = (TextView) findViewById(R.id.text_title_alert);

		Bundle b = getIntent().getExtras();
		if (b == null) {
			finish();
			return;
		}

		mGroupId = b.getInt("groupid");
		mItemId = b.getInt("itemid");
		mNameEn = b.getString("nameen");
		mRaiseLow = b.getInt("raiselow");
		String content = b.getString("content");
		String title = b.getString("title");
		textTitle.setText("" + title);
		text.setText("" + content);

		btnIn.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnIgnore.setOnClickListener(this);

		Debug.e("", "alert on craete");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_alert_positiveButton:
			// 进入详情
			ActivityManager.popAll();
			Intent i = new Intent(AlertActivity.this, MainActivity.class);
			i.putExtra("groupid", mGroupId);
			i.putExtra("itemid", mItemId);
			i.putExtra("raiselow", mRaiseLow);
			startActivity(i);
			finish();
			break;
		case R.id.btn_alert_negativeButton:
			// 删除提醒
			if (DbOpera.getInstance().deleteAlarm(mNameEn, mRaiseLow)) {
				MainActivity.mNeedRefreshAlarmsViews = true;
				Controller.getInstace().refreshAlarms(null);
				ToastView.showToast(AlertActivity.this, "提醒已删除！", ToastView.LENGTH_LONG);
				finish();
			} else {
				ToastView.showToast(AlertActivity.this, "提醒删除失败，请稍后再试", ToastView.LENGTH_LONG);
			}
			break;
		case R.id.btn_alert_ignore:
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
	
	

}
