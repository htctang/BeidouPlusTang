/**
 * 
 */
package com.tang.bluetoothlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.tang.bluetoothlib.ourinterface.OnConnectListener;
import com.tang.bluetoothlib.ourinterface.OnErrorListener;
import com.tang.bluetoothlib.ourinterface.OnGetInfoListener;
import com.tang.bluetoothlib.ourinterface.OpenBluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

/**
 * @author �ƴ�
 *
 * @data 2016��2��26��
 * 
 * @description: ���������õ��� Ĭ�ϱ���GBK
 * 
 */
@SuppressLint("NewApi")
public class BluetoothServer{

	private BluetoothServerSocket mserverSocket = null;
	private ServerThread startServerThread = null;
	private clientThread clientConnectThread = null;
	private BluetoothSocket socket = null;
	private BluetoothDevice device = null;
	private readThread mreadThread = null;;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	/* һЩ��������������������� */
	public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
	public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
	public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";

	private Context context;
	//private Handler mHandler;

	private OnConnectListener connectListener;
	private OnErrorListener errorListener;
	private OnGetInfoListener getInfoListener;
	

	private List<Bluetooth> bluetoothList = new ArrayList<Bluetooth>();
	private List<Bluetooth> parcelablebluetoothList = new ArrayList<Bluetooth>();
	private Bluetooth havaChooseBluetooth;
	//private Set<BluetoothDevice> pairedDevices;
	/**
	 * 
	 */
	public BluetoothServer(Context context) {
		
		this.context = context;
		
		 IntentFilter discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        context.registerReceiver(mReceiver, discoveryFilter);

	        // Register for broadcasts when discovery has finished
	        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        context.registerReceiver(mReceiver, foundFilter);
	        
	        //pairedDevices = mBluetoothAdapter.getBondedDevices();
	}

	public void uneregistBlueServer(){
		context.unregisterReceiver(mReceiver);
	}
	
	/**
	 * �����ͻ��˶��̲߳��ҷ������������
	 * 
	 * 
	 */
	public void startClientSide(Bluetooth bluetooth,OnErrorListener errorListener,OnConnectListener connectListener,OnGetInfoListener getInfoListener) {
		this.errorListener = errorListener;
		this.connectListener = connectListener;
		this.getInfoListener=getInfoListener;
		this.havaChooseBluetooth = bluetooth;
		clientConnectThread = new clientThread();
		clientConnectThread.start();
		
	}
	/**
	 * ��������˶��̲߳��ҷ������������
	 * 
	 * 
	 */
	public void startServeSide(OnErrorListener errorListener,OnConnectListener connectListener,OnGetInfoListener getInfoListener) {
		this.errorListener = errorListener;
		this.connectListener = connectListener;
		this.getInfoListener=getInfoListener;
		startServerThread = new ServerThread();
		startServerThread.start();
		
	}

	// �����ͻ��� �ͻ��˺ͷ���˶���Ҫ��ȡ����
	/**
	 * �ͻ��˵�Socket��ֱ�Ӵ�UUID�л�ȡ ����˵�Socket�Ǵ�BluetoothServiceSocket�л�ȡ
	 */
	@SuppressLint("NewApi")
	private class clientThread extends Thread {

		public void run() {
			try {
				// ����һ��Socket���ӣ�ֻ��Ҫ��������ע��ʱ��UUID��
				// socket =
				// device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
				device = mBluetoothAdapter.getRemoteDevice(havaChooseBluetooth.getBtAddress());
				if(device==null){
					errorListener.onError(OnErrorListener.NOT_HAVE_CHOOSE_BLUETOOTH, "û��ѡ�������豸");
					try {
						join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //�����߳�
				}
				socket = device
						.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				// ����

				connectListener.OnConnect(OnConnectListener.CLIENT_CONNECT_ING,"���Ժ��������ӷ�������:");
				socket.connect();// �������Ϸ�������������Ϸ�������һֱ���߳��еȴ�

				connectListener.OnConnect(OnConnectListener.CLIENT_CONNECT_ED,"�Ѿ������Ϸ���ˣ����Է�����Ϣ��");
				// ������������
				mreadThread = new readThread();
				mreadThread.start();
			} catch (IOException e) {
				Log.e("connect", "", e);
				/*
				 * Message msg = new Message(); msg.obj = "���ӷ�����쳣���Ͽ�����������һ�ԡ�";
				 * msg.what = 0;
				 */
				errorListener.onError(OnErrorListener.SERVER_EXCEPTION, "���ӷ�����쳣���Ͽ�����������һ��!");
			}
		}
	};

	// ����������
	@SuppressLint("NewApi")
	private class ServerThread extends Thread {

