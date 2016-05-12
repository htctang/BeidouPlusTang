/**
 * 
 */
package com.tang.bluetoothlib.ourinterface;

/**
 * @author 唐川
 *
 * @data 2016年3月13日
 * 
 * @description: 监听错误的借口
 */
public interface OnErrorListener {
	
	/**
	 * 蓝牙服务器异常
	 * */
	 final static int SERVER_EXCEPTION = 0;
	 /**
	  * 没有连接
	  * */
	 final static int NOT_CONNECT = 1;
	 
	 /**
	  * 本机没有蓝牙设备
	  * 
	  * */
	 final static int NOT_HAVE_BLUETOOTH = 2;
	 /**
	  * ，没有开启蓝牙设备
	  * */
	 final static int NOT_OPEN_BLUETOOTH = 3;
	 /**
	  * 没有选择蓝牙设备
	  * */
	 final static int NOT_HAVE_CHOOSE_BLUETOOTH = 3;
	 /**
	  * 关闭失败
	  * */
	// final static int NOT_HAVE_SHUTDOWN = 3;
	/**
	 * @param1 errorStyle 错误状态码
	 * @param2 errorMessage 错误信息
	 * 
	 * */
	void onError(int errorStyle,String errorMssage);
}
