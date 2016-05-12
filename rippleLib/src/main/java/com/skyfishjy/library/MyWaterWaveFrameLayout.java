/**
 * 
 */
package com.skyfishjy.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * @author ÌÆ´¨
 *
 * @data 2016Äê3ÔÂ12ÈÕ
 * 
 * @description: 
 */
public class MyWaterWaveFrameLayout extends FrameLayout {

	
	/**
	 * @param context
	 */
	public MyWaterWaveFrameLayout(Context context) {
		super(context);
		//waveList = Collections.synchronizedList(new ArrayList<Wave>());
		//this.context=context;
		WaterWave waterWave = new WaterWave(context);
		addView(waterWave);
	}
	
	
	
    
    public MyWaterWaveFrameLayout(Context context, AttributeSet attrs) {
       super(context, attrs);
      //waveList = Collections.synchronizedList(new ArrayList<Wave>());
      // this.context=context;
       WaterWave waterWave = new WaterWave(context);
       addView(waterWave);
    }

    public MyWaterWaveFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
      //  waveList = Collections.synchronizedList(new ArrayList<Wave>());
        //this.context=context;
        WaterWave waterWave = new WaterWave(context);
        addView(waterWave);
    }

    public MyWaterWaveFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
    	super(context, attrs, defStyleAttr, defStyleRes);
    	//waveList = Collections.synchronizedList(new ArrayList<Wave>());
    	//this.context=context;
    	WaterWave waterWave = new WaterWave(context);
    	addView(waterWave);
    } 


	
}


