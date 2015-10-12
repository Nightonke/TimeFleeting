package com.timefleeting.app;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;

public class Util {

	static private float darkerCoefficient = (float)0.8;
	
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}
	
	public static int darkerColor(int color, int darkerDegree) {
		int a = Color.alpha(color);
	    int r = Color.red(color);
	    int g = Color.green(color);
	    int b = Color.blue(color);

	    double darkerC = Math.pow(darkerCoefficient, darkerDegree);
	    
	    return Color.argb(a,
	            Math.max((int)(r * darkerC), 0),
	            Math.max((int)(g * darkerC), 0),
	            Math.max((int)(b * darkerC), 0));
	}

}
