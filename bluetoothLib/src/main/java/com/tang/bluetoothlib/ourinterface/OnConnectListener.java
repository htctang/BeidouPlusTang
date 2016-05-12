/**
 * 
 */
package com.tang.bluetoothlib.ourinterface;

/**
 * @author �ƴ�
 *
 * @data 2016��3��13��
 * 
 * @description: ���������ǵ�״̬ eg������������
 */
public interface OnConnectListener {
	
	/**
	 * �ͻ�����������
	 * */
	final static int CLIENT_CONNECT_ING = 0;
	/**
	 * �ͻ������ӳɹ�
	 * */
	final static int CLIENT_CONNECT_ED = 1;
	/**
	 * �������������
	 * 
	 * */
	final static int SERVER_CONNECT_ING = 2;
	/**
	 * ��������ӳɹ�
	 * 
	 * */
	final static int SERVER_CONNECT_ED = 3;
	
	/**
	 * @param1 connectStyle ���ӵ�״̬��
	 * @param2 msg ������Ϣ
	 * */
	void OnConnect(int connectStyle,String msg);
}
