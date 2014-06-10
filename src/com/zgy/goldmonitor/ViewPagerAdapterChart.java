package com.zgy.goldmonitor;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.zgy.goldmonitor.bean.ChartTabItem;

/**
 * 消息页view pager适配器
 * @Author zhuanggy
 * @Date:2014-4-16
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version 
 * @since
 */

public class ViewPagerAdapterChart extends FragmentPagerAdapter {
	private ArrayList<ChartTabItem> tabs = null;
	private Context context = null;

	public ViewPagerAdapterChart(Context context, FragmentManager fm, ArrayList<ChartTabItem> tabs) {
		super(fm);
		this.tabs = tabs;
		this.context = context;
	}

	@Override
	public Fragment getItem(int pos) {
		Fragment fragment = null;
		if (tabs != null && pos < tabs.size()) {
			ChartTabItem tab = tabs.get(pos);
			if (tab == null)
				return null;
			fragment = tab.createFragment();
		}
		return fragment;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		if (tabs != null && tabs.size() > 0)
			return tabs.size();
		return 0;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ChartTabItem tab = tabs.get(position);
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		tab.fragment = fragment;
		return fragment;
	}
}
