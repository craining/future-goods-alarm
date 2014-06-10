package com.zgy.goldmonitor.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zgy.goldmonitor.MainApp;

public class BitmapUtil {

	/**
	 * bitmap转换为二进制
	 * 
	 * @param @param bitmap
	 * @param @return 
	 * @date 2014-4-22
	 */
	public static byte[] getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * 根据一个网络连接获取bitmap图像
	 * 
	 * @param @param imageUri
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-4-22
	 */
	public static Bitmap getbitmap(String imageUri) {
		Log.v("", "getbitmap:" + imageUri);
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.v("", "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			Log.v("", "getbitmap bmp fail---");
			return null;
		}
		return bitmap;
	}

	/**
	 * 省内存的方式读取本地资源的图片
	 * 
	 * @Description:
	 * @param resId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 */
	public static Bitmap readBitMap(int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = MainApp.getInstance().getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 保存图片到相册
	 * 
	 * @param
	 */
	public static void saveToSDcard(String path, String title) {
		Bitmap bmp = BitmapFactory.decodeFile(ImageLoader.getInstance().getDiscCache().get(path).getPath());
		try {
			ContentResolver cr = MainApp.getInstance().getContentResolver();
			String url = MediaStore.Images.Media.insertImage(cr, bmp, title, "from fontwriter");
			Toast.makeText(MainApp.getInstance(), "图片已保存至相册", Toast.LENGTH_SHORT).show();
			// Debug.v("", "save url=" + url + "     file name =" + title);
			MainApp.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(url))); // 更新媒体库
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
