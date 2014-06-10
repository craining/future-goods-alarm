package com.zgy.goldmonitor.bean;

import java.lang.reflect.Constructor;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * 消息页，选显卡
 * @Author zhuanggy
 * @Date:2014-4-11
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version 
 * @since
 */
public class ChartTabItem implements Parcelable {

	public int id;
	public int icon;
	public String name = null;
	public boolean hasTips = false;
	public String tipString = "";
	public Fragment fragment = null;
	public boolean notifyChange = false;
	@SuppressWarnings("rawtypes")
	public Class fragmentClass = null;

	@SuppressWarnings("rawtypes")
	public ChartTabItem(int id, String name, Class clazz) {
		this(id, name, 0, clazz);
	}

	@SuppressWarnings("rawtypes")
	public ChartTabItem(int id, String name, boolean hasTips, Class clazz) {
		this(id, name, 0, clazz);
		this.hasTips = hasTips;
	}

	@SuppressWarnings("rawtypes")
	public ChartTabItem(int id, String name, int iconid, Class clazz) {
		super();

		this.name = name;
		this.id = id;
		icon = iconid;
		fragmentClass = clazz;
	}

	public ChartTabItem(Parcel p) {
		this.id = p.readInt();
		this.name = p.readString();
		this.icon = p.readInt();
		this.notifyChange = p.readInt() == 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Fragment createFragment() {
		if (fragment == null) {
			Constructor constructor;
			try {
				constructor = fragmentClass.getConstructor(new Class[0]);
				fragment = (Fragment) constructor.newInstance(new Object[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fragment;
	}

	public static final Parcelable.Creator<ChartTabItem> CREATOR = new Parcelable.Creator<ChartTabItem>() {
		public ChartTabItem createFromParcel(Parcel p) {
			return new ChartTabItem(p);
		}

		public ChartTabItem[] newArray(int size) {
			return new ChartTabItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int flags) {
		p.writeInt(id);
		p.writeString(name);
		p.writeInt(icon);
		p.writeInt(notifyChange ? 1 : 0);
	}

}
