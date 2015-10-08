package com.timefleeting.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class EditPastActivity extends FragmentActivity 
	implements OnDateSetListener {

	private Context mContext;
	
	private LinearLayout titleLinearLayout;
	private LinearLayout dateLinearLayout;
	private LinearLayout repeatLinearLayout;
	private LinearLayout remarksLinearLayout;
	
	private EditText titleEditText;
	private EditText textEditText;
	
	private TextView dateTextView;

	
	private String dateString;
	private String oldDateString;
	
	private String[] dataStrings = {"Every year", 
									"Every month",
									"Every week",
									"Every hundred days",
									"Every thousand days",
									"Don't repeat"};
	
	private ArrayAdapter<String> arr_adapter;
	
	private Spinner spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_past);
		
		mContext = this;
		
		titleLinearLayout = (LinearLayout)findViewById(R.id.past_edit_title_ly);
		dateLinearLayout = (LinearLayout)findViewById(R.id.past_edit_date_ly);
		repeatLinearLayout = (LinearLayout)findViewById(R.id.past_edit_date_ly);
		remarksLinearLayout = (LinearLayout)findViewById(R.id.past_edit_text_ly);
		
		titleEditText = (EditText)findViewById(R.id.past_edit_title);
		textEditText = (EditText)findViewById(R.id.past_edit_text);
		
		dateTextView = (TextView)findViewById(R.id.past_edit_date);
		
		titleLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				titleEditText.requestFocus();
			}
		});
		
		dateLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setTime();
			}
		});
		
		spinner = (Spinner)findViewById(R.id.past_edit_spinner);
        
        arr_adapter= new SpinnerArrayAdapter(mContext, dataStrings);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);
		
	}

	private void setTime() {
		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		datePickerDialog.setYearRange(1915, 2036);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.setCancelable(false);
        datePickerDialog.show(getSupportFragmentManager(), GlobalSettings.DATEPICKER_TAG);
	}
	
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		month++;
		dateString = String.valueOf(year) + "-";
		dateString += (month < 10 ? "0" + String.valueOf(month) : String.valueOf(month)) + "-";
		dateString += (day < 10 ? "0" + String.valueOf(day) : String.valueOf(day));
	}

}
