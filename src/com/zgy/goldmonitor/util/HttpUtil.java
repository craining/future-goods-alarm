package com.zgy.goldmonitor.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;
import android.util.Log;

import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.bean.HttpRequestResult;

/**
 * http请求工具�? *
 * 
 * @Author zhuanggy
 * @Date:2014-1-7
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version
 * @since
 */
public class HttpUtil {

	private static final String TAG = "HttpUtil";

	private static final int CONNECTION_TIME_OUT = 10000;// 连接超时时间
	private static final int DATA_TIME_OUT = 10000;// 数据超时时间
	private static final String CHARSET = "GBK";//UTF-8

	private static HttpUtil mHttpUtil;

	private HttpUtil() {

	}

	public static HttpUtil getInstence() {
		if (mHttpUtil == null) {
			mHttpUtil = new HttpUtil();
		}

		return mHttpUtil;
	}

	/**
	 * 发�?请求
	 * 
	 * @Description:
	 * @param url
	 *            目标url
	 * @param params
	 *            参数
	 * @param isPost
	 *            是否为Post提交
	 * @return
	 */
	public HttpRequestResult sendRequest(String url, List<NameValuePair> params, boolean isPost) {
		HttpRequestResult result = new HttpRequestResult();
		try {
			if (isPost) {
				result = postMethod2(result, url, params);
			} else {
				result = getMethod(result, url, params);
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, " sendRequest " + url + " error:" + e.getMessage());
		} catch (ClientProtocolException e) {
			Log.e(TAG, " sendRequest " + url + " error:" + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, " sendRequest " + url + " error:" + e.getMessage());
		}

		return result;
	}

	/**
	 * 提交POST请求
	 * 
	 * @Description:
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	private HttpRequestResult postMethod2(HttpRequestResult requestResult, String url, List<NameValuePair> params) throws IOException {

		HttpPost post = new HttpPost(url);

		try {
			// 设置连接超时时间(单位毫秒)
			HttpConnectionParams.setConnectionTimeout(post.getParams(), CONNECTION_TIME_OUT);
			// 设置读数据超时时�?单位毫秒)
			HttpConnectionParams.setSoTimeout(post.getParams(), DATA_TIME_OUT);

			HttpEntity entity = new UrlEncodedFormEntity(params, CHARSET);
			post.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);
			requestResult.responseCode = response.getStatusLine().getStatusCode();
			Debug.v(TAG, "responseCode =" + requestResult.responseCode);
			requestResult.result = EntityUtils.toString(response.getEntity());

			return requestResult;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 提交GET请求，不用httpurlconnection是为了和4.0兼容
	 * 
	 * @Description:
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	private HttpRequestResult getMethod(HttpRequestResult requestResult, String url, List<NameValuePair> params) throws IOException {
		StringBuffer buffer = new StringBuffer();

		// 拼url
		if (params != null) {
			int i = 0;
			for (NameValuePair value : params) {
				buffer.append(i == 0 ? "?" : "&");
				buffer.append(value.getName());
				buffer.append("=");
				String v = value.getValue();
				// if (!StringUtil.isEmpty(v, true)) {
				if (!TextUtils.isEmpty(v)) {
					buffer.append(URLEncoder.encode(value.getValue(), CHARSET));
				}
				i++;
			}
		}

		HttpParams httpParams = new BasicHttpParams();
		// 设置超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIME_OUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpGet get = new HttpGet(url + buffer.toString());
		Debug.i(TAG, "REQUEST URL:" + url + buffer.toString());
		HttpResponse response = client.execute(get);

		String result = null;

		requestResult.responseCode = response.getStatusLine().getStatusCode();

		if (requestResult.responseCode == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			Header header = entity.getContentEncoding();
			if (header == null || !"gzip".equalsIgnoreCase(header.getValue())) {
				result = requestResult(entity.getContent(), false);
			} else {
				result = requestResult(entity.getContent(), true);
			}
			requestResult.result = result;
		}
		return requestResult;
	}

	/**
	 * 解析服务器返回数�? *
	 * 
	 * @Description:
	 * @param is
	 * @param isGzip
	 * @return
	 * @throws IOException
	 */
	private String requestResult(InputStream is, boolean isGzip) throws IOException {
		BufferedReader bufferedReader = null;
		StringBuilder builder = new StringBuilder();
		try {
			if (isGzip) {
				is = new GZIPInputStream(is);
			}
			bufferedReader = new BufferedReader(new InputStreamReader(is, CHARSET));
			// 读取服务器返回数据，转换成BufferedReader
			for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
				builder.append(s);
			}
		} finally {
			is.close();
			bufferedReader.close();
		}
		return builder.toString();
	}

}
