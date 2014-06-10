package com.zgy.goldmonitor.views;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.util.BitmapUtil;
import com.zgy.goldmonitor.util.ImageLoaderUtil;
import com.zgy.goldmonitor.util.TimeUtil;

/**
 * 展示大图的对话框。可双指缩放
 * 
 * @Author zhuanggy
 * @Date:2014-5-15
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version
 * @since
 */
public class PicShowDlg {

	 

	/**
	 * 展示某张大图
	 * 
	 * @param @param activity
	 * @param @param subject
	 * @param @param detail
	 * @param @param url
	 * @param @param time 
	 * @author zhuanggy
	 * @date 2014-5-15
	 */
	public static void showPic(Activity activity,  final String fileName, final String url) {

		final View dialog_view = activity.getLayoutInflater().inflate(R.layout.dlg_photo_zoom, null);
		final Dialog dialog = new Dialog(activity, R.style.custom_dialog_pic_zoom);
		dialog.setContentView(dialog_view);
		dialog.show();
		dialog_view.findViewById(R.id.save).setVisibility(View.GONE);
//		dialog_view.findViewById(R.id.save).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// 保存图片
//				BitmapUtil.saveToSDcard(url, fileName + "_" + url.hashCode());// TODO 文件名用什么？不重名，不存在特殊符号
//				// dialog.cancel();
//			}
//		});
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		params.width = dm.widthPixels;
		params.height = dm.heightPixels;
		dialog.getWindow().setAttributes(params);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog_view.findViewById(R.id.zoomin).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		ImageLoader.getInstance().displayImage(url, (ImageView) dialog_view.findViewById(R.id.zoomin), ImageLoaderUtil.getInstance().getNormalPicOpinions());

	}
}
