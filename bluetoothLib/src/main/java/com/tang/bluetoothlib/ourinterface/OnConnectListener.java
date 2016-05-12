/**
 * 
 */
package com.tang.bluetoothlib.ourinterface;

/**
 * @author 唐川
 *
 * @data 2016年3月13日
 * 
 * @description: 监听连接是的状态 eg：正在连接中
 */
public interface OnConnectListener {
	
	/**
	 * 客户端正在连接
	 * */
	final static int CLIENT_CONNECT_ING = 0;
	/**
	 * 客户端连接成功
	 * */
	final static int CLIENT_CONNECT_ED = 1;
	/**
	 * 服务端正在连接
	 * 
	 * */
	final static int SERVER_CONNECT_ING = 2;
	/**
	 * 服务端连接成功
	 * 
	 * */
	final static int SERVER_CONNECT_ED = 3;
	
	/**
	 * @param1 connectStyle 连接的状态吗
	 * @param2 msg 连接信息
	 * */
	void OnConnect(int connectStyle,String msg);
}
