package com.zgy.goldmonitor.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.bean.AlarmInfo;

public class PopupAlarmView {

	/**
	 * 适配
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 */
	public class MyViewPagerAdapter extends PagerAdapter {

		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	// 实现

	private PopupWindow mPopWin;
	private Context mContext;
	private AlarmInfo mAlarmInfo;
	private ViewPager mViewPager;
	private boolean mIsAddNew;
	private boolean mNoSelectRaiseLow;

	public PopupAlarmView(Context ctx) {
		this.mContext = ctx;
	}
	public boolean ismNoSelectRaiseLow() {
		return mNoSelectRaiseLow;
	}
	public boolean ismIsAddNew() {
		return mIsAddNew;
	}

	public AlarmInfo getmAlarmInfo() {
		return mAlarmInfo;
	}

	public PopupWindow getmPopWin() {
		return mPopWin;
	}

	public ViewPager getmViewPager() {
		return mViewPager;
	}

	// public void show(View showAnchor, AlarmInfo info, int groupId, int itemId) {
	// if (info == null) {
	// showAdd(showAnchor, groupId, itemId);
	// } else {
	// showEdit(showAnchor, info);
	// }
	// }

	public void showAdd(View showAnchor, int groupId, int itemId, String title) {
		mNoSelectRaiseLow = true;
		mIsAddNew = true;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_popup_addalarm, null);
		((TextView) layout.findViewById(R.id.text_pop_addalarm_title_title)).setText(title);
		mViewPager = (ViewPager) layout.findViewById(R.id.viewpager_pop_addalarm);
		final TextView textTitle = (TextView) layout.findViewById(R.id.text_pop_addalarm_title);
		final ImageView imgPre = (ImageView) layout.findViewById(R.id.img_pop_addalarm_pre);
		final ImageView imgNext = (ImageView) layout.findViewById(R.id.img_pop_addalarm_next);
		imgPre.setVisibility(View.INVISIBLE);

		mPopWin = new PopupWindow(layout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

		imgNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

			}
		});
		imgPre.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			}
		});

		mAlarmInfo = new AlarmInfo();
		mAlarmInfo.nameEn = Data.getItemNameEn(groupId, itemId);
		mAlarmInfo.nameCn = Data.getItemNameCn(groupId, itemId);
		mAlarmInfo.groupId = groupId;
		final List<View> views = new ArrayList<View>();

		views.add(new AddAlarm0(mContext, this));
		views.add(new AddAlarm1(mContext, this));
		views.add(new AddAlarm2(mContext, this));
		views.add(new AddAlarm3(mContext, this));
		views.add(new AddAlarm4(mContext, this));
		mViewPager.setAdapter(new MyViewPagerAdapter(views));
		textTitle.setText("1、设置提醒类别");

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					imgPre.setVisibility(View.INVISIBLE);
					imgNext.setVisibility(View.VISIBLE);
					textTitle.setText("1、设置提醒类别");
					break;
				case 1:
					imgNext.setVisibility(View.VISIBLE);
					imgPre.setVisibility(View.VISIBLE);
					textTitle.setText("2、设置监听字段");
					break;
				case 2:
					imgNext.setVisibility(View.VISIBLE);
					imgPre.setVisibility(View.VISIBLE);
					textTitle.setText("3、设置被监听字段的基准值");
					break;
				case 3:
					imgNext.setVisibility(View.VISIBLE);
					imgPre.setVisibility(View.VISIBLE);
					textTitle.setText("4、设置被监听字段值的变化范围");
					break;
				case 4:
					((AddAlarm4) views.get(4)).refreshInfo();
					imgNext.setVisibility(View.INVISIBLE);
					imgPre.setVisibility(View.VISIBLE);
					textTitle.setText("5、完成设置");
					break;
				default:
					break;
				}
			}
		});

		// mPopWin.setAnimationStyle(R.style.menu_popupwindow_style);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(showAnchor, 5, 1);
		mPopWin.update();
	}

	public void showEdit(View showAnchor, AlarmInfo info, boolean addNew, String title) {
		mNoSelectRaiseLow = false;
		mIsAddNew = addNew;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_popup_addalarm, null);
		mViewPager = (ViewPager) layout.findViewById(R.id.viewpager_pop_addalarm);
		((TextView) layout.findViewById(R.id.text_pop_addalarm_title_title)).setText(title);
		final TextView textTitle = (TextView) layout.findViewById(R.id.text_pop_addalarm_title);
		final ImageView imgPre = (ImageView) layout.findViewById(R.id.img_pop_addalarm_pre);
		final ImageView imgNext = (ImageView) layout.findViewById(R.id.img_pop_addalarm_next);

		mPopWin = new PopupWindow(layout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

		imgNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

			}
		});
		imgPre.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			}
		});

		mAlarmInfo = info;
		final List<View> views = new ArrayList<View>();
		views.add(new AddAlarm1(mContext, this));
		views.add(new AddAlarm2(mContext, this));
		views.add(new AddAlarm3(mContext, this));
		views.add(new AddAlarm4(mContext, this));
		mViewPager.setAdapter(new MyViewPagerAdapter(views));
		if(mIsAddNew) {
			textTitle.setText("1、监听字段");
			mViewPager.setCurrentItem(0);
			imgPre.setVisibility(View.INVISIBLE);
			imgNext.setVisibility(View.VISIBLE);
		} else {
			imgPre.setVisibility(View.VISIBLE);
			imgNext.setVisibility(View.INVISIBLE);
			textTitle.setText("4、完成更改");
			mViewPager.setCurrentItem(views.size() - 1);
		}
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					imgPre.setVisibility(View.INVISIBLE);
					imgNext.setVisibility(View.VISIBLE);
					textTitle.setText("1、监听字段");
					break;
				case 1:
					imgNext.setVisibility(View.VISIBLE);
					imgPre.setVisibility(View.VISIBLE);
					textTitle.setText("2、被监听字段的基准值");
					break;
				case 2:
					imgNext.setVisibility(View.VISIBLE);
					imgPre.setVisibility(View.VISIBLE);
					textTitle.setText("3、被监听字段值变化范围");
					break;
				case 3:
					((AddAlarm4) views.get(3)).refreshInfo();
					imgNext.setVisibility(View.INVISIBLE);
					imgPre.setVisibility(View.VISIBLE);
					textTitle.setText("4、完成更改");
					break;
				default:
					break;
				}
			}
		});

		// mPopWin.setAnimationStyle(R.style.menu_popupwindow_style);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(showAnchor, 5, 1);
		mPopWin.update();
	}

	public void dismiss() {
		if (mPopWin.isShowing()) {
			mPopWin.dismiss();
		}
	}
}
