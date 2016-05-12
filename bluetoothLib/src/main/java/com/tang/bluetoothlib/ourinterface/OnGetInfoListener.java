/**
 * 
 */
package com.tang.bluetoothlib.ourinterface;

/**
 * @author 唐川
 *
 * @data 2016年3月13日
 * 
 * @description: 当收到蓝牙传来的数据时 回调
 */
public interface OnGetInfoListener {
	/**
	 * @param info 蓝牙传回的数据
	 * */
	void onGetInfo(String info);
}
