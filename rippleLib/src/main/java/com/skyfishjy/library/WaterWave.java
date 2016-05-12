/**
 * 
 */
package com.skyfishjy.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author �ƴ�
 *
 * @data 2016��3��11��
 * 
 * @description:
 */
public class WaterWave extends View {

/*	private Paint paint;
	private int alpha;
	private float radius;
	private float xDown;
	private float yDown;
	private float width;*/
	final int MAX_ALPHA = 255;
	
	/**
     * ���ε�List
     */
    private List<Wave> waveList;
    private boolean isStart = true;
	// private float xDown;
    private int[] colors = new int[]{
    		0xFF0000,0xFF9C00,0xFFFF00,0x00ff00,0x00ffff,0x0000ff,0xff00ff
    };

	/**
	 * @param context
	 */
	public WaterWave(Context context) {
		super(context);
	/*	alpha = 0;
		radius = 0;*/
		//initPaint();
		
		//�����̰߳�ȫ��list
		waveList = Collections.synchronizedList(new ArrayList<Wave>());
	}

	/**
	 * ȷ���ؼ��Ĵ�С��ʹ��Ĭ�ϵĿؼ���С
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		//canvas.drawCircle(xDown, yDown, radius, paint);
		 for (int i = 0; i < waveList.size(); i++) {
	            Wave wave = waveList.get(i);
	            canvas.drawCircle(wave.xDown, wave.yDown, wave.radius, wave.paint);
	        }
	}

	private Paint initPaint(int alpha,float width) {
		Paint paint = new Paint();
		paint.setStyle(paint.getStyle().STROKE);
		paint.setAntiAlias(true);// ?
		paint.setAlpha(alpha);
		paint.setColor(colors[(int) (Math.random() * (colors.length - 1))]);
		 paint.setStrokeWidth(width);
		return paint;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		super.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			 
	           
		case MotionEvent.ACTION_MOVE:
			 Wave wave = new Wave();
	            wave.radius = 0;
	            wave.alpha = MAX_ALPHA;
	            wave.width = wave.radius / 4;
	            wave.xDown = (int) event.getX();
	            wave.yDown = (int) event.getY();
	            wave.paint = initPaint(wave.alpha, wave.width);
	            if (waveList.size() == 0) {
	                isStart = true;
	            }
	           // System.out.println(isStart= + isStart);
	            waveList.add(wave);
	            // ���֮��ˢϴһ��ͼ��
	            invalidate();
	            if (isStart) {
	                handler.sendEmptyMessage(0);
	            }
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}

		return true;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				flushState();

				invalidate();

				if (waveList !=null&&waveList.size()>0) {
					// ���͸����û�е�0�������ˢ�£���tֹͣˢ��
					handler.sendEmptyMessageDelayed(0, 50);
				}

				break;

			default:
				break;
			}
		}

		/**
		 * ˢ��״̬
		 */
		private void flushState() {
			 for (int i = 0; i < waveList.size(); i++) {
		            Wave wave = waveList.get(i);
		            if (isStart == false && wave.alpha == 0) {
		                waveList.remove(i);
		                wave.paint = null;
		                wave = null;
		                continue;
		            } else if (isStart == true) {
		                isStart = false;
		            }
		            wave.radius += 5;
		            wave.alpha -= 10;
		            if (wave.alpha < 0) {
		                wave.alpha = 0;
		            }
		            wave.width = wave.radius / 4;
		            wave.paint.setAlpha(wave.alpha);
		            wave.paint.setStrokeWidth(wave.width);
		        }
		}

	};

	private class Wave {
		int waveX;
		int waveY;
		/**
		 * ������ʾԲ���İ뾶
		 */
		float radius;
		Paint paint;
		/**
		 * ���µ�ʱ��x����
		 */
		int xDown;
		/**
		 * ���µ�ʱ��y������
		 */
		int yDown;
		float width;
		int alpha;
	}
}