		public void run() {

			try {
				/*
				 * ����һ������������ �����ֱ𣺷��������ơ�UUID
				 */
				mserverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
						UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

				Log.d("server", "wait cilent connect...");

				
				connectListener.OnConnect(OnConnectListener.SERVER_CONNECT_ING,"���Ժ����ڵȴ��ͻ��˵�����...");

				
				socket = mserverSocket.accept();
				Log.d("server", "accept success !");

				connectListener.OnConnect(OnConnectListener.SERVER_CONNECT_ED,"�ͻ����Ѿ������ϣ����Է�����Ϣ��");
				// ������������
				mreadThread = new readThread();
				mreadThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	/* ֹͣ������ */

	public void shutdownServer() {
		new Thread() {
			public void run() {
				if (startServerThread != null) {
					startServerThread.interrupt();
					startServerThread = null;
				}
				if (mreadThread != null) {
					mreadThread.interrupt();
					mreadThread = null;
				}
				try {
					if (socket != null) {
						socket.close();
						socket = null;
					}
					if (mserverSocket != null) {
						mserverSocket.close();/* �رշ����� */
						mserverSocket = null;
					}
				} catch (IOException e) {
					Log.e("server", "mserverSocket.close()", e);
				}
			};
		}.start();
	}

	/* ֹͣ�ͻ������� */
	public void shutdownClient() {
		new Thread() {
			public void run() {
				if (clientConnectThread != null) {
					clientConnectThread.interrupt();
					clientConnectThread = null;
				}
				if (mreadThread != null) {
					mreadThread.interrupt();
					mreadThread = null;
				}
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//errorListener.onError(OnErrorListener.NOT_HAVE_SHUTDOWN, "�ر�ʧ��");
					}
					socket = null;
				}
			};
		}.start();
	}

	@SuppressLint("NewApi")
	// ��������
	public void sendMessageHandle(String msg,BluetoothSocket socket) {
		if (socket == null) {
			/*Toast.makeText(context, "û������", Toast.LENGTH_SHORT).show();*/
			errorListener.onError(OnErrorListener.NOT_CONNECT,"û������,����������");
			return;
		}
		try {
			OutputStream os = socket.getOutputStream();
			os.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public BluetoothSocket getBluetoothSocket(){
		if(socket==null){
			Toast.makeText(context, "socketΪ��,�����ӷ�����", Toast.LENGTH_SHORT).show();
			return null;
		}else{
			return socket;
		}
		
	}
	// ��ȡ����
	private class readThread extends Thread {
		public void run() {

			byte[] buffer = new byte[1024];
			int bytes;
			InputStream mmInStream = null;

			try {
				mmInStream = socket.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (true) {
				try {
					// Read from the InputStream
					if ((bytes = mmInStream.read(buffer)) > 0) {
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						String s = new String(buf_data);
						
						getInfoListener.onGetInfo(s);
					}
				} catch (IOException e) {
					try {
						mmInStream.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			}
		}
	}
	public List<Bluetooth> getHavepairedBluetooth(){
		
	        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
	        if(bluetoothList.size()!=0){
	        	   bluetoothList.clear();
	        }
	     
		if (pairedDevices.size() > 0) {
	            for (BluetoothDevice device : pairedDevices) {
	            	//list.add(new SiriListItem(device.getName() + "\n" + device.getAddress(), true));
	            	Bluetooth bluetooth = new Bluetooth();
	            	bluetooth.setBtName(device.getName());
	            	bluetooth.setBtAddress(device.getAddress());
	            	bluetoothList.add(bluetooth);
	            }
	            }else{
	            	bluetoothList=null;
	            }
		return bluetoothList;
	}
	public List<Bluetooth> getParcelableBluetooth(){
		return parcelablebluetoothList;
	}
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) 
            {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) 
                {
                	/*list.add(new SiriListItem(device.getName() + "\n" + device.getAddress(), false));
                	mAdapter.add(device.getName() + "\n" + device.getAddress());
                	mAdapter.notifyDataSetChanged();
            		mDeviceList.setSelection(list.size() - 1);*/
                	Bluetooth bluetooth = new Bluetooth();
                	bluetooth.setBtName(device.getName());
                	bluetooth.setBtAddress(device.getAddress());
                	parcelablebluetoothList.add(bluetooth);
                	
                }
            // When discovery is finished, change the Activity title
            } 
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) 
            {
               /* setProgressBarIndeterminateVisibility(false);
                if (mDeviceList.getCount() == 0) 
                {
                	list.add(new SiriListItem("û�з��������豸", false));
                	mAdapter.add("û�з��������豸");
                	mAdapter.notifyDataSetChanged();
            		mDeviceList.setSelection(list.size() - 1);
                }
                mBtuSearchDevice.setText("��������");*/
            	parcelablebluetoothList=null;
            }
        }
    };	

}
