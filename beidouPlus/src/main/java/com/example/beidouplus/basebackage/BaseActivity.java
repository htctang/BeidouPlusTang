/**
 * 
 */
package com.example.beidouplus.basebackage;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.beidouplus.activity.manger.MangerOfActivity;

/**
 * @author �ƴ�
 *
 * @data 2016��3��5��
 * 
 * @description: 
 */
public abstract class BaseActivity extends Activity {
	
	//private GestureDetector mGestureDetector;
	//private RippleBackground rippleBackground=null;
	 /*private static final int FLING_MIN_DISTANCE = 50;     
	  private static final int FLING_MIN_VELOCITY = 0;  */
	protected static  BluetoothSocket btBaseSocket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE); //ȡ������
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN); //����ȫ��
		
		MangerOfActivity.getInstance().pushOneActivity(this);
		
		initViews();
	}
	
	
	@Override
	protected void onResume() {
		 /**
		  * ����Ϊ����
		  */
		 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 }
		 
		 super.onResume();
	}

	protected abstract void initViews();
	/*protected abstract void dealLeft();
	protected abstract void dealRight();*/
	
	/*public void setRippleBackground(RippleBackground rippleBackground){
		this.rippleBackground = rippleBackground;
	}*/
	
	/*GestureDetector.SimpleOnGestureListener myGesturListener = new GestureDetector.SimpleOnGestureListener(){
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Log.e("<--��������-->", "��ʼ����");
			float x = e1.getX()-e2.getX();
			float x2 = e2.getX()-e1.getX();
			if(x>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
				//  Toast.makeText(BaseActivity.this, "��������", Toast.LENGTH_SHORT).show();  
			
				  dealLeft();
			}else if(x2>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
				
				dealRight();
			}
			
			
			return false;
		};
	};
	
	public boolean onTouch(View v, MotionEvent event) {
			
		 if(rippleBackground!=null){
			 rippleBackground.startRippleAnimation();
		 }
		 return mGestureDetector.onTouchEvent(event); 
	}
	*/
	
	
	/**
     * Log.e
     * */
    public void printE(String string,String msg){
    	Log.e("<----"+string+"--->", msg);
    }
    /**
     * 
     * Log.d
     * */
    public void printD(String string,String msg){
    	Log.d("<----"+string+"--->", msg);
    }
    /**
     * log.v
     * */
    public void printV(String string,String msg){
    	Log.v("<----"+string+"--->", msg);
    }
   
	
}
