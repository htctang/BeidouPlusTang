/**
 * 
 */
package com.example.beidouplus.myview;

import org.rajawali3d.view.SurfaceView;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;

/**
 * @author ÌÆ´¨
 *
 * @data 2016Äê3ÔÂ12ÈÕ
 * 
 * @description: 
 */
public class MySurfaceView extends SurfaceView {

	/**
	 * @param context
	 */
	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	  public MySurfaceView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	
	@Override
	public void draw(Canvas canvas) {
		
		super.draw(canvas);
		canvas.drawColor(Color.BLUE,Mode.CLEAR);
	}
	
	

}
