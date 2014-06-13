package com.zgy.goldmonitor.logic;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.MainApp;

public class PlatformLogic {

	private static PlatformLogic instance;

	private PlatformLogic() {
	}

	public static PlatformLogic getInstance() {
		if (instance == null) {
			instance = new PlatformLogic();
		}
		return instance;
	}

	   

	/**
	 * 分享到新浪微博
	 * 
	 * @param @param title
	 * @param @param content
	 * @param @param img
	 * @param @param imgLocalOrOnline
	 * @param @param listener 
	 * @author zhuanggy
	 * @date 2014-4-25
	 */
	public void shareToSina(String title, String content, String img, boolean imgLocalOrOnline) {

		Platform platform = ShareSDK.getPlatform(MainApp.getInstance(), SinaWeibo.NAME);
		platform.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				arg2.printStackTrace();
			}

			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
			}
		});

		int shareType = Platform.SHARE_TEXT;

		ShareParams sp = new ShareParams();
		sp.setText(content);
		sp.setTitle(title);
		if (!TextUtils.isEmpty(img)) {
			shareType = Platform.SHARE_IMAGE;
			if (imgLocalOrOnline) {
				sp.setImagePath(img);
			} else {
				sp.setImageUrl(img);
			}
		}
		sp.setShareType(shareType);
//		platform.SSOSetting(true);//暂不使用sso方式绑定，签名未设置正确
	 
		platform.share(sp);
	}

	/**
	 * 此分享操作用tencent_open_sdk.jar类库
	 * 
	 * @param @param activity
	 * @param @param title
	 * @param @param content
	 * @param @param img
	 * @param @param imgLocalOrOnline
	 * @param @param jumpUrl
	 * @param @param listener 
	 * @author zhuanggy
	 * @date 2014-4-25
	 */
	public void shareToQzone(final Activity activity, String title, String content, String img, boolean imgLocalOrOnline, String jumpUrl) {
		int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_NO_TYPE;

		final Bundle params = new Bundle();

		if (!TextUtils.isEmpty(img)) {
			shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
			if (!imgLocalOrOnline) {
				// 支持传多个imageUrl
				ArrayList<String> imageUrls = new ArrayList<String>();
				imageUrls.add(img);
				params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
			} else {
				//本地image TODO

			}
		}
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, jumpUrl);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tencent.createInstance("1101501719", MainApp.getInstance()).shareToQzone(activity, params, new IUiListener() {

					@Override
					public void onCancel() {
					}

					@Override
					public void onError(UiError arg0) {
						Debug.e("", "onError");
						Debug.e("arg0.errorCode", arg0.errorCode + "");
						Debug.e("arg0.errorDetail", arg0.errorDetail + "");
						Debug.e("arg0.errorMessage", arg0.errorMessage + "");
					}

					@Override
					public void onComplete(Object arg0) {
						Debug.e("", "onComplete");
						Debug.e("arg0.toString", ((JSONObject) arg0).toString());
					}

				});
			}
		}).start();
	}

	
	public void shareToQzone2() {
		ShareParams sp = new ShareParams();
		sp.setTitle("测试分享的标题");
		sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
		sp.setText("测试分享的文本");
		sp.setImageUrl("http://www.baidu.com/img/baidu_sylogo1.gif");
		sp.setSite("发布分享的网站名称");
		sp.setSiteUrl("发布分享网站的地址");

		Platform qzone = ShareSDK.getPlatform (MainApp.getInstance(), QZone.NAME);
//		qzone. setPlatformActionListener (paListener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}
	
	
	public static final String WECHAT_SHARE_TYPE_FRIEND = "Wechat";//微信好友
	public static final String WECHAT_SHARE_TYPE_MOMENT = "WechatMoments";//微信朋友圈
//	public static final String WECHAT_SHARE_TYPE_FAVOURITE = "WechatFavorite"; //微信收藏
	
	/**
	 * 分享到微信
	 * @param @param type   {@link #WECHAT_SHARE_TYPE_FAVOURITE} OR {@link #WECHAT_SHARE_TYPE_MOMENT} OR {@link #WECHAT_SHARE_TYPE_MOMENT}
	 * @param @param title
	 * @param @param content
	 * @param @param img
	 * @param @param imgLocalOrOnline
	 * @param @param listener 
	 * @author zhuanggy
	 * @date 2014-5-20
	 */
	public void shareToWeChat(String type, String title, String content, String img, boolean imgLocalOrOnline) {
		
		Platform platform = ShareSDK.getPlatform(MainApp.getInstance(), type);
		platform.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				arg2.printStackTrace();
			}

			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
			}
		});

		int shareType = Platform.SHARE_TEXT;

		ShareParams sp = new ShareParams();
		sp.setText(content);
		sp.setTitle(title);
		if (!TextUtils.isEmpty(img)) {
			shareType = Platform.SHARE_IMAGE;
			if (imgLocalOrOnline) {
				sp.setImagePath(img);
			} else {
				sp.setImageUrl(img);
			}
		}
		sp.setShareType(shareType);
		platform.SSOSetting(false);
		platform.share(sp);
	}
	
	
}
