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
 * @author 唐川
 *
 * @data 2016年2月26日
 * 
 * @description: 蓝牙传输用的类 默认编码GBK
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

	/* 一些常量，代表服务器的名称 */
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
	 * 开启客户端端线程并且访问蓝牙服务端
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
	 * 开启服务端端线程并且访问蓝牙服务端
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

	// 开启客户端 客户端和服务端都需要读取数据
	/**
	 * 客户端的Socket是直接从UUID中获取 服务端的Socket是从BluetoothServiceSocket中获取
	 */
	@SuppressLint("NewApi")
	private class clientThread extends Thread {

		public void run() {
			try {
				// 创建一个Socket连接：只需要服务器在注册时的UUID号
				// socket =
				// device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
				device = mBluetoothAdapter.getRemoteDevice(havaChooseBluetooth.getBtAddress());
				if(device==null){
					errorListener.onError(OnErrorListener.NOT_HAVE_CHOOSE_BLUETOOTH, "没有选择蓝牙设备");
					try {
						join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //结束线程
				}
				socket = device
						.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				// 连接

				connectListener.OnConnect(OnConnectListener.CLIENT_CONNECT_ING,"请稍候，正在连接服务器…:");
				socket.connect();// 这里连上服务器如果连不上服务器将一直在线程中等待

				connectListener.OnConnect(OnConnectListener.CLIENT_CONNECT_ED,"已经连接上服务端！可以发送信息…");
				// 启动接受数据
				mreadThread = new readThread();
				mreadThread.start();
			} catch (IOException e) {
				Log.e("connect", "", e);
				/*
				 * Message msg = new Message(); msg.obj = "连接服务端异常！断开连接重新试一试。";
				 * msg.what = 0;
				 */
				errorListener.onError(OnErrorListener.SERVER_EXCEPTION, "连接服务端异常！断开连接重新试一试!");
			}
		}
	};

	// 开启服务器
	@SuppressLint("NewApi")
	private class ServerThread extends Thread {

		public void run() {

			try {
				/*
				 * 创建一个蓝牙服务器 参数分别：服务器名称、UUID
				 */
				mserverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
						UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

				Log.d("server", "wait cilent connect...");

				
				connectListener.OnConnect(OnConnectListener.SERVER_CONNECT_ING,"请稍候，正在等待客户端的连接...");

				
				socket = mserverSocket.accept();
				Log.d("server", "accept success !");

				connectListener.OnConnect(OnConnectListener.SERVER_CONNECT_ED,"客户端已经连接上！可以发送信息…");
				// 启动接受数据
				mreadThread = new readThread();
				mreadThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	/* 停止服务器 */

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
						mserverSocket.close();/* 关闭服务器 */
						mserverSocket = null;
					}
				} catch (IOException e) {
					Log.e("server", "mserverSocket.close()", e);
				}
			};
		}.start();
	}

	/* 停止客户端连接 */
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
						//errorListener.onError(OnErrorListener.NOT_HAVE_SHUTDOWN, "关闭失败");
					}
					socket = null;
				}
			};
		}.start();
	}

	@SuppressLint("NewApi")
	// 发送数据
	public void sendMessageHandle(String msg,BluetoothSocket socket) {
		if (socket == null) {
			/*Toast.makeText(context, "没有连接", Toast.LENGTH_SHORT).show();*/
			errorListener.onError(OnErrorListener.NOT_CONNECT,"没有连接,请重新连接");
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
			Toast.makeText(context, "socket为空,请连接服务器", Toast.LENGTH_SHORT).show();
			return null;
		}else{
			return socket;
		}
		
	}
	// 读取数据
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
                	list.add(new SiriListItem("没有发现蓝牙设备", false));
                	mAdapter.add("没有发现蓝牙设备");
                	mAdapter.notifyDataSetChanged();
            		mDeviceList.setSelection(list.size() - 1);
                }
                mBtuSearchDevice.setText("重新搜索");*/
            	parcelablebluetoothList=null;
            }
        }
    };	

}
