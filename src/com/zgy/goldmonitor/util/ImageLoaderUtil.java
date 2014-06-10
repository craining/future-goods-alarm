package com.zgy.goldmonitor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zgy.goldmonitor.R;

/**
 * 在此定义在线图片展示参数
 * 
 * @Date:2014-4-17
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version
 * @since
 */
public class ImageLoaderUtil {

	private DisplayImageOptions mOptionsNormalPic;// 一般图片
	private DisplayImageOptions mOptionsUserPortrait;// 头像

	private static ImageLoaderUtil instance;

	private ImageLoaderUtil() {

	}

	public static ImageLoaderUtil getInstance() {
		if (instance == null) {
			instance = new ImageLoaderUtil();
		}
		return instance;
	}

	/**
	 * 初始化配置
	 * 
	 * @param  
	 * @author zhuanggy
	 * @date 2014-4-17
	 */
	public void initConfig(Context context) {
		initConfigOpinions();
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		// @@@@@@@@@@@@@@@@@@@22//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(mOptionsNormalPic).discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		// @@@@@@@@@@@@@@@@@@@@@@//
		
		ImageLoader.getInstance().init(config);
		 
	}

	private void initConfigOpinions() {

		if (mOptionsNormalPic == null) {
			Options option = new Options();
			option.inSampleSize = 8;
			// inflater = activity.getLayoutInflater();

			mOptionsNormalPic = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_onlineimg_default_normal).showImageForEmptyUri(R.drawable.bg_onlineimg_null_normal)
					.showImageOnFail(R.drawable.bg_onlineimg_null_normal).decodingOptions(option).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
					// .cacheInMemory(true)
					.cacheOnDisc(true)
					// .displayer(new RoundedBitmapDisplayer(20))
					.build();
		}

		if (mOptionsUserPortrait == null) {
			Options option = new Options();
			option.inSampleSize = 8;
			// inflater = activity.getLayoutInflater();

			mOptionsUserPortrait = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_onlineimg_default_userportrait).showImageForEmptyUri(R.drawable.bg_onlineimg_default_userportrait)
					.showImageOnFail(R.drawable.bg_onlineimg_default_userportrait).decodingOptions(option).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
					// .cacheInMemory(true)
					.cacheOnDisc(true)
					// .displayer(new RoundedBitmapDisplayer(20))
					.build();
		}

	}

	/**
	 * 获得一般网络图片的加载参数
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-4-17
	 */
	public DisplayImageOptions getNormalPicOpinions() {
		if (mOptionsNormalPic == null) {
			initConfigOpinions();
		}
		return mOptionsNormalPic;
	}

	/**
	 * 获得头像图片的加载参数
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-4-17
	 */
	public DisplayImageOptions getUserPortraitPicOpinions() {
		if (mOptionsUserPortrait == null) {
			initConfigOpinions();
		}
		return mOptionsUserPortrait;
	}
}
