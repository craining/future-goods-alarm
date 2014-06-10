package com.zgy.goldmonitor.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.bean.PopupMenuItem;

public class PopupMenuView {
	/**
	 * 点击回调
	 * 
	 * @Author zhuanggy
	 * @Date:2014-5-26
	 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
	 * @version PopupMenu
	 * @since
	 */
	public interface OnPopupMenuItemClickedListener {
		public void onPopuoMenuItemClicked(PopupMenuItem item);

	}

	/**
	 * 列表适配
	 * 
	 * @Author zhuanggy
	 * @Date:2014-5-26
	 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
	 * @version PopupMenuView
	 * @since
	 */
	public class PopupMenuListAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<PopupMenuItem> itemList;

		public PopupMenuListAdapter(Context context, ArrayList<PopupMenuItem> item) {
			this.context = context;
			this.itemList = item;
		}

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.item_popupmenu, null);

			viewHolder.mNameTextView = (TextView) convertView.findViewById(R.id.text_gird_item);
			viewHolder.mNameTextView.setText(itemList.get(position).itemText);

			viewHolder.mNameTextView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PopupMenuItem itemClicked = itemList.get(position);
					mPopWin.dismiss();
					if (mListener != null) {
						mListener.onPopuoMenuItemClicked(itemClicked);
					}
				}
			});
			return convertView;
		}

		private class ViewHolder {
			public TextView mNameTextView;
		}

	}

	// 实现

	private ArrayList<PopupMenuItem> mItems;
	private OnPopupMenuItemClickedListener mListener;
	private PopupWindow mPopWin;
	private Context mContext;
	private int w;
	private int h;
	private int numCloumn;

	public PopupMenuView(Context ctx, ArrayList<PopupMenuItem> items, int w, int h, int numCloumn, OnPopupMenuItemClickedListener listener) {
		this.mItems = items;
		this.mContext = ctx;
		this.mListener = listener;
		this.numCloumn = numCloumn;
		this.w = w;
		this.h = h;

	}

	/**
	 * 显示分级菜单
	 * 
	 * @param @param width
	 * @param @param height
	 * @param @param showAnchor 
	 * @author zhuanggy
	 * @date 2014-5-26
	 */
	public void show(View showAnchor) {
		if (mItems == null || mItems.size() <= 0) {
			return;// TODO
		}

		LinearLayout layout;

		layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_popupmenu, null);

		GridView grid = (GridView) layout.findViewById(R.id.grid_popupmenu);

		grid.setNumColumns(numCloumn);

		PopupMenuListAdapter cla = new PopupMenuListAdapter(mContext, mItems);
		grid.setAdapter(cla);
		cla.notifyDataSetChanged();

		// mPopWin = new PopupWindow(layout, width, height * 2 / 3, true);
		mPopWin = new PopupWindow(layout, w, h, true);
//		mPopWin.setAnimationStyle(R.style.menu_popupwindow_style);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(showAnchor, 5, 1);
		mPopWin.update();
//		grid.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				PopupMenuItem itemClicked = (PopupMenuItem) ((PopupMenuListAdapter) parent.getAdapter()).getItem(position);
//				mPopWin.dismiss();
//				if (mListener != null) {
//					mListener.onPopuoMenuItemClicked(itemClicked);
//				}
//
//			}
//		});
	}

	public void dismiss() {
		if (mPopWin.isShowing()) {
			mPopWin.dismiss();
		}
	}

	/**
	 * 获得某父Item的子Item列表
	 * 
	 * @param @param parentId
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-5-26
	 */
	private ArrayList<PopupMenuItem> getChildItems(int parentId) {
		ArrayList<PopupMenuItem> childitemList = new ArrayList<PopupMenuItem>();
		for (PopupMenuItem item : mItems) {
			if (item.parentId == parentId) {
			}
			childitemList.add(item);
		}

		return childitemList;
	}

	private View.OnClickListener mLayoutClicked = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.e("", "on Clicked");
			if (mPopWin.isShowing()) {
				mPopWin.dismiss();
			}
		}
	};
}
