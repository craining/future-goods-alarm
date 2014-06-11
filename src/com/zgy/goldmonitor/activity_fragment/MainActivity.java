package com.zgy.goldmonitor.activity_fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.umeng.analytics.MobclickAgent;
import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.Preference;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.ViewPagerAdapterChart;
import com.zgy.goldmonitor.adapter.GridInfoAdapter;
import com.zgy.goldmonitor.bean.AlarmInfo;
import com.zgy.goldmonitor.bean.ChartTabItem;
import com.zgy.goldmonitor.bean.GoodAlarmInfo;
import com.zgy.goldmonitor.bean.Info;
import com.zgy.goldmonitor.bean.InfoList;
import com.zgy.goldmonitor.bean.PopupMenuItem;
import com.zgy.goldmonitor.logic.Controller;
import com.zgy.goldmonitor.logic.Listener;
import com.zgy.goldmonitor.util.ActivityManager;
import com.zgy.goldmonitor.util.FileUtil;
import com.zgy.goldmonitor.util.NetworkUtil;
import com.zgy.goldmonitor.util.PhoneUtil;
import com.zgy.goldmonitor.views.ChartTabIndicatorView;
import com.zgy.goldmonitor.views.CompatViewPager;
import com.zgy.goldmonitor.views.PopupAlarmView;
import com.zgy.goldmonitor.views.PopupMenuView;
import com.zgy.goldmonitor.views.PopupMenuView.OnPopupMenuItemClickedListener;
import com.zgy.goldmonitor.views.ToastView;

public class MainActivity extends FragmentActivity implements OnClickListener {

	public static final int ADD_RAISE = 0;
	public static final int ADD_LOW = 1;
	public static final int ADD_OR = 2;

	private int mAddType;

	private TextView mTextTitle;
	private TextView mTextInfo;
	private TextView mTextGroup;
	private TextView mTextRefresh;

	private TextView mTextAlarmAdd;
	private TextView mTextAlarmRaise;
	private TextView mTextAlarmLow;

	private PopupMenuView mItemMenu;
	private PopupMenuView mGroupMenu;
	private GridView mGridView;
	private ImageView mImgSetting;

	private AdView mAdView;
	
	public static int selectedItemId;
	public static int selectedGroupId;
	private GoodAlarmInfo mGoodAlarmInfo;

	public static ArrayList<String> mSelectedItems;

	public static Info mInfoShow;
	public static boolean mNeedRefreshAlarmsViews;

	private int mRaiseOrLow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		ActivityManager.push(this);

		initViews();
		initAdView();
		
		mRaiseOrLow = -1;
		Bundle b = getIntent().getExtras();
		if (b != null && b.containsKey("groupid")) {
			selectedGroupId = b.getInt("groupid");
			selectedItemId = b.getInt("itemid");
			mRaiseOrLow = b.getInt("raiselow");
			Debug.e("", "mRaiseOrLow=" + mRaiseOrLow);
		} else {
			selectedGroupId = Preference.getInstance().getLastViewGroupId();
			selectedItemId = Preference.getInstance().getLastViewItemId(selectedGroupId);
		}

		mTextGroup.setText(Data.Group_name[selectedGroupId]);
		mTextTitle.setText(Data.getItemNameCn(selectedGroupId, selectedItemId));
		initGroupMenu();
		initItemMenu();

		mSelectedItems = FileUtil.readArray(MainApp.getSelectedItemsFile(selectedGroupId));
		getAlarmsandRefresh();
		refreshData();
		// CheckLogic.getInstance().check(MainActivity.this);
		
		Debug.e("", "getDeviceInfo=" + PhoneUtil.getDeviceInfo(MainActivity.this));
		
