package com.timefleeting.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.Object;

import com.capricorn.RayMenu;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ScrollingView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class EditActivity extends FragmentActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener, ScrollViewListener  {

	public static final String DATEPICKER_TAG = "选择日期";
    public static final String TIMEPICKER_TAG = "选择时间";
	
	private Context mContext;
	private TimeFleetingData timeFleetingData;
	private String createTimeString;
	private String remindTimeString;
	private String starString = "3";
	
	private TextView createTimeTextView;
	
	private RayMenu rayMenu;
	private boolean lastIsScrollDown = false;
	
	private static final int[] ITEM_DRAWABLES_FUTURE = {
		R.drawable.save,
		R.drawable.sort_by_remind_time,
		R.drawable.sort_by_star,
		R.drawable.copy,
		R.drawable.statistics,
		R.drawable.home};
	
	private boolean isSaved = false;
	private boolean isRemind = false;
	private boolean isStared = false;
	private boolean afterSetTimeBack = false;
	
	private EditText titleEditText;
	private EditText contentEditText;
	private int saveId = -1;
	
	private ObservableScrollView observableScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_layout);
		
		mContext = this;

		observableScrollView = (ObservableScrollView)findViewById(R.id.editview_scrollview);
		observableScrollView.setScrollViewListener(this);
		
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		createTimeString = formatter.format(curDate);  
		
		createTimeTextView = (TextView)findViewById(R.id.create_time_textview);
		createTimeTextView.setText("---" + createTimeString);
		
		titleEditText = (EditText)findViewById(R.id.edit_layout_title);
		titleEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				isSaved = false;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		contentEditText = (EditText)findViewById(R.id.edit_layout_content);

		rayMenu = (RayMenu)findViewById(R.id.edit_layout_ray_menu);
		for (int i = 0; i < ITEM_DRAWABLES_FUTURE.length; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageResource(ITEM_DRAWABLES_FUTURE[i]);
			final int menuPosition = i;
			rayMenu.addItem(imageView, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(EditActivity.this, "Click " + menuPosition, Toast.LENGTH_SHORT).show();
					if (menuPosition == 0) {
						// save
						save(false);
					} else if (menuPosition == 1) {
						// set the remind time
						setRemindTime();
					} else if (menuPosition == 2) {
						
					} else if (menuPosition == 5) {
						goBack(true);
					}
				}
			});
		}
		
	}
	
	@Override
	public void onBackPressed() {
		goBack(true);
	}
	
	private void goBack(boolean isBack) {

		save(isBack);

	}
	
	// call this function when:
	// click the save button
	// back button
	private void save(boolean isBack) {
		
		try {
			timeFleetingData = TimeFleetingData.getInstance(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		String titleString = titleEditText.getText().toString();
		String contentString = contentEditText.getText().toString();

		if (isSaved) {
			// have click the save button
			timeFleetingData.saveRecord(new Record(
					saveId,
					titleString,
					contentString,
					remindTimeString,
					createTimeString,
					starString,
					"FUTURE"));
			if (isBack) {
				returnHome();
			}
		} else {
			// haven't click the save button
			if (!isRemind) {
				// haven't click the remind button
				afterSetTimeBack = isBack;
				isSaved = true;
				setRemindTime();
			} else {
				// haven't click the remind button
				saveId = timeFleetingData.saveRecord(new Record(
						-1,
						titleString,
						contentString,
						remindTimeString,
						createTimeString,
						starString,
						"FUTURE"));
				isSaved = true;
				if (isBack) {
					returnHome();
				}
			}
		}
	}
	
	private void setRemindTime() {
		isRemind = false;
		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		datePickerDialog.setYearRange(2015, 2036);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		remindTimeString += (hourOfDay < 10 ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay)) + ":";
		remindTimeString += (minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute)) + ":";
		remindTimeString += "00";
		
		isRemind = true;
		
		if (afterSetTimeBack) {
			save(true);
			returnHome();
		}
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub
		
		isRemind = false;
		
		// a bug in the datetimepicker-library
		// which will cause the month is month - 1
		month++;
		
		remindTimeString = String.valueOf(year) + "-";
		remindTimeString += (month < 10 ? "0" + String.valueOf(month) : String.valueOf(month)) + "-";
		remindTimeString += (day < 10 ? "0" + String.valueOf(day) : String.valueOf(day)) + "-";
		
		final Calendar calendar = Calendar.getInstance();
		
	    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
		
		timePickerDialog.setVibrate(false);
        timePickerDialog.setCloseOnSingleTapMinute(false);
        timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
	}
	
	private void returnHome() {
		Intent intent = new Intent();
		intent.putExtra("isEditActivityFinished", true);
		setResult(RESULT_OK, intent);

		finish();
	}

	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// TODO Auto-generated method stub
		if (oldy < y) {
			// down
			// should disappear
			if (!lastIsScrollDown) {
				AnimationSet animationSet = new AnimationSet(true);
				TranslateAnimation translateAnimation = 
						new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 1f);
				translateAnimation.setDuration(1000);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
;				rayMenu.startAnimation(animationSet);
			} else {
				
			}
			lastIsScrollDown = true;
		} else if (oldy > y) {
			// up
			// should appear
			if (lastIsScrollDown) {
				AnimationSet animationSet = new AnimationSet(true);
				TranslateAnimation translateAnimation = 
						new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 1f,
								Animation.RELATIVE_TO_SELF, 0f);
				translateAnimation.setDuration(1000);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
;				rayMenu.startAnimation(animationSet);
			} else {
				
			}
			lastIsScrollDown = false;
		}
	}
	
}
