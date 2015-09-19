package com.timefleeting.app;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;



public class EditActivity extends Activity {

	private Context mContext;
	private TimeFleetingData timeFleetingData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_layout);
		
	}
	
	@Override
	public void onBackPressed() {
		try {
			timeFleetingData = TimeFleetingData.getInstance(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
		EditText titleEditText = (EditText)findViewById(R.id.edit_layout_title);
		EditText contentEditText = (EditText)findViewById(R.id.edit_layout_content);
		String titleString = titleEditText.getText().toString();
		String contentString = contentEditText.getText().toString();
		timeFleetingData.saveRecord(new Record(-1, titleString, contentString, "2015-09-19", "2015-09-19", "4", "FUTURE"));
		finish();
//		if (currentLevel == LEVEL_COUNTY) {
//			queryCities();
//		} else if (currentLevel == LEVEL_CITY) {
//			queryProvinces();
//		} else {
//			if (isFromWeatherActivity) {
//				Intent intent = new Intent(this, WeatherActivity.class);
//				startActivity(intent);
//			}
//			finish();
//		}
	}
	
}
