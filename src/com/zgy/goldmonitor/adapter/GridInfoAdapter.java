package com.zgy.goldmonitor.adapter;

import java.io.File;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.activity_fragment.MainActivity;
import com.zgy.goldmonitor.util.FileUtil;
import com.zgy.goldmonitor.views.ToastView;

public class GridInfoAdapter extends BaseAdapter {

	private Context context;
	private String[] items;

	public GridInfoAdapter(Context context, String[] item) {
		this.context = context;
		this.items = item;
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder = new ViewHolder();

		convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_info, null);

		viewHolder.mNameTextView = (TextView) convertView.findViewById(R.id.text_gird_item);
		viewHolder.mNameTextView.setText(items[position]);
		if (MainActivity.mSelectedItems != null && MainActivity.mSelectedItems.contains("" + position)) {
			viewHolder.mNameTextView.setBackgroundResource(R.drawable.bg_grid_info_item_p);
		} else {
			viewHolder.mNameTextView.setBackgroundResource(R.drawable.selecter_grid_info_item_bg);
		}
		try {
			if (!Data.isInner(MainActivity.selectedGroupId) && position == 1) {
				String v = items[position].substring(4, items[position].length());
				if (Float.parseFloat(v) - 0 > 0) {
					viewHolder.mNameTextView.setTextColor(Color.RED);
				} else if (Float.parseFloat(v) - 0 < 0) {
					viewHolder.mNameTextView.setTextColor(Color.rgb(0, 128, 0));
				} else {
					viewHolder.mNameTextView.setTextColor(Color.BLACK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		viewHolder.mNameTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MainActivity.mSelectedItems == null || MainActivity.mSelectedItems.size() == 0) {
					MainActivity.mSelectedItems.add(position + "");
					ToastView.showToast(context, "已加关注", ToastView.LENGTH_SHORT);
				} else {
					if (MainActivity.mSelectedItems.contains(position + "")) {
						MainActivity.mSelectedItems.remove(position + "");
						ToastView.showToast(context, "已取消关注", ToastView.LENGTH_SHORT);
					} else {
						MainActivity.mSelectedItems.add(position + "");
						ToastView.showToast(context, "已加关注", ToastView.LENGTH_SHORT);
					}
				}
				FileUtil.writeArray(MainActivity.mSelectedItems, MainApp.getSelectedItemsFile(MainActivity.selectedGroupId));

				// ((GridInfoAdapter) mGridView.getAdapter()).
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	private class ViewHolder {
		public TextView mNameTextView;
	}

}
