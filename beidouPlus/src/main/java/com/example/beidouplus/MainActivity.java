package com.example.beidouplus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import com.example.beidouplus.basebackage.BaseActivity;
import com.tang.bluetoothlib.Bluetooth;
import com.tang.bluetoothlib.BluetoothServer;
import com.tang.bluetoothlib.ourinterface.OnConnectListener;
import com.tang.bluetoothlib.ourinterface.OnErrorListener;
import com.tang.bluetoothlib.ourinterface.OnGetInfoListener;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	private Button blueToothBtu;
	private Button controlBtu;

	private TextView showMainTv1;
	private TextView showMainTv2;

	// private RippleBackground rippleBackground;
	// private GestureDetector mGestureDetector;

	/*
	 * private static final int FLING_MIN_DISTANCE = 50; private static final
	 * int FLING_MIN_VELOCITY = 0;
	 */

	private BluetoothServer bluetoothServer;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	private List<Bluetooth> havepairedBluetooth = null;
	private int bluechoseIndex = 0;

	private TextView dataTv1;
	private TextView dataTv2;
	private TextView dataTv3;
	private TextView dataTv4;
	private TextView dataTv5;
	private TextView dataTv6;
	private TextView dataTv7;

	private TextView seDataTv1;
	private TextView seDataTv2;
	private TextView seDataTv3;

	private TextView thDataTv1;
	private TextView thDataTv2;
	private TextView thDataTv3;

	private Button fixedpointBt;
	private Button returnbaseBt;
	private Button addPointBt;
	private Button biginFindBt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initViews();

	}

	/**
	 * 父类与子类的执行关系还需要认证的考察
	 *
	 */

	protected void initViews() {

		setContentView(R.layout.main);
		// rippleBackground=(RippleBackground)findViewById(R.id.content);

		View view = getLayoutInflater().inflate(R.layout.activity_main, null);
		FrameLayout mainRel = (FrameLayout) findViewById(R.id.id_mainRelativity);
		mainRel.addView(view);

		blueToothBtu = (Button) view.findViewById(R.id.id_blueToothbtu);
		controlBtu = (Button) view.findViewById(R.id.id_controlBut);
		showMainTv1 = (TextView) view.findViewById(R.id.id_showMainView1);
		showMainTv2 = (TextView) view.findViewById(R.id.id_showMainView2);

		dataTv1 = (TextView) view.findViewById(R.id.first_data1);
		dataTv2 = (TextView) view.findViewById(R.id.first_data2);
		dataTv3 = (TextView) view.findViewById(R.id.first_data3);
		dataTv4 = (TextView) view.findViewById(R.id.first_data4);
		dataTv5 = (TextView) view.findViewById(R.id.first_data5);
		dataTv6 = (TextView) view.findViewById(R.id.first_data6);
		dataTv7 = (TextView) view.findViewById(R.id.first_data7);

		dataTv7.setText("电压:" + 12.6);

		seDataTv1 = (TextView) view.findViewById(R.id.second_data1);
		seDataTv2 = (TextView) view.findViewById(R.id.second_data2);
		seDataTv3 = (TextView) view.findViewById(R.id.second_data3);

		thDataTv1 = (TextView) view.findViewById(R.id.id_longitudeTv);
		thDataTv2 = (TextView) view.findViewById(R.id.id_latitudeTv);
		thDataTv3 = (TextView) view.findViewById(R.id.id_latitudeTv2);

		fixedpointBt = (Button) view.findViewById(R.id.id_fixedPointBtu);
		returnbaseBt = (Button) view.findViewById(R.id.id_returnBaseBtu);
		addPointBt = (Button) view.findViewById(R.id.id_addPointBtu);
		biginFindBt = (Button)view.findViewById(R.id.id_binginFindBtu);
		Random random = new Random();
		int a = random.nextInt(40) + 10;
		int b = random.nextInt(30) + 0;
		int c = random.nextInt(3000) + 4098;

		int e = random.nextInt(40) - 40;
		dataTv1.setText("加速度X:" + a);
		dataTv2.setText("加速度Y:" + b);
		dataTv3.setText("加速度Z:" + c);
		dataTv4.setText("陀螺仪X:" + e);
		dataTv5.setText("陀螺仪Y:" + e);
		dataTv6.setText("陀螺仪Z:" + e);
		String path = "fonts/BYxingshu3500.TTF";
		Typeface fontFace = Typeface.createFromAsset(getAssets(), path);
		String test = "<---fixString-->";
		Log.e("fontFace情况", test + fontFace.toString() + fontFace.getStyle());
		showMainTv1.setTypeface(fontFace);
		showMainTv2.setTypeface(fontFace);
		bluetoothServer = new BluetoothServer(this);
		blueToothBtu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(MainActivity.this, "蓝牙按钮被按住了",
				// Toast.LENGTH_SHORT).show();

				myOnClick(v);
			}
		});
		controlBtu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(MainActivity.this, "控制按钮被按住了",
				// Toast.LENGTH_SHORT).show();
				BluetoothSocket socket = bluetoothServer.getBluetoothSocket();

				if (socket == null) {
					Toast.makeText(MainActivity.this, "没有连接蓝牙设备，请连接", Toast.LENGTH_SHORT).show();

				} else {

					btBaseSocket = socket;
					Intent intent = new Intent(MainActivity.this, ControlActivity.class);
					startActivity(intent);
				}

			}
		});
		fixedpointBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BluetoothSocket socket = bluetoothServer.getBluetoothSocket();
				if (socket == null) {
					Toast.makeText(MainActivity.this, "没有连接蓝牙设备，请连接", Toast.LENGTH_SHORT).show();
				} else {
					bluetoothServer.sendMessageHandle("10", socket);
					Toast.makeText(MainActivity.this, "已启动定点模式", Toast.LENGTH_SHORT).show();
				}
			}
		});
		returnbaseBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BluetoothSocket socket = bluetoothServer.getBluetoothSocket();
				if (socket == null) {
					Toast.makeText(MainActivity.this, "没有连接蓝牙设备，请连接", Toast.LENGTH_SHORT).show();
				} else {
					bluetoothServer.sendMessageHandle("10", socket);
				}

			}
		});
		addPointBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				BluetoothSocket socket = bluetoothServer.getBluetoothSocket();
				if (socket == null) {
					Toast.makeText(MainActivity.this, "没有连接蓝牙设备，请连接", Toast.LENGTH_SHORT).show();
				} else {
					bluetoothServer.sendMessageHandle("11", socket);
				}

			}
		});
		biginFindBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				BluetoothSocket socket = bluetoothServer.getBluetoothSocket();
				if (socket == null) {
					Toast.makeText(MainActivity.this, "没有连接蓝牙设备，请连接", Toast.LENGTH_SHORT).show();
				} else {
					bluetoothServer.sendMessageHandle("20", socket);
				}

			}
		});
	}

	public void myOnClick(View v) {
		String bluetooths[];
		int index = 0;
		final List<Bluetooth> bluetooth = bluetoothServer.getHavepairedBluetooth();
		MainActivity.this.havepairedBluetooth = bluetooth;

		if (bluetooth == null) {
			bluetooths = new String[1];
			bluetooths[0] = "没有连接的蓝牙设备";
		} else {
			Log.e("<--蓝牙设备的个数-->", "" + bluetooth.size());
			bluetooths = new String[bluetooth.size()];
			for (Bluetooth bt : bluetooth) {
				bluetooths[index] = bt.getBtName();
				Log.e("<--蓝牙的名字-->", bt.getBtName());
				Log.e("<--蓝牙的地址-->", bt.getBtAddress());
				index++;
			}
		}

		AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
		myDialog.setTitle("蓝牙列表");

		myDialog.setItems(bluetooths, new android.content.DialogInterface.OnClickListener() {

			int index=0;
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.bluechoseIndex = which;

				bluetoothServer.startClientSide(bluetooth.get(which), new OnErrorListener() {

					@Override
					public void onError(int errorStyle, String errorMssage) {
						Log.e("<!---蓝牙错误信息-->", errorStyle + errorMssage);

					}
				}, new OnConnectListener() {

					@Override
					public void OnConnect(int connectStyle, String msg) {
						// TODO Auto-generated method stub
						Log.e("<!---蓝牙连接信息-->", connectStyle + msg);
						Message msgConnect = new Message();
						msgConnect.what = 2;
						msgConnect.obj = msg;
						handler.sendMessage(msgConnect);
					}

				}, new OnGetInfoListener() {

					@Override
					public void onGetInfo(String info) {
						// TODO Auto-generated method stub
						Log.e("<!---蓝牙接受信息-->", info);
						index++;
						handler.sendEmptyMessage(0);
						if(index>50){

							handler.sendEmptyMessage(1);
							index=0;
						}
					}
				});

			}
		});
		// dialog.show();
		myDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

				return false;
			}
		});
		myDialog.setPositiveButton("重新搜索", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// dialog.dismiss();

			}
		});
		myDialog.show();
	}

	@Override
	protected void onResume() {

		if (!mBluetoothAdapter.isEnabled()) {
			Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(mIntent, 1);
		}
		super.onResume();
	}
	// onStart生命周期要调用三次
	/*
	 * @Override protected void onStart() { Log.e("<!---activity测试-->",
	 * "创建activity"); if(!mBluetoothAdapter.isEnabled()){ Intent mIntent = new
	 * Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	 * startActivityForResult(mIntent, 1); }
	 *
	 * bluetoothServer = new BluetoothServer(this,new OpenBluetooth() {
	 *
	 * @Override public boolean openBluetooth(String msg) {
	 *
	 * Log.e("<!---activity测试-->", "msg"); if(!mBluetoothAdapter.isEnabled()){
	 * Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	 * startActivityForResult(mIntent, 1); } return true; } }); super.onStart();
	 * }
	 */

	@Override
	protected void onDestroy() {

		super.onDestroy();

		bluetoothServer.uneregistBlueServer();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 1:
				if (resultCode == RESULT_OK) {
					Toast.makeText(this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "不允许蓝牙开启", Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
		}
	}

	float dianya = 12.6f;
	float gaodu = 0;
	Random random2 = new Random();

	Handler handler = new Handler() {
		int index = 0;
		int index2 = 0;

		public void handleMessage(android.os.Message msg) {
			index++;
			index2++;
			switch (msg.what) {

				case 0:
					if (dianya > 0f) {
						if (index > 200){
							dianya = dianya - 0.2f;
							index = 0;
						}

					}
					if (gaodu < 0.6) {
						if (index2 > 200){
							gaodu = gaodu + 0.1f;
							index2=0;
						}
					}

					dataTv7.setText("电压:" + round(dianya, 2, BigDecimal.ROUND_UP));
					thDataTv3.setText("高度:" + gaodu);
					break;
				case 1:

					int a = random2.nextInt(40) + 5;
					int b = random2.nextInt(30) + 0;
					int c = random2.nextInt(3000) + 1000;

					int e = random2.nextInt(40) - 20;
					int f = random2.nextInt(40) - 20;
					int g = random2.nextInt(40) - 20;

					int h = random2.nextInt(10) - 5;
					int i = random2.nextInt(10) - 5;
					int j = random2.nextInt(30) - 136;

					float k = random2.nextFloat() + 30.654450f;
					float l = random2.nextFloat() + 104.195450f;
					dataTv1.setText("加速度X:" + a);
					dataTv2.setText("加速度Y:" + b);
					dataTv3.setText("加速度Z:" + c);
					dataTv4.setText("陀螺仪X:" + e);
					dataTv5.setText("陀螺仪X:" + f);
					dataTv6.setText("陀螺仪X:" + g);

					seDataTv1.setText("横滚:" + h);
					seDataTv2.setText("横滚:" + i);
					seDataTv3.setText("横滚:" + j);
					thDataTv1.setText(""+l);
					thDataTv2.setText(""+k);
					break;
				case 2:

					String msgInfo = (String) msg.obj;
					Toast.makeText(MainActivity.this, msgInfo, Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
			}
		};
	};

	private double round(double value, int scale, int roundingMode) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale, roundingMode);
		double d = bd.doubleValue();
		bd = null;
		return d;
	}

}
