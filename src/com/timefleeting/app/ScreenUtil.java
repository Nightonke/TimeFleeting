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
	 * ��ȡ��Ļ�ĸ߶�
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
	 * ��ȡ��Ļ�пؼ�����λ�õĸ߶�--���ؼ�������Y��
	 * 
	 * @return
	 */
	public static int getScreenViewTopHeight(View view) {
		return view.getTop();
	}

	/**
	 * ��ȡ��Ļ�пؼ��ײ�λ�õĸ߶�--���ؼ��ײ���Y��
	 * 
	 * @return
	 */
	public static int getScreenViewBottomHeight(View view) {
		return view.getBottom();
	}

	/**
	 * ��ȡ��Ļ�пؼ�����λ��--���ؼ�����X��
	 * 
	 * @return
	 */
	public static int getScreenViewLeftHeight(View view) {
		return view.getLeft();
	}

	/**
	 * ��ȡ��Ļ�пؼ��Ҳ��λ��--���ؼ��Ҳ��X��
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
