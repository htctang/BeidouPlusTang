package com.example.beidouplus.myview;

import java.util.Random;

import com.example.beidouplus.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class FishView extends SurfaceView implements SurfaceHolder.Callback {
	final static float FISH_SPEED = 6;
	final static int FISH_ANGLE = 100;
	
	final static float START_FIASHX2 = 700;
	final static float START_FIASHY2 = 540;
	
	private float start_fiashx = 1088;
	private float start_fiashy = 888;
	
	private float start_fiashx2 = 1088;
	private float start_fiashy2 = 999;
	
	private SurfaceHolder holder;
	private UpdateViewThread updateThread;
	private boolean hasSurface;
	private Bitmap back;
	private Bitmap[] fishs;
	private Bitmap[] fishs2;
	private int fishIndex = 0; // 定义变量记录绘制第几张鱼的图片
	// 下面定义2个变量，记录鱼的初始位置
	private float fishX =start_fiashx;
	private float fishY = start_fiashy;
	
	private float fish2X = start_fiashx;//第二条鱼的起始位置
	private float fish2Y = start_fiashy;//
	
	private float fishSpeed = 6; // 鱼的游动速度
	private float fish2Speed = 6; // 鱼的游动速度
	
	private int outSceenPx = 0;
	// 定义鱼游动的角度
	private int fishAngle = new Random().nextInt(FISH_ANGLE);
	private int fish2Angle = new Random().nextInt(FISH_ANGLE);
	Matrix matrix = new Matrix();
	private int width;
	private int height;
	public FishView(Context ctx, AttributeSet set) {
		super(ctx, set);
		// 获取该SurfaceView对应的SurfaceHolder，并将该类的实例作为其Callback
		WindowManager wm = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
		 width = wm.getDefaultDisplay().getWidth();
	     height = wm.getDefaultDisplay().getHeight();
	     Log.e("当前屏幕的width", ""+width);
	     Log.e("当前屏幕的height", ""+height);
	     checkSceen(width, height);
		holder = getHolder();
		holder.addCallback(this);
		hasSurface = false;
		back = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.fishbg);
		fishs = new Bitmap[10];
		fishs2 = new Bitmap[10];
		// 初始化鱼游动动画的10张图片
		for (int i = 0; i < 10; i++) {
			try {
				int fishId = (Integer) R.drawable.class.getField("fish" + i).get(null);
				fishs[i] = BitmapFactory.decodeResource(ctx.getResources(), fishId);
				fishs2[i] = BitmapFactory.decodeResource(ctx.getResources(), fishId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void resume() {
		// 创建和启动图像更新线程
		if (updateThread == null) {
			updateThread = new UpdateViewThread();
			if (hasSurface == true)
				updateThread.start();
		}
	}

	public void pause() {
		// 停止图像更新线程
		if (updateThread != null) {
			updateThread.requestExitAndWait();
			updateThread = null;
		}
	}

	// 当SurfaceView被创建时回调该方法
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		hasSurface = true;
		resume();
	}

	// 当SurfaceView将要被销毁时回调该方法
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
		pause();
	}

	// 当SurfaceView发生改变时回调该方法
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (updateThread != null)
			updateThread.onWindowResize(w, h);
	}
	//根据当前屏幕的宽度调整当前 的某些参数
	public void checkSceen(int wedit,int height){
		start_fiashy = 250+new Random().nextInt(width-640);
		start_fiashy2 = 480+new Random().nextInt(width-480);
		if(wedit==1080){
			start_fiashx = 1080+8;
			start_fiashx2 = 1080+8;
			outSceenPx = -120;
		}else if(wedit==720){
			start_fiashx = 720+8;
			start_fiashx2 = 720+8;
			outSceenPx = -100;
		}
		
	}
	class UpdateViewThread extends Thread {
		// 定义一个记录图形是否更新完成的旗标
		private boolean done;

		UpdateViewThread() {
			super();
			done = false;
		}

		@Override
		public void run() {
			SurfaceHolder surfaceHolder = holder;
			// 重复绘图循环，直到线程停止 
			while (!done) {
				// 锁定SurfaceView，并返回到要绘图的Canvas
				Canvas canvas = surfaceHolder.lockCanvas(); // ①
				// 绘制背景图片
				RectF rectF = new RectF(0, 0, width, height);
				canvas.drawBitmap(back, null, rectF, null);//这句话 会有事报出 空指针异常  还需改动
				// 如果鱼“游出”屏幕之外，重新初始鱼的位置
				if (fishX < outSceenPx) {
					fishX = start_fiashx;
					fishY = start_fiashy;
					fishAngle = new Random().nextInt(FISH_ANGLE);
				}
				if (fishY < outSceenPx) {
					fishX = start_fiashx;
					fishY = start_fiashy;
					fishAngle = new Random().nextInt(FISH_ANGLE);

				}
				if (fish2X < outSceenPx) {
					fish2X = start_fiashx2;
					fishY =  start_fiashy2;
					fish2Angle = new Random().nextInt(FISH_ANGLE);
				}
				if (fish2Y < outSceenPx) {
					fish2X =  start_fiashx2;
					fish2Y =  start_fiashy2;
					fish2Angle = new Random().nextInt(FISH_ANGLE);
					
				}
				// 使用Matrix来控制鱼的旋转角度和位置
				matrix.reset();
				matrix.setRotate(fishAngle);
				Matrix matrix2 = new Matrix();
				matrix2.setRotate(fish2Angle);
				//移动图片
				matrix.postTranslate(fishX -= fishSpeed * Math.cos(Math.toRadians(fishAngle)),
						fishY -= fishSpeed * Math.sin(Math.toRadians(fishAngle)));
				canvas.drawBitmap(fishs[fishIndex++ % fishs.length], matrix, null);
				
				matrix2.postTranslate(fish2X -= fish2Speed * Math.cos(Math.toRadians(fish2Angle)),
						fish2Y -= fish2Speed * Math.sin(Math.toRadians(fish2Angle)));
				canvas.drawBitmap(fishs2[fishIndex++ % fishs2.length], matrix2, null);
				// 解锁Canvas，并渲染当前图像
				surfaceHolder.unlockCanvasAndPost(canvas); // ②
				try {
					Thread.sleep(60);
				} catch (InterruptedException e) {
				}
			}
		}

		public void requestExitAndWait() {
			// 把这个线程标记为完成，并合并到主程序线程 合并到主线程 就是等待
			done = true;
			try {
				join();
			} catch (InterruptedException ex) {
			}
		}

		public void onWindowResize(int w, int h) {
			// 处理SurfaceView的大小改变事件
		}
	}
}