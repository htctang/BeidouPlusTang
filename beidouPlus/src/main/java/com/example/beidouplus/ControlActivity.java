package com.example.beidouplus;

import org.rajawali3d.view.ISurface;
import org.rajawali3d.view.SurfaceView;

import com.example.beidouplus.basebackage.BaseActivity;
import com.example.beidouplus.myview.MyTestRenderer;
import com.tang.bluetoothlib.BluetoothServer;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class ControlActivity extends BaseActivity {

	private MyTestRenderer renderer;
	private Button forwardBtu;
	private Button downwardBtu;
	private Button leftBtu;
	private Button rightBtu;

	private Button takeoffBtu;
	private Button loadingBtu;
	private Button ultrasonicModeBt;

	private Button addLockBt;
	private Button canelLockBt;

	private BluetoothServer btServer;
	private BluetoothSocket btSocket;

	private boolean isFirst = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	protected void initViews() {




		setContentView(R.layout.activity_control);
		forwardBtu = (Button)findViewById(R.id.id_forwardBtu);
		downwardBtu = (Button)findViewById(R.id.id_downwardBtu);
		leftBtu = (Button)findViewById(R.id.id_leftBtu);
		rightBtu = (Button)findViewById(R.id.id_rightBtu);

		takeoffBtu = (Button)findViewById(R.id.id_takeOffBtu);
		loadingBtu = (Button)findViewById(R.id.id_landingBtu);
		ultrasonicModeBt = (Button)findViewById(R.id.id_ultrasonicModeBt);
		addLockBt = (Button)findViewById(R.id.id_addLockBtu);
		canelLockBt = (Button)findViewById(R.id.id_cancleLockBtu);



		btServer = new BluetoothServer(this);
		btSocket = btBaseSocket;

		Log.e("btSocket测试", btSocket.toString());
		SurfaceView surfaceView = (SurfaceView)findViewById(R.id.id_surfaceVeiwTest);
		surfaceView.setTransparent(true);
		//	surfaceView.setFrameRate(60.0); 如果要设置透明 不能有这句话
		surfaceView.setRenderMode(ISurface.RENDERMODE_WHEN_DIRTY);
		renderer = new MyTestRenderer(this);
		surfaceView.setSurfaceRenderer(renderer);


		forwardBtu.setOnClickListener(myClickLitnener);
		downwardBtu.setOnClickListener(myClickLitnener);
		leftBtu.setOnClickListener(myClickLitnener);
		rightBtu.setOnClickListener(myClickLitnener);
		takeoffBtu.setOnClickListener(myClickLitnener);
		loadingBtu.setOnClickListener(myClickLitnener);
		ultrasonicModeBt.setOnClickListener(myClickLitnener);
		addLockBt.setOnClickListener(myClickLitnener);
		canelLockBt.setOnClickListener(myClickLitnener);





	}


	OnClickListener myClickLitnener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			myButtonListener(v);

		}
	};
	public void myButtonListener(View view){
		switch (view.getId()) {
			case R.id.id_takeOffBtu:
				Log.e("<--起飞测试-->", "take off fffff");
				btServer.sendMessageHandle("7", btSocket);
				break;
			case R.id.id_landingBtu:

				Log.e("<--起飞测试-->", "take off fffff");
				btServer.sendMessageHandle("8", btSocket);
				break;
			case R.id.id_forwardBtu:

				Log.e("<--起飞测试-->", "take off fffff");
				btServer.sendMessageHandle("12", btSocket);
				break;
			case R.id.id_downwardBtu:

				Log.e("<--起飞测试-->", "take off fffff");
				btServer.sendMessageHandle("13", btSocket);
				break;
			case R.id.id_leftBtu:

				Log.e("<--起飞测试-->", "take off fffff");
				btServer.sendMessageHandle("14", btSocket);
				break;
			case R.id.id_rightBtu:

				Log.e("<--起飞测试-->", "take off fffff");
				btServer.sendMessageHandle("15", btSocket);
				break;
			case R.id.id_addLockBtu:

				Log.e("<--起飞测试-->", "take off fffff");
				btServer.sendMessageHandle("6", btSocket);
				break;
			case R.id.id_cancleLockBtu:

				btServer.sendMessageHandle("5", btSocket);
				break;
			case R.id.id_ultrasonicModeBt:

				btServer.sendMessageHandle("9", btSocket);
				break;

			default:
				break;
		}
	}



	protected void onDestroy() {

		super.onDestroy();
		btServer.uneregistBlueServer();
	}


}
