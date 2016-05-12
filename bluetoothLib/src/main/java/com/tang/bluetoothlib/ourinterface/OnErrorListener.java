/**
 * 
 */
package com.tang.bluetoothlib.ourinterface;

/**
 * @author �ƴ�
 *
 * @data 2016��3��13��
 * 
 * @description: ��������Ľ��
 */
public interface OnErrorListener {
	
	/**
	 * �����������쳣
	 * */
	 final static int SERVER_EXCEPTION = 0;
	 /**
	  * û������
	  * */
	 final static int NOT_CONNECT = 1;
	 
	 /**
	  * ����û�������豸
	  * 
	  * */
	 final static int NOT_HAVE_BLUETOOTH = 2;
	 /**
	  * ��û�п��������豸
	  * */
	 final static int NOT_OPEN_BLUETOOTH = 3;
	 /**
	  * û��ѡ�������豸
	  * */
	 final static int NOT_HAVE_CHOOSE_BLUETOOTH = 3;
	 /**
	  * �ر�ʧ��
	  * */
	// final static int NOT_HAVE_SHUTDOWN = 3;
	/**
	 * @param1 errorStyle ����״̬��
	 * @param2 errorMessage ������Ϣ
	 * 
	 * */
	void onError(int errorStyle,String errorMssage);
}
