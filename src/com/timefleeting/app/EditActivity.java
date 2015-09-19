package com.timefleeting.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;



public class EditActivity extends Activity {

	private Context mContext;
	private TimeFleetingData timeFleetingData;
	private String createTimeString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_layout);
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		createTimeString = formatter.format(curDate);  
		
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
		timeFleetingData.saveRecord(new Record(-1, titleString, contentString, "2015-09-19", createTimeString, "4", "FUTURE"));
		
		Intent intent = new Intent();
		intent.putExtra("isEditActivityFinished", true);
		setResult(RESULT_OK, intent);

		finish();
	}
	
}