		MobclickAgent.updateOnlineConfig(MainActivity.this);
	}

	
	
	private void initAdView() {
		mAdView.setListener(new AdViewListener() {

			@Override
			public void onVideoStart() {
			}

			@Override
			public void onVideoFinish() {
			}

			@Override
			public void onVideoError() {
			}

			@Override
			public void onVideoClickReplay() {
			}

			@Override
			public void onVideoClickClose() {
			}

			@Override
			public void onVideoClickAd() {
			}

			@Override
			public void onAdSwitch() {
				Debug.e("", "onAdSwitch");
			}

			@Override
			public void onAdShow(JSONObject arg0) {
				Debug.e("", "onAdShow");
				if (mAdView.getVisibility() == View.GONE) {
					mAdView.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onAdReady(AdView arg0) {
				Debug.e("", "onAdReady");
				if (mAdView.getVisibility() == View.GONE) {
					mAdView.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onAdFailed(String arg0) {
				Debug.e("", "onAdFailed");
				if (mAdView.getVisibility() == View.VISIBLE) {
					mAdView.setVisibility(View.GONE);
				}
			}

			@Override
			public void onAdClick(JSONObject arg0) {
				Debug.e("", "onAdClick");
			}
		});
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (mNeedRefreshAlarmsViews) {
			mNeedRefreshAlarmsViews = false;
			refreshAlarmsViews();
		}
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}



	public void getAlarmsandRefresh() {
		Controller.getInstace().refreshAlarms(mListener);
	}

	private void initItemMenu() {
		ArrayList<PopupMenuItem> items = new ArrayList<PopupMenuItem>();

		String[] itemss = Data.getItemNameCn(selectedGroupId);
		for (int i = 0; i < itemss.length; i++) {
			PopupMenuItem item = new PopupMenuItem();
			item.id = i;
			item.hasChild = false;
			item.itemText = itemss[i];
			item.parentId = -1;
			items.add(item);
		}

		mItemMenu = new PopupMenuView(MainActivity.this, items, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 3, new OnPopupMenuItemClickedListener() {

			@Override
			public void onPopuoMenuItemClicked(PopupMenuItem item) {
				selectedItemId = item.id;
				Preference.getInstance().setLastViewItemId(selectedGroupId, selectedItemId);
				refreshData();
				refreshChart();
				refreshAlarmsViews();
			}

		});
	}

	private void initGroupMenu() {

		ArrayList<PopupMenuItem> itemsGroup = new ArrayList<PopupMenuItem>();
		for (int i = 0; i < Data.Group_name.length; i++) {
			if (i != 3 && i != 4) {
				PopupMenuItem item = new PopupMenuItem();
				item.id = i;
				item.hasChild = false;
				item.itemText = Data.Group_name[i];
				item.parentId = -1;
				itemsGroup.add(item);
			}
		}

		mGroupMenu = new PopupMenuView(MainActivity.this, itemsGroup, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1, new OnPopupMenuItemClickedListener() {

			@Override
			public void onPopuoMenuItemClicked(PopupMenuItem item) {

				selectedGroupId = item.id;
				mTextGroup.setText(Data.Group_name[selectedGroupId]);
				Preference.getInstance().setLastViewGroupId(selectedGroupId);
				mSelectedItems = FileUtil.readArray(MainApp.getSelectedItemsFile(selectedGroupId));
				selectedItemId = Preference.getInstance().getLastViewItemId(selectedGroupId);
				refreshData();
				refreshChart();
				refreshAlarmsViews();
				initItemMenu();
			}

		});

	}

	private void refreshData() {
		mInfoShow = null;
		mTextInfo.setText("正在获取相关信息...");
		mTextInfo.setVisibility(View.VISIBLE);
		mGridView.setVisibility(View.INVISIBLE);
		mTextTitle.setText(Data.getItemNameCn(selectedGroupId, selectedItemId));
		Controller.getInstace().getData(Data.isInner(selectedGroupId), new String[] { Data.getItemNameEn(selectedGroupId, selectedItemId) }, mListener);
	}

	private void refreshChart() {
		((FragmentChartMin) mAdapterViewPager.getItem(0)).refreshPicChart();
		((FragmentChartDaly) mAdapterViewPager.getItem(1)).refreshPicChart();
		((FragmentChartWeek) mAdapterViewPager.getItem(2)).refreshPicChart();
		((FragmentChartMon) mAdapterViewPager.getItem(3)).refreshPicChart();
	}

	public void refreshAlarmsViews() {

		mGoodAlarmInfo = Controller.getGoodAlarmInfo(selectedGroupId, selectedItemId);
		mTextRefresh.setVisibility(View.VISIBLE);
		if (mGoodAlarmInfo.alarmLow != null && mGoodAlarmInfo.alarmRaise != null) {
			mTextAlarmLow.setVisibility(View.VISIBLE);
			mTextAlarmRaise.setVisibility(View.VISIBLE);
			mTextAlarmAdd.setVisibility(View.GONE);
		} else if (mGoodAlarmInfo.alarmLow != null && mGoodAlarmInfo.alarmRaise == null) {
			mTextAlarmLow.setVisibility(View.VISIBLE);
			mTextAlarmRaise.setVisibility(View.GONE);
			mTextAlarmAdd.setText("+涨提醒");
			mTextAlarmAdd.setVisibility(View.VISIBLE);
			mAddType = ADD_RAISE;
		} else if (mGoodAlarmInfo.alarmLow == null && mGoodAlarmInfo.alarmRaise != null) {
			mTextAlarmLow.setVisibility(View.GONE);
			mTextAlarmRaise.setVisibility(View.VISIBLE);
			mTextAlarmAdd.setText("+跌提醒");
			mTextAlarmAdd.setVisibility(View.VISIBLE);
			mAddType = ADD_LOW;
		} else {
			mTextAlarmLow.setVisibility(View.GONE);
			mTextAlarmRaise.setVisibility(View.GONE);
			mTextAlarmAdd.setVisibility(View.VISIBLE);
			mTextAlarmAdd.setText("+提醒");
			mAddType = ADD_OR;
		}

//		if (mRaiseOrLow != -1) {
//			try {
//
//				if (mRaiseOrLow == AlarmInfo.ALARM_LOW) {
//					if (mGoodAlarmInfo != null && mGoodAlarmInfo.alarmLow != null) {
//						PopupAlarmView view1 = new PopupAlarmView(MainActivity.this);
//						view1.showEdit(findViewById(R.id.layout_main_info), mGoodAlarmInfo.alarmLow, false, "编辑跌提醒");
//					}
//				} else {
//					if (mGoodAlarmInfo != null && mGoodAlarmInfo.alarmRaise != null) {
//						PopupAlarmView view1 = new PopupAlarmView(MainActivity.this);
//						view1.showEdit(findViewById(R.id.layout_main_info), mGoodAlarmInfo.alarmRaise, false, "编辑涨提醒");
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			mRaiseOrLow = -1;
//		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_main_title:
			mItemMenu.show(v);
			break;
		case R.id.text_main_group:
			mGroupMenu.show(v);
			break;
		case R.id.img_main_setting:
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			break;
		case R.id.text_main_refresh:
			if(!NetworkUtil.isNetworkAvailable(MainApp.getInstance())) {
				ToastView.showToast(MainActivity.this, "当前无网络，请稍后再试", ToastView.LENGTH_LONG);
				return;
			}
			
			refreshData();
			try {
				refreshChart();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.text_main_mark_add:
			showAlarmViews(v);
			break;
		case R.id.text_main_mark_raise:
			PopupAlarmView view1 = new PopupAlarmView(MainActivity.this);
			view1.showEdit(findViewById(R.id.layout_main_info), mGoodAlarmInfo.alarmRaise, false, ((TextView) v).getText().toString());
			break;
		case R.id.text_main_mark_low:
			PopupAlarmView view2 = new PopupAlarmView(MainActivity.this);
			view2.showEdit(findViewById(R.id.layout_main_info), mGoodAlarmInfo.alarmLow, false, ((TextView) v).getText().toString());

			break;

		default:
			break;
		}
	}

	private void showAlarmViews(View v) {
		PopupAlarmView view = new PopupAlarmView(MainActivity.this);
		switch (mAddType) {
		case ADD_LOW:
			mGoodAlarmInfo.alarmLow = new AlarmInfo();
			mGoodAlarmInfo.alarmLow.raseOrLow = AlarmInfo.ALARM_LOW;
			mGoodAlarmInfo.alarmLow.groupId = selectedGroupId;
			mGoodAlarmInfo.alarmLow.nameCn = Data.getItemNameCn(selectedGroupId, selectedItemId);
			mGoodAlarmInfo.alarmLow.nameEn = Data.getItemNameEn(selectedGroupId, selectedItemId);
			view.showEdit(findViewById(R.id.layout_main_info), mGoodAlarmInfo.alarmLow, true, ((TextView) v).getText().toString());
			break;
		case ADD_RAISE:
			mGoodAlarmInfo.alarmRaise = new AlarmInfo();
			mGoodAlarmInfo.alarmRaise.raseOrLow = AlarmInfo.ALARM_RAISE;
			mGoodAlarmInfo.alarmRaise.groupId = selectedGroupId;
			mGoodAlarmInfo.alarmRaise.nameCn = Data.getItemNameCn(selectedGroupId, selectedItemId);
			mGoodAlarmInfo.alarmRaise.nameEn = Data.getItemNameEn(selectedGroupId, selectedItemId);
			view.showEdit(findViewById(R.id.layout_main_info), mGoodAlarmInfo.alarmRaise, true, ((TextView) v).getText().toString());
			break;
		case ADD_OR:
			view.showAdd(findViewById(R.id.layout_main_info), selectedGroupId, selectedItemId, ((TextView) v).getText().toString());
			break;

		default:
			break;
		}

	}

	private Listener mListener = new Listener() {
		@Override
		public void getDataFinished(final boolean result, final InfoList data) {
			super.getDataFinished(result, data);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (result) {
						if (data.infos.containsKey(mTextTitle.getText().toString())) {
							mInfoShow = data.infos.get(mTextTitle.getText().toString());
							String[] items = null;
							if (Data.isInner(selectedGroupId)) {
								items = new String[mInfoShow.names.length - 3];
								items[0] = mInfoShow.names[0] + mInfoShow.values[0];
								for (int i = 2; i < items.length + 1; i++) {
									items[i - 1] = mInfoShow.names[i] + mInfoShow.values[i];
								}
							} else {
								items = new String[mInfoShow.names.length];
								for (int i = 0; i < items.length; i++) {
									items[i] = mInfoShow.names[i] + mInfoShow.values[i];
								}
							}

							mGridView.setVisibility(View.VISIBLE);
							GridInfoAdapter cla = new GridInfoAdapter(MainActivity.this, items);
							mGridView.setAdapter(cla);

							mTextInfo.setVisibility(View.GONE);
						}
						// if(mTextTitle.getText().toString().equals(object)) {
						// }
					} else {
						mTextInfo.setText("获取失败！");
					}

				}
			});

		}

		@Override
		public void updateAlarmsFinished(boolean result) {
			super.updateAlarmsFinished(result);
			if (result) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						refreshAlarmsViews();
					}
				});
			}
		}

	};

	@Override
	protected void onDestroy() {
		mChartTabs.clear();
		mChartTabs = null;
		mAdapterViewPager.notifyDataSetChanged();
		mAdapterViewPager = null;
		mViewPager.setAdapter(null);
		mViewPager = null;
		mIndicator = null;
		ActivityManager.pop(this);
		super.onDestroy();
	}

	// //////滑动菜单相关
	public static final int FRAGMENT_ONE = 0;
	public static final int FRAGMENT_TWO = 1;
	public static final int FRAGMENT_THREE = 2;
	public static final int FRAGMENT_FOUR = 3;

	// 存放选项卡信息的列表
	protected ArrayList<ChartTabItem> mChartTabs = new ArrayList<ChartTabItem>();
	protected ViewPagerAdapterChart mAdapterViewPager = null;
	protected CompatViewPager mViewPager;

	// 选项卡控件
	protected ChartTabIndicatorView mIndicator;

	public ChartTabIndicatorView getIndicator() {
		return mIndicator;
	}

	private final void initViews() {

		mTextGroup = (TextView) findViewById(R.id.text_main_group);
		mTextTitle = (TextView) findViewById(R.id.text_main_title);
		mTextInfo = (TextView) findViewById(R.id.text_main_info);
		mTextRefresh = (TextView) findViewById(R.id.text_main_refresh);
		mTextAlarmAdd = (TextView) findViewById(R.id.text_main_mark_add);
		mTextAlarmRaise = (TextView) findViewById(R.id.text_main_mark_raise);
		mTextAlarmLow = (TextView) findViewById(R.id.text_main_mark_low);

		mGridView = (GridView) findViewById(R.id.grid_main_info);
		mImgSetting = (ImageView) findViewById(R.id.img_main_setting);
		mViewPager = (CompatViewPager) findViewById(R.id.viewpager_message);
		mIndicator = (ChartTabIndicatorView) findViewById(R.id.pagerindicator_message);
		
		mAdView = (AdView) findViewById(R.id.addview_main);
		 
		mTextRefresh.setOnClickListener(this);
		mTextAlarmAdd.setOnClickListener(this);
		mTextAlarmRaise.setOnClickListener(this);
		mTextAlarmLow.setOnClickListener(this);
		mImgSetting.setOnClickListener(this);
		mTextTitle.setOnClickListener(this);
		mTextGroup.setOnClickListener(this);

		// 这里初始化界面
		mChartTabs = new ArrayList<ChartTabItem>();
		mChartTabs.add(new ChartTabItem(FRAGMENT_ONE, "分", FragmentChartMin.class));
		mChartTabs.add(new ChartTabItem(FRAGMENT_TWO, "天", FragmentChartDaly.class));
		mChartTabs.add(new ChartTabItem(FRAGMENT_THREE, "周", FragmentChartWeek.class));
		mChartTabs.add(new ChartTabItem(FRAGMENT_FOUR, "月", FragmentChartMon.class));

		mAdapterViewPager = new ViewPagerAdapterChart(this, getSupportFragmentManager(), mChartTabs);

		mViewPager.setAdapter(mAdapterViewPager);
		mViewPager.setOnPageChangeListener(mPageChangeListener);
		mViewPager.setOffscreenPageLimit(mChartTabs.size());// 默认全部加载

		mIndicator.init(0, mChartTabs, mViewPager);

		mViewPager.setCurrentItem(0);
	}

	/**
	 * view pager的滑动监听
	 */
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			mIndicator.onScrolled((mViewPager.getWidth() + mViewPager.getPageMargin()) * position + positionOffsetPixels);
		}

		@Override
		public void onPageSelected(int position) {
			mIndicator.onSwitched(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	};

	// protected ChartTabItem getMessageTabItemById(int tabId) {
	// if (mChartTabs == null || mChartTabs.size() < tabId + 2)
	// return null;
	// return mChartTabs.get(tabId);
	// }
	//
	// /**
	// * 获得所有选项卡
	// *
	// * @param @return 
	// * @author zhuanggy
	// * @date 2014-4-16
	// */
	// protected ArrayList<ChartTabItem> gtMessageTabItems() {
	// return mChartTabs;
	// }

	/**
	 * 跳转到任意选项卡
	 * 
	 * @param tabId
	 *            选项卡下标
	 */
	public void navigateTo(int tabId) {
		mViewPager.setCurrentItem(tabId);
	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// // for fix a known issue in support library
	// // https://code.google.com/p/android/issues/detail?id=19917
	// outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
	// super.onSaveInstanceState(outState);
	// }

}
