package com.timefleeting.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerArrayAdapter extends ArrayAdapter<String> {

	private Context mContext;
	private String[] stringArray;
	private int textSize;
	
	public SpinnerArrayAdapter(Context context, String[] stringArray, int textSize) {
		super(context, android.R.layout.simple_spinner_item, stringArray);
		this.mContext = context;
		this.stringArray = stringArray;
		this.textSize = textSize;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
	    if (convertView == null) {
	    	LayoutInflater inflater = LayoutInflater.from(mContext);
	    	convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,false);
	    }

	    TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
	    tv.setText(stringArray[position]);
	    tv.setTextSize(textSize);
	    tv.setTextColor(mContext.getResources().getColor(R.color.future_listview_title));

	    return convertView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    if (convertView == null) {
	    	LayoutInflater inflater = LayoutInflater.from(mContext);
	    	convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
	    }

	    TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
	    tv.setText(stringArray[position]);
	    tv.setTextSize(textSize);
	    tv.setTextColor(mContext.getResources().getColor(R.color.future_listview_title));
	    return convertView;
	  }
	
}
