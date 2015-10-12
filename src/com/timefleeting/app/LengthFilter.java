package com.timefleeting.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

public class LengthFilter implements InputFilter { 

	int MAX_EN; // ���Ӣ��/���ֳ��� һ��������������ĸ
	String regEx = "[\\u4e00-\\u9fa5]";  // unicode���룬�ж��Ƿ�Ϊ����

	public LengthFilter(int mAX_EN) {
		super();
		MAX_EN = mAX_EN;
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		int destCount = dest.toString().length()
				+ getChineseCount(dest.toString());
		int sourceCount = source.toString().length()
				+ getChineseCount(source.toString());
		if (destCount + sourceCount > MAX_EN) {
			return "";
		} else {
			return source;
		}
	}

	private int getChineseCount(String str) {
		int count = 0;
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		return count;
	}
	
}
