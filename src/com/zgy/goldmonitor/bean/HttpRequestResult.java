package com.zgy.goldmonitor.bean;

/**
 * 网络请求返回结果的bean
 * 
 * @Author zhuanggy
 * @Date:2014-4-14
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version
 * @since
 */
public class HttpRequestResult {

	public int responseCode;// 响应值
	public String result;// 返回结果

	/**
	 * 连接正常
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-4-14
	 */
	public boolean isConnectionOk() {
		// TODO 暂定返回值小于400时正常，0为服务器拒绝
		if (responseCode < 400 && responseCode > 0) {
			return true;
		}
		return false;

	}
}
