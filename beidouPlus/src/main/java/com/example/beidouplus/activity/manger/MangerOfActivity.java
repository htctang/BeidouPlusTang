/**
 * 
 */
package com.example.beidouplus.activity.manger;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;
/**
 * @author 唐川
 *
 * @data 2016年3月6日
 * 
 * @description: 
 */
public class MangerOfActivity {

	    private static  MangerOfActivity instance;
	    private Stack<Activity> activityStack;
	    private MangerOfActivity(){}
	    //鍗曚緥妯″紡
	    public  static MangerOfActivity getInstance(){
	        if(instance==null){
	            instance = new MangerOfActivity();
	        }
	        return instance;
	    }
	    //鎶婁竴涓猘ctivity鍘嬪叆鏍堜腑
	    public void pushOneActivity(Activity actvity) {
	        if (activityStack == null) {
	            activityStack = new Stack<Activity>();
	        }
	        activityStack.add(actvity);
	        Log.d("MyActivityManager ", "size = " + activityStack.size());
	    }
	    //鑾峰彇鏍堥《鐨刟ctivity锛屽厛杩涘悗鍑哄師鍒�
	    public Activity getLastActivity() {
	        return activityStack.lastElement();
	    }
	    //绉婚櫎涓�涓猘ctivity
	    public void popOneActivity(Activity activity) {
	        if (activityStack != null && activityStack.size() > 0) {
	            if (activity != null) {
	                activity.finish();
	                activityStack.remove(activity);
	                activity = null;
	            }
	        }
	    }
	    public void finishAllActivity() {
	        if (activityStack != null) {
	            while (activityStack.size() > 0) {
	                Activity activity = getLastActivity();
	                if (activity == null) break;
	                popOneActivity(activity);
	            }
	        }
	    }
	}
