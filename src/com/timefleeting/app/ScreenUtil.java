package com.timefleeting.app;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ScreenUtil {

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
//		Display display = manager.getDefaultDisplay();
//		return display.getWidth();
		
		DisplayMetrics dm = new DisplayMetrics();     
		manager.getDefaultDisplay().getMetrics(dm);     
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
//		Display display = manager.getDefaultDisplay();
//		return display.getHeight();
		
		DisplayMetrics dm = new DisplayMetrics();     
		manager.getDefaultDisplay().getMetrics(dm);     
		return dm.heightPixels;
	}

	/**
	 * 获取屏幕中控件顶部位置的高度--即控件顶部的Y点
	 * 
	 * @return
	 */
	public static int getScreenViewTopHeight(View view) {
		return view.getTop();
	}

	/**
	 * 获取屏幕中控件底部位置的高度--即控件底部的Y点
	 * 
	 * @return
	 */
	public static int getScreenViewBottomHeight(View view) {
		return view.getBottom();
	}

	/**
	 * 获取屏幕中控件左侧的位置--即控件左侧的X点
	 * 
	 * @return
	 */
	public static int getScreenViewLeftHeight(View view) {
		return view.getLeft();
	}

	/**
	 * 获取屏幕中控件右侧的位置--即控件右侧的X点
	 * 
	 * @return
	 */
	public static int getScreenViewRightHeight(View view) {
		return view.getRight();
	}
	
	public static int getTotalHeightofListView(ListView listView) {  
	    ListAdapter mAdapter = listView.getAdapter();   
	    if (mAdapter == null) {  
	        return -1;  
	    }  
	    int totalHeight = 0;  
	    for (int i = 0; i < mAdapter.getCount(); i++) {  
	        View mView = mAdapter.getView(i, null, listView);  
	        mView.measure(  
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),  
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
	        //mView.measure(0, 0);  
	        totalHeight += mView.getMeasuredHeight();  
	    }
	    return totalHeight + listView.getDividerHeight() * (mAdapter.getCount() - 1);    
	}  
	
}
