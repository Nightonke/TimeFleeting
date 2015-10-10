package com.timefleeting.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class TopBottomScrollView extends ScrollView {

	private OnScrollToBottomListener onScrollToBottom;
	private OnScrollToTopListener onScrollToTop;
	
	public TopBottomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopBottomScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if(scrollY > 0 && clampedY && null != onScrollToBottom){
			onScrollToBottom.onScrollBottomListener(clampedY);
		} else if (scrollY <= 0 && null != onScrollToTop) {
			onScrollToTop.onScrollTopListener(clampedY);
		}
	}
	
	public void setOnScrollToBottomLintener(OnScrollToBottomListener listener) {
		onScrollToBottom = listener;
	}
	
	public void setOnScrollToTopLintener(OnScrollToTopListener listener) {
		onScrollToTop = listener;
	}

	public interface OnScrollToBottomListener {
		public void onScrollBottomListener(boolean isBottom);
	}
	
	public interface OnScrollToTopListener {
		public void onScrollTopListener(boolean isTop);
	}
}
