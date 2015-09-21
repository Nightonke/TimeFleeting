package com.timefleeting.app;

import java.text.AttributedCharacterIterator.Attribute;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

	public ObservableScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private ScrollViewListener scrollViewListener = null;
	
	public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle); 
		// TODO Auto-generated constructor stub
	}
	
	public ObservableScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
	
	public void setScrollViewListener(ScrollViewListener scrollViewListener) {  
        this.scrollViewListener = scrollViewListener;  
    }  
  
    @Override  
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {  
        super.onScrollChanged(x, y, oldx, oldy);  
        if (scrollViewListener != null) {  
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);  
        }  
    }
	
}
